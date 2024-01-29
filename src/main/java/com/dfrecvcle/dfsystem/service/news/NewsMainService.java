package com.dfrecvcle.dfsystem.service.news;

import com.dfrecvcle.dfsystem.constants.BusinessConstants;
import com.dfrecvcle.dfsystem.constants.ExceptionConstants;
import com.dfrecvcle.dfsystem.exception.BusinessRunTimeException;
import com.dfrecvcle.dfsystem.exception.HfException;
import com.dfrecvcle.dfsystem.live.entities.News;
import com.dfrecvcle.dfsystem.live.entities.NewsMain;
import com.dfrecvcle.dfsystem.live.entities.User;
import com.dfrecvcle.dfsystem.live.mappers.NewsMainMapper;
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
public class NewsMainService {

    private Logger logger = LoggerFactory.getLogger(NewsMainService.class);

    @Resource
    private NewsMainMapper newsMainMapper;

    @Resource
    private UserService userService;

    @Resource
    private LogService logService;





    public List<NewsMain> getNewsMainList(Byte visible, String title, Integer currentPage, Integer pageSize) {
        return newsMainMapper.getListByTypeAndVisible(visible, title, currentPage*pageSize, pageSize);
    }

    public int getNewsCount( Byte visible, String title) {
        return newsMainMapper.getListByTypeANdVisibleCount(visible, title);
    }

    public NewsMain getNews(long id)throws Exception {
        NewsMain result = null;
        try{
            result = newsMainMapper.selectByPrimaryKey(id);
        }catch(Exception e){
            HfException.readFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void deleteNews(long id, HttpServletRequest request) {
        try {
            newsMainMapper.deleteByPrimaryKey(id);

            logService.insertLog("最新消息",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_DELETE).append(id).toString(), request);
        } catch (Exception e) {
            HfException.writeFail(logger, e);
        }
    }
}
