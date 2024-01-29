package com.dfrecvcle.dfsystem.service.articletag;

import com.alibaba.fastjson.JSONObject;
import com.dfrecvcle.dfsystem.constants.BusinessConstants;
import com.dfrecvcle.dfsystem.constants.ExceptionConstants;
import com.dfrecvcle.dfsystem.exception.BusinessRunTimeException;
import com.dfrecvcle.dfsystem.exception.HfException;
import com.dfrecvcle.dfsystem.live.entities.ArticleTagVo4Article;
import com.dfrecvcle.dfsystem.live.entities.ArticleTags;
import com.dfrecvcle.dfsystem.live.entities.User;
import com.dfrecvcle.dfsystem.live.mappers.ArticleTagsMapper;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleTagService {

    private Logger logger = LoggerFactory.getLogger(ArticleTagService.class);

    @Resource
    private ArticleTagsMapper articleTagsMapper;

    @Resource
    private UserService userService;

    @Resource
    private LogService logService;

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void insert(ArticleTags tag, HttpServletRequest request) {
        if(tag.getName() == null || tag.getName().isEmpty()) {
            throw new BusinessRunTimeException(ExceptionConstants.ARTICLE_TAG_ADD_FAILED_CODE,
                    ExceptionConstants.ARTICLE_TAG_ADD_FAILED_MSG);
        }
        int count = articleTagsMapper.isExistTagName(null, tag.getName());
        if(count > 0) {
            throw new BusinessRunTimeException(ExceptionConstants.ARTICLE_TAG_IS_EXIST_CODE,
                    String.format(ExceptionConstants.ARTICLE_TAG_IS_EXIST_MSG, tag.getName()));
        }

        int result = 0;
        try{
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if(tag.getCreatedAt()==null) {
                User user = userService.getCurrentUser();
                tag.setCreatedAt(now);
                tag.setCreatedBy(user.getUsername());
            }
            if(tag.getSort()==null) {
                tag.setSort(1);
            }
            if(tag.getIsDelete()==null) {
                tag.setIsDelete(0);
            }
            tag.setUpdatedAt(now);
            tag.setUpdatedBy(null);
            result = articleTagsMapper.insertArticleTag(tag);

            logService.insertLog("關鍵字標籤",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_ADD).append(tag.getName()).toString(), request);
        }catch(Exception e){
            HfException.writeFail(logger, e);
        }
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void update(ArticleTags tag, HttpServletRequest request) {
        if(tag.getId() == null) {
            throw new BusinessRunTimeException(ExceptionConstants.ARTICLE_TAG_EDIT_FAILED_CODE,
                    ExceptionConstants.ARTICLE_TAG_EDIT_FAILED_MSG);
        }

        // 關鍵字標籤已存在
        if(tag.getName() != null && !tag.getName().isEmpty()) {
            int count = articleTagsMapper.isExistTagName(tag.getId(), tag.getName());
            if(count > 0) {
                throw new BusinessRunTimeException(ExceptionConstants.ARTICLE_TAG_IS_EXIST_CODE,
                        String.format(ExceptionConstants.ARTICLE_TAG_IS_EXIST_MSG, tag.getName()));
            }
        }

        int result = 0;
        try{
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if(tag.getUpdatedAt()==null) {
                User user = userService.getCurrentUser();
                tag.setUpdatedAt(now);
                tag.setUpdatedBy(user.getUsername());
            }
            result = articleTagsMapper.updateByPrimaryKeySelective(tag);

            logService.insertLog("關鍵字標籤",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append(tag.getName()).toString(), request);
        }catch(Exception e){
            HfException.writeFail(logger, e);
        }
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void deleteTag(Integer id, HttpServletRequest request) {
        ArticleTags tag = articleTagsMapper.selectByPrimaryKey(id);
        if(tag == null) {
            throw new BusinessRunTimeException(ExceptionConstants.ARTICLE_TAG_EDIT_FAILED_CODE,
                    ExceptionConstants.ARTICLE_TAG_EDIT_FAILED_MSG);
        }
        try {
            tag.setIsDelete(1);
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if(tag.getUpdatedAt()==null) {
                User user = userService.getCurrentUser();
                tag.setUpdatedAt(now);
                tag.setUpdatedBy(user.getUsername());
            }
            articleTagsMapper.updateByPrimaryKeySelective(tag);

            logService.insertLog("關鍵字標籤",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_DELETE).append(id).toString(), request);
        } catch (Exception e) {
            HfException.writeFail(logger, e);
        }
    }

    public List<JSONObject> getAllArticleTag() {
        List<ArticleTagVo4Article> list = articleTagsMapper.getListByExample(
                null ,null, null, null, null, null);
        List<JSONObject> retList = new ArrayList<>();
        list.stream().forEach(tag->{
            JSONObject json = new JSONObject();
            json.put("id", tag.getId());
            json.put("name", tag.getName());
            retList.add(json);
        });
        return retList;
    }

    public List<ArticleTagVo4Article> getArticleTagList(String keyword, String start, String end, String isDelete,
                                                        Integer currentPage, Integer pageSize) {
        return articleTagsMapper.getListByExample(keyword, start, end, isDelete, currentPage*pageSize, pageSize);
    }

    public int getArticleTagCount(String keyword, String start, String end, String isDelete) {
        return articleTagsMapper.getListByExampleCount(keyword, start, end, isDelete);
    }

    public ArticleTags getTag(Integer id) {
        ArticleTags result = null;
        try{
            result = articleTagsMapper.selectByPrimaryKey(id);
        }catch(Exception e){
            HfException.readFail(logger, e);
        }
        return result;
    }
}
