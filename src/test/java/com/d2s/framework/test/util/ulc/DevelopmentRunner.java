/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.test.util.ulc;

import com.d2s.framework.util.swing.SwingUtil;

/**
 * Custom development runner to cope with formatted textfield font bug.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DevelopmentRunner extends
    com.ulcjava.base.development.DevelopmentRunner {

  /**
   * Overriden to cope with formatted textfield font bug.
   * 
   * @param args
   *          arguments.
   */
  public static void main(String[] args) {
    SwingUtil.installDefaults();
    com.ulcjava.base.development.DevelopmentRunner.main(args);
  }
}
