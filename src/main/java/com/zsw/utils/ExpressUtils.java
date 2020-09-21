/*
 * Copyright 2018 yifen7.com All right reserved. This software is the
 * confidential and proprietary information of yifen7.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with yifen7.com.
 */

package com.zsw.utils;

import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

/**
 * 表达式处理工具类
 *
 * @author xujiali
 * @since 2018年10月18日 下午3:16:40
 */
public class ExpressUtils {

    /**
     * 表达式中参数标识前缀
     */
    public static final String  EXPRESSION_PREFIX    = "[";
    /**
     * 表达式中参数标识后缀
     */
    public static final String  EXPRESSION_SUFFIX    = "]";
    /**
     * 表达式String类型标识
     */
    private static final String EXPRESSION_STR_AFFIX = "'";
    /**
     * 表达式String类型标识
     */
    private static final String EXPRESSION_END_AFFIX = "'";
    /**
     * 表达式空字符串
     */
    private static final String EXPRESSION_EMPTY     = "";

    /**
     * 参数按照具体类型进行解析
     *
     * @param itemValue 参数值
     * @return 参数解析后结果
     */
    private static String itemValueParse(Object itemValue) {
        String result = null;
        if (itemValue instanceof String) {
            result = String.format("%s%s%s", EXPRESSION_STR_AFFIX, String.valueOf(itemValue), EXPRESSION_END_AFFIX);
        } else {
            result = String.valueOf(itemValue);
        }
        return result;
    }

    /**
     * 表达式处理工具类
     *
     * @param expression 表达式
     * @param paramMap 参数map数据
     * @return 解析后表达式
     */
    public static String replaceExpressItem(String expression, Map<String, Object> paramMap) {
        String result = expression;
        List<String> expressionParamList = StringDealUtils.subStrWithAffix(expression, EXPRESSION_PREFIX,
                EXPRESSION_SUFFIX);
        String paramStr = null;
        String paramValue = null;
        if (!CollectionUtils.isEmpty(expressionParamList)) {
            for (int i = 0; i < expressionParamList.size(); i++) {
                paramStr = expressionParamList.get(i);
                Object paramObject = paramMap.get(paramStr.replace(EXPRESSION_PREFIX, EXPRESSION_EMPTY)
                        .replace(EXPRESSION_SUFFIX, EXPRESSION_EMPTY));
                paramValue = itemValueParse(paramObject);
                result = result.replace(paramStr, paramValue);
            }
        }
        return result;
    }

}
