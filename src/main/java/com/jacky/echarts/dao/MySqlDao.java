package com.jacky.echarts.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @Description
 * @auther Jacky
 * @create 2019-03-04 19:28
 */
@Mapper
@Repository
public interface MySqlDao {

    @Select("SELECT COUNT(DISTINCT user_id) FROM tb_pay_log WHERE trade_state=1 AND pay_time LIKE '${time}%'")
    public int getData(@Param("time") String time);


}
