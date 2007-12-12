/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.wings.file;

import java.util.List;
import java.util.Map;

import com.d2s.framework.application.frontend.action.wings.AbstractWingsAction;

/**
 * Initiates a file choosing action.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class ChooseFileAction extends AbstractWingsAction {

  private String                    defaultFileName;
  private Map<String, List<String>> fileFilter;

  /**
   * Sets the defaultFileName.
   * 
   * @param defaultFileName
   *            the defaultFileName to set.
   */
  public void setDefaultFileName(String defaultFileName) {
    this.defaultFileName = defaultFileName;
  }

  /**
   * Sets the fileFilter. Filter file types are a map of descriptions keying
   * file extension arays.
   * 
   * @param fileFilter
   *            the fileFilter to set.
   */
  public void setFileFilter(Map<String, List<String>> fileFilter) {
    this.fileFilter = fileFilter;
  }

  /**
   * Gets the defaultFileName.
   * 
   * @return the defaultFileName.
   */
  protected String getDefaultFileName() {
    return defaultFileName;
  }

  /**
   * Gets the fileFilter.
   * 
   * @return the fileFilter.
   */
  protected Map<String, List<String>> getFileFilter() {
    return fileFilter;
  }
}
