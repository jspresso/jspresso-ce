/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view;

import java.util.List;

/**
 * Basic composite view.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            the type of component this view uses.
 */
public class BasicCompositeView<E> extends BasicView<E> implements
    ICompositeView<E> {

  private List<IView<E>> children;

  /**
   * Constructs a new <code>BasicCompositeView</code> instance.
   * 
   * @param peer
   *            the peer component.
   */
  public BasicCompositeView(E peer) {
    super(peer);
  }

  /**
   * {@inheritDoc}
   */
  public List<IView<E>> getChildren() {
    return children;
  }

  /**
   * Sets the children.
   * 
   * @param children
   *            the children to set.
   */
  public void setChildren(List<IView<E>> children) {
    this.children = children;
  }
}
