/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.view.swing;

import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.jspresso.framework.util.url.UrlHelper;
import org.jspresso.framework.view.AbstractIconFactory;


/**
 * A factory to create (and cache) swing icons.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
      URL imageURL = UrlHelper.createURL(urlSpec);
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
