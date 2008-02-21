/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.util.descriptor;

/**
 * Default implementation of IIconDescriptor. It handles a icon image URL
 * reference.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultIconDescriptor extends DefaultDescriptor implements
    IIconDescriptor {

  private String iconImageURL;

  /**
   * {@inheritDoc}
   */
  @Override
  public DefaultIconDescriptor clone() {
    return (DefaultIconDescriptor) super.clone();
  }

  /**
   * Gets the iconImageURL.
   * 
   * @return the iconImageURL.
   */
  public String getIconImageURL() {
    return iconImageURL;
  }

  /**
   * Sets the iconImageURL.
   * 
   * @param iconImageURL
   *            the iconImageURL to set.
   */
  public void setIconImageURL(String iconImageURL) {
    this.iconImageURL = iconImageURL;
  }
}
