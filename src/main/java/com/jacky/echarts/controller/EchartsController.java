package com.jacky.echarts.controller;

import com.jacky.echarts.pojo.Result;
import com.jacky.echarts.service.EchartsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Description
 * @auther Jacky
 * @create 2019-03-04 19:27
 */
@Controller
public class EchartsController {

    @Autowired
    private EchartsService echartsService;

    @RequestMapping("/getZhl")
    @ResponseBody
    public Result getZhl(@RequestBody String[] item){
        return echartsService.getZhl(item);
    }

}
