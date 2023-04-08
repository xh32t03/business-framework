package com.easyway.business.framework.springmvc.controller;

import java.io.Serializable;

import com.easyway.business.framework.bo.CrudBo;
import com.easyway.business.framework.dao.CrudDao;
import com.easyway.business.framework.pojo.BasePojo;
import com.easyway.business.framework.springmvc.result.ResultBody;

public class CrudController<Pojo extends BasePojo, Bo extends CrudBo<Pojo, ? extends CrudDao<Pojo>>>
        extends SchController<Pojo, Bo> {

    public ResultBody save(Pojo pojo) {
        if (null == pojo) {
            return ResultBody.error("数据不能为空");
        }
        bo.save(pojo);
        return ResultBody.success();
    }

    public ResultBody update(Pojo pojo) {
        if (null == pojo) {
            return ResultBody.error("数据不能为空");
        }
        bo.update(pojo);
        return ResultBody.success();
    }

    public ResultBody delete(Serializable id) {
        if (null == id) {
            return ResultBody.error("数据不能为空");
        }
        bo.delete(id);
        return ResultBody.success();
    }
}
