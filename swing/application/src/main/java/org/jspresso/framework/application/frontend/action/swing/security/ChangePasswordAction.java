/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.swing.security;

import java.util.HashMap;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.application.backend.action.security.AbstractChangePasswordAction;
import org.jspresso.framework.application.frontend.action.swing.std.EditComponentAction;
import org.jspresso.framework.view.descriptor.basic.BasicComponentViewDescriptor;


/**
 * The frontend action to initiate the password change.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
