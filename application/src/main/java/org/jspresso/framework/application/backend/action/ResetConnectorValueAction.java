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

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;

/**
 * Resets the model connector value to null.
 *
 * @author Vincent Vandenschrick
 */
public class ResetConnectorValueAction extends BackendAction {

  private String modelPath;

  /**
   * Resets the model connector value to null.
   * <p>
   * {@inheritDoc}
   *
   * @param actionHandler
   *     the action handler
   * @param context
   *     the context
   * @return the boolean
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    getModelConnector(getModelPath(context), context).setConnectorValue(null);
    return super.execute(actionHandler, context);
  }

  /**
   * Gets model path.
   *
   * @param context
   *     the context
   * @return the model path
   */
  protected String getModelPath(Map<String, Object> context) {
    return modelPath;
  }

  /**
   * Sets model path.
   *
   * @param modelPath
   *     the model path
   */
  public void setModelPath(String modelPath) {
    this.modelPath = modelPath;
  }
}
