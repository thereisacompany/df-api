package com.dfrecvcle.dfsystem.controller;

import com.alibaba.fastjson.JSONObject;
import com.dfrecvcle.dfsystem.constants.ExceptionConstants;
import com.dfrecvcle.dfsystem.live.entities.ArticleDTO;
import com.dfrecvcle.dfsystem.live.entities.ArticleTagVo4Article;
import com.dfrecvcle.dfsystem.live.entities.ArticleTags;
import com.dfrecvcle.dfsystem.service.articletag.ArticleTagService;
import com.dfrecvcle.dfsystem.utils.BaseResponseInfo;
import com.dfrecvcle.dfsystem.utils.datasource.DataSourceContextHolder;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
@ApiIgnore
@RestController
@RequestMapping(value = "/article/tag")
@Api(tags = {"關鍵字標籤管理(後端使用)"})
public class ArticleTagsController {

    private Logger logger = LoggerFactory.getLogger(ArticleTagsController.class);

    @Resource
    private ArticleTagService articleTagService;

    @GetMapping("/list")
    @ApiOperation("關鍵字標籤列表")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ArticleTagVo4Article.class, responseContainer = "List")})
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

        try{
            DataSourceContextHolder.setDBType("live");
            if(isDelete != null && isDelete.equals(0)) {
                isDelete = null;
            }
            List<ArticleTagVo4Article> dataList = articleTagService.getArticleTagList(keyword, begin, end, isDelete,currentPage, pageSize);
            int total = articleTagService.getArticleTagCount(keyword, begin, end, isDelete);
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
    @ApiOperation(value = "取得所有關鍵字標籤")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ArticleDTO.class, responseContainer = "List")})
    public BaseResponseInfo getAllList(HttpServletRequest request) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<>();
        try{
            DataSourceContextHolder.setDBType("live");
            map.put("rows", articleTagService.getAllArticleTag());

            res.code = 200;
            res.result = map;
        } catch (Exception e) {
            e.printStackTrace();
            res.code = 500;
            res.result = "取得資料失敗";
        }

        return res;
    }

    @GetMapping(value = "/getTag/{id}")
    @ApiOperation(value = "取得指定關鍵字標籤")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ArticleTags.class)})
    public BaseResponseInfo getTag(HttpServletRequest request, @PathVariable("id") Integer id) {
        BaseResponseInfo res = new BaseResponseInfo();
        try {
            DataSourceContextHolder.setDBType("live");
            Map<String, Object> data = new HashMap<>();
            data.put("tag", articleTagService.getTag(id));
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
    @ApiOperation(value = "新增關鍵字標籤(後台使用)")
    public Object addArticleTag(@RequestBody ArticleTags tag, HttpServletRequest request) {
        JSONObject result = ExceptionConstants.standardSuccess();
        DataSourceContextHolder.setDBType("live");
        articleTagService.insert(tag, request);

        return result;
    }

    @PutMapping(value = "/update")
    @ApiOperation(value = "編輯關鍵字標籤(後台使用)")
    public Object updateTag(@RequestBody ArticleTags tag, HttpServletRequest request) {
        JSONObject result = ExceptionConstants.standardSuccess();
        DataSourceContextHolder.setDBType("live");
        articleTagService.update(tag, request);
        return result;
    }

    @DeleteMapping(value = "/delete/{id}")
    @ApiOperation(value = "刪除關鍵字標籤(後台使用)")
    public Object deleteTag(@PathVariable("id") Integer id, HttpServletRequest request) {
        JSONObject result = ExceptionConstants.standardSuccess();
        DataSourceContextHolder.setDBType("live");
        articleTagService.deleteTag(id, request);
        return result;
    }

}
