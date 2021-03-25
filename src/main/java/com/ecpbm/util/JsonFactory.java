package com.ecpbm.util;

import java.util.ArrayList;
import java.util.List;

import com.ecpbm.pojo.TreeNode;


public class JsonFactory {
	public static List<TreeNode> buildtree(List<TreeNode> nodes, int id) {
		List<TreeNode> treeNodes = new ArrayList<TreeNode>();
		for (TreeNode treeNode : nodes) {
			TreeNode node = new TreeNode();
			node.setId(treeNode.getId());
			node.setText(treeNode.getText());
			if (id == treeNode.getFid()) {
				//遞給調用buildtree方法給TreeNode中的children屬性賦值
				node.setChildren(buildtree(nodes, node.getId()));
				treeNodes.add(node);
			}
		}
		return treeNodes;
	}
}
