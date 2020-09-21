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
import java.util.Iterator;
import java.util.List;

import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 树节点工具类
 *
 * @author system
 */
@Slf4j
public class TreeUtil {

    /**
     * 构建整个菜单树
     *
     * @param nodeList 节点集合
     * @param rootPid 根节点
     */
    public static void buildNodeTree(List<? extends BaseTreeNode> nodeList, Integer rootPid) {
        for (BaseTreeNode treeNode : nodeList) {
            List<? extends BaseTreeNode> linkedList = findChildNodes(nodeList, treeNode.getId(), rootPid);
            if (linkedList.size() > 0) {
                treeNode.setChildren(linkedList);
            }
        }
    }

    /**
     * 查询子节点的集合
     *
     * @param nodeList 树节点集合
     * @param parentId 父节点
     * @param rootPid 根节点
     * @return 子节点集合
     */
    private static List<? extends BaseTreeNode> findChildNodes(List<? extends BaseTreeNode> nodeList, Integer parentId,
                                                               Integer rootPid) {
        if (nodeList == null && parentId == null) {
            return null;
        }
        List<BaseTreeNode> linkedList = new ArrayList<>();
        for (Iterator<? extends BaseTreeNode> iterator = nodeList.iterator(); iterator.hasNext();) {
            BaseTreeNode node = iterator.next();
            // 根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (!node.getParentId().equals(rootPid) && parentId.equals(node.getParentId())) {
                recursionFn(nodeList, node, parentId, linkedList);
            }
        }
        return linkedList;
    }

    /**
     * 遍历一个节点的子节点
     *
     * @param nodeList 节点集合
     * @param node 树形结构基本要素
     * @param pId 父节点
     * @param linkedList 子节点集合
     */
    private static void recursionFn(List<? extends BaseTreeNode> nodeList, BaseTreeNode node, Integer pId,
                                    List<BaseTreeNode> linkedList) {
        // 得到子节点列表
        List<? extends BaseTreeNode> childList = getChildList(nodeList, node);
        // 判断是否有子节点
        if (childList.size() > 0) {
            if (node.getParentId().equals(pId)) {
                linkedList.add(node);
            }
            Iterator<? extends BaseTreeNode> it = childList.iterator();
            while (it.hasNext()) {
                BaseTreeNode n = it.next();
                recursionFn(nodeList, n, pId, linkedList);
            }
        } else {
            if (node.getParentId().equals(pId)) {
                linkedList.add(node);
            }
        }
    }

    /**
     * 得到子节点列表
     *
     * @param list 树形节点集合
     * @param node 树形结构基本要素
     * @return 子节点列表
     */
    private static List getChildList(List<? extends BaseTreeNode> list, BaseTreeNode node) {
        List nodeList = new ArrayList();
        Iterator<? extends BaseTreeNode> it = list.iterator();
        while (it.hasNext()) {
            BaseTreeNode n = it.next();
            if (n.getParentId().equals(node.getId())) {
                nodeList.add(n);
            }
        }
        return nodeList;
    }

    /**
     * 获取某个级别的树形
     *
     * @param nodes 节点集合
     * @param level 菜单级别
     */
    public static void genLevelTree(List<? extends BaseTreeNode> nodes, Integer level) {
        if (null != level && !CollectionUtils.isEmpty(nodes)) {
            Iterator<? extends BaseTreeNode> it = nodes.iterator();
            while (it.hasNext()) {
                BaseTreeNode node = it.next();
                if (!level.equals(node.getLevel())) {
                    it.remove();
                }
            }
        }
    }

    /**
     * 树形结构中子节点排序
     *
     * @param treeNodeList 树节点集合
     */
    public static void sortTreeChildNode(List<? extends BaseTreeNode> treeNodeList) {
        for (BaseTreeNode menuNode : treeNodeList) {
            if (menuNode.getChildren() != null && menuNode.getChildren().size() > 0) {
                Collections.sort(menuNode.getChildren());
            }
        }
    }
}
