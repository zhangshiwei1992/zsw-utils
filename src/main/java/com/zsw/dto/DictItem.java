package com.zsw.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;

/**
 * 封装字典选项值
 *
 * @param <T> 泛型
 * @author Jerry.Chen
 * @since 2018年5月9日 下午4:33:50
 */
@Data
public class DictItem<T extends Serializable> implements Serializable {
    private static final long   serialVersionUID = -4979428360062313584L;
    /**
     * 值
     */
    private T                   value;
    /**
     * 代码
     */
    private String              code;
    /**
     * 名称
     */
    private String              name;
    /**
     * 描述信息
     */
    private String              desc;
    /**
     * 扩展信息
     */
    private Map<String, Object> extraInfo;

    /**
     * 增加扩展信息
     *
     * @param key 扩展信息中的key
     * @param value 扩展信息中的value
     */
    public void addExtraInfo(String key, Object value) {
        if (this.extraInfo == null) {
            this.extraInfo = new HashMap<>();
        }
        this.extraInfo.put(key, value);
    }

    /**
     * 获取扩展信息
     *
     * @param key 扩展信息中的key
     * @return 扩展信息中的value
     */
    public Object getExtraInfo(String key) {
        return (this.extraInfo != null ? this.extraInfo.get(key) : null);
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
