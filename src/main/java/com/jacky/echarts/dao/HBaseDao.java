package com.jacky.echarts.dao;

import com.jacky.echarts.pojo.UserAccessInFo;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class HBaseDao {

    private static Admin admin;

    /**
     * 连接Hbase集群
     * @return
     * @throws IOException
     */
    public static Connection initHbase() throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        configuration.set("hbase.zookeeper.quorum", "192.168.88.20");//,192.168.88.21,192.168.88.22
        configuration.set("hbase.master", "192.168.88.20:60000");
        //集群配置↓
        //configuration.set("hbase.zookeeper.quorum", "101.236.39.141,101.236.46.114,101.236.46.113");
        return ConnectionFactory.createConnection(configuration);
    }

    //根据rowKey进行查询
    public static String getDataByRowKey(String tableName, String rowKey) throws IOException {
        String accessSum="";
        Table table = initHbase().getTable(TableName.valueOf(tableName));
        Get get = new Get(rowKey.getBytes());
        //先判断是否有此条数据
        if (!get.isCheckExistenceOnly()) {
            Result result = table.get(get);
            // 结果集的处理
            for (Cell cell : result.rawCells()) {
                String colName = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
                accessSum = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
            }
        }
        return accessSum;
    }


    //创建表
    public static void createTable(String tableNmae, String[] cols) throws IOException {
        TableName tableName = TableName.valueOf(tableNmae);
        admin = initHbase().getAdmin();
        if (admin.tableExists(tableName)) {
            System.out.println("表已存在！");
        } else {
            HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);
            for (String col : cols) {
                HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(col);
                hTableDescriptor.addFamily(hColumnDescriptor);
            }
            admin.createTable(hTableDescriptor);
        }
    }

    //插入数据
    public static void insertData(String tableName, Map<String,Integer> map){
        Set<String> set = map.keySet();
        for (String date : set) {
            TableName tablename = TableName.valueOf(tableName);

            // rowkeyHash 以hash值存入HBase 避免 数据 扎堆 存入相同分区服务器
            int rowkeyHash = date.hashCode();
            System.out.println("rowkeyHash ："+rowkeyHash);

            Put put = new Put(date.getBytes());
            //参数：1.列族名  2.列名  3.值
            System.out.println("访问量："+map.get(date));
            put.addColumn("ip".getBytes(), "AccessSum".getBytes(), (map.get(date)+"").getBytes());
            Table table = null;
            try {
                table = initHbase().getTable(tablename);
                table.put(put);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 根据表名和rowkey查询当天总访问量
     * @param rowkey
     * @return
     */
    public int getPeople(String rowkey) {
        try {
            String accessSum = getDataByRowKey("NginxAccessLog", rowkey);
            if ("".equals(accessSum)){
                accessSum="0";
            }
//            System.out.println(rowkey+"  访问量是："+accessSum+"->"+Integer.parseInt(accessSum));
            return Integer.parseInt(accessSum);
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
