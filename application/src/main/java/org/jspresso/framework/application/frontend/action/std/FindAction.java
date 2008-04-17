/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.std;

import org.jspresso.framework.application.frontend.action.ActionWrapper;

/**
 * A standard find action. Since it is a chained action, it can be chained with
 * another action.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            the actual gui component type used.
 * @param <F>
 *            the actual icon type used.
 * @param <G>
 *            the actual action type used.
 */
public class FindAction<E, F, G> extends ActionWrapper<E, F, G> {

  /**
   * Constructs a new <code>FindAction</code> instance.
   */
  public FindAction() {
    setName("find.name");
    setIconImageURL("classpath:com/d2s/framework/application/images/find-48x48.png");
  }
}
