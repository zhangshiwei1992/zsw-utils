/*
 * Copyright 2018 yifen7.com All right reserved. This software is the
 * confidential and proprietary information of yifen7.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with yifen7.com.
 */

package com.zsw.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;

import app.myoss.cloud.mybatis.table.TableMetaObject;
import lombok.extern.slf4j.Slf4j;

/**
 * 实体类处理工具类
 *
 * @author xujiali
 * @since 2018年10月18日 下午4:26:34
 */
@Slf4j
public class BeanUtils {

    /**
     * 获取对象的所有可修改的属性列表
     *
     * @param object 对象
     * @return 可修改的属性列表
     */
    public static List<Field> genObjectFieldList(Object object) {
        return TableMetaObject.getFieldList(object.getClass());
    }

    /**
     * 获取属性名(name)，属性值(value)的map
     *
     * @param object 对象
     * @return 对象属性信息map
     */
    public static Map<String, Object> convertObjToMap(Object object) {
        log.info("convertObjToMap param:{}", JSON.toJSON(object));
        Map<String, Object> result = new HashMap<>();
        try {
            List<Field> fieldList = genObjectFieldList(object);
            if (!CollectionUtils.isEmpty(fieldList)) {
                for (Field field : fieldList) {
                    field.setAccessible(true);
                    result.put(field.getName(), field.get(object));
                }
            }
        } catch (Exception e) {
            log.error("convertObjToMap exception:{}", e);
        }
        log.info("convertObjToMap result:{}", JSON.toJSON(result));
        return result;
    }
}
