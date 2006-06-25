/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import java.util.List;
import java.util.Map;

import com.d2s.framework.model.descriptor.IBinaryPropertyDescriptor;

/**
 * Default implementation of a binary descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicBinaryPropertyDescriptor extends
    BasicScalarPropertyDescriptor implements IBinaryPropertyDescriptor {

  private Integer                   maxLength;
  private Map<String, List<String>> fileFilter;

  /**
   * {@inheritDoc}
   */
  public Integer getMaxLength() {
    if (maxLength != null) {
      return maxLength;
    }
    if (getParentDescriptor() != null) {
      return ((IBinaryPropertyDescriptor) getParentDescriptor())
          .getMaxLength();
    }
    return maxLength;
  }

  /**
   * Sets the maxLength property.
   * 
   * @param maxLength
   *          the maxLength to set.
   */
  public void setMaxLength(Integer maxLength) {
    this.maxLength = maxLength;
  }

  /**
   * {@inheritDoc}
   */
  public Class getModelType() {
    return byte[].class;
  }

  /**
   * {@inheritDoc}
   */
  public Map<String, List<String>> getFileFilter() {
    if (fileFilter != null) {
      return fileFilter;
    }
    if (getParentDescriptor() != null) {
      return ((IBinaryPropertyDescriptor) getParentDescriptor())
          .getFileFilter();
    }
    return fileFilter;
  }

  /**
   * Sets the fileFilter.
   * 
   * @param fileFilter
   *          the fileFilter to set.
   */
  public void setFileFilter(Map<String, List<String>> fileFilter) {
    this.fileFilter = fileFilter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isQueryable() {
    return false;
  }
}
