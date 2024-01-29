package com.dfrecvcle.dfsystem.controller;

import com.alibaba.fastjson.JSONObject;
import com.dfrecvcle.dfsystem.constants.ExceptionConstants;
import com.dfrecvcle.dfsystem.live.entities.Article;
import com.dfrecvcle.dfsystem.live.entities.ArticleDTO;
import com.dfrecvcle.dfsystem.service.GcsFileService;
import com.dfrecvcle.dfsystem.service.article.ArticleService;

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
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/article")
@Api(tags = {"最新消息(後端使用)"})
public class ArticleController {

    private Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @Resource
    private ArticleService articleService;

//    @Resource
//    private SportService sportService;

    @Resource
    private GcsFileService gcsFileService;

    @GetMapping("/list")
    @ApiOperation("文章列表")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Article.class, responseContainer = "List")})
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
            List<Article> dataList = articleService.getArticleList(keyword, category, filter, hotTag, begin, end, currentPage, pageSize);
            int total = articleService.getArticleCount(keyword, category, filter, hotTag, begin, end);
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
            data.put("article", articleService.getArticle(id));
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
    public Object addArticle(@RequestBody Article tag, HttpServletRequest request) {
        JSONObject result = ExceptionConstants.standardSuccess();
        DataSourceContextHolder.setDBType("live");
        articleService.insert(tag, request);

        return result;
    }

    @PutMapping(value = "/update")
    @ApiOperation(value = "編輯文章(後台使用)")
    public Object updateArticle(@RequestBody Article tag, HttpServletRequest request) {
        JSONObject result = ExceptionConstants.standardSuccess();
        DataSourceContextHolder.setDBType("live");
        articleService.update(tag, request);
        return result;
    }

    @PutMapping(value = "/update/browserCount/{id}")
    @ApiOperation(value = "更新文章瀏覽數(後台使用)")
    public Object deleteArticleBrowserCount(@PathVariable("id") Long id, @RequestParam("count") Integer count, HttpServletRequest request) {
        JSONObject result = ExceptionConstants.standardSuccess();
        DataSourceContextHolder.setDBType("live");
        articleService.updateBrowserCount(id, count, request);
        return result;
    }

    @DeleteMapping(value = "/delete/{id}")
    @ApiOperation(value = "刪除文章(後台使用)")
    public Object deleteArticle(@PathVariable("id") Long id, HttpServletRequest request) {
        JSONObject result = ExceptionConstants.standardSuccess();
        DataSourceContextHolder.setDBType("live");
        articleService.deleteArticle(id, request);
        return result;
    }

//    @GetMapping(value = "/game/list")
//    @ApiOperation(value = "取得相關賽事設定(所有賽事清單)(後台使用)")
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ArticleDTO.class, responseContainer = "List")})
//    public BaseResponseInfo getGameList(@RequestParam(value = "beginDate", required = false) String begin,
//                                        @RequestParam(value = "endDate", required = false) String end,
//                                        HttpServletRequest request){
//        BaseResponseInfo res = new BaseResponseInfo();
//        Map<String, Object> map = new HashMap<>();
//
//        try {
//            DataSourceContextHolder.setDBType("sport");
//
//            map.put("rows", sportService.findGameList(begin, end, request));
//
//            res.code = 200;
//            res.result = map;
//        } catch (Exception e) {
//            e.printStackTrace();
//            res.code = 500;
//            res.result = "取得資料失敗";
//        }
//        return res;
//    }

//    @PostMapping("/upload")
//    @ApiOperation(value = "列表圖片、內頁圖片、頁面縮圖上傳")
//    public String uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
//        return gcsFileService.uploadFile(file, "article");
//    }
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

        return fileName;
    }

}
