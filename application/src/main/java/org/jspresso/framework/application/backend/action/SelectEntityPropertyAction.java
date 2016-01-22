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

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.entity.IEntity;

/**
 * A generic action to fill-in the context {@code ActionParameter} with the
 * value of an entity property.
 *
 * @author Vincent Vandenschrick
 */
public class SelectEntityPropertyAction extends BackendAction {

  private String property;

  /**
   * Saves the account information as well as the account devices.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    Object model = getModelConnector(context).getConnectorValue();
    if (model != null) {
      Class<?> targetContract;
      if (model instanceof IComponent) {
        targetContract = ((IComponent) model).getComponentContract();
      } else {
        targetContract = model.getClass();
      }
      try {
        Object propertyValue = getAccessorFactory(context)
            .createPropertyAccessor(property, targetContract)
            .getValue(model);
        setActionParameter(propertyValue, context);
      } catch (IllegalAccessException | NoSuchMethodException ex) {
        throw new ActionException(ex);
      } catch (InvocationTargetException ex) {
        if (ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        throw new ActionException(ex.getCause());
      }
    }
    return true;
  }

  /**
   * Configures the property to extract out of the underlying model.
   *
   * @param property
   *          the property to set.
   */
  public void setProperty(String property) {
    this.property = property;
  }

}
