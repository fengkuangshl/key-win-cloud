package com.key.win.sms.service;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.key.win.common.web.PageResult;
import com.key.win.sms.model.Sms;

import java.util.Map;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
 * @version 创建时间：2017年11月12日 上午22:57:51
 * 短信service 
 */
public interface SmsService {

	/**
	 * 保存短信
	 * @param sms
	 * @param params
	 */
	void save(Sms sms, Map<String, String> params);

	/**
	 * 修改短信
	 * @param sms
	 */
	void update(Sms sms);

	/**
	 * 查询短信
	 * @param id
	 * @return
	 */
	Sms findById(Long id);

	/**
	 * 短信列表
	 * @param params
	 * @return
	 */
	PageResult<Sms> findSms(Map<String, Object> params);

	/**
	 * 发送短信
	 */
	SendSmsResponse sendSmsMsg(Sms sms);
}
