package com.dfrecvcle.dfsystem.service.news;

import com.dfrecvcle.dfsystem.constants.BusinessConstants;
import com.dfrecvcle.dfsystem.constants.ExceptionConstants;
import com.dfrecvcle.dfsystem.exception.BusinessRunTimeException;
import com.dfrecvcle.dfsystem.exception.HfException;
import com.dfrecvcle.dfsystem.live.entities.News;
import com.dfrecvcle.dfsystem.live.entities.NewsOut;
import com.dfrecvcle.dfsystem.live.entities.User;
import com.dfrecvcle.dfsystem.live.mappers.NewsMapper;
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
public class NewsService {

    private Logger logger = LoggerFactory.getLogger(NewsService.class);

    @Resource
    private NewsMapper newsMapper;

    @Resource
    private UserService userService;

    @Resource
    private LogService logService;

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void insert(News news, HttpServletRequest request) {

        if(news.getType() == null || news.getTitle() == null || news.getTitle().isEmpty()) {
            throw new BusinessRunTimeException(ExceptionConstants.NEWS_ADD_FAILED_CODE, ExceptionConstants.NEWS_ADD_FAILED_MSG);
        }

        // TODO 標題已存在
        if(newsMapper.selectSameTitleCount(news.getTitle())>0) {
            throw new BusinessRunTimeException(ExceptionConstants.NEWS_TITLE_ALREADY_EXIST_CODE, ExceptionConstants.NEWS_TITLE_ALREADY_EXIST_MSG);
        }

        int result = 0;
        try{
//            User user = userService.getCurrentUser();
//
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//            news.setCreatedAt(LocalDateTime.now().format(formatter));
//            news.setCreatedBy(user.getUsername());
//            if(news.getVisibleAt() != null) {
//
//            }
            result = newsMapper.insertNews(news);

            logService.insertLog("最新消息",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_ADD).append(news.getTitle()).toString(), request);
        }catch(Exception e){
            HfException.writeFail(logger, e);
        }
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void update(News news, HttpServletRequest request) {
        if(news.getId() == null) {
            throw new BusinessRunTimeException(ExceptionConstants.NEWS_EDIT_FAILED_CODE, ExceptionConstants.NEWS_EDIT_FAILED_MSG);
        }

        // TODO 標題已存在

        int result = 0;
        try{
            result = newsMapper.updateByPrimaryKeySelective(news);

            logService.insertLog("最新消息",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append(news.getTitle()).toString(), request);
        }catch(Exception e){
            HfException.writeFail(logger, e);
        }
    }

    public List<NewsOut> getNewsList(String year,Byte type, Byte visible, String title, Integer currentPage, Integer pageSize) {
        return newsMapper.getListByTypeAndVisible(year,type, visible, title, currentPage*pageSize, pageSize);
    }

    public int getNewsCount(String year,Byte type, Byte visible, String title) {
        return newsMapper.getListByTypeANdVisibleCount(year,type, visible, title);
    }

    public NewsOut getNews(long id,Byte type)throws Exception {
        NewsOut result = null;
        try{
            System.out.println("近來");
            System.out.println(id);
            result = newsMapper.selectByPrimaryKey(id,type);
            System.out.println(result);
        }catch(Exception e){
            HfException.readFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void deleteNews(long id, HttpServletRequest request) {
        try {
            newsMapper.deleteByPrimaryKey(id);

            logService.insertLog("最新消息",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_DELETE).append(id).toString(), request);
        } catch (Exception e) {
            HfException.writeFail(logger, e);
        }
    }
}
