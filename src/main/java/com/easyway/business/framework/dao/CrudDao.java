package com.easyway.business.framework.dao;

import java.io.Serializable;
import com.easyway.business.framework.pojo.BasePojo;

/**
 * 持久层增、删、改、查通用父接口
 * 
 * @author xl.liu
 */
public interface CrudDao<T extends BasePojo> extends SchDao<T> {

    /**
     * 新增数据库记录
     * 
     * @param pojo
     * @return 包含<selectKey>语句返回值为主键,否则为null
     */
    public int save(T pojo);

    /**
     * 删除数据库指定主键记录
     * 
     * @param id
     * @return 删除的行数
     */
    public int delete(Serializable id);

    /**
     * 更新数据库记录
     * 
     * @param pojo
     * @return 更新的行数
     */
    public int update(T pojo);
}
