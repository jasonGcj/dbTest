package com.jacky.echarts.kafka;

import com.jacky.echarts.dao.HBaseDao;
import com.jacky.echarts.pojo.Log;
import com.jacky.echarts.pojo.UserAccessInFo;
import com.jacky.echarts.util.NginxString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

//@Component
public class KafkaC {

    @Autowired
    private HBaseDao hBaseDao;

    private Set<String> set = new HashSet<>();
    private Map<String,Integer> map = new HashMap<>();

    @KafkaListener(topics = {"nginxLog"})
    public void receive(String log){
//        String log = "www.pinyougou.com|||192.168.88.1|||7/Oct/2019 14:25:36 +8000|||get|||200|||http1.1";
        if (log.length()>0){
            Log log1 = NginxString.splitLog(log);
            String ip = log1.getIp();
//            date = 2019-03-05
            String date = log1.getDate();
            //            System.out.println("date:"+date+" ip:"+ip);

            Integer count = map.get(date);
//            System.out.println("count:"+count);
            if (count==null){
                if (!map.isEmpty()){ // 判断是否为 初始化 容器为空不存入HBase
                    System.out.println("map 不为空！存入HBase");
                    addDataToHBase(date);
                }else{// 容器初始化 存入容器
                    set.add(ip);
                    map.put(date,set.size());
                    System.out.println("实时访问量："+map.get(date));
                }
            }else{
                // ip 出重
                set.add(ip);
                // 访问量
                map.put(date,set.size());
                System.out.println("实时访问量："+map.get(date));
            }
        }
    }

    /**
     * 提交 map容器 并重置 访问量map容器 和 ip set 容器
     * @param date
     */
    private void addDataToHBase(String date) {
//        Integer count = map.get(date);
        // 提交map容器
        hBaseDao.insertData("NginxAccessLog",map);
        // 初始化容器
        map = new HashMap<>();
        set=new HashSet<>();
    }
}
