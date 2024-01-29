package com.dfrecvcle.dfsystem.controller;

import com.alibaba.fastjson.JSONObject;
import com.dfrecvcle.dfsystem.constants.ExceptionConstants;
import com.dfrecvcle.dfsystem.live.entities.ArticleCategory;
import com.dfrecvcle.dfsystem.live.entities.ArticleCategory4Article;
import com.dfrecvcle.dfsystem.live.entities.ArticleDTO;
import com.dfrecvcle.dfsystem.service.GcsFileService;
import com.dfrecvcle.dfsystem.service.articlecategory.ArticleCategoryService;
import com.dfrecvcle.dfsystem.utils.BaseResponseInfo;
import com.dfrecvcle.dfsystem.utils.UploadUtil;
import com.dfrecvcle.dfsystem.utils.datasource.DataSourceContextHolder;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/news/category")
//@RequestMapping(value = "/article/category")
@Api(tags = {"最新消息分類管理(後端使用)"})
public class ArticleCategoryController {

    private Logger logger = LoggerFactory.getLogger(ArticleCategoryController.class);

    @Resource
    private ArticleCategoryService articleCategoryService;

    @Resource
    private GcsFileService gcsFileService;



    @GetMapping("/list")
    @ApiOperation("最新消息分類列表")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ArticleCategory4Article.class, responseContainer = "List")})
    public BaseResponseInfo getList(@RequestParam("currentPage") Integer currentPage,
                                    @RequestParam("pageSize") Integer pageSize,
                                    @RequestParam(value = "keyword", required = false) String keyword,
                                    @RequestParam(value = "beginDate", required = false) String begin,
                                    @RequestParam(value = "endDate", required = false) String end,
                                    @ApiParam("不帶及填0，會過濾被刪除的項目")
                                        @RequestParam(value = "isDelete", required = false) String isDelete,
                                    HttpServletRequest request) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<>();

        try {
            DataSourceContextHolder.setDBType("live");
            if(isDelete != null && isDelete.equals(0)) {
                isDelete = null;
            }
            List<ArticleCategory4Article> dataList = articleCategoryService.getArticleCategoryList(keyword, begin, end,
                    isDelete, currentPage, pageSize);
            int total = articleCategoryService.getArticleCategoryCount(keyword, begin, end, isDelete);
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

    @GetMapping("/allList")
    @ApiOperation(value = "取得所有文章分類")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ArticleDTO.class, responseContainer = "List")})
    public BaseResponseInfo getAllList(HttpServletRequest request) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<>();
        try{
            DataSourceContextHolder.setDBType("live");
            map.put("rows", articleCategoryService.getAllArticleCategory());

            res.code = 200;
            res.result = map;
        } catch (Exception e) {
            e.printStackTrace();
            res.code = 500;
            res.result = "取得資料失敗";
        }

        return res;
    }

    @GetMapping(value = "/getCategory/{id}")
    @ApiOperation(value = "取得指定文章分類")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ArticleCategory.class)})
    public BaseResponseInfo getCategory(HttpServletRequest request, @PathVariable("id") Integer id) {
        BaseResponseInfo res = new BaseResponseInfo();
        try {
            DataSourceContextHolder.setDBType("live");
            Map<String, Object> data = new HashMap<>();
            data.put("category", articleCategoryService.getCategory(id));
            res.code = 200;
            res.result = data;
        } catch (Exception e) {
            e.printStackTrace();
            res.code = 500;
            res.result = "取得資料失敗";
        }
        return res;
    }

//    @PostMapping("/upload")
//    @ApiOperation(value = "圖片上傳")
//    public String uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
//        return gcsFileService.uploadFile(file, "article-category");
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
        System.out.println(fileName);
        return fileName;
    }

    @GetMapping("/gcs/download")
    @ApiOperation(value = "下載檔案", hidden = true)
    public void downloadFile(@RequestParam("filename") String filename, @RequestParam("uniqueFileName") String object) throws FileNotFoundException {
        String destinationFilePath = "/path/to/downloaded/files/" + filename;
        gcsFileService.downloadFile(object, destinationFilePath);
    }



    @PostMapping(value = "/add")
    @ApiOperation(value = "新增文章分類(後台使用)")
    public Object addArticleCategory(@RequestBody ArticleCategory tag, HttpServletRequest request) {
        JSONObject result = ExceptionConstants.standardSuccess();
        DataSourceContextHolder.setDBType("live");
        articleCategoryService.insert(tag, request);

        return result;
    }

    @PutMapping(value = "/update")
    @ApiOperation(value = "編輯文章分類((後台使用)")
    public Object updateCategory(@RequestBody ArticleCategory tag, HttpServletRequest request) {
        JSONObject result = ExceptionConstants.standardSuccess();
        DataSourceContextHolder.setDBType("live");
        articleCategoryService.update(tag, request);
        return result;
    }

    @DeleteMapping(value = "/delete/{id}")
    @ApiOperation(value = "刪除文章分類((後台使用)")
    public Object deleteCategory(@PathVariable("id") Integer id, HttpServletRequest request) {
        JSONObject result = ExceptionConstants.standardSuccess();
        DataSourceContextHolder.setDBType("live");
        articleCategoryService.deleteCategory(id, request);
        return result;
    }
}
