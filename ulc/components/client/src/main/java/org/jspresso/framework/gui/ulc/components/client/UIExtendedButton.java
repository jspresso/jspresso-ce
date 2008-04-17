/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.gui.ulc.components.client;

import org.jspresso.framework.util.swing.SwingUtil;

import com.ulcjava.base.client.UIButton;
import com.ulcjava.base.client.UiJButton;
import com.ulcjava.base.shared.internal.Anything;

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
public class UIExtendedButton extends UIButton {

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object createBasicObject(@SuppressWarnings("unused")
  Anything args) {
    UiJButton button = (UiJButton) super.createBasicObject(args);
    SwingUtil.configureButton(button);
    return button;
  }

}
