/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.swing;

import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.d2s.framework.util.url.UrlHelper;
import com.d2s.framework.view.AbstractIconFactory;

/**
 * A factory to create (and cache) swing icons.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SwingIconFactory extends AbstractIconFactory<Icon> {

  /**
   * {@inheritDoc}
   */
  @Override
  protected Icon createIcon(String urlSpec, Dimension iconSize) {
    if (urlSpec != null) {
      URL imageURL = UrlHelper.createURL(urlSpec, Thread.currentThread()
          .getContextClassLoader());
      if (imageURL != null) {
        ImageIcon imageIcon = new ImageIcon(imageURL);
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(
            (int) iconSize.getWidth(), (int) iconSize.getHeight(),
            Image.SCALE_SMOOTH));
        return imageIcon;
      }
    }
    return null;
  }
}
