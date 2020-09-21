/*
 * Copyright 2018 yifen7.com All right reserved. This software is the
 * confidential and proprietary information of yifen7.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with yifen7.com.
 */

package com.zsw.utils.tree;

import lombok.Data;

/**
 * 地址信息属性节点
 *
 * @author system
 */
@Data
public class DistrictTreeNode extends BaseTreeNode {

    private String code;

    private String lable;
}
