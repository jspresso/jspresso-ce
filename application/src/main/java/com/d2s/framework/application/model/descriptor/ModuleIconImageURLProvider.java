/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.model.descriptor;

import com.d2s.framework.application.model.BeanModule;
import com.d2s.framework.application.model.Module;
import com.d2s.framework.application.model.SubModule;
import com.d2s.framework.util.IIconImageURLProvider;

/**
 * This image url provider uses a delegate provider to look up the rendering
 * image of a bean module based on its projected object.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ModuleIconImageURLProvider implements IIconImageURLProvider {

  private IIconImageURLProvider delegateProvider;

  /**
   * {@inheritDoc}
   */
  public String getIconImageURLForObject(Object userObject) {
    if (delegateProvider != null && userObject instanceof BeanModule
        && ((BeanModule) userObject).getModuleObject() != null
        && ((BeanModule) userObject).getIconImageURL() == null) {
      return delegateProvider
          .getIconImageURLForObject(((BeanModule) userObject).getModuleObject());
    } else if (userObject instanceof SubModule) {
      return ((SubModule) userObject).getIconImageURL();
    } else if (userObject instanceof Module) {
      return ((Module) userObject).getIconImageURL();
    }
    return null;
  }

  /**
   * Sets the delegateProvider.
   * 
   * @param delegateProvider
   *            the delegateProvider to set.
   */
  public void setDelegateProvider(IIconImageURLProvider delegateProvider) {
    this.delegateProvider = delegateProvider;
  }

}
