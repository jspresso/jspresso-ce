/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.binding;

/**
 * This class is a helper for connector bindings for an MVC relationship.
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
  @Override
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
    if (modelConnector != null) {
      modelConnector.boundAsModel();
    }
  }

  private void bindChildren(ICompositeValueConnector viewConnector,
      ICompositeValueConnector modelConnector) {
    for (String nextConnectorId : viewConnector.getChildConnectorKeys()) {
      IValueConnector nextChildViewConnector = viewConnector
          .getChildConnector(nextConnectorId);
      if (modelConnector != null) {
        IValueConnector nextChildModelConnector = modelConnector
            .getChildConnector(/* nextChildViewConnector.getId() */nextConnectorId);
        if (nextChildModelConnector == null) {
          throw new MissingConnectorException("Missing model connector for id "
              + nextChildViewConnector.getId());
        }
        bind(nextChildViewConnector, nextChildModelConnector);
      } else {
        bind(nextChildViewConnector, null);
      }
    }
  }
}
