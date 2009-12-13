/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
import org.jspresso.framework.util.gui.IIconImageURLProvider;
import org.jspresso.framework.view.descriptor.ITreeLevelDescriptor;
import org.jspresso.framework.view.descriptor.ITreeViewDescriptor;

/**
 * This descriptor is use to design a tree view. The way to define a tree view
 * in Jspresso is a matter of assembling <i>tree level descriptors</i>
 * hierarchically. A <i>tree level descriptor</i> is a group of sibling nodes
 * that represent a collection property.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicTreeViewDescriptor extends BasicViewDescriptor implements
    ITreeViewDescriptor {

  private ITreeLevelDescriptor       childDescriptor;
  private List<ITreeLevelDescriptor> childrenDescriptors;
  private IIconImageURLProvider      iconImageURLProvider;
  private int                        maxDepth = 10;

  private String                     renderedProperty;
  private ITreeLevelDescriptor       rootSubtreeDescriptor;
  private IAction                    itemSelectionAction;
  private boolean                    expanded;

  /**
   * Constructs a new <code>BasicTreeViewDescriptor</code> instance.
   */
  public BasicTreeViewDescriptor() {
    expanded = false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getIconImageURL() {
    String iconImageURL = super.getIconImageURL();
    if (iconImageURL == null) {
      iconImageURL = getRootSubtreeDescriptor().getNodeGroupDescriptor()
          .getIconImageURL();
      setIconImageURL(iconImageURL);
    }
    return iconImageURL;
  }

  /**
   * Gets the iconImageURLProvider.
   * 
   * @return the iconImageURLProvider.
   */
  public IIconImageURLProvider getIconImageURLProvider() {
    return iconImageURLProvider;
  }

  /**
   * {@inheritDoc}
   */
  public int getMaxDepth() {
    return maxDepth;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public ITreeLevelDescriptor getRootSubtreeDescriptor() {
    if (rootSubtreeDescriptor == null) {
      BasicListDescriptor<Object> fakeCollDescriptor = new BasicListDescriptor<Object>();
      fakeCollDescriptor
          .setElementDescriptor((IComponentDescriptor<Object>) getModelDescriptor());
      BasicCollectionPropertyDescriptor<Object> fakeCollPropDescriptor = new BasicCollectionPropertyDescriptor<Object>();
      fakeCollPropDescriptor.setReferencedDescriptor(fakeCollDescriptor);
      BasicListViewDescriptor fakeListViewDescriptor = new BasicListViewDescriptor();
      fakeListViewDescriptor.setRenderedProperty(renderedProperty);
      fakeListViewDescriptor.setModelDescriptor(fakeCollPropDescriptor);
      if (childDescriptor != null) {
        rootSubtreeDescriptor = new BasicSimpleTreeLevelDescriptor();
        ((BasicSimpleTreeLevelDescriptor) rootSubtreeDescriptor)
            .setNodeGroupDescriptor(fakeListViewDescriptor);
        ((BasicSimpleTreeLevelDescriptor) rootSubtreeDescriptor)
            .setChildDescriptor(childDescriptor);
      } else if (childrenDescriptors != null) {
        rootSubtreeDescriptor = new BasicCompositeTreeLevelDescriptor();
        ((BasicCompositeTreeLevelDescriptor) rootSubtreeDescriptor)
            .setNodeGroupDescriptor(fakeListViewDescriptor);
        ((BasicCompositeTreeLevelDescriptor) rootSubtreeDescriptor)
            .setChildrenDescriptors(childrenDescriptors);
      }
    }
    return rootSubtreeDescriptor;
  }

  /**
   * Configures the first tree level as being a single collection of sibling
   * nodes. For instance, if the child tree level is mapped to a collection
   * (collA) containing 5 elements (collA_Elt-1 to 5), the tree would look like
   * :
   * 
   * <pre>
   * rootItem
   *   coll<b>A</b>_Elt-<b>1</b>
   *   coll<b>A</b>_Elt-<b>2</b>
   *   coll<b>A</b>_Elt-<b>3</b>
   *   coll<b>A</b>_Elt-<b>4</b>
   *   coll<b>A</b>_Elt-<b>5</b>
   * </pre>
   * 
   * In the example above, you should notice that there is no need for the tree
   * to install an intermediary node to visually group the collection elements
   * since the collection is alone on its level.
   * 
   * @param childDescriptor
   *          the childDescriptor to set.
   */
  public void setChildDescriptor(ITreeLevelDescriptor childDescriptor) {
    this.childDescriptor = childDescriptor;
    this.childrenDescriptors = null;
  }

  /**
   * Sets the iconImageURLProvider.
   * 
   * @param iconImageURLProvider
   *          the iconImageURLProvider to set.
   */
  public void setIconImageURLProvider(IIconImageURLProvider iconImageURLProvider) {
    this.iconImageURLProvider = iconImageURLProvider;
  }

  /**
   * Sets the maxDepth.
   * 
   * @param maxDepth
   *          the maxDepth to set.
   */
  public void setMaxDepth(int maxDepth) {
    this.maxDepth = maxDepth;
  }

  /**
   * Sets the renderedProperty.
   * 
   * @param renderedProperty
   *          the renderedProperty to set.
   */
  public void setRenderedProperty(String renderedProperty) {
    this.renderedProperty = renderedProperty;
  }

  /**
   * Sets the rootSubtreeDescriptor.
   * 
   * @param rootSubtreeDescriptor
   *          the rootSubtreeDescriptor to set.
   */
  public void setRootSubtreeDescriptor(
      ITreeLevelDescriptor rootSubtreeDescriptor) {
    this.rootSubtreeDescriptor = rootSubtreeDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public IAction getItemSelectionAction() {
    return itemSelectionAction;
  }

  /**
   * Sets the itemSelectionAction.
   * 
   * @param itemSelectionAction
   *          the itemSelectionAction to set.
   */
  public void setItemSelectionAction(IAction itemSelectionAction) {
    this.itemSelectionAction = itemSelectionAction;
  }

  /**
   * Gets the expanded.
   * 
   * @return the expanded.
   */
  public boolean isExpanded() {
    return expanded;
  }

  /**
   * Sets the expanded.
   * 
   * @param expanded
   *          the expanded to set.
   */
  public void setExpanded(boolean expanded) {
    this.expanded = expanded;
  }

  /**
   * Configures the first tree level as being a list of collections of sibling
   * nodes. For instance, if the children tree levels are mapped to 2 collection
   * properties (collA, collB) each containing 3 elements (collA_Elt-1 to 3 and
   * collB_Elt-1 to 3), the tree would look like :
   * 
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
   * 
   * In the example above, you should notice intermediate collection property
   * grouping nodes (collA and collB in italic). They automatically appeared to
   * clearly group the tree nodes belonging to the different collections.
   * 
   * @param childrenDescriptors
   *          the childrenDescriptor to set.
   */
  public void setChildrenDescriptors(
      List<ITreeLevelDescriptor> childrenDescriptors) {
    this.childrenDescriptors = childrenDescriptors;
    this.childDescriptor = null;
  }
}
