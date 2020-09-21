/*
 * Copyright 2018 yifen7.com All right reserved. This software is the
 * confidential and proprietary information of yifen7.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with yifen7.com.
 */

package com.zsw.utils.tree;

import java.io.Serializable;

import lombok.Data;

/**
 * jquery ztree 插件的节点
 *
 * @author fengshuonan
 * @since 2017年2月17日 下午8:25:14
 */
@Data
public class ZTreeNode implements Serializable {

    private static final long serialVersionUID = 1L;
    //节点id
    private Integer           id;
    //父节点id
    private Integer           pId;
    //节点名称
    private String            name;
    //节点备注
    private String            remark;
    //是否打开节点
    private Boolean           open;
    //是否被选中
    private Boolean           checked;

    private Integer           level;
    // 排序
    private Integer           num;

    /**
     * 创建父节点
     *
     * @return 父级节点
     */
    public static ZTreeNode createParent() {
        ZTreeNode zTreeNode = new ZTreeNode();
        zTreeNode.setChecked(true);
        zTreeNode.setId(0);
        zTreeNode.setName("顶级");
        zTreeNode.setOpen(true);
        zTreeNode.setPId(0);
        zTreeNode.setNum(0);
        return zTreeNode;
    }
}
