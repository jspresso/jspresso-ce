/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.std;

import java.util.HashMap;
import java.util.Map;

import com.d2s.framework.application.frontend.action.ActionWrapper;
import com.d2s.framework.model.descriptor.entity.IEntityDescriptor;
import com.d2s.framework.view.action.ActionContextConstants;
import com.d2s.framework.view.action.IActionHandler;

/**
 * Creates and adds an entity to the selected master detail collection.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class AddToMasterAction<E, F, G> extends ActionWrapper<E, F, G> {

  private IEntityDescriptor elementEntityDescriptor;

  /**
   * Completes the action context with the element entity descriptor
   * parametrized.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void execute(IActionHandler actionHandler, Map<String, Object> context) {
    if (context == null) {
      context = new HashMap<String, Object>();
    }
    context.put(ActionContextConstants.ELEMENT_DESCRIPTOR,
        elementEntityDescriptor);
    super.execute(actionHandler, context);
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
      setName("ADD " + elementEntityDescriptor.getName());
      setIconImageURL(elementEntityDescriptor.getIconImageURL());
      setDescription("ADD " + elementEntityDescriptor.getName());
    }
  }
}
