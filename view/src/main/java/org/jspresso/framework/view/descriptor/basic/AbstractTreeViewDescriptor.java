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
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicCollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicListDescriptor;
import org.jspresso.framework.util.gui.Icon;
import org.jspresso.framework.util.gui.IconProvider;
import org.jspresso.framework.view.descriptor.ITreeLevelDescriptor;
import org.jspresso.framework.view.descriptor.ITreeViewDescriptor;

/**
 * This descriptor is use to design a tree view. The way to define a tree view
 * in Jspresso is a matter of assembling <i>tree level descriptors</i>
 * hierarchically. A <i>tree level descriptor</i> is a group of sibling nodes
 * that usually represent a component collection property. Each individual tree
 * node collection can be secured by using role-based authorization (i.e.
 * {@code grantedRoles}) on its descriptor.
 *
 * @author Vincent Vandenschrick
 */
public abstract class AbstractTreeViewDescriptor extends BasicViewDescriptor implements ITreeViewDescriptor {

  private ITreeLevelDescriptor       childDescriptor;
  private List<ITreeLevelDescriptor> childrenDescriptors;
  private IconProvider               iconImageURLProvider;

  private int maxDepth = 10;
  private String               renderedProperty;
  private ITreeLevelDescriptor rootSubtreeDescriptor;
  private IAction              itemSelectionAction;
  private boolean              displayIcon;
  private IAction rowAction;

  /**
   * Instantiates a new Abstract tree view descriptor.
   */
  public AbstractTreeViewDescriptor() {
    displayIcon = true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Icon getIcon() {
    Icon icon = super.getIcon();
    if (icon == null) {
      icon = getRootSubtreeDescriptor().getNodeGroupDescriptor().getIcon();
      setIcon(icon);
    }
    return icon;
  }

  /**
   * Gets the iconImageURLProvider.
   *
   * @return the iconImageURLProvider.
   */
  @Override
  public IconProvider getIconImageURLProvider() {
    return iconImageURLProvider;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMaxDepth() {
    return maxDepth;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public ITreeLevelDescriptor getRootSubtreeDescriptor() {
    if (rootSubtreeDescriptor == null) {
      BasicListDescriptor<Object> fakeCollDescriptor = new BasicListDescriptor<>();
      fakeCollDescriptor.setElementDescriptor((IComponentDescriptor<Object>) getModelDescriptor());
      BasicCollectionPropertyDescriptor<Object> fakeCollPropDescriptor = new BasicCollectionPropertyDescriptor<>();
      fakeCollPropDescriptor.setReferencedDescriptor(fakeCollDescriptor);
      BasicListViewDescriptor fakeListViewDescriptor = new BasicListViewDescriptor();
      fakeListViewDescriptor.setRenderedProperty(renderedProperty);
      fakeListViewDescriptor.setModelDescriptor(fakeCollPropDescriptor);
      if (childDescriptor != null) {
        rootSubtreeDescriptor = new BasicSimpleTreeLevelDescriptor();
        ((BasicSimpleTreeLevelDescriptor) rootSubtreeDescriptor).setNodeGroupDescriptor(fakeListViewDescriptor);
        ((BasicSimpleTreeLevelDescriptor) rootSubtreeDescriptor).setChildDescriptor(childDescriptor);
      } else if (childrenDescriptors != null) {
        rootSubtreeDescriptor = new BasicCompositeTreeLevelDescriptor();
        ((BasicCompositeTreeLevelDescriptor) rootSubtreeDescriptor).setNodeGroupDescriptor(fakeListViewDescriptor);
        ((BasicCompositeTreeLevelDescriptor) rootSubtreeDescriptor).setChildrenDescriptors(childrenDescriptors);
      }
    }
    return rootSubtreeDescriptor;
  }

  /**
   * Configures the first tree level as being a single collection of sibling
   * nodes. For instance, if the child tree level is mapped to a collection
   * (collA) containing 5 elements (collA_Elt-1 to 5), the tree would look like
   * :
   * <p/>
   * <pre>
   * rootItem
   *   coll<b>A</b>_Elt-<b>1</b>
   *   coll<b>A</b>_Elt-<b>2</b>
   *   coll<b>A</b>_Elt-<b>3</b>
   *   coll<b>A</b>_Elt-<b>4</b>
   *   coll<b>A</b>_Elt-<b>5</b>
   * </pre>
   * <p/>
   * In the example above, you should notice that there is no need for the tree
   * to install an intermediary node to visually group the collection elements
   * since the collection is alone on its level.
   * <p/>
   * This property is only used if the {@code rootSubtreeDescriptor} is not
   * explicitly set. In the latter case, nested subtrees are determined from
   * the {@code rootSubtreeDescriptor}.
   *
   * @param childDescriptor
   *     the childDescriptor to set.
   */
  public void setChildDescriptor(ITreeLevelDescriptor childDescriptor) {
    this.childDescriptor = childDescriptor;
    this.childrenDescriptors = null;
  }

  /**
   * Configures the first tree level as being a list of collections of sibling
   * nodes (subtrees). For instance, if the children tree levels are mapped to 2
   * collection properties (collA, collB) each containing 3 elements
   * (collA_Elt-1 to 3 and collB_Elt-1 to 3), the tree would look like :
   * <p/>
   * <pre>
   * rootItem
   *   <i>collA</i>
   *     coll<b>A</b>_Elt-<b>1</b>
   *     coll<b>A</b>_Elt-<b>2</b>
   *     coll<b>A</b>_Elt-<b>3</b>
   *   <i>collB</i>
   *     coll<b>B</b>_Elt-<b>1</b>
   *     coll<b>B</b>_Elt-<b>2</b>
   *     coll<b>B</b>_Elt-<b>3</b>
   * </pre>
   * <p/>
   * In the example above, you should notice intermediate collection property
   * grouping nodes (collA and collB in italic). They automatically appeared to
   * clearly group the tree nodes belonging to the different collections.
   * <p/>
   * This property is only used if the {@code rootSubtreeDescriptor} is not
   * explicitly set. In the latter case, nested subtrees are determined from
   * the {@code rootSubtreeDescriptor}.
   *
   * @param childrenDescriptors
   *     the childrenDescriptor to set.
   */
  public void setChildrenDescriptors(List<ITreeLevelDescriptor> childrenDescriptors) {
    this.childrenDescriptors = childrenDescriptors;
    this.childDescriptor = null;
  }

  /**
   * The icon image URL provider is the delegate responsible for inferring a
   * tree node icon based on its underlying model. By default (i.e. when
   * {@code iconImageURLProvider} is {@code null}), Jspresso will use
   * the underlying component descriptor icon, if any. Using a custom icon image
   * URL provider allows to implement finer rules like using different icons
   * based on the underlying object state. There is a single method to implement
   * to achieve this :
   * <p/>
   * {@code String getIconImageURLForObject(Object userObject);}
   *
   * @param iconImageURLProvider
   *     the iconImageURLProvider to set.
   */
  public void setIconImageURLProvider(IconProvider iconImageURLProvider) {
    this.iconImageURLProvider = iconImageURLProvider;
  }

  /**
   * This property is used only when the tree (or sub-tree) is declared
   * recursively, i.e. a tree level belongs to its own children hierarchy.
   * Default value is <i>10</i>, meaning that a maximum number of 10 levels can
   * be nested.
   *
   * @param maxDepth
   *     the maxDepth to set.
   */
  public void setMaxDepth(int maxDepth) {
    this.maxDepth = maxDepth;
  }

  /**
   * This property allows to define the model property used to label the root
   * node.
   * <p/>
   * This property is only used if the {@code rootSubtreeDescriptor} is not
   * explicitly set. In the latter case, {@code renderedProperty} is
   * determined from the {@code rootSubtreeDescriptor}.
   *
   * @param renderedProperty
   *     the renderedProperty to set.
   */
  public void setRenderedProperty(String renderedProperty) {
    this.renderedProperty = renderedProperty;
  }

  /**
   * This property allows to explicitly define the tree root level as any other
   * level. Most of the time, you will prefer using the following shortcut
   * properties :
   * <ul>
   * <li>{@code childDescriptor}</li>
   * <li>{@code childrenDescriptors}</li>
   * <li>{@code renderedProperty}</li>
   * </ul>
   * Whenever {@code rootSubtreeDescriptor} is explicitly set, the
   * properties above are simply ignored since all values are determined from
   * {@code rootSubtreeDescriptor}.
   *
   * @param rootSubtreeDescriptor
   *     the rootSubtreeDescriptor to set.
   */
  public void setRootSubtreeDescriptor(ITreeLevelDescriptor rootSubtreeDescriptor) {
    this.rootSubtreeDescriptor = rootSubtreeDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IAction getItemSelectionAction() {
    return itemSelectionAction;
  }

  /**
   * This property allows to bind an action that gets triggered every time the
   * selection changes on the tree view. The action context passed to the action
   * when it is executed is the same as if it had been registered on the tree
   * view.
   *
   * @param itemSelectionAction
   *     the itemSelectionAction to set.
   */
  public void setItemSelectionAction(IAction itemSelectionAction) {
    this.itemSelectionAction = itemSelectionAction;
  }

  /**
   * Gets whether the tree view should be created expanded.
   *
   * @return true if the tree view should be created expanded.
   */
  @Override
  public boolean isDisplayIcon() {
    return displayIcon;
  }

  /**
   * Configures if the tree view should show icon based on the icon image url provider. Defaults to {@code true}.
   *
   * @param displayIcon
   *     the show icon
   */
  public void setDisplayIcon(boolean displayIcon) {
    this.displayIcon = displayIcon;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IAction getRowAction() {
    return rowAction;
  }

  /**
   * Registers an action that is implicitly triggered every time a row is
   * activated (e.g. double-clicked for current UI channels) on the collection
   * view UI peer. The context of the action execution is the same as if the
   * action was registered in the view action map.
   *
   * @param rowAction
   *          the rowAction to set.
   */
  public void setRowAction(IAction rowAction) {
    this.rowAction = rowAction;
  }
}
