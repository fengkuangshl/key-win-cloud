package com.key.win.common.exception.controller;

import com.key.win.common.exception.BaseException;

/**
 * blog: https://blog.51cto.com/13005375 
 * code: https://gitee.com/owenwangwen/key-win-cloud
 */

public class ControllerException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1412104290896291466L;

	public ControllerException(String msg) {
		super(msg);
	}

	public ControllerException(Exception e) {
		this(e.getMessage());
	}

}
