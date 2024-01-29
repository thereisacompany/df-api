package com.dfrecvcle.dfsystem.controller;

import com.alibaba.fastjson.JSONObject;
import com.dfrecvcle.dfsystem.constants.ExceptionConstants;
import com.dfrecvcle.dfsystem.live.entities.Article;
import com.dfrecvcle.dfsystem.live.entities.HistoryBackend;
import com.dfrecvcle.dfsystem.live.entities.HistoryOut;
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
@RequestMapping(value = "/history")
@Api(tags = {"重要紀事管理(後端使用)"})
public class HistoryBackEndController {

    private Logger logger = LoggerFactory.getLogger(HistoryBackEndController.class);

   @Resource
   private HistoryService_bak historyService_bak;

    @Resource
    private HistoryService historyService;

//    @Resource
//    private LogService logService;


    @GetMapping("/list")
    @ApiOperation("文章列表")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = HistoryBackend.class, responseContainer = "List")})
    public BaseResponseInfo getList(@RequestParam("currentPage") Integer currentPage,
                                    @RequestParam("pageSize") Integer pageSize,
                                    @RequestParam(value = "keyword", required = false) String keyword,
                                    @RequestParam(value = "category", required = false) List<Integer> category,
                                    @ApiParam("latest,featured,publish,top,delete 可填入此五組篩選")
                                    @RequestParam(value = "filter", required = false) List<String> filter,
                                    @ApiParam("熱門搜尋")
                                    @RequestParam(value = "hotTag", required = false) Long hotTag,
                                    @RequestParam(value = "beginDate", required = false) String begin,
                                    @RequestParam(value = "endDate", required = false) String end,
                                    HttpServletRequest request) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<>();

        try {
            DataSourceContextHolder.setDBType("live");
            List<HistoryBackend> dataList = historyService.getArticleList(keyword, category, filter, hotTag, begin, end, currentPage, pageSize);
            int total = historyService.getArticleCount(keyword, category, filter, hotTag, begin, end);
            map.put("total", total);
            map.put("rows", dataList);

            res.code = 200;
            res.result = map;
        } catch (Exception e) {
            e.printStackTrace();
            res.code = 500;
            res.result = "取得資料失敗";
        }
        return res;
    }

    @GetMapping(value = "/getArticle/{id}")
    @ApiOperation(value = "取得指定文章")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Article.class)})
    public BaseResponseInfo getArticle(HttpServletRequest request, @PathVariable("id") Long id) {
        BaseResponseInfo res = new BaseResponseInfo();
        try {
            DataSourceContextHolder.setDBType("live");
            Map<String, Object> data = new HashMap<>();
            data.put("article", historyService.getArticle(id));
            res.code = 200;
            res.result = data;
        } catch (Exception e) {
            e.printStackTrace();
            res.code = 500;
            res.result = "取得資料失敗";
        }
        return res;
    }
    @PostMapping(value = "/add")
    @ApiOperation(value = "新增文章(後台使用)")
    public Object addArticle(@RequestBody HistoryBackend tag, HttpServletRequest request) {
        JSONObject result = ExceptionConstants.standardSuccess();
        DataSourceContextHolder.setDBType("live");
        historyService.insert(tag, request);

        return result;
    }

    @PutMapping(value = "/update")
    @ApiOperation(value = "編輯文章(後台使用)")
    public Object updateArticle(@RequestBody HistoryBackend tag, HttpServletRequest request) {
        JSONObject result = ExceptionConstants.standardSuccess();
        DataSourceContextHolder.setDBType("live");
        historyService.update(tag, request);
        return result;
    }

    @PutMapping(value = "/update/browserCount/{id}")
    @ApiOperation(value = "更新文章瀏覽數(後台使用)")
    public Object deleteArticleBrowserCount(@PathVariable("id") Long id, @RequestParam("count") Integer count, HttpServletRequest request) {
        JSONObject result = ExceptionConstants.standardSuccess();
        DataSourceContextHolder.setDBType("live");
        historyService.updateBrowserCount(id, count, request);
        return result;
    }

    @DeleteMapping(value = "/delete/{id}")
    @ApiOperation(value = "刪除文章(後台使用)")
    public Object deleteArticle(@PathVariable("id") Long id, HttpServletRequest request) {
        JSONObject result = ExceptionConstants.standardSuccess();
        DataSourceContextHolder.setDBType("live");
        historyService.deleteArticle(id, request);
        return result;
    }

    @PostMapping("/upload")
    @ApiOperation(value = "圖片上傳")
    public String uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {

        UploadUtil uploadUtil = new UploadUtil();

        String fileName = "";
        if (uploadUtil.doUpload(file, request, "uploadImg")) {
            fileName = uploadUtil.getUploadFile();
        } else {
            fileName = "file";
        }
        System.out.println(fileName);
        return fileName;
    }



}
