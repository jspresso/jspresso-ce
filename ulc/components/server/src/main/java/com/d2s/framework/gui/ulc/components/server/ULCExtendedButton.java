/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.server;

import com.ulcjava.base.application.ULCButton;
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
public class ULCExtendedButton extends ULCButton {

  private static final long serialVersionUID = 9221025686823039202L;

  /**
   * {@inheritDoc}
   */
  @Override
  protected String typeString() {
    return "com.d2s.framework.gui.ulc.components.client.UIExtendedButton";
  }

  /**
   * Constructs a new <code>ULCExtendedButton</code> instance.
   */
  public ULCExtendedButton() {
    super();
  }

  /**
   * Constructs a new <code>ULCExtendedButton</code> instance.
   * 
   * @param icon
   *          the button icon
   */
  public ULCExtendedButton(ULCIcon icon) {
    super(icon);
  }

}
