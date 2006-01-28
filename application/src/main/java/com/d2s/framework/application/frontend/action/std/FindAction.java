/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.std;

import com.d2s.framework.application.frontend.action.ActionWrapper;

/**
 * A standard find action. Since it is a chained action, it can be chained with
 * another action.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class FindAction extends ActionWrapper {

  /**
   * Constructs a new <code>FindAction</code> instance.
   */
  public FindAction() {
    setName("FIND");
    setIconImageURL("classpath:images/find-48x48.png");
  }
}
