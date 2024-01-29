package com.dfrecvcle.dfsystem.controller;

import com.alibaba.fastjson.JSONObject;
import com.dfrecvcle.dfsystem.constants.ExceptionConstants;
import com.dfrecvcle.dfsystem.live.entities.*;
import com.dfrecvcle.dfsystem.service.history.HistoryService;
import com.dfrecvcle.dfsystem.service.history.HistoryService_bak;
import com.dfrecvcle.dfsystem.utils.BaseResponseInfo;
import com.dfrecvcle.dfsystem.utils.UploadUtil;
import com.dfrecvcle.dfsystem.utils.datasource.DataSourceContextHolder;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/milestones/")
@Api(tags = {"重要紀事管理"})
public class HistoryController {

    private Logger logger = LoggerFactory.getLogger(HistoryController.class);

   @Resource
   private HistoryService_bak historyService_bak;

    @Resource
    private HistoryService historyService;

//    @Resource
//    private LogService logService;


    @GetMapping(value = "/list")
    @ApiOperation(value = "取得重要紀事列表",response = HistoryOut.class)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "code", value = "HTTP Status Code", dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "result", value = "Result Object", dataTypeClass = HistoryOut.class, paramType = "query")
//    })
    public BaseResponseInfo getNewsList(
//                                 @RequestParam(value = "region", required = false) String region,
//                                 @RequestParam(value = "businessType", required = false) String businessType,
//                                 @RequestParam(value = "title", required = false) String title,
                                 @ApiParam(value = "時間範圍", required = false) @RequestParam(value = "filter", required = false) String filter,

    HttpServletRequest request) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<>();
        String title="";

        try {
            DataSourceContextHolder.setDBType("live");
            List<HistoryOut> dataList = historyService_bak.getHistoryList(filter,title);

            map.put("milestones", dataList);


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


    @GetMapping(value = "/listyear")
    @ApiOperation(value = "取得重要紀事年列表",response = HistoryOut.class)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "code", value = "HTTP Status Code", dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "result", value = "Result Object", dataTypeClass = HistoryOut.class, paramType = "query")
//    })
    public BaseResponseInfo getNewsYearList(
//                                 @RequestParam(value = "region", required = false) String region,
//                                 @RequestParam(value = "businessType", required = false) String businessType,
//                                 @RequestParam(value = "title", required = false) String title,
            @ApiParam(value = "時間範圍", required = false) @RequestParam(value = "filter", required = false) String filter,

            HttpServletRequest request) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<>();
        String title="";

        try {
            DataSourceContextHolder.setDBType("live");
            List<HistoryMainOut> dataList = historyService_bak.getHistoryMainList(filter,title);

            map.put("milestonesMain", dataList);


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
