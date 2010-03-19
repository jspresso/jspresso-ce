/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.action;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.model.entity.IEntity;

/**
 * A generic action to fill-in the context ACTION_PARAM with the value of an
 * entity property.
 * 
 * @version $LastChangedRevision: 475 $
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
  public boolean execute(
      @SuppressWarnings("unused") IActionHandler actionHandler,
      Map<String, Object> context) {
    IEntity model = (IEntity) getModelConnector(context).getConnectorValue();
    if (model != null) {
      try {
        Object propertyValue = getAccessorFactory(context)
            .createPropertyAccessor(property, model.getComponentContract())
            .getValue(model);
        setActionParameter(propertyValue, context);
      } catch (IllegalAccessException ex) {
        throw new ActionException(ex);
      } catch (InvocationTargetException ex) {
        if (ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        throw new ActionException(ex.getCause());
      } catch (NoSuchMethodException ex) {
        throw new ActionException(ex);
      }
    }
    return true;
  }

  /**
   * Sets the property.
   * 
   * @param property
   *          the property to set.
   */
  public void setProperty(String property) {
    this.property = property;
  }

}
