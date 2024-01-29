package com.dfrecvcle.dfsystem.service.other;

import com.alibaba.fastjson.JSONObject;
import com.dfrecvcle.dfsystem.constants.BusinessConstants;
import com.dfrecvcle.dfsystem.constants.ExceptionConstants;
import com.dfrecvcle.dfsystem.exception.BusinessRunTimeException;
import com.dfrecvcle.dfsystem.exception.HfException;
import com.dfrecvcle.dfsystem.live.entities.*;
import com.dfrecvcle.dfsystem.live.mappers.ArticleCategoryMapper;
import com.dfrecvcle.dfsystem.live.mappers.OtherCategoryMapper;
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
public class OtherCategoryService {

    private Logger logger = LoggerFactory.getLogger(OtherCategoryService.class);

    @Resource
    private OtherCategoryMapper otherCategoryMapper;

    @Resource
    private UserService userService;

    @Resource
    private LogService logService;

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void insert(OtherCategory category, HttpServletRequest request) {
        if(category.getName() == null || category.getName().isEmpty()) {
            throw new BusinessRunTimeException(ExceptionConstants.ARTICLE_CATEGORY_ADD_FAILED_CODE,
                    ExceptionConstants.ARTICLE_CATEGORY_ADD_FAILED_MSG);
        }
        int count = otherCategoryMapper.isExistCategoryName(null, category.getName());
        if(count > 0) {
            throw new BusinessRunTimeException(ExceptionConstants.ARTICLE_CATEGORY_IS_EXIST_CODE,
                    String.format(ExceptionConstants.ARTICLE_CATEGORY_IS_EXIST_MSG, category.getName()));
        }

        int result = 0;
        try{
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if(category.getCreatedAt()==null) {
                User user = userService.getCurrentUser();
                category.setCreatedAt(now);
                category.setCreatedBy(user.getUsername());
            }
            if(category.getSort()==null) {
                category.setSort(1);
            }
            if(category.getIsTop()==null) {
                category.setIsTop(0);
            }
            if(category.getIsPublish()==null) {
                category.setIsPublish(0);
            }
            if(category.getIsEnable()==null) {
                category.setIsEnable(1);
            }
            if(category.getIsDelete()==null) {
                category.setIsDelete(0);
            }
            category.setUpdatedAt(now);
            category.setUpdatedBy(null);
            result = otherCategoryMapper.insertArticleCategory(category);

            logService.insertLog("文章分類",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_ADD).append(category.getName()).toString(), request);
        }catch(Exception e){
            HfException.writeFail(logger, e);
        }
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void update(OtherCategory category, HttpServletRequest request) {
        if(category.getId() == null) {
            throw new BusinessRunTimeException(ExceptionConstants.ARTICLE_CATEGORY_EDIT_FAILED_CODE,
                    ExceptionConstants.ARTICLE_CATEGORY_EDIT_FAILED_MSG);
        }

        // 分類名稱已存在
        if(category.getName() != null && !category.getName().isEmpty()) {
            int count = otherCategoryMapper.isExistCategoryName(category.getId(), category.getName());
            if(count > 0) {
                throw new BusinessRunTimeException(ExceptionConstants.ARTICLE_CATEGORY_IS_EXIST_CODE,
                        String.format(ExceptionConstants.ARTICLE_CATEGORY_IS_EXIST_MSG, category.getName()));
            }
        }

        int result = 0;
        try{
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if(category.getUpdatedAt()==null) {
                User user = userService.getCurrentUser();
                category.setUpdatedAt(now);
                category.setUpdatedBy(user.getUsername());
            }
            result = otherCategoryMapper.updateByPrimaryKeySelective(category);

            logService.insertLog("文章分類",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append(category.getName()).toString(), request);
        }catch(Exception e){
            HfException.writeFail(logger, e);
        }
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void deleteCategory(Integer id, HttpServletRequest request) {
        OtherCategory tag = otherCategoryMapper.selectByPrimaryKey(id);
        if(tag == null) {
            throw new BusinessRunTimeException(ExceptionConstants.ARTICLE_CATEGORY_EDIT_FAILED_CODE,
                    ExceptionConstants.ARTICLE_CATEGORY_EDIT_FAILED_MSG);
        }
        try {
            tag.setIsDelete(1);
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if(tag.getUpdatedAt()==null) {
                User user = userService.getCurrentUser();
                tag.setUpdatedAt(now);
                tag.setUpdatedBy(user.getUsername());
            }
            otherCategoryMapper.updateByPrimaryKeySelective(tag);

            logService.insertLog("文章分類",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_DELETE).append(id).toString(), request);
        } catch (Exception e) {
            HfException.writeFail(logger, e);
        }
    }

    public List<JSONObject> getAllArticleCategory() {
        List<OtherCategory> list = otherCategoryMapper.getListAll();
        List<JSONObject> retList = new ArrayList<>();
        System.out.println(list.size());
        list.stream().forEach(tag->{
            JSONObject json = new JSONObject();
            json.put("id", tag.getId());
            json.put("name", tag.getName());
            json.put("code", tag.getCode());
            retList.add(json);
        });
        return retList;
    }

    public List<OtherResponse> getAllMeta() {

        return otherCategoryMapper.getListMetaAll();
    }

    public List<OtherCategory4Article> getArticleCategoryList(String keyword, String start, String end, String isDelete,
                                                              Integer currentPage, Integer pageSize) {
        return otherCategoryMapper.getListByExample(keyword, start, end, isDelete, currentPage*pageSize, pageSize);
    }

    public int getArticleCategoryCount(String keyword, String start, String end, String isDelete) {
        return otherCategoryMapper.getListByExampleCount(keyword, start, end, isDelete);
    }

    public OtherCategory getCategory(Integer id) {
        OtherCategory result = null;
        try{
            result = otherCategoryMapper.selectByPrimaryKey(id);
        }catch(Exception e){
            HfException.readFail(logger, e);
        }
        return result;
    }

}
