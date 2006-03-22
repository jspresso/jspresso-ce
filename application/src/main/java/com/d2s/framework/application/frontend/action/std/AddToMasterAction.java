/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.std;

import java.util.HashMap;
import java.util.Map;

import com.d2s.framework.application.frontend.action.ActionWrapper;
import com.d2s.framework.model.descriptor.entity.IEntityDescriptor;
import com.d2s.framework.view.action.ActionContextConstants;

/**
 * Creates and adds an entity to the selected master detail collection.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class AddToMasterAction extends ActionWrapper {

  private IEntityDescriptor elementEntityDescriptor;

  /**
   * Completes the action context with the element entity descriptor
   * parametrized.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public Map<String, Object> getContext() {
    Map<String, Object> context = super.getContext();
    if (context == null) {
      context = new HashMap<String, Object>();
      setContext(context);
    }
    context.put(ActionContextConstants.ELEMENT_DESCRIPTOR,
        elementEntityDescriptor);
    return context;
  }

  /**
   * Sets the elementEntityDescriptor. Entities of this type (which must be a
   * subclass of the collection element) are created and added to the detail
   * collection.
   * 
   * @param elementEntityDescriptor
   *          the elementEntityDescriptor to set.
   */
  public void setElementEntityDescriptor(
      IEntityDescriptor elementEntityDescriptor) {
    this.elementEntityDescriptor = elementEntityDescriptor;
    if (elementEntityDescriptor != null) {
      setName("Add " + elementEntityDescriptor.getName());
      setIconImageURL(elementEntityDescriptor.getIconImageURL());
      setDescription("adds a new " + elementEntityDescriptor.getName());
    }
  }
}
