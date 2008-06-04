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
package org.jspresso.framework.binding.masterdetail;

import org.jspresso.framework.binding.ConnectorSelectionEvent;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IConnectorSelectionListener;
import org.jspresso.framework.binding.IConnectorSelector;
import org.jspresso.framework.binding.IMvcBinder;
import org.jspresso.framework.binding.IValueConnector;

/**
 * Default implementation of <code>IModelCascadingBinder</code>.
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
public class DefaultModelCascadingBinder implements IModelCascadingBinder {

  private IMvcBinder mvcBinder;

  /**
   * {@inheritDoc}
   */
  public void bind(IValueConnector masterConnector,
      IValueConnector detailConnector) {
    if (masterConnector instanceof IConnectorSelector) {
      ((IConnectorSelector) masterConnector)
          .addConnectorSelectionListener(new BoundConnectorSelectionListener(
              detailConnector));
    } else if (masterConnector instanceof ICompositeValueConnector) {
      ((ICompositeValueConnector) masterConnector)
          .addChildConnector(detailConnector);
    }
  }

  /**
   * Sets the mvcBinder.
   * 
   * @param mvcBinder
   *            the mvcBinder to set.
   */
  public void setMvcBinder(IMvcBinder mvcBinder) {
    this.mvcBinder = mvcBinder;
  }

  private final class BoundConnectorSelectionListener implements
      IConnectorSelectionListener {

    private IValueConnector detailConnector;

    /**
     * Constructs a new <code>BoundConnectorSelectionListener</code> instance.
     * 
     * @param detailConnector
     *            The detail connector tracking master connector's selection.
     */
    public BoundConnectorSelectionListener(IValueConnector detailConnector) {
      this.detailConnector = detailConnector;
    }

    /**
     * {@inheritDoc}
     */
    public void selectedConnectorChange(ConnectorSelectionEvent event) {
      if (event.getSelectedConnector() != null) {
        mvcBinder.bind(detailConnector, event.getSelectedConnector()
            .getModelConnector());
      } else {
        mvcBinder.bind(detailConnector, null);
      }
    }
  }
}
