/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
 * Default implementation of a collection view descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class BasicCollectionViewDescriptor extends BasicViewDescriptor
    implements ICollectionViewDescriptor {

  private ESelectionMode selectionMode = ESelectionMode.MULTIPLE_INTERVAL_SELECTION;
  private IAction        rowAction;
  private IAction        itemSelectionAction;

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
   * Gets the selectionMode.
   * 
   * @return the selectionMode.
   */
  public ESelectionMode getSelectionMode() {
    return selectionMode;
  }

  /**
   * Sets the selectionMode.
   * 
   * @param selectionMode
   *          the selectionMode to set.
   */
  public void setSelectionMode(ESelectionMode selectionMode) {
    this.selectionMode = selectionMode;
  }

  /**
   * {@inheritDoc}
   */
  public IAction getRowAction() {
    return rowAction;
  }

  /**
   * Sets the rowAction.
   * 
   * @param rowAction
   *          the rowAction to set.
   */
  public void setRowAction(IAction rowAction) {
    this.rowAction = rowAction;
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

}
