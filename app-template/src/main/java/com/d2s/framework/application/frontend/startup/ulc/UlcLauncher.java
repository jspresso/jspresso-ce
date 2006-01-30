/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.startup.ulc;

import com.d2s.framework.util.swing.SwingUtil;
import com.ulcjava.base.development.DevelopmentRunner;

/**
 * Custom development runner to cope with formatted textfield font bug.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class UlcLauncher extends DevelopmentRunner {

  private UlcLauncher() {
    // Helper class constructor.
  }

  /**
   * Overriden to cope with formatted textfield font bug.
   * 
   * @param args
   *          arguments.
   */
  public static void main(String[] args) {
    SwingUtil.installDefaults();
    DevelopmentRunner.main(args);
  }
}
