/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.server;

import com.ulcjava.base.application.ULCScrollPane;

/**
 * This subclass implements some default behaviors which are not yet
 * configurable using ULC.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCExtendedScrollPane extends ULCScrollPane {


  /**
   * TODO Comment needed for <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = 2629813640962214434L;

  /**
   * {@inheritDoc}
   */
  @Override
  protected String typeString() {
    return "com.d2s.framework.gui.ulc.components.client.UIExtendedScrollPane";
  }

}
