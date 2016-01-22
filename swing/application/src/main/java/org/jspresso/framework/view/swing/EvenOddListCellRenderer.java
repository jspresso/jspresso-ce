/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.view.swing;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ToolTipManager;

import org.jspresso.framework.binding.IRenderableCompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.swing.CollectionConnectorListModel;
import org.jspresso.framework.util.swing.SwingUtil;
import org.jspresso.framework.view.IIconFactory;

/**
 * A list cell renderer alternating background color.
 *
 * @author Vincent Vandenschrick
 */
public class EvenOddListCellRenderer extends DefaultListCellRenderer {

  private static final long serialVersionUID = 2051850807889065438L;
  private       Color              backgroundBase;
  private final IIconFactory<Icon> iconFactory;
  private final String             cellConnectorKey;
  private final boolean            displayIcon;

  /**
   * Constructs a new {@code EvenOddListCellRenderer} instance.
   *
   * @param iconFactory
   *     the icon factory.
   * @param cellConnectorKey
   *     the key used to retrieve the child cell connector.
   * @param displayIcon
   *     the display icon
   */
  public EvenOddListCellRenderer(IIconFactory<Icon> iconFactory, String cellConnectorKey, boolean displayIcon) {
    super();
    this.iconFactory = iconFactory;
    this.cellConnectorKey = cellConnectorKey;
    this.displayIcon = displayIcon;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                                                boolean cellHasFocus) {
    JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    if (value instanceof IValueConnector) {
      if (value instanceof IRenderableCompositeValueConnector) {
        IValueConnector cellConnector = ((IRenderableCompositeValueConnector) value).getChildConnector(
            cellConnectorKey);
        if (cellConnector.getConnectorValue() != null) {
          renderer.setText(cellConnector.getConnectorValue().toString());
        } else {
          renderer.setText("");
        }
        if (displayIcon) {
          renderer.setIcon(iconFactory.getIcon(((IRenderableCompositeValueConnector) value).getDisplayIcon(),
              iconFactory.getSmallIconSize()));
        }
        ListModel<?> lm = list.getModel();
        if (lm instanceof CollectionConnectorListModel) {
          setToolTipText(((CollectionConnectorListModel) lm).getRowToolTip(index));
        }
        if (((IRenderableCompositeValueConnector) value).getDisplayDescription() != null) {
          ToolTipManager.sharedInstance().registerComponent(list);
          renderer.setToolTipText(((IRenderableCompositeValueConnector) value).getDisplayDescription());
        }
      } else {
        renderer.setText(value.toString());
      }
    }
    Color actualBackground = list.getBackground();
    if (backgroundBase != null) {
      actualBackground = backgroundBase;
    }
    renderer.setBackground(SwingUtil.computeEvenOddBackground(actualBackground, isSelected, index));
    return renderer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setBackground(Color c) {
    backgroundBase = c;
    super.setBackground(c);
  }
}
