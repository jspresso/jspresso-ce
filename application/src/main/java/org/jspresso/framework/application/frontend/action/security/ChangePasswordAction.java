/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.security;

import java.util.HashMap;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.application.backend.action.security.AbstractChangePasswordAction;
import org.jspresso.framework.application.frontend.action.std.EditComponentAction;
import org.jspresso.framework.view.descriptor.basic.BasicComponentViewDescriptor;


/**
 * The frontend action to initiate the password change.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class ChangePasswordAction<E, F, G> extends EditComponentAction<E, F, G> {

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
