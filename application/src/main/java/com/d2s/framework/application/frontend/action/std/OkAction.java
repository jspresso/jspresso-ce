/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.std;

import com.d2s.framework.application.frontend.action.AbstractChainedAction;

/**
 * A standard ok action. Since it is a chained action, it can be chained with
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
public class OkAction<E, F, G> extends AbstractChainedAction<E, F, G> {

  /**
   * Constructs a new <code>OkAction</code> instance.
   */
  public OkAction() {
    setName("OK");
    setIconImageURL("classpath:images/ok-48x48.png");
  }
}
