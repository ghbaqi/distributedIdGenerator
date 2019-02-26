## 分布式环境下数据库 ID 生产器 ##

- 符合自己公司需求的 ID 生成器 ， 希望在分布式环境下能够从指定 startValue 开始递增生成 ID 。只支持 long 类型 ID 。 
- 主要思想 从配置的数据库取一段值到服务器内存中， 之后访问这个服务时 ， 线程安全的递增从这个区段中获取 ID 。 此区段使用完毕，再去数据库同步下一个区段。
- 使用方式 初始化数据库idgenerator.sql ， 可以自定义取值区段大小 my.sqlseq.blocksize 。 调用 SqlSeqUtil.getId(String tableName) 为指定表生成主键。