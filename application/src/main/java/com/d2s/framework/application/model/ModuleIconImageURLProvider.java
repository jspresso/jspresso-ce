/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.model;

import com.d2s.framework.util.IIconImageURLProvider;

/**
 * This image url provider uses a delegate provider to look up the rendering
 * image of a bean module based on its projected object.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
