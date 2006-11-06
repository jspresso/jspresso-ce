/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.server;

import com.ulcjava.base.application.IComboBoxModel;

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
public class ULCComboBox extends com.ulcjava.base.application.ULCComboBox {

  /**
   * TODO Comment needed for <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = -7959139931231333809L;

  /**
   * {@inheritDoc}
   */
  @Override
  protected String typeString() {
    return "com.d2s.framework.gui.ulc.components.client.UIComboBox";
  }

  /**
   * Constructs a new <code>ULCComboBox</code> instance.
   */
  public ULCComboBox() {
    super();
  }

  /**
   * Constructs a new <code>ULCComboBox</code> instance.
   *
   * @param model
   *          the combobox model.
   */
  public ULCComboBox(IComboBoxModel model) {
    super(model);
  }

}
