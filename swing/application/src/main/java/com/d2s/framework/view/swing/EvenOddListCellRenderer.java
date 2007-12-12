/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.swing;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import com.d2s.framework.util.swing.SwingUtil;

/**
 * A default list cell renderer rendering even and odd rows background slightly
 * differently.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EvenOddListCellRenderer extends DefaultListCellRenderer {

  private static final long serialVersionUID = 2051850807889065438L;

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getListCellRendererComponent(JList list, Object value,
      int index, boolean isSelected, boolean cellHasFocus) {
    Component renderer = super.getListCellRendererComponent(list, value, index,
        isSelected, cellHasFocus);
    SwingUtil.alternateEvenOddBackground(renderer, list, isSelected, index);
    return renderer;
  }
}
