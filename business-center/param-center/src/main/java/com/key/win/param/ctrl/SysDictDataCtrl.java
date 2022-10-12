package com.key.win.param.ctrl;

import com.key.win.common.exception.business.BizException;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.common.web.Result;
import com.key.win.log.annotation.LogAnnotation;
import com.key.win.param.model.SysDictData;
import com.key.win.param.service.SysDictDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Api("DictData相关的api")
@RequestMapping("/sysDictData")
public class SysDictDataCtrl {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final static String AUTHORITY_PREFIX = "common::param::dict-data::DictData::";
    @Autowired
    private SysDictDataService sysDictDataService;

    @GetMapping("/get/{id}")
    @ApiOperation(value = "get")
    @LogAnnotation(module = "param-center", recordRequestParam = false)
    @PreAuthorize("hasAuthority('" + AUTHORITY_PREFIX + "QUERY::ID')")
    public Result get(@PathVariable Long id) {
        return Result.succeed(sysDictDataService.getSysDictDataById(id), "");
    }

    /**
     * 删除菜单
     *
     * @param id
     */
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除")
    @LogAnnotation(module = "param-center", recordRequestParam = false)
    @PreAuthorize("hasAuthority('" + AUTHORITY_PREFIX + "DELETE')")
    public Result delete(@PathVariable Long id) {
        boolean b = sysDictDataService.deleteSysDictData(id);
        return b ? Result.succeed("删除成功！") : Result.failed("删除失败");
    }

    /**
     * 添加菜单 或者 更新
     *
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value = "新增")
    @LogAnnotation(module = "param-center", recordRequestParam = false)
    @PreAuthorize("hasAnyAuthority('" + AUTHORITY_PREFIX + "MODIFY','" + AUTHORITY_PREFIX + "ADD')")
    public Result saveOrUpdate(@RequestBody SysDictData param) {
        if(param==null){
            logger.error("数据字典为空！！");
            throw  new BizException("数据字典信息为空");
        }
        if(param.getType()==null || param.getType() == -1L){
            logger.error("数据字典类型为空！！");
            throw  new BizException("数据字典类型为空");
        }
        boolean b = sysDictDataService.saveOrUpdateSysDictData(param);
        return b ? Result.succeed("保存成功！") : Result.failed("保存失败");
    }

    @ApiOperation("分页")
    @LogAnnotation(module = "param-center", recordRequestParam = false)
    @PostMapping("/getSysDictDataByPaged")
    @PreAuthorize("hasAuthority('" + AUTHORITY_PREFIX + "QUERY::PAGED')")
    public PageResult<SysDictData> getSysDictDataByPaged(@RequestBody PageRequest<SysDictData> t) {
        return sysDictDataService.getSysDictDataByPaged(t);
    }

    @ApiOperation("根据条件查询")
    @LogAnnotation(module = "param-center", recordRequestParam = false)
    @PostMapping("/findSysDictData")
    @PreAuthorize("hasAuthority('" + AUTHORITY_PREFIX + "QUERY::LIST')")
    public Result getSysDictData(@RequestBody SysDictData sysDictData) {
        List<SysDictData> list = sysDictDataService.findSysDictData(sysDictData);
        return Result.succeed(list, "");
    }

    @ApiOperation("根据Type查询")
    @LogAnnotation(module = "param-center", recordRequestParam = false)
    @GetMapping("/findSysDictData/{type}")
    @PreAuthorize("hasAuthority('" + AUTHORITY_PREFIX + "QUERY::LIST')")
    public Result getSysDictDataByType(@PathVariable Long type) {
        List<SysDictData> list = sysDictDataService.findSysDictDataByType(type);
        return Result.succeed(list, "");
    }

    @ApiOperation("更新状态")
    @LogAnnotation(module = "param-center", recordRequestParam = false)
    @GetMapping("/updateEnabled/{id}/{status}")
    @PreAuthorize("hasAuthority('" + AUTHORITY_PREFIX + "UPDATE::ENABLED')")
    public Result updateEnabled(@PathVariable Long id,@PathVariable Boolean status){
        return Result.succeed(sysDictDataService.updateEnabled(id,status));
    }
}
