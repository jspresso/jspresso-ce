/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view;

import java.util.Locale;

import com.d2s.framework.model.descriptor.entity.IEntityDescriptor;
import com.d2s.framework.view.action.IActionHandler;

/**
 * Factory for list-of-value views.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 */
public interface ILovViewFactory<E> {

  /**
   * Creates a new lov view for a component descriptor.
   * 
   * @param entityDescriptor
   *          the entity descriptor.
   * @param actionHandler
   *          the object responsible for executing the view actions (generally
   *          the frontend controller itself).
   * @param locale
   *          the locale the view must use for I18N.
   * @return the created view.
   */
  IView<E> createLovView(IEntityDescriptor entityDescriptor,
      IActionHandler actionHandler, Locale locale);
}
