package com.example.idutil.distributedidgenerator.core;

import com.example.idutil.distributedidgenerator.utils.FileUtils;

import org.springframework.util.StringUtils;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

/**
 * @author : gaohui
 * @Date : 2019/2/18 13:46
 * @Description :
 */
public class SqlSeqUtil {

    private static Map<String, Sequence> sequenceMap;
    private static int blockSize = 10;

    public synchronized static void initSqlSeq(DataSource dataSource) {
        if (sequenceMap != null) {
            return;
        }
        sequenceMap = new ConcurrentHashMap<>();
        Set<String> keyList = getKeyList();
        for (String key : keyList) {
            if (!StringUtils.isEmpty(key)) {
                Sequence sequence = new Sequence(dataSource, key, blockSize);
                sequenceMap.put(key, sequence);
            }
        }
    }


    public static long getId(String tableName) {
        if (StringUtils.isEmpty(tableName)) {
            throw new IllegalArgumentException("tableName 不能为空");
        }
        return sequenceMap.get(tableName).getId(tableName);
    }


    private static Set<String> getKeyList() {
        Set<String> keyList = new HashSet();
        String path = FileUtils.getClassPathName("seqKeyName.properties");
        Properties properties = FileUtils.readProperties(path);
        Enumeration enu = properties.elements();

        while (enu.hasMoreElements()) {
            Object value = enu.nextElement();
            if (value != null) {
                String valueStr = String.valueOf(value);
                if (!StringUtils.isEmpty(valueStr)) {
                    keyList.add(valueStr);
                }
            }
        }

        return keyList;
    }


}
