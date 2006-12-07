/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

/**
 * This class is a helper for connector bindings for an MVC relationship.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultMvcBinder implements IMvcBinder {

  /**
   * Binds two connectors altogether.
   * 
   * @param viewConnector
   *          The connector for the view
   * @param modelConnector
   *          The connector for the model
   */
  public void bind(IValueConnector viewConnector, IValueConnector modelConnector) {
    if (viewConnector.getModelConnector() == modelConnector) {
      return;
    }
    viewConnector.setModelConnector(modelConnector);
    if (viewConnector instanceof ICompositeValueConnector) {
      bindChildren((ICompositeValueConnector) viewConnector,
          (ICompositeValueConnector) modelConnector);
    }
    viewConnector.boundAsView();
  }

  private void bindChildren(ICompositeValueConnector viewConnector,
      ICompositeValueConnector modelConnector) {
    for (String nextConnectorId : viewConnector.getChildConnectorKeys()) {
      IValueConnector nextChildViewConnector = viewConnector
          .getChildConnector(nextConnectorId);
      if (modelConnector != null) {
        IValueConnector nextChildModelConnector = modelConnector
            .getChildConnector(nextChildViewConnector.getId());
        if (nextChildModelConnector == null) {
          throw new MissingConnectorException("Missing model connector for id "
              + nextConnectorId);
        }
        bind(nextChildViewConnector, nextChildModelConnector);
      } else {
        bind(nextChildViewConnector, null);
      }
    }
  }
}
