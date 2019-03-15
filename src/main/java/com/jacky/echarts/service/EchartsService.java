package com.jacky.echarts.service;

import com.jacky.echarts.dao.HBaseDao;
import com.jacky.echarts.dao.MySqlDao;
import com.jacky.echarts.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @auther Jacky
 * @create 2019-03-04 19:27
 */
@Service
public class EchartsService {

    @Autowired
    private MySqlDao mySqlDao;

    @Autowired
    private HBaseDao hBaseDao;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 获取 页面显示 所需数据
     * @param times
     * @return
     */
    public Result getZhl(String[] times){
        // 返回结果 各个容器的集合
        Result result = new Result();
        // 各个数据的 容器
        List<Float> zhlList = new ArrayList<>();
        List<Integer> accessSumList = new ArrayList<>();
        List<Integer> successSumList = new ArrayList<>();

        // 遍历需要日期 查询对应的访问量、成交单数、并计算访问量
        for (String nowTime : times) {
            try {
                // 获取访问量
                Integer accessSum = (Integer) redisTemplate.boundHashOps("accessSum").get(nowTime);
                // 获取成交单数
                Integer successSum = (Integer) redisTemplate.boundHashOps("successSum").get(nowTime);
                // 获取转化率
                Float zhl = (Float) redisTemplate.boundHashOps("zhl").get(nowTime);

                if (zhl==null){
                    if (accessSum==null){
                        accessSum = getAccess(nowTime);
                        if(accessSum==null){
                            accessSum=0;
                        }
                    }
                    if (successSum==null){
                        successSum = getSuccess(nowTime);
                        if (successSum==null){
                            successSum=0;
                        }
                    }
                    zhl = calcZhl(accessSum, successSum);
                    // 保存数据到redis
                    redisTemplate.boundHashOps("zhl").put(nowTime,zhl);
                    redisTemplate.boundHashOps("accessSum").put(nowTime,accessSum);
                    redisTemplate.boundHashOps("successSum").put(nowTime,successSum);
                }
                // 分别
                zhlList.add(zhl);
                accessSumList.add(accessSum);
                successSumList.add(successSum);
                // 封装数据返回前端
                result.setZhl(zhlList);
                result.setAccessSum(accessSumList);
                result.setSuccessSum(successSumList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
     }

    /**
     * 计算转化率
     * 转化率 = 当天成交单数 / 当天总访问量
     * @param accessSum 访问量
     * @param successSum 成交单数
     * @return
     */
    private float calcZhl(int accessSum, float successSum) {
        float zhl;
        if (accessSum==0){
            zhl=0;
        }else{
            DecimalFormat df = new DecimalFormat("0.00");// 格式化两位小数
            String data = df.format(((float) successSum / accessSum) * 100);
            zhl=Float.valueOf(data);
        }
        return zhl;
    }

    /**
     * 根据当天时间 查询当天总访问量
     * @param nowTime
     * @return
     */
    private int getAccess(String nowTime) {
        return hBaseDao.getPeople(nowTime);
    }

    /**
     * 根据当前日期 查询当天成交单数
     * @param nowTime
     * @return
     */
    private int getSuccess(String nowTime) {
        return  mySqlDao.getData(nowTime);
    }

}
