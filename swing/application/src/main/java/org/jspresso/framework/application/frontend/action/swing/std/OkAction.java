/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.swing.std;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.swing.AbstractSwingAction;


/**
 * A standard ok action. Since it is a chained action, it can be chained with
 * another action.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class OkAction extends AbstractSwingAction {

  /**
   * Constructs a new <code>OkAction</code> instance.
   */
  public OkAction() {
    setName("ok");
    setIconImageURL("classpath:com/d2s/framework/application/images/ok-48x48.png");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    closeDialog(context);
    return super.execute(actionHandler, context);
  }
}
