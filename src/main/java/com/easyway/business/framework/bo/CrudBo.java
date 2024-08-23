package com.easyway.business.framework.bo;

import java.io.Serializable;
import com.easyway.business.framework.common.exception.DaoException;
import com.easyway.business.framework.dao.CrudDao;
import com.easyway.business.framework.pojo.BasePojo;

/**
 * 业务层增、删、改、查通用父类
 * 
 * @author xl.liu
 */
public class CrudBo<T extends BasePojo, Dao extends CrudDao<T>> extends SchBo<T, Dao> {

    /**
     * 新增数据
     * 
     * @param pojo
     */
    public void save(T pojo) {
        try {
            dao.save(pojo);
        } catch (Exception ex) {
            throw new DaoException(ex);
        }
    }

    /**
     * 删除数据
     * 
     * @param id
     */
    public void delete(Serializable id) {
        try {
            dao.delete(id);
        } catch (Exception ex) {
            throw new DaoException(ex);
        }
    }

    /**
     * 修改数据
     * 
     * @param pojo
     */
    public void update(T pojo) {
        try {
            dao.update(pojo);
        } catch (Exception ex) {
            throw new DaoException(ex);
        }
    }

}
