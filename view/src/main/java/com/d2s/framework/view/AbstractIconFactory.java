/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

/**
 * A factory for icons.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual icon class created.
 */
public abstract class AbstractIconFactory<E> implements IIconFactory<E> {

  private Map<String, Map<Dimension, E>> iconStore;

  /**
   * Constructs a new <code>AbstractIconFactory</code> instance.
   */
  protected AbstractIconFactory() {
    iconStore = new HashMap<String, Map<Dimension, E>>();
  }

  /**
   * Creates a swing icon from an image url.
   * 
   * @param urlSpec
   *          the url of the image to be used on the icon.
   * @param iconSize
   *          the size of the constructed icon. The image will be resized if
   *          nacessary to match the requested size.
   * @return the constructed icon.
   */
  protected abstract E createIcon(String urlSpec, Dimension iconSize);

  /**
   * {@inheritDoc}
   */
  public E getIcon(String urlSpec, Dimension iconSize) {
    Map<Dimension, E> multiDimStore = iconStore.get(urlSpec);
    if (multiDimStore == null) {
      multiDimStore = new HashMap<Dimension, E>();
      iconStore.put(urlSpec, multiDimStore);
    }
    E cachedIcon = multiDimStore.get(iconSize);
    if (cachedIcon == null) {
      cachedIcon = createIcon(urlSpec, iconSize);
      multiDimStore.put(iconSize, cachedIcon);
    }
    return cachedIcon;
  }
}
