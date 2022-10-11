package com.key.win.common.exception.illegal;

import com.key.win.common.exception.BaseException;

public class UserIllegalException extends BaseException {

	private static final long serialVersionUID = 1L;

	public UserIllegalException(String errorMsg) {
		super(errorMsg);
	}

}
