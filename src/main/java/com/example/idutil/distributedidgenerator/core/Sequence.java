package com.example.idutil.distributedidgenerator.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicLong;

import javax.sql.DataSource;

/**
 * @author : gaohui
 * @Date : 2019/2/18 14:26
 * @Description :
 */
public class Sequence {

    private static final Logger LOGGER = LoggerFactory.getLogger(Sequence.class);

    private DataSource dataSource;
    private int        blockSize;
    private volatile long startValue = 0L;
    private volatile long   endValue;
    //    private Step   step;
    private          String tableName;

    private AtomicLong atomicLong;

    public Sequence(DataSource dataSource, String tableName, int blockSize) {
        this.tableName = tableName;
        this.dataSource = dataSource;
        this.blockSize = blockSize;
        endValue = startValue + blockSize;
        atomicLong = new AtomicLong(startValue);
        //        step = new Step(startValue, blockSize);
        // 第一次进入需要同步数据库的值
        getNextBlock();
    }

    // todo 是否需要加同步 ？

    /**
     * 获取 id 值   第一次进入方法需要同步数据库的值
     * 1. 内存中 blocksize 没有使用完， 直接在内存中自增获取（线程安全）
     * 2. 去数据库中同步新的一段值 （线程安全）
     */
    public synchronized Long getId(String tableName) {

        if (atomicLong.get() < endValue) { // 有没有线程安全问题 ？
            return atomicLong.incrementAndGet();
        }

        getNextBlock();

        return atomicLong.incrementAndGet();

    }


    /**
     * 当内存中 blockSize 区间的值使用完毕 ， 需要去数据库 并发安全的 获取下一段值
     * <p>
     * 1. 获取 db 中的当前值
     * 2. 加上 block Size 设置回去
     */
    private void getNextBlock() {
        LOGGER.info("getNextBlock");
        Connection connection = null;

        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            // 获取当前值
            Long value = getPersistenceValue(connection, tableName);
            if (value == null || value.intValue() < 0) {
                value = initPersistenceValue(connection, tableName);
            }
            if (value > Long.MAX_VALUE - blockSize) {
                throw new RuntimeException("No more id value.");
            }

            // 2. 加上 block Size 设置回去
            saveValue(connection, value, tableName);
            atomicLong.set(value);
            endValue = value + blockSize;
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * //todo
     * 在并发的情况下 ， 第一次设置失败 ， 需要重试
     */
    private boolean saveValue(Connection connection, Long value, String key) {
        PreparedStatement statement = null;
        try {
            // 并发安全的更新 ! ! !
            statement = connection.prepareStatement("update idgenerator set current_value = ?  where table_name = ? and current_value = ?  ");
            statement.setLong(1, value + blockSize);
            statement.setString(2, key);
            statement.setLong(3, value);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 初始化 id 的值
     * <p>
     * //todo 是否需要线程安全
     */
    private Long initPersistenceValue(Connection connection, String key) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("insert into idgenerator (table_name,current_value) values (?,?)");
            statement.setString(1, key);
            statement.setLong(2, startValue);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return startValue;
    }

    private Long getPersistenceValue(Connection connection, String key) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        // SELECT current_value FROM idgenerator WHERE table_name = 'intention'
        try {
            statement = connection.prepareStatement(" SELECT current_value FROM idgenerator WHERE table_name = ?");
            statement.setString(1, key);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("current_value");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }
}



















