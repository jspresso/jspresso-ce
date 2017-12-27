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

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.IMvcBinder;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.util.event.IValueChangeListener;
import org.jspresso.framework.util.event.ValueChangeEvent;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.IViewFactory;
import org.jspresso.framework.view.descriptor.IRepeaterViewDescriptor;

/**
 * Repeater for JComponents.
 *
 * @author Vincent Vandenschrick
 */
public class JRepeater {

  private IView<JComponent>                      repeaterView;
  private Container                              container;
  private IViewFactory<JComponent, Icon, Action> viewFactory;
  private Action                                 rowAction;
  private IActionHandler                         actionHandler;
  private Locale                                 locale;
  private IMvcBinder                             mvcBinder;
  private Map<IValueConnector, JComponent>       componentTank;
  private MouseListener                          selectionListener;

  /**
   * Instantiates a new J repeater.
   *
   * @param repeaterView
   *     the repeater view
   * @param container
   *     the container
   * @param viewFactory
   *     the view factory
   * @param mvcBinder
   *     the mvc binder
   * @param rowAction
   *     the row action
   * @param actionHandler
   *     the action handler
   * @param locale
   *     the locale
   */
  public JRepeater(IView<JComponent> repeaterView, JComponent container, DefaultSwingViewFactory viewFactory,
                   IMvcBinder mvcBinder, Action rowAction, IActionHandler actionHandler, Locale locale) {
    this.repeaterView = repeaterView;
    this.container = container;
    this.viewFactory = viewFactory;
    this.mvcBinder = mvcBinder;
    this.componentTank = new HashMap<>();
    this.actionHandler = actionHandler;
    this.locale = locale;
    this.rowAction = rowAction;

    ICollectionConnector collectionConnector = (ICollectionConnector) repeaterView.getConnector();
    collectionConnector.addValueChangeListener(new IValueChangeListener() {
      @Override
      public void valueChange(ValueChangeEvent evt) {
        rebindDataProvider();
      }
    });
    selectionListener = new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        if (e.getClickCount() > 1) {
          childDblClicked(e);
        } else {
          childClicked(e);
        }
      }
    };
  }

  private void rebindDataProvider() {
    IRepeaterViewDescriptor viewDescriptor = (IRepeaterViewDescriptor) repeaterView.getDescriptor();
    ICollectionConnector collectionConnector = (ICollectionConnector) repeaterView.getConnector();
    container.removeAll();
    for (int i = 0; i < collectionConnector.getChildConnectorCount(); i++) {
      IValueConnector child = collectionConnector.getChildConnector(i);
      JComponent component = componentTank.get(child);
      if (component == null) {
        IView<JComponent> repeatedView = viewFactory.createView(viewDescriptor.getRepeatedViewDescriptor(),
            actionHandler, locale);
        repeatedView.setParent(repeaterView);
        component = repeatedView.getPeer();
        deepAttachListener(component);
        mvcBinder.bind(repeatedView.getConnector(), child);
        componentTank.put(child, component);
      }
      container.add(component);
    }
  }

  private void deepAttachListener(Component component) {
    component.addMouseListener(selectionListener);
    if (component instanceof Container) {
      for (Component child : ((Container) component).getComponents()) {
        deepAttachListener(child);
      }
    }
  }

  private void childClicked(MouseEvent evt) {
    ICollectionConnector collectionConnector = (ICollectionConnector) repeaterView.getConnector();
    Component child = (Component) evt.getSource();
    int index = -1;
    while (index < 0 && child != null) {
      index = Arrays.asList(container.getComponents()).indexOf(child);
      child = child.getParent();
    }
    if (index >= 0) {
      collectionConnector.setSelectedIndices(new int[]{index}, index);
    } else {
      collectionConnector.setSelectedIndices(new int[0], -1);
    }
  }

  private void childDblClicked(MouseEvent evt) {
    if (rowAction != null) {
      ActionEvent ae = new ActionEvent(evt.getSource(), ActionEvent.ACTION_PERFORMED, null, evt.getWhen(),
          evt.getModifiers());
      rowAction.actionPerformed(ae);
    }
  }
}
