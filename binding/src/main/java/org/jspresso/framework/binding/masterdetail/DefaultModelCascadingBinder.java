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
package org.jspresso.framework.binding.masterdetail;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.jspresso.framework.binding.IMvcBinder;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.util.event.IItemSelectable;
import org.jspresso.framework.util.event.IItemSelectionListener;
import org.jspresso.framework.util.event.ItemSelectionEvent;

/**
 * Default implementation of {@code IModelCascadingBinder}.
 *
 * @author Vincent Vandenschrick
 */
public class DefaultModelCascadingBinder implements IModelCascadingBinder {

  private IMvcBinder mvcBinder;

  /**
   * {@inheritDoc}
   */
  @Override
  public void bind(IValueConnector masterConnector,
      final IValueConnector detailConnector) {
    if (masterConnector instanceof IItemSelectable) {
      ((IItemSelectable) masterConnector)
          .addItemSelectionListener(new BoundConnectorSelectionListener(
              detailConnector));
    } else {
      // Do not simply add the detail connector since it will propagate
      // writability of the master to the detail which is wrong. see bug#342.
      // } else if (masterConnector instanceof ICompositeValueConnector) {
      // ((ICompositeValueConnector) masterConnector)
      // .addChildConnector(detailConnector);
      mvcBinder.bind(detailConnector, masterConnector.getModelConnector());
      masterConnector.addPropertyChangeListener(
          IValueConnector.MODEL_CONNECTOR_PROPERTY,
          new BoundConnectorModelListener(detailConnector));
    }
  }

  /**
   * Sets the mvcBinder.
   *
   * @param mvcBinder
   *          the mvcBinder to set.
   */
  public void setMvcBinder(IMvcBinder mvcBinder) {
    this.mvcBinder = mvcBinder;
  }

  private final class BoundConnectorSelectionListener implements
      IItemSelectionListener {

    private final IValueConnector detailConnector;

    /**
     * Constructs a new {@code BoundConnectorSelectionListener} instance.
     *
     * @param detailConnector
     *          The detail connector tracking master connector's selection.
     */
    public BoundConnectorSelectionListener(IValueConnector detailConnector) {
      this.detailConnector = detailConnector;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void selectedItemChange(ItemSelectionEvent event) {
      if (event.getSelectedItem() != null) {
        mvcBinder.bind(detailConnector,
            ((IValueConnector) event.getSelectedItem()).getModelConnector());
      } else {
        mvcBinder.bind(detailConnector, null);
      }
    }
  }

  private final class BoundConnectorModelListener implements
      PropertyChangeListener {

    private final IValueConnector detailConnector;

    /**
     * Constructs a new {@code BoundConnectorModelListener} instance.
     *
     * @param detailConnector
     *          The detail connector tracking master connector's model.
     */
    public BoundConnectorModelListener(IValueConnector detailConnector) {
      this.detailConnector = detailConnector;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      IValueConnector newModelConnector = (IValueConnector) evt.getNewValue();
      mvcBinder.bind(detailConnector, newModelConnector);
      detailConnector.setModelConnector(newModelConnector);
    }
  }
}
