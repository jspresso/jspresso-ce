/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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
