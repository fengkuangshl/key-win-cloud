package com.key.win.param.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.param.model.SysDictData;

import java.util.List;

public interface SysDictDataService extends IService<SysDictData> {

    PageResult<SysDictData> getSysDictDataByPaged(PageRequest<SysDictData> t);

    List<SysDictData> findSysDictData(SysDictData sysDictData);

    boolean saveOrUpdateSysDictData(SysDictData sysDictData);

    boolean deleteSysDictData(Long id);

    SysDictData getSysDictDataById(Long id);

    List<SysDictData> findSysDictDataByType(Long type);

    boolean updateEnabled(Long id, Boolean status);
}
