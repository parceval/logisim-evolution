/*
 * Logisim-evolution - digital logic design tool and simulator
 * Copyright by the Logisim-evolution developers
 *
 * https://github.com/logisim-evolution/
 *
 * This is free software released under GNU GPLv3 license
 */

package com.cburch.logisim.gui.main;

import com.cburch.draw.toolbar.Toolbar;
import com.cburch.logisim.gui.generic.ProjectExplorerModel;
import com.cburch.logisim.gui.generic.ProjectExplorer;
import com.cburch.logisim.gui.menu.MenuListener;
import com.cburch.logisim.proj.Project;
import com.cburch.logisim.tools.Tool;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

class Toolbox extends JPanel {
  private static final long serialVersionUID = 1L;
  private final ProjectExplorer toolbox;

  Toolbox(Project proj, Frame frame, MenuListener menu) {
    super(new BorderLayout());

    final var toolbarModel = new ToolboxToolbarModel(frame, menu);
    final var toolbar = new Toolbar(toolbarModel);
    add(toolbar, BorderLayout.NORTH);

    // Search Panel
    JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JTextField searchField = new JTextField(20);
    JButton searchButton = new JButton("Search");
    JButton clearButton = new JButton("Clear");
    searchPanel.add(new JLabel("Search:"));
    searchPanel.add(searchField);
    searchPanel.add(searchButton);
    searchPanel.add(clearButton);

    add(searchPanel, BorderLayout.SOUTH);

    clearButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        searchField.setText("");
        ((ProjectExplorerModel) toolbox.getModel()).setFilter(searchField.getText());
      }
    });

    // Add action listener to the search button
    searchButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ((ProjectExplorerModel) toolbox.getModel()).setFilter(searchField.getText());
      }
    });

    // Add action listener to the search field for "Enter" key press
    searchField.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ((ProjectExplorerModel) toolbox.getModel()).setFilter(searchField.getText());
      }
    });

    toolbox = new ProjectExplorer(proj, false);
    toolbox.setListener(new ToolboxManip(proj, toolbox));
    add(new JScrollPane(toolbox), BorderLayout.CENTER);

    toolbarModel.menuEnableChanged(menu);
  }

  void setHaloedTool(Tool value) {
    toolbox.setHaloedTool(value);
  }

  public void updateStructure() {
    toolbox.updateStructure();
  }
}
