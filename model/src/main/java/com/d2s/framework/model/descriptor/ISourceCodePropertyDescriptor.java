/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor;

/**
 * This interface is implemented by descriptors of source code string
 * properties.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ISourceCodePropertyDescriptor extends ITextPropertyDescriptor {

  /**
   * Gets the language the source code is written in.
   * 
   * @return the language the source code is written in.
   */
  String getLanguage();
}
