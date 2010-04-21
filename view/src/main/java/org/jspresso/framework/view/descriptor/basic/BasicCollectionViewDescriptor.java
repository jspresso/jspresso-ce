/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.view.descriptor.ESelectionMode;
import org.jspresso.framework.view.descriptor.ICollectionViewDescriptor;

/**
 * This is the abstract base descriptor of all views used to display a
 * collection of elements. A collection view must be backed by a collection
 * descriptor model. Most of the time, the model will be a collection property
 * descriptor so that the collecion to display is directly inferred from the
 * collection property value through the binding layer.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class BasicCollectionViewDescriptor extends BasicViewDescriptor
    implements ICollectionViewDescriptor {

  private IAction        itemSelectionAction;
  private IAction        rowAction;
  private ESelectionMode selectionMode = ESelectionMode.MULTIPLE_INTERVAL_SELECTION;

  /**
   * {@inheritDoc}
   */
  @Override
  public String getIconImageURL() {
    String iconImageURL = super.getIconImageURL();
    if (iconImageURL == null
        && getModelDescriptor() instanceof ICollectionDescriptorProvider<?>) {
      IComponentDescriptor<?> elementDescriptor = ((ICollectionDescriptorProvider<?>) getModelDescriptor())
          .getCollectionDescriptor().getElementDescriptor();
      if (elementDescriptor != null) {
        iconImageURL = elementDescriptor.getIconImageURL();
        setIconImageURL(iconImageURL);
      }
    }
    return iconImageURL;
  }

  /**
   * {@inheritDoc}
   */
  public IAction getItemSelectionAction() {
    return itemSelectionAction;
  }

  /**
   * {@inheritDoc}
   */
  public IAction getRowAction() {
    return rowAction;
  }

  /**
   * Gets the selectionMode.
   * 
   * @return the selectionMode.
   */
  public ESelectionMode getSelectionMode() {
    return selectionMode;
  }

  /**
   * Registers an action that is implicitely triggered everytime the selection
   * changes on the collection view UI peer. The context of the action execution
   * is the same as if the action was registered in the view action map.
   * 
   * @param itemSelectionAction
   *          the itemSelectionAction to set.
   */
  public void setItemSelectionAction(IAction itemSelectionAction) {
    this.itemSelectionAction = itemSelectionAction;
  }

  /**
   * Registers an action that is implicitely triggered everytime a row is
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

  /**
   * Sets the selection mode of the collection view. This is either a value of
   * the <code>ESelectionMode</code> enum or its equivalent string
   * representation :
   * <ul>
   * <li><code>MULTIPLE_INTERVAL_SELECTION</code> for allowing any type of
   * selection</li>
   * <li><code>SINGLE_INTERVAL_SELECTION</code> for allowing only contiguous
   * interval selection</li>
   * <li><code>SINGLE_SELECTION</code> for allowing only a single item selection
   * </li>
   * </ul>
   * <p>
   * Default value is <code>ESelectionMode.MULTIPLE_INTERVAL_SELECTION</code>,
   * i.e. any type of selection allowed.
   * 
   * @param selectionMode
   *          the selectionMode to set.
   */
  public void setSelectionMode(ESelectionMode selectionMode) {
    this.selectionMode = selectionMode;
  }

}
