package com.dfrecvcle.dfsystem.service.feedback;

import com.dfrecvcle.dfsystem.constants.BusinessConstants;
import com.dfrecvcle.dfsystem.constants.ExceptionConstants;
import com.dfrecvcle.dfsystem.exception.BusinessRunTimeException;
import com.dfrecvcle.dfsystem.exception.HfException;
import com.dfrecvcle.dfsystem.live.entities.Feedback;
import com.dfrecvcle.dfsystem.live.entities.User;
import com.dfrecvcle.dfsystem.live.mappers.FeedbackMapper;
import com.dfrecvcle.dfsystem.service.log.LogService;
import com.dfrecvcle.dfsystem.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class FeedbackService {

    private Logger logger = LoggerFactory.getLogger(FeedbackService.class);

    @Resource
    private FeedbackMapper feedbackMapper;

    @Resource
    private LogService logService;

    @Resource
    private UserService userService;

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void insert(Feedback feedback, HttpServletRequest request) {

        if(feedback.getUsername() == null || feedback.getUsername().isEmpty() ||
                feedback.getContent() == null || feedback.getContent().isEmpty()) {
            throw new BusinessRunTimeException(ExceptionConstants.FEEDBACK_ADD_FAILED_CODE,
                    ExceptionConstants.FEEDBACK_ADD_FAILED_MSG);
        }

        int result = 0;
        try{
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            feedback.setResponse(null);
            if(feedback.getCreatedAt()==null) {
//                User user = userService.getCurrentUser();
                feedback.setCreatedAt(now);
                feedback.setCreatedBy(feedback.getUsername());
            }
            feedback.setUpdatedAt(now);
            feedback.setUpdatedBy(null);
            result = feedbackMapper.insertFeedback(feedback);

            logService.insertLog("意見回饋",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_ADD).append(feedback.getQuestion()).toString(), request);
        }catch(Exception e){
            HfException.writeFail(logger, e);
        }
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void update(Feedback feedback, HttpServletRequest request) {
        if(feedback.getId() == null || feedback.getResponse().isEmpty()) {
            throw new BusinessRunTimeException(ExceptionConstants.FEEDBACK_EDIT_FAILED_CODE, ExceptionConstants.FEEDBACK_EDIT_FAILED_MSG);
        }

        if (getFeedback(feedback.getId()) == null) {
            throw new BusinessRunTimeException(ExceptionConstants.FEEDBACK_ID_NO_DATA_CODE,
                    String.format(ExceptionConstants.FEEDBACK_ID_NO_DATA_MSG, feedback.getId()));
        }

        int result = 0;
        try{
            if(feedback.getUpdatedAt() == null) {
                User user = userService.getCurrentUser();
                feedback.setUpdatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                feedback.setUpdatedBy(user.getUsername());
            }
            result = feedbackMapper.updateByPrimaryKeySelective(feedback);

            logService.insertLog("意見回饋",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append(feedback.getId()).toString(), request);
        }catch(Exception e){
            HfException.writeFail(logger, e);
        }
    }

    public List<Feedback> getFeedbackList(String member, String begin, String end, Integer currentPage, Integer pageSize) {
        return feedbackMapper.getListByCreatedByAndCreatedAt(member, begin, end, currentPage*pageSize, pageSize);
    }

    public int getFeedbackCount(String member, String begin, String end) {
        return feedbackMapper.getListByCreatedByAndCreatedAtCount(member, begin, end);
    }

    public Feedback getFeedback(Long id) {
        Feedback result = null;
        try{
            result = feedbackMapper.selectByPrimaryKey(id);
        }catch(Exception e){
            HfException.readFail(logger, e);
        }
        return result;
    }
}
