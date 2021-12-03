package com.key.win.uaa.service;

import com.key.win.common.model.auth.SysClient;
import com.key.win.common.web.PageResult;
import com.key.win.common.web.Result;

import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public interface SysClientService {

	
	Result saveOrUpdate(SysClient clientDto);
	
	void delete(String id);
	
	Result updateEnabled(Map<String, Object> params);
	
	SysClient getById(String id) ;

  
    
    public PageResult<SysClient> list(Map<String, Object> params);
    
    List<SysClient> findList(Map<String, Object> params) ;
    

	
    
}
