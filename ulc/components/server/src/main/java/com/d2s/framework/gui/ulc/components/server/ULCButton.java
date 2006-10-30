/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.server;

import com.ulcjava.base.application.util.ULCIcon;

/**
 * This subclass implements some default behaviors which are not yet
 * configurable using ULC.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCButton extends com.ulcjava.base.application.ULCButton {

  private static final long serialVersionUID = 9221025686823039202L;

  /**
   * {@inheritDoc}
   */
  @Override
  protected String typeString() {
    return "com.d2s.framework.gui.ulc.components.client.UIButton";
  }

  /**
   * Constructs a new <code>ULCButton</code> instance.
   *
   */
  public ULCButton() {
    super();
  }

  /**
   * Constructs a new <code>ULCButton</code> instance.
   *
   * @param icon the button icon
   */
  public ULCButton(ULCIcon icon) {
    super(icon);
  }

}
