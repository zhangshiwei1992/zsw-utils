/*
 * Copyright 2018 yifen7.com All right reserved. This software is the
 * confidential and proprietary information of yifen7.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with yifen7.com.
 */

package com.zsw.utils.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 菜单节点信息
 *
 * @author fengshuonan
 * @since 2016年12月6日 上午11:34:17
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuNode extends BaseTreeNode {

    /**
     * 按钮级别
     */
    private Integer        levels;

    /**
     * 按钮级别
     */
    private Integer        ismenu;

    /**
     * 喵购车HOST - node-zulin-loan: http://biz-node-loan-dev.miaogoche.cn
     */
    private String         miaoGouCheLoanHost;

    /**
     * 节点的url
     */
    private String         url;

    /**
     * 节点图标
     */
    private String         icon;

    /**
     * 动作
     */
    private String         action;

    /**
     * 查询子节点时候的临时集合
     */
    private List<MenuNode> linkedList = new ArrayList<>();

    /**
     * 构建整个菜单树
     *
     * @param nodeList 节点集合
     */
    public void buildNodeTree(List<MenuNode> nodeList) {
        TreeUtil.buildNodeTree(nodeList, 0);
    }

    /**
     * 清除掉按钮级别的资源
     *
     * @param nodes 节点集合
     * @return 执行结果
     */
    public static List<MenuNode> clearBtn(List<MenuNode> nodes) {
        ArrayList<MenuNode> noBtns = new ArrayList<>();
        for (MenuNode node : nodes) {
            if (node.getIsmenu() == 1) {
                noBtns.add(node);
            }
        }
        return noBtns;
    }

    /**
     * 清除所有二级菜单
     *
     * @param nodes 节点集合
     * @return 执行结果
     */
    public static List<MenuNode> clearLevelTwo(List<MenuNode> nodes) {
        ArrayList<MenuNode> results = new ArrayList<>();
        for (MenuNode node : nodes) {
            Integer levels = node.getLevels();
            if (levels.equals(1)) {
                results.add(node);
            }
        }
        return results;
    }

    /**
     * 构建菜单列表
     *
     * @param nodes 节点结婚
     * @return 构建菜单列表
     */
    public static List<MenuNode> buildTitle(List<MenuNode> nodes) {

        List<MenuNode> clearBtn = clearBtn(nodes);

        new MenuNode().buildNodeTree(clearBtn);

        List<MenuNode> menuNodes = clearLevelTwo(clearBtn);

        //对菜单排序
        Collections.sort(menuNodes);

        TreeUtil.sortTreeChildNode(menuNodes);

        return menuNodes;
    }
}
