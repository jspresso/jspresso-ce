/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.std;

import com.d2s.framework.application.frontend.action.AbstractChainedAction;

/**
 * A standard cancel action. Since it is a chained action, it can be chained
 * with another action.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class CancelAction extends AbstractChainedAction {

  /**
   * Constructs a new <code>CancelAction</code> instance.
   */
  public CancelAction() {
    setName("cancel");
    setIconImageURL("classpath:images/cancel-48x48.png");
  }
}
