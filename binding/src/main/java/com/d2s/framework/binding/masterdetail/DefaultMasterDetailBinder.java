/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.masterdetail;

import com.d2s.framework.binding.ConnectorSelectionEvent;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.binding.IConnectorSelectionListener;
import com.d2s.framework.binding.IConnectorSelector;
import com.d2s.framework.binding.IMvcBinder;
import com.d2s.framework.binding.IValueConnector;

/**
 * Default implementation of <code>IMasterDetailBinder</code>.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultMasterDetailBinder implements IMasterDetailBinder {

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

  private final class BoundConnectorSelectionListener implements
      IConnectorSelectionListener {

    private IValueConnector detailConnector;

    /**
     * Constructs a new <code>BoundConnectorSelectionListener</code> instance.
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
    public void selectedConnectorChange(ConnectorSelectionEvent event) {
      IValueConnector masterConnector = event.getSelectedConnector();
      if (masterConnector != null) {
        if (detailConnector.getModelConnector() == null
            && masterConnector.getModelConnector() != null) {
          mvcBinder.bind(detailConnector, masterConnector.getModelConnector()
              .clone());
        }
        if (detailConnector.getModelConnector() != null) {
          detailConnector.getModelConnector().setConnectorValue(
              masterConnector.getConnectorValue());
        }
      } else {
        if (detailConnector.getModelConnector() != null) {
          detailConnector.getModelConnector().setConnectorValue(null);
        }
      }
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
}
