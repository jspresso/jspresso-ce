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
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class FindAction<E, F, G> extends ActionWrapper<E, F, G> {

  /**
   * Constructs a new <code>FindAction</code> instance.
   */
  public FindAction() {
    setName("find.name");
    setIconImageURL("classpath:images/find-48x48.png");
  }
}
