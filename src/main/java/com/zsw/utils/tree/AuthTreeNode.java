/*
 * Copyright 2018 yifen7.com All right reserved. This software is the
 * confidential and proprietary information of yifen7.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with yifen7.com.
 */

package com.zsw.utils.tree;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 用户权限树形结构
 *
 * @author xujiali
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class AuthTreeNode extends BaseTreeNode {

    /**
     * 是否打开节点
     */
    private Boolean open;

    /**
     * 是否被选中
     */
    private Boolean checked;

    /**
     * 备注
     */
    private String  remark;

    @Override
    public int compareTo(Object o) {
        return super.compareTo(o);
    }
}
