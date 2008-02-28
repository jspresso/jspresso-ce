/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.wings.std;

import java.util.Map;

import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.action.wings.AbstractWingsAction;

/**
 * A standard cancel action. Since it is a chained action, it can be chained
 * with another action.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class CancelAction extends AbstractWingsAction {

  /**
   * Constructs a new <code>CancelAction</code> instance.
   */
  public CancelAction() {
    setName("cancel");
    setIconImageURL("classpath:com/d2s/framework/application/images/cancel-48x48.png");
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
