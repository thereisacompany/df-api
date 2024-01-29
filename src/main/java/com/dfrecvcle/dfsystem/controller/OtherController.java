package com.dfrecvcle.dfsystem.controller;

import com.alibaba.fastjson.JSONObject;
import com.dfrecvcle.dfsystem.constants.ExceptionConstants;
import com.dfrecvcle.dfsystem.live.entities.*;
import com.dfrecvcle.dfsystem.service.GcsFileService;
import com.dfrecvcle.dfsystem.service.articlecategory.ArticleCategoryService;
import com.dfrecvcle.dfsystem.service.other.OtherCategoryService;
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
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/other/category")
@Api(tags = {"其他管理"})
public class OtherController {

    private Logger logger = LoggerFactory.getLogger(OtherController.class);

    @Resource
    private OtherCategoryService otherCategoryService;

    @Resource
    private GcsFileService gcsFileService;



    @GetMapping("/list")
    @ApiOperation("頁面Meta管理列表(後台使用)")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = OtherCategory4Article.class, responseContainer = "List")})
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
            List<OtherCategory4Article> dataList = otherCategoryService.getArticleCategoryList(keyword, begin, end,
                    isDelete, currentPage, pageSize);
            int total = otherCategoryService.getArticleCategoryCount(keyword, begin, end, isDelete);
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
    @ApiOperation(value = "取得所有Meta List(後台使用)")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ArticleDTO.class, responseContainer = "List")})
    public BaseResponseInfo getAllList(HttpServletRequest request) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<>();
        try{
            DataSourceContextHolder.setDBType("live");
            map.put("rows", otherCategoryService.getAllArticleCategory());

            res.code = 200;
            res.result = map;
        } catch (Exception e) {
            e.printStackTrace();
            res.code = 500;
            res.result = "取得資料失敗";
        }

        return res;
    }


    @GetMapping("/getallList")
    @ApiOperation(value = "取得所有Meta List")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", responseContainer = "List")})
    public BaseResponseInfo getAllListMeta(HttpServletRequest request) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<>();
        try{
            DataSourceContextHolder.setDBType("live");

            List<OtherResponse> dataList = otherCategoryService.getAllMeta();
            System.out.println(dataList.size());
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

    @GetMapping(value = "/getCategory/{id}")
    @ApiOperation(value = "取得指定資料(後台使用)")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = OtherCategory.class)})
    public BaseResponseInfo getCategory(HttpServletRequest request, @PathVariable("id") Integer id) {
        BaseResponseInfo res = new BaseResponseInfo();
        try {
            DataSourceContextHolder.setDBType("live");
            Map<String, Object> data = new HashMap<>();
            data.put("category", otherCategoryService.getCategory(id));
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
//
//        String fileName = "";
//        if (uploadUtil.doUpload(file, request, "uploadImg")) {
//            fileName = uploadUtil.getUploadFile();
//        } else {
//            fileName = "file";
//        }
//        System.out.println(fileName);
//        return fileName;
//    }
//
//    @GetMapping("/gcs/download")
//    @ApiOperation(value = "下載檔案", hidden = true)
//    public void downloadFile(@RequestParam("filename") String filename, @RequestParam("uniqueFileName") String object) throws FileNotFoundException {
//        String destinationFilePath = "/path/to/downloaded/files/" + filename;
//        gcsFileService.downloadFile(object, destinationFilePath);
//    }



    @PostMapping(value = "/add")
    @ApiOperation(value = "新增Meta資料(後台使用)")
    public Object addArticleCategory(@RequestBody OtherCategory tag, HttpServletRequest request) {
        JSONObject result = ExceptionConstants.standardSuccess();
        DataSourceContextHolder.setDBType("live");
        otherCategoryService.insert(tag, request);

        return result;
    }

    @PutMapping(value = "/update")
    @ApiOperation(value = "編輯Meta資料((後台使用)")
    public Object updateCategory(@RequestBody OtherCategory tag, HttpServletRequest request) {
        JSONObject result = ExceptionConstants.standardSuccess();
        DataSourceContextHolder.setDBType("live");
        otherCategoryService.update(tag, request);
        return result;
    }

    @DeleteMapping(value = "/delete/{id}")
    @ApiOperation(value = "刪除Meta((後台使用)")
    public Object deleteCategory(@PathVariable("id") Integer id, HttpServletRequest request) {
        JSONObject result = ExceptionConstants.standardSuccess();
        DataSourceContextHolder.setDBType("live");
        otherCategoryService.deleteCategory(id, request);
        return result;
    }
}
