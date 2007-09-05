/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of an enumerationValues descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicEnumerationPropertyDescriptor extends
    AbstractEnumerationPropertyDescriptor {

  private Map<String, String> valuesAndIconImageUrls;

  /**
   * {@inheritDoc}
   */
  public List<String> getEnumerationValues() {
    return new ArrayList<String>(valuesAndIconImageUrls.keySet());
  }

  /**
   * {@inheritDoc}
   */
  public String getIconImageURL(String value) {
    if (valuesAndIconImageUrls != null) {
      return valuesAndIconImageUrls.get(value);
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isTranslated() {
    return true;
  }

  /**
   * Sets the valuesAndIconImageUrls property.
   *
   * @param valuesAndIconImageUrls
   *          the valuesAndIconImageUrls to set.
   */
  public void setValuesAndIconImageUrls(
      Map<String, String> valuesAndIconImageUrls) {
    this.valuesAndIconImageUrls = valuesAndIconImageUrls;
  }
}
