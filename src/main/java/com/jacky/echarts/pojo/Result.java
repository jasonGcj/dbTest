package com.jacky.echarts.pojo;

import java.util.List;

public class Result {

    private List<Float> zhl;
    private List<Integer> accessSum;
    private List<Integer> successSum;

    public List<Float> getZhl() {
        return zhl;
    }

    public void setZhl(List<Float> zhl) {
        this.zhl = zhl;
    }

    public List<Integer> getAccessSum() {
        return accessSum;
    }

    public void setAccessSum(List<Integer> accessSum) {
        this.accessSum = accessSum;
    }

    public List<Integer> getSuccessSum() {
        return successSum;
    }

    public void setSuccessSum(List<Integer> successSum) {
        this.successSum = successSum;
    }
}
