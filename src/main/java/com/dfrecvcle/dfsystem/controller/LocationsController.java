package com.dfrecvcle.dfsystem.controller;

import com.alibaba.fastjson.JSONObject;
import com.dfrecvcle.dfsystem.constants.ExceptionConstants;
import com.dfrecvcle.dfsystem.live.entities.*;
import com.dfrecvcle.dfsystem.service.locations.LocationsService;
import com.dfrecvcle.dfsystem.service.news.NewsMainService;
import com.dfrecvcle.dfsystem.service.news.NewsService;
import com.dfrecvcle.dfsystem.utils.BaseResponseInfo;
import com.dfrecvcle.dfsystem.utils.datasource.DataSourceContextHolder;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/locations/")
@Api(tags = {"服務據點管理"})
public class LocationsController {

    private Logger logger = LoggerFactory.getLogger(LocationsController.class);

    @Resource
    private NewsService newsService;

    @Resource
    private LocationsService locationsService;

    @Resource
    private NewsMainService newsMainService;

//    @Resource
//    private LogService logService;



    @GetMapping(value="/regions")
    @ApiOperation(value = "縣市地區選項列表")
    public BaseResponseInfo getRegions(HttpServletRequest request){
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<>();
        try {
            DataSourceContextHolder.setDBType("live");
            List<Regions> dataList = locationsService.getRegionsList();
            map.put("regions", dataList);
            res.code = 200;
            res.result = map;

        } catch (Exception e) {
                e.printStackTrace();
                map.put("total", 0);
                map.put("rows", new ArrayList<>());
                res.code = 500;
                res.result = map;
        }
        return res;
    }

    @GetMapping(value="/business")
    @ApiOperation(value = "經營型態列表")
    public BaseResponseInfo getBusiness(HttpServletRequest request){
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<>();
        try {
            DataSourceContextHolder.setDBType("live");
            List<Business> dataList = locationsService.getBusinessList();
            map.put("business", dataList);
            res.code = 200;
            res.result = map;

        } catch (Exception e) {
            e.printStackTrace();
            map.put("total", 0);
            map.put("rows", new ArrayList<>());
            res.code = 500;
            res.result = map;
        }
        return res;
    }

    @GetMapping(value = "/list")
    @ApiOperation(value = "取得服務據點列表")
    public BaseResponseInfo getNewsList(
//                                 @RequestParam(value = "region", required = false) String region,
//                                 @RequestParam(value = "businessType", required = false) String businessType,
//                                 @RequestParam(value = "title", required = false) String title,
                                 @ApiParam(value = "地區", required = false) @RequestParam(value = "region", required = false) String region,
                                 @ApiParam(value = "業務類型", required = false) @RequestParam(value = "businessType", required = false) String businessType,
                                 @ApiParam(value = "標題", required = false) @RequestParam(value = "title", required = false) String title,
    HttpServletRequest request) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<>();

        try {
            DataSourceContextHolder.setDBType("live");
            List<LocationsOut> dataList = locationsService.getLocationList(region, businessType, title);

            map.put("locations", dataList);


            res.code = 200;
            res.result = map;
        } catch (Exception e) {
            e.printStackTrace();
            map.put("total", 0);
            map.put("rows", new ArrayList<>());
            res.code = 500;
            res.result = map;
        }
        return res;
    }




}
