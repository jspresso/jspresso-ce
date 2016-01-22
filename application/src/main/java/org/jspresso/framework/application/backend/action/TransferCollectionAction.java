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
package org.jspresso.framework.application.backend.action;

import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.datatransfer.ComponentTransferStructure;
import org.jspresso.framework.model.datatransfer.ETransferMode;

/**
 * An action used to register a collection of domain objects into the
 * application's clipboard along with a transfer mode semantics.
 *
 * @author Vincent Vandenschrick
 */
public class TransferCollectionAction extends BackendAction {

  private ETransferMode transferMode;

  /**
   * Retrieves the managed collection from the model connector then registers
   * the selected elements.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    List<IComponent> transferedComponents = getSelectedModels(context);
    getController(context).storeComponents(
        new ComponentTransferStructure<>(transferedComponents,
            transferMode));
    return super.execute(actionHandler, context);
  }

  /**
   * Configures the transferMode to use when pasting will be requested, i.e. :
   * <ul>
   * <li>{@code ETransferMode.COPY} for copy semantics.</li>
   * <li>{@code ETransferMode.MOVE} for move/cut semantics.</li>
   * </ul>
   *
   * @param transferMode
   *          the transferMode to set.
   */
  public void setTransferMode(ETransferMode transferMode) {
    this.transferMode = transferMode;
  }
}
