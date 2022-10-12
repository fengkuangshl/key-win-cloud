package com.key.win.param.utils;

import com.key.win.common.util.KeyWinConstantUtils;

public class ParamUtils {
    public static final String PARAMENT = "parament:";
    public static final String SYS_DICT_TYPE = PARAMENT + "sys-dict-type:";
    public static final String REDIS_SYS_DICT_TYPE_KEY_PREFIX = KeyWinConstantUtils.REDIS_ROOT_KEY_PREFIX + SYS_DICT_TYPE;

    public static final String SYS_DICT_TREE = PARAMENT + "sys-dict-tree:";
    public static final String REDIS_SYS_DICT_TREE_KEY_PREFIX = KeyWinConstantUtils.REDIS_ROOT_KEY_PREFIX + SYS_DICT_TREE;


    public static final String SYS_DICT_DATA = PARAMENT + "sys-dict-data:";
    public static final String REDIS_SYS_DICT_DATA_KEY_PREFIX = KeyWinConstantUtils.REDIS_ROOT_KEY_PREFIX + SYS_DICT_DATA;
}
