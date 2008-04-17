/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.gui.ulc.components.server;

import com.ulcjava.base.application.IComboBoxModel;
import com.ulcjava.base.application.ULCComboBox;

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
public class ULCExtendedComboBox extends ULCComboBox {

  private static final long serialVersionUID = -7959139931231333809L;

  /**
   * Constructs a new <code>ULCExtendedComboBox</code> instance.
   */
  public ULCExtendedComboBox() {
    super();
  }

  /**
   * Constructs a new <code>ULCExtendedComboBox</code> instance.
   * 
   * @param model
   *            the combobox model.
   */
  public ULCExtendedComboBox(IComboBoxModel model) {
    super(model);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String typeString() {
    return "org.jspresso.framework.gui.ulc.components.client.UIExtendedComboBox";
  }

}
