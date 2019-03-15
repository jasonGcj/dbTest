package com.jacky.echarts.pojo;

import java.util.HashSet;
import java.util.Set;

public class UserAccessInFo {

    public String date;

    public Set<String> set = new HashSet<String>();

    public UserAccessInFo(String date, Set<String> set) {
        this.date = date;
        this.set = set;
    }
    public UserAccessInFo() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Set<String> getSet() {
        return set;
    }

    public void setSet(Set<String> set) {
        this.set = set;
    }



}
