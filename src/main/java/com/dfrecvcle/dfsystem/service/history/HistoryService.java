package com.dfrecvcle.dfsystem.service.history;

import com.dfrecvcle.dfsystem.constants.BusinessConstants;
import com.dfrecvcle.dfsystem.constants.ExceptionConstants;
import com.dfrecvcle.dfsystem.exception.BusinessRunTimeException;
import com.dfrecvcle.dfsystem.exception.HfException;
import com.dfrecvcle.dfsystem.live.entities.Article;
import com.dfrecvcle.dfsystem.live.entities.HistoryBackend;
import com.dfrecvcle.dfsystem.live.entities.User;
import com.dfrecvcle.dfsystem.live.mappers.ArticleMapper;
import com.dfrecvcle.dfsystem.live.mappers.HistoryBackendMapper;
import com.dfrecvcle.dfsystem.live.mappers.HistoryMapper;
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
public class HistoryService {

    private Logger logger = LoggerFactory.getLogger(HistoryService.class);

    @Resource
    private HistoryBackendMapper historyBackendMapper;

    @Resource
    private UserService userService;

    @Resource
    private LogService logService;

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void insert(HistoryBackend article, HttpServletRequest request) {
        if(article.getTitle() == null || article.getTitle().isEmpty()
                || article.getCode() == null || article.getCode().isEmpty()) {
            throw new BusinessRunTimeException(ExceptionConstants.ARTICLE_ADD_FAILED_CODE,
                    ExceptionConstants.ARTICLE_ADD_FAILED_MSG);
        }
        int count = historyBackendMapper.isExistTitle(null, article.getTitle());
        if(count > 0) {
            throw new BusinessRunTimeException(ExceptionConstants.ARTICLE_TITLE_IS_EXIST_CODE,
                    String.format(ExceptionConstants.ARTICLE_TITLE_IS_EXIST_MSG, article.getTitle()));
        }

        int result = 0;
        try{
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if(article.getCreatedAt()==null) {
                User user = userService.getCurrentUser();
                article.setCreatedAt(now);
                article.setCreatedBy(user.getUsername());
            }
            if(article.getSort()==null) {
                article.setSort(1);
            }
            if(article.getIsLatest()==null) {
                article.setIsLatest(1);
            }
            if(article.getIsFeatured()==null) {
                article.setIsFeatured(0);
            }
            if(article.getIsTop()==null) {
                article.setIsTop(0);
            }
            if(article.getIsPublish()==null) {
                article.setIsPublish(0);
            }
            if(article.getIsEnable()==null) {
                article.setIsEnable(1);
            }
            if(article.getBrowserCount()==null) {
                article.setBrowserCount(0);
            }
            article.setUpdatedAt(now);
            article.setUpdatedBy(null);
            result = historyBackendMapper.insertArticle(article);

            logService.insertLog("文章",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_ADD).append(article.getTitle()).toString(), request);
        }catch(Exception e){
            HfException.writeFail(logger, e);
        }
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void update(HistoryBackend article, HttpServletRequest request) {
        if(article.getId() == null) {
            throw new BusinessRunTimeException(ExceptionConstants.ARTICLE_CATEGORY_EDIT_FAILED_CODE,
                    ExceptionConstants.ARTICLE_CATEGORY_EDIT_FAILED_MSG);
        }

        // 分類名稱已存在
        if(article.getTitle() != null && !article.getTitle().isEmpty()) {
            int count = historyBackendMapper.isExistTitle(article.getId(), article.getTitle());
            if(count > 0) {
                throw new BusinessRunTimeException(ExceptionConstants.ARTICLE_TITLE_IS_EXIST_CODE,
                        String.format(ExceptionConstants.ARTICLE_TITLE_IS_EXIST_MSG, article.getTitle()));
            }
        }
        int result = 0;
        try{
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if(article.getUpdatedAt()==null) {
                User user = userService.getCurrentUser();
                article.setUpdatedAt(now);
                article.setUpdatedBy(user.getUsername());
            }

            result = historyBackendMapper.updateByPrimaryKeySelective(article);

            logService.insertLog("文章",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append(article.getTitle()).toString(), request);
        }catch(Exception e){
            HfException.writeFail(logger, e);
        }
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void updateBrowserCount(Long id, Integer count, HttpServletRequest request) {
        try {
            HistoryBackend article = historyBackendMapper.selectByPrimaryKey(id);
            if(article == null) {
                throw new BusinessRunTimeException(ExceptionConstants.ARTICLE_EDIT_FAILED_CODE,
                        ExceptionConstants.ARTICLE_EDIT_FAILED_MSG);
            }
            article.setBrowserCount(article.getBrowserCount()+count);
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if(article.getUpdatedAt()==null) {
                User user = userService.getCurrentUser();
                article.setUpdatedAt(now);
                article.setUpdatedBy(user.getUsername());
            }
            historyBackendMapper.updateByPrimaryKeySelective(article);

            logService.insertLog("文章(瀏覽次數)",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append(id).toString(), request);
        } catch (Exception e) {
            HfException.writeFail(logger, e);
        }
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void deleteArticle(Long id, HttpServletRequest request) {
        HistoryBackend article = historyBackendMapper.selectByPrimaryKey(id);
        if (article == null) {
            throw new BusinessRunTimeException(ExceptionConstants.ARTICLE_EDIT_FAILED_CODE,
                    ExceptionConstants.ARTICLE_EDIT_FAILED_MSG);
        }
        try {
            article.setIsDelete(1);
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if(article.getUpdatedAt()==null) {
                User user = userService.getCurrentUser();
                article.setUpdatedAt(now);
                article.setUpdatedBy(user.getUsername());
            }
            historyBackendMapper.updateByPrimaryKeySelective(article);

            logService.insertLog("文章",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_DELETE).append(id).toString(), request);
        } catch (Exception e) {
            HfException.writeFail(logger, e);
        }
    }

    public List<HistoryBackend> getArticleList(String keyword, List<Integer> category, List<String> filter, Long hotTag,
                                        String start, String end, Integer currentPage, Integer pageSize) {
        Integer latest = null;
        Integer featured = null;
        Integer publish = null;
        Integer top = null;
        Integer delete = null;
        if(filter != null && !filter.isEmpty()) {
            for (String name : filter) {
                if(name.toLowerCase().equals("latest")) {
                    latest = 1;
                }
                if(name.toLowerCase().equals("featured")) {
                    featured = 1;
                }
                if(name.toLowerCase().equals("publish")) {
                    publish = 1;
                }
                if(name.toLowerCase().equals("top")) {
                    top = 1;
                }
                if(name.toLowerCase().equals("delete")) {
                    delete = 1;
                }
            }
        }
        return historyBackendMapper.getListByExample(keyword, category, hotTag, latest, featured, publish, top, delete,
                start, end, currentPage*pageSize, pageSize);
    }

    public int getArticleCount(String keyword, List<Integer> category, List<String> filter, Long hotTag,
                               String start, String end) {
        Integer latest = null;
        Integer featured = null;
        Integer publish = null;
        Integer top = null;
        Integer delete = null;
        if(filter != null && !filter.isEmpty()) {
            for (String name : filter) {
                if(name.toLowerCase().equals("latest")) {
                    latest = 1;
                }
                if(name.toLowerCase().equals("featured")) {
                    featured = 1;
                }
                if(name.toLowerCase().equals("publish")) {
                    publish = 1;
                }
                if(name.toLowerCase().equals("top")) {
                    top = 1;
                }
                if(name.toLowerCase().equals("delete")) {
                    delete = 1;
                }
            }
        }
        return historyBackendMapper.getListByExampleCount(keyword, category, hotTag, latest, featured, publish, top, delete, start, end);
    }

    public HistoryBackend getArticle(Long id) {
        HistoryBackend result = null;
        try{
            result = historyBackendMapper.selectByPrimaryKey(id);
        }catch(Exception e){
            HfException.readFail(logger, e);
        }
        return result;
    }

}
