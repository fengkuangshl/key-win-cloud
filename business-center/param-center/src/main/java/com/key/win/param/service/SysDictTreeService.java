package com.key.win.param.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.param.model.SysDictTree;

import java.util.List;
import java.util.Set;

public interface SysDictTreeService extends IService<SysDictTree> {

    PageResult<SysDictTree> findSysDictTreeByPaged(PageRequest<SysDictTree> t);

    PageResult<SysDictTree> findChildrenNodeByParentId(PageRequest<SysDictTree> t);

    PageResult<SysDictTree> findParentNodeByParentId(PageRequest<SysDictTree> t);

    List<SysDictTree> findSysDictTree(SysDictTree sysDictTree);

    List<SysDictTree> findSysDictTreeByParentId(Long parentId);

    List<SysDictTree> getDictTree(Long type);

    List<SysDictTree> findLeafNode(Long type);

    boolean saveOrUpdateDictTree(SysDictTree sysDictTree);

    boolean deleteById(Long id);

    SysDictTree getDictTreeFullById(Long id);

    List<SysDictTree> getSysDictTreeByIds(Set<Long> ids);

    boolean updateEnabled(Long id, Boolean status);
}
