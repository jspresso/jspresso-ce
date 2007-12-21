/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Basic map view.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            the type of component this view uses.
 */
public class BasicMapView<E> extends BasicView<E> implements IMapView<E> {

  private Map<String, IView<E>> children;

  /**
   * Constructs a new <code>BasicMapView</code> instance.
   * 
   * @param peer
   *            the peer component.
   */
  public BasicMapView(E peer) {
    super(peer);
  }

  /**
   * {@inheritDoc}
   */
  public IView<E> getChild(String key) {
    if (children == null) {
      return null;
    }
    return children.get(key);
  }

  /**
   * {@inheritDoc}
   */
  public List<IView<E>> getChildren() {
    if (children == null) {
      return null;
    }
    return new ArrayList<IView<E>>(children.values());
  }

  /**
   * Sets the children.
   * 
   * @param children
   *            the children to set.
   */
  public void setChildren(Map<String, IView<E>> children) {
    this.children = children;
  }
}
