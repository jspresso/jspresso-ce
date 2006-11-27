/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.echo;

import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

import nextapp.echo2.app.AwtImageReference;
import nextapp.echo2.app.ImageReference;

import com.d2s.framework.util.url.UrlHelper;
import com.d2s.framework.view.AbstractIconFactory;

/**
 * A factory to create (and cache) echo icons.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EchoIconFactory extends AbstractIconFactory<ImageReference> {

  /**
   * {@inheritDoc}
   */
  @Override
  protected ImageReference createIcon(String urlSpec, Dimension iconSize) {
    if (urlSpec != null) {
      URL imageURL = UrlHelper.createURL(urlSpec);
      if (imageURL != null) {
        ImageIcon imageIcon = new ImageIcon(imageURL);
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(
            (int) iconSize.getWidth(), (int) iconSize.getHeight(),
            Image.SCALE_SMOOTH));
        return new AwtImageReference(imageIcon.getImage());
      }
    }
    return null;
  }
}
