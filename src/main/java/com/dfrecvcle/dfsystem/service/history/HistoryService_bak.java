package com.dfrecvcle.dfsystem.service.history;

import com.dfrecvcle.dfsystem.constants.BusinessConstants;
import com.dfrecvcle.dfsystem.constants.ExceptionConstants;
import com.dfrecvcle.dfsystem.exception.BusinessRunTimeException;
import com.dfrecvcle.dfsystem.exception.HfException;
import com.dfrecvcle.dfsystem.live.entities.*;
import com.dfrecvcle.dfsystem.live.mappers.HistoryMapper;
import com.dfrecvcle.dfsystem.live.mappers.LocationsMapper;
import com.dfrecvcle.dfsystem.live.mappers.NewsMapper;
import com.dfrecvcle.dfsystem.service.log.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class HistoryService_bak {

    private Logger logger = LoggerFactory.getLogger(HistoryService_bak.class);

    @Resource
    private NewsMapper newsMapper;

    @Resource
    private LocationsMapper locationsMapper;



    @Resource
    HistoryMapper historyMapper;

    @Resource
    private LogService logService;

    private HistoryService_bak locationsService;

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


    public List<Regions> getRegionsList() {
        return locationsMapper.getListRegionsByTypeAndVisible();
    }

    public List<Business> getBusinessList() {
        return locationsMapper.getListBusinessByTypeAndVisible();
    }

    public List<HistoryOut> getHistoryList(String filter,String title) {
        return historyMapper.getListHistoryByTypeAndVisible(filter,title);
    }

    public List<HistoryMainOut> getHistoryMainList(String filter,String title) {
        return historyMapper.getListMainHistory(filter,title);
    }


    public int getNewsCount(Byte type, Byte visible, String title) {
        return newsMapper.getListByTypeANdVisibleBackendCount(type, visible, title);
    }

//    public NewsOut getNews(long id)throws Exception {
//        NewsOut result = null;
//        try{
//            result = newsMapper.selectByPrimaryKey(id);
//        }catch(Exception e){
//            HfException.readFail(logger, e);
//        }
//        return result;
//    }

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
