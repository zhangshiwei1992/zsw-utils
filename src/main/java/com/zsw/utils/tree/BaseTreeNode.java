/*
 * Copyright 2018 yifen7.com All right reserved. This software is the
 * confidential and proprietary information of yifen7.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with yifen7.com.
 */

package com.zsw.utils.tree;

import java.util.List;

import lombok.Data;

/**
 * 树形结构基本要素
 *
 * @author xujiali
 */
@Data
public class BaseTreeNode implements Comparable {

    /**
     * 节点id
     */
    private Integer id;

    /**
     * 父节点
     */
    private Integer parentId;

    /**
     * 节点名称
     */
    private String  name;

    /**
     * 子节点的集合
     */
    private List    children;

    /**
     * 按钮的排序
     */
    private Integer num;

    /**
     * 按钮级别
     */
    private Integer level;

    @Override
    public int compareTo(Object o) {
        if (null == this.num) {
            return 0;
        }
        BaseTreeNode menuNode = (BaseTreeNode) o;
        Integer num = menuNode.getNum();
        if (num == null) {
            num = 0;
        }
        return this.num.compareTo(num);
    }
}
