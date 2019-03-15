package com.jacky.echarts.util;

import com.jacky.echarts.pojo.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @Description 日志格式处理类
 * @auther 稀罕
 * @create 2019-02-28 9:04
 */
public class NginxString {

    //分
    private static final String token = "\\|\\|\\|"; //分割符


    public static Log splitLog(String log){ //根据分割符得到数组
//        String log = "www.pinyougou.com|||192.168.88.1|||7/Oct/2019 14:25:36 +8000|||get|||200|||http1.1";

        String[] arr = log.split(token);
        Log log1 = new Log(arr[2],arr[1]);
        log1.setHost(arr[0]);

        return log1;
    }

}
