/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import java.util.List;
import java.util.Map;

import com.d2s.framework.model.descriptor.ITextPropertyDescriptor;

/**
 * Default implementation of a text descriptor.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicTextPropertyDescriptor extends BasicStringPropertyDescriptor
    implements ITextPropertyDescriptor {

  private Map<String, List<String>> fileFilter;

  /**
   * {@inheritDoc}
   */
  public Map<String, List<String>> getFileFilter() {
    if (fileFilter != null) {
      return fileFilter;
    }
    if (getParentDescriptor() != null) {
      return ((ITextPropertyDescriptor) getParentDescriptor()).getFileFilter();
    }
    return fileFilter;
  }

  /**
   * Sets the fileFilter.
   * 
   * @param fileFilter
   *            the fileFilter to set.
   */
  public void setFileFilter(Map<String, List<String>> fileFilter) {
    this.fileFilter = fileFilter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicTextPropertyDescriptor clone() {
    BasicTextPropertyDescriptor clonedDescriptor = (BasicTextPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }
}
