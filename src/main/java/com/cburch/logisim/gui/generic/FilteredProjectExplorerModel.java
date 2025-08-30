package com.cburch.logisim.gui.generic;

import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.cburch.logisim.proj.Project;
import com.cburch.logisim.proj.ProjectListener;

public class FilteredProjectExplorerModel extends ProjectExplorerModel {
  private static final long serialVersionUID = 1L;
  private String filter = "";
  private final JTree tree;
  // private TreeModel originalModel;

  FilteredProjectExplorerModel(Project proj, JTree gui, boolean showMouseTools) {
    super(proj, gui, showMouseTools);
    this.tree = gui;
    // this.proj = proj;
    // this.showMouseTools = showMouseTools;
    // setRoot(new ProjectExplorerLibraryNode(this, proj.getLogisimFile(), gui,
    // showMouseTools));
    // proj.addProjectListener(this);
    // uiElement = gui;
  }

  // public FilteredTreeModel(TreeModel originalModel) {
  // super((TreeNode) originalModel.getRoot());
  // this.originalModel = originalModel;
  // }

  public void setFilter(String filter) {
    this.filter = filter.toLowerCase();
    // This will reload the tree and apply the filter
    reload();
    // if (!this.filter.isEmpty()) {
    //   for (int i = 0; i < tree.getRowCount(); i++) {
    //     this.tree.expandRow(i);
    //   }
    // }
  }

  @Override
  public Object getChild(Object parent, int index) {
    int count = 0;
    for (int i = 0; i < super.getChildCount(parent); i++) {
      Object child = super.getChild(parent, i);
      if (matches(child)) {
        if (count == index) {
          return child;
        }
        count++;
      }
    }
    return null;
  }

  @Override
  public int getChildCount(Object parent) {
    int count = 0;
    for (int i = 0; i < super.getChildCount(parent); i++) {
      if (matches(super.getChild(parent, i))) {
        count++;
      }
    }
    return count;
  }

  // This is the core filtering logic
  private boolean matches(Object node) {
    String nodeText = node.toString().toLowerCase();
    if (nodeText.contains(filter)) {
      return true;
    }

    for (int i = 0; i < super.getChildCount(node); i++) {
      if (matches(super.getChild(node, i))) {
        return true;
      }
    }
    return false;
  }

  private void expandMatchingNodes(TreeNode node) {
    // Using an enumeration to traverse the tree
    if (node instanceof DefaultMutableTreeNode) {
      Enumeration<?> e = ((DefaultMutableTreeNode) node).breadthFirstEnumeration();
      while (e.hasMoreElements()) {
        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) e.nextElement();
        String nodeText = currentNode.getUserObject().toString().toLowerCase();

        // Check if the node itself is a leaf and matches
        if (currentNode.isLeaf() && nodeText.contains(filter)) {
          // Create a TreePath and expand it.
          // The TreePath must be constructed from the nodes in the *filtered* model
          // which is why we get the path from the superclass (DefaultTreeModel).
          TreeNode[] nodes = getPathToRoot(currentNode);
          TreePath path = new TreePath(nodes);
          tree.expandPath(path);
        }
      }
    }
  }
}
