package com.dfrecvcle.dfsystem.controller;

import com.alibaba.fastjson.JSONObject;
import com.dfrecvcle.dfsystem.constants.ExceptionConstants;
import com.dfrecvcle.dfsystem.live.entities.ArticleCategory4Article;
import com.dfrecvcle.dfsystem.live.entities.Feedback;
import com.dfrecvcle.dfsystem.service.feedback.FeedbackService;
import com.dfrecvcle.dfsystem.utils.BaseResponseInfo;
import com.dfrecvcle.dfsystem.utils.RandImageUtil;
import com.dfrecvcle.dfsystem.utils.Tools;
import com.dfrecvcle.dfsystem.utils.datasource.DataSourceContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/feedback")
@Api(tags = {"與我聯絡"})
public class FeedBackController {

    private Logger logger = LoggerFactory.getLogger(FeedBackController.class);

    @Resource
    private FeedbackService feedbackService;

//    @Resource
//    private LogService logService;

    @GetMapping("/list")
    @ApiOperation("意見列表(後台使用)")
    public BaseResponseInfo getList(@RequestParam("currentPage") Integer currentPage,
                                    @RequestParam("pageSize") Integer pageSize,
                                    @RequestParam(value = "member", required = false) String member,
                                    @RequestParam(value = "beginDate", required = false) String begin,
                                    @RequestParam(value = "endDate", required = false) String end,
                                    HttpServletRequest request) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<>();

        try {
            DataSourceContextHolder.setDBType("live");
            List<Feedback> dataList = feedbackService.getFeedbackList(member, begin, end, currentPage, pageSize);
            int total = feedbackService.getFeedbackCount(member, begin, end);
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

    @GetMapping(value = "/getFeedback/{id}")
    @ApiOperation(value = "取得指定意見(後台使用)")
    public BaseResponseInfo getFeedback(HttpServletResponse response, @PathVariable("id") long id) {
        BaseResponseInfo res = new BaseResponseInfo();

        try {
            DataSourceContextHolder.setDBType("live");
            Map<String, Object> data = new HashMap<>();
            data.put("feedback", feedbackService.getFeedback(id));
            res.code = 200;
            res.result = data;
        } catch (Exception e) {
            e.printStackTrace();
            res.code = 500;
            res.result = "取得資料失敗";
        }

        return res;
    }

    @PostMapping("/addFeedback")
    @ApiOperation("新增意見回饋")
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Feedback.class, responseContainer = "List")})
    public Object addFeedback(@RequestBody JSONObject json,
                              HttpServletRequest request) throws Exception {
        JSONObject result = ExceptionConstants.standardSuccess();
        DataSourceContextHolder.setDBType("live");
        Feedback feedback = JSONObject.parseObject(json.toJSONString(), Feedback.class);
        feedbackService.insert(feedback, request);

//        logService.insertLog("意見回饋",
//                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_ADD).append(feedback.getTitle()).toString(), userId, request);
        return result;
    }

    @PutMapping("/responseFeedback")
    @ApiOperation("回覆意見(後台使用)")
    public Object responseFeedback(@RequestBody JSONObject json, @RequestParam(required = false) Long userId, HttpServletRequest request) throws Exception {
        JSONObject result = ExceptionConstants.standardSuccess();
        DataSourceContextHolder.setDBType("live");
        Feedback feedback = JSONObject.parseObject(json.toJSONString(), Feedback.class);
        feedbackService.update(feedback, request);

//        logService.insertLog("意見回饋",
//                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append(feedback.getTitle()).toString(), userId, request);
        return result;
    }
    /**
     * 获取随机校验码
     * @param response
     *
     * @return
     */
    @GetMapping(value = "/randomImage")
    @ApiOperation(value = "取得隨機校驗碼")
    public BaseResponseInfo randomImage(HttpServletResponse response){
        BaseResponseInfo res = new BaseResponseInfo();
        try {
            Map<String, Object> data = new HashMap<>();
            String codeNum = Tools.getCharAndNum(4);
            String base64 = RandImageUtil.generate(codeNum);
            data.put("codeNum", codeNum);
            data.put("base64", base64);
            res.code = 200;
            res.result = data;
        } catch (Exception e) {
            e.printStackTrace();
            res.code = 500;
            res.result = "取得失敗";
        }
        return res;
    }
}
