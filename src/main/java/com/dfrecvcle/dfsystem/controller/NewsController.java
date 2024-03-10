package com.dfrecvcle.dfsystem.controller;

import com.alibaba.fastjson.JSONObject;
import com.dfrecvcle.dfsystem.constants.ExceptionConstants;
import com.dfrecvcle.dfsystem.live.entities.News;
import com.dfrecvcle.dfsystem.live.entities.NewsMain;
import com.dfrecvcle.dfsystem.live.entities.NewsOut;
import com.dfrecvcle.dfsystem.service.news.NewsMainService;
import com.dfrecvcle.dfsystem.service.news.NewsService;
import com.dfrecvcle.dfsystem.utils.BaseResponseInfo;
import com.dfrecvcle.dfsystem.utils.datasource.DataSourceContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping(value = "/api/v1/news")
@Api(tags = {"最新消息管理"})
public class NewsController {

    private Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Resource
    private NewsService newsService;

    @Resource
    private NewsMainService newsMainService;

//    @Resource
//    private LogService logService;


//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "X-Access-Token", value = "Access Token", required = true, dataType = "string", paramType = "header")
//    })
//    @PostMapping(value = "/addNewsMain")
//    @ApiOperation(value = "建立消息分類(後台使用)")
//    public Object addNewsMain(@RequestBody JSONObject json, @RequestParam(required = false) Long userId, HttpServletRequest request) throws Exception {
//        JSONObject result = ExceptionConstants.standardSuccess();
//        System.out.println(json.toJSONString());
//        DataSourceContextHolder.setDBType("live");
//        NewsMain newsMain =JSONObject.parseObject(json.toJSONString(), NewsMain.class);
//        System.out.println(newsMain.getTitle());
//        newsMainService.insert(newsMain,request);
//        return result;
//    }

//    @PutMapping(value = "/updateNewsMain")
//    @ApiOperation(value = "更新消息分類(後台使用)")
//    public Object updateNewsMain(@RequestBody JSONObject json, @RequestParam Long userId, HttpServletRequest request) throws Exception {
//        JSONObject result = ExceptionConstants.standardSuccess();
//        DataSourceContextHolder.setDBType("live");
//        NewsMain newsMain = JSONObject.parseObject(json.toJSONString(), NewsMain.class);
//        newsMainService.update(newsMain, request);
//
////        logService.insertLog("最新消息",
////                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append(news.getTitle()).toString(), userId, request);
//
//        return result;
//    }

//    @DeleteMapping(value = "/deleteNewsMain/{id}")
//    @ApiOperation(value = "刪除最新消息分類(後台使用)")
//    public Object deleteNewsMain(HttpServletRequest request, @PathVariable("id") long id) {
//        JSONObject result = ExceptionConstants.standardSuccess();
//        DataSourceContextHolder.setDBType("live");
//        newsMainService.deleteNews(id, request);
//
////        logService.insertLog("最新消息",
////                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_DELETE).append(id).toString(), userId, request);
//
//        return result;
//    }


    @GetMapping(value = "/getNewsMainList")
    @ApiOperation(value = "取得最新消息分類列表")
    public BaseResponseInfo getNewsMainList(@RequestParam("currentPage") Integer currentPage,
                                        @RequestParam("pageSize") Integer pageSize,
                                        @RequestParam(value = "visible", required = false) Byte visible,
                                        @RequestParam(value = "title", required = false) String title,
                                        HttpServletRequest request) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<>();

        try {
            DataSourceContextHolder.setDBType("live");
            List<NewsMain> dataList = newsMainService.getNewsMainList( visible, title, currentPage, pageSize);
            int total = newsMainService.getNewsCount( visible, title);
            map.put("total", total);
            map.put("rows", dataList);

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

//    @PostMapping(value = "/addNews")
//    @ApiOperation(value = "建立消息(後台使用)")
//    public Object addNews(@RequestBody JSONObject json, @RequestParam(required = false) Long userId, HttpServletRequest request) throws Exception {
//        JSONObject result = ExceptionConstants.standardSuccess();
//        DataSourceContextHolder.setDBType("live");
//        News news = JSONObject.parseObject(json.toJSONString(), News.class);
//        newsService.insert(news, request);
//
////        logService.insertLog("最新消息",
////                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_ADD).append(news.getTitle()).toString(), userId, request);
//
//        return result;
//    }

//    @PutMapping(value = "/updateNews")
//    @ApiOperation(value = "更新消息(後台使用)")
//    public Object updateNews(@RequestBody JSONObject json, @RequestParam Long userId, HttpServletRequest request) throws Exception {
//        JSONObject result = ExceptionConstants.standardSuccess();
//        DataSourceContextHolder.setDBType("live");
//        News news = JSONObject.parseObject(json.toJSONString(), News.class);
//        newsService.update(news, request);
//
////        logService.insertLog("最新消息",
////                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append(news.getTitle()).toString(), userId, request);
//
//        return result;
//    }

    @GetMapping(value = "/list")
    @ApiOperation(value = "取得最新消息列表")
    public BaseResponseInfo getNewsList(@RequestParam(value="currentPage", required = false,defaultValue = "0") Integer currentPage,
                                 @RequestParam(value="pageSize", required = false,defaultValue = "0") Integer pageSize,
                                 @RequestParam(value = "type", required = false) Byte type, @RequestParam(value = "year", required = false) String year,
                                 @RequestParam(value = "visible", required = false) Byte visible,
                                 @RequestParam(value = "title", required = false) String title,
                                 HttpServletRequest request) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<>();

        try {
            DataSourceContextHolder.setDBType("live");
            List<NewsOut> dataList = newsService.getNewsList(year,type, visible, title, currentPage, pageSize);

            System.out.println(dataList);
            int total = newsService.getNewsCount(year,type, visible, title);
            map.put("total", total);
            map.put("news", dataList);
            map.put("page", pageSize);
            map.put("limit", currentPage);

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

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "取得指定最新消息")
    public BaseResponseInfo getNews(HttpServletResponse response, @PathVariable("id") long id,
                                    @RequestParam(value = "type", required = false) Byte type) {
        BaseResponseInfo res = new BaseResponseInfo();

        try {
            DataSourceContextHolder.setDBType("live");
            Map<String, Object> data = new HashMap<>();
            data.put("data", newsService.getNews(id,type));
            res.code = 200;
            res.result = data.get("data");
//            res.result = data;
        } catch (Exception e) {
            e.printStackTrace();
            res.code = 500;
            res.result = "取得資料失敗";
        }

        return res;
    }

//    @DeleteMapping(value = "/delete/{id}")
//    @ApiOperation(value = "刪除最新消息(後台使用)")
//    public Object deleteNews(HttpServletRequest request, @PathVariable("id") long id) {
//        JSONObject result = ExceptionConstants.standardSuccess();
//        DataSourceContextHolder.setDBType("live");
//        newsService.deleteNews(id, request);
//
////        logService.insertLog("最新消息",
////                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_DELETE).append(id).toString(), userId, request);
//
//        return result;
//    }
}
