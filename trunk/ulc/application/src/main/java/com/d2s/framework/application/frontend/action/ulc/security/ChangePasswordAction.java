/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.ulc.security;

import java.util.HashMap;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.application.backend.action.security.AbstractChangePasswordAction;
import com.d2s.framework.application.frontend.action.ulc.std.EditComponentAction;
import com.d2s.framework.view.descriptor.basic.BasicComponentViewDescriptor;

/**
 * The frontend action to initiate the password change.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ChangePasswordAction extends EditComponentAction {

  /**
   * Constructs a new <code>ChangePasswordAction</code> instance.
   */
  public ChangePasswordAction() {
    putInitialContext(ActionContextConstants.ACTION_PARAM,
        new HashMap<String, Object>());
    BasicComponentViewDescriptor viewDescriptor = new BasicComponentViewDescriptor();
    viewDescriptor
        .setModelDescriptor(AbstractChangePasswordAction.PASSWD_CHANGE_DESCRIPTOR);
    setViewDescriptor(viewDescriptor);
  }
}
