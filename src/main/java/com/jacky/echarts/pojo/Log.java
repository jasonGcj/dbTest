package com.jacky.echarts.pojo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日志格式化 类 （可扩展）
 */
public class Log {

    private String date;
    private String ip;
    private String host;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Log() {
    }

    public Log(String date, String ip) {
        this.date = formatDate(date);
        this.ip = ip;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = formatDate(date);
    }

    // 重构  7/Oct/2019 14:25:36 +8000
    private String formatDate(String date) {
        String formatDate="";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.US);
            Date d = sdf.parse(date.split(" ")[0]);
            SimpleDateFormat localSdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            formatDate = localSdf.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatDate;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
