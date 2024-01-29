package com.dfrecvcle.dfsystem.controller;

import com.alibaba.fastjson.JSONObject;
import com.dfrecvcle.dfsystem.constants.ExceptionConstants;
import com.dfrecvcle.dfsystem.live.entities.*;
import com.dfrecvcle.dfsystem.service.GcsFileService;
import com.dfrecvcle.dfsystem.service.articlecategory.ArticleCategoryService;
import com.dfrecvcle.dfsystem.service.locations.BusinessCategoryService;
import com.dfrecvcle.dfsystem.service.locations.RegionsCategoryService;
import com.dfrecvcle.dfsystem.utils.BaseResponseInfo;
import com.dfrecvcle.dfsystem.utils.datasource.DataSourceContextHolder;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/location/backend/category")
//@RequestMapping(value = "/article/category")
@Api(tags = {"服務據點分類管理(後端使用)"})
public class locationBackendCategoryController {

    private Logger logger = LoggerFactory.getLogger(locationBackendCategoryController.class);



    @Resource
    private BusinessCategoryService businessCategoryService;

    @Resource
    private RegionsCategoryService regionsCategoryService;

//    @Resource
//    private GcsFileService gcsFileService;



    @GetMapping("/regions/list")
    @ApiOperation("地區分類列表")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RegionsCategory4Article.class, responseContainer = "List")})
    public BaseResponseInfo getRegionsList(@RequestParam("currentPage") Integer currentPage,
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
            List<RegionsCategory4Article> dataList = regionsCategoryService.getArticleCategoryList(keyword, begin, end,
                    isDelete, currentPage, pageSize);
            int total = regionsCategoryService.getArticleCategoryCount(keyword, begin, end, isDelete);
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


    @GetMapping("/regions/allList")
    @ApiOperation(value = "取得所有文章分類")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ArticleDTO.class, responseContainer = "List")})
    public BaseResponseInfo getAllList(HttpServletRequest request) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<>();
        try{
            DataSourceContextHolder.setDBType("live");
            map.put("rows", regionsCategoryService.getAllArticleCategory());

            res.code = 200;
            res.result = map;
        } catch (Exception e) {
            e.printStackTrace();
            res.code = 500;
            res.result = "取得資料失敗";
        }

        return res;
    }

    @GetMapping(value = "/regions/getCategory/{id}")
    @ApiOperation(value = "取得指定地區分類")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RegionsCategory.class)})
    public BaseResponseInfo getCategory(HttpServletRequest request, @PathVariable("id") Integer id) {
        BaseResponseInfo res = new BaseResponseInfo();
        try {
            DataSourceContextHolder.setDBType("live");
            Map<String, Object> data = new HashMap<>();
            data.put("category", regionsCategoryService.getCategory(id));
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


//    @PostMapping("/upload")
//    @ApiOperation(value = "圖片上傳")
//    public String uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
//
//        UploadUtil uploadUtil = new UploadUtil();
//        String fileName = "";
//        if (uploadUtil.doUpload(file, request, "uploadImg")) {
//            fileName = uploadUtil.getUploadFile();
//        } else {
//            fileName = "file";
//        }
//
//        return fileName;
//    }

//    @GetMapping("/gcs/download")
//    @ApiOperation(value = "下載檔案", hidden = true)
//    public void downloadFile(@RequestParam("filename") String filename, @RequestParam("uniqueFileName") String object) throws FileNotFoundException {
//        String destinationFilePath = "/path/to/downloaded/files/" + filename;
//        gcsFileService.downloadFile(object, destinationFilePath);
//    }



    @PostMapping(value = "/regions/add")
    @ApiOperation(value = "新增地區分類(後台使用)")
    public Object addArticleCategory(@RequestBody RegionsCategory tag, HttpServletRequest request) {
        JSONObject result = ExceptionConstants.standardSuccess();
        DataSourceContextHolder.setDBType("live");
        regionsCategoryService.insert(tag, request);

        return result;
    }

    @PutMapping(value = "/regions/update")
    @ApiOperation(value = "編輯地區分類((後台使用)")
    public Object updateCategory(@RequestBody RegionsCategory tag, HttpServletRequest request) {
        JSONObject result = ExceptionConstants.standardSuccess();
        DataSourceContextHolder.setDBType("live");
        regionsCategoryService.update(tag, request);
        return result;
    }

    @DeleteMapping(value = "/regions/delete/{id}")
    @ApiOperation(value = "刪除地區分類((後台使用)")
    public Object deleteCategory(@PathVariable("id") Integer id, HttpServletRequest request) {
        JSONObject result = ExceptionConstants.standardSuccess();
        DataSourceContextHolder.setDBType("live");
        regionsCategoryService.deleteCategory(id, request);
        return result;
    }

    //--------- 經營型態 ----------


    @GetMapping("/business/list")
    @ApiOperation("經營型態分類列表")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ArticleCategory4Article.class, responseContainer = "List")})
    public BaseResponseInfo getBusinessList(@RequestParam("currentPage") Integer currentPage,
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
            List<BusinessCategory4Article> dataList = businessCategoryService.getArticleCategoryList(keyword, begin, end,
                    isDelete, currentPage, pageSize);
            int total = businessCategoryService.getArticleCategoryCount(keyword, begin, end, isDelete);
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

    @Deprecated
    @GetMapping("/business/allList")
    @ApiOperation(value = "取得所有文章分類")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ArticleDTO.class, responseContainer = "List")})
    public BaseResponseInfo getbusinessAllList(HttpServletRequest request) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<>();
        try{
            DataSourceContextHolder.setDBType("live");
            map.put("rows", businessCategoryService.getAllArticleCategory());

            res.code = 200;
            res.result = map;
        } catch (Exception e) {
            e.printStackTrace();
            res.code = 500;
            res.result = "取得資料失敗";
        }

        return res;
    }

    @GetMapping(value = "/business/getCategory/{id}")
    @ApiOperation(value = "取得指定經營型態分類")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = BusinessCategory.class)})
    public BaseResponseInfo getBusinessCategory(HttpServletRequest request, @PathVariable("id") Integer id) {
        BaseResponseInfo res = new BaseResponseInfo();
        try {
            DataSourceContextHolder.setDBType("live");
            Map<String, Object> data = new HashMap<>();
            data.put("category", businessCategoryService.getCategory(id));
            res.code = 200;
            res.result = data;
        } catch (Exception e) {
            e.printStackTrace();
            res.code = 500;
            res.result = "取得資料失敗";
        }
        return res;
    }

    @PostMapping(value = "/business/add")
    @ApiOperation(value = "新增經營型態分類(後台使用)")
    public Object addBusinessCategory(@RequestBody BusinessCategory tag, HttpServletRequest request) {
        JSONObject result = ExceptionConstants.standardSuccess();
        DataSourceContextHolder.setDBType("live");
        businessCategoryService.insert(tag, request);

        return result;
    }

    @PutMapping(value = "/business/update")
    @ApiOperation(value = "編輯經營型態分類((後台使用)")
    public Object updateBusinessCategory(@RequestBody BusinessCategory tag, HttpServletRequest request) {
        JSONObject result = ExceptionConstants.standardSuccess();
        DataSourceContextHolder.setDBType("live");
        businessCategoryService.update(tag, request);
        return result;
    }

    @DeleteMapping(value = "/business/delete/{id}")
    @ApiOperation(value = "刪除經營型態分類((後台使用)")
    public Object deleteBusinessCategory(@PathVariable("id") Integer id, HttpServletRequest request) {
        JSONObject result = ExceptionConstants.standardSuccess();
        DataSourceContextHolder.setDBType("live");
        businessCategoryService.deleteCategory(id, request);
        return result;
    }
}
