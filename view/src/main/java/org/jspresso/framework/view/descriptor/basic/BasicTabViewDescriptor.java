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
package org.jspresso.framework.view.descriptor.basic;

import java.util.List;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.util.gui.ERenderingOptions;
import org.jspresso.framework.view.descriptor.ITabViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * This composite view arranges its children in tabs. Each tab potentially
 * displays a label (that is translated based on the name of the view in the
 * tab), an icon (based on the icon of the view in the tab) and a toolTip (based
 * on the description of the view in the tab).
 * <p/>
 * Default cascading order follows the order of nested view registrations in the
 * container.
 *
 * @author Vincent Vandenschrick
 */
public class BasicTabViewDescriptor extends BasicCompositeViewDescriptor implements ITabViewDescriptor {

  private ERenderingOptions renderingOptions = ERenderingOptions.LABEL_ICON;
  private List<IViewDescriptor> tabs;
  private boolean               lazy;
  private IAction               tabSelectionAction;


  /**
   * Constructs a new {@code BasicTabViewDescriptor} instance.
   */
  public BasicTabViewDescriptor() {
    this.lazy = true;
  }

  /**
   * {@inheritDoc}
   *
   * @return the child view descriptors
   */
  @Override
  public List<IViewDescriptor> getChildViewDescriptors() {
    if (tabs != null) {
      IViewDescriptor previousViewDescriptor = null;
      for (IViewDescriptor childViewDescriptor : tabs) {
        completeChildDescriptor(childViewDescriptor, previousViewDescriptor);
        previousViewDescriptor = childViewDescriptor;
      }
    }
    return tabs;
  }

  /**
   * Gets the renderingOptions.
   *
   * @return the renderingOptions.
   */
  @Override
  public ERenderingOptions getRenderingOptions() {
    return renderingOptions;
  }

  /**
   * Indicates how the tabs should be rendered. This is either a value of the
   * {@code ERenderingOptions} enum or its equivalent string representation
   * :
   * <ul>
   * <li>{@code LABEL_ICON} for label and icon</li>
   * <li>{@code LABEL} for label only</li>
   * <li>{@code ICON} for icon only.</li>
   * </ul>
   * <p/>
   * Default value is {@code ERenderingOptions.LABEL_ICON}, i.e. label and
   * icon.
   *
   * @param renderingOptions
   *     the renderingOptions to set.
   */
  public void setRenderingOptions(ERenderingOptions renderingOptions) {
    this.renderingOptions = renderingOptions;
  }

  /**
   * Registers the list of views to be displayed as tabs. The tabs order follows
   * the children views order of this list.
   *
   * @param tabs
   *     the tabs to set.
   */
  public void setTabs(List<IViewDescriptor> tabs) {
    this.tabs = tabs;
  }

  /**
   * Sets the viewDescriptors.
   *
   * @param viewDescriptors
   *     the viewDescriptors to set.
   * @deprecated use setTabs instead.
   */
  @Deprecated
  public void setViewDescriptors(List<IViewDescriptor> viewDescriptors) {
    setTabs(viewDescriptors);
  }

  /**
   * Gets the lazy.
   *
   * @return the lazy.
   */
  @Override
  public boolean isLazy() {
    return lazy && !isCascadingModels();
  }

  /**
   * When set to true, this parameter configures the tabs to be lazy bound
   * (binding occurs only for the selected tab). This feature is only supported
   * for tab views with {@code cascadingModel} set to false. default value
   * is {@code true}.
   *
   * @param lazy
   *     the lazy to set.
   */
  public void setLazy(boolean lazy) {
    this.lazy = lazy;
  }

  /**
   * Gets tab selection action.
   *
   * @return the tab selection action
   */
  @Override
  public IAction getTabSelectionAction() {
    return tabSelectionAction;
  }

  /**
   * Registers an action that is implicitly triggered every time the tab selection
   * changes on the tab view UI peer. The context of the action execution
   * is the same as if the action was registered in the view action map.
   *
   * @param tabSelectionAction
   *     the tab selection action
   */
  public void setTabSelectionAction(IAction tabSelectionAction) {
    this.tabSelectionAction = tabSelectionAction;
  }
}
