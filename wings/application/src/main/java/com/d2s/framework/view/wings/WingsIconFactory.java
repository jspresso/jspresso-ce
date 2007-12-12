/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.wings;

import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

import org.wings.SIcon;
import org.wings.SImageIcon;

import com.d2s.framework.util.url.UrlHelper;
import com.d2s.framework.view.AbstractIconFactory;

/**
 * A factory to create (and cache) swing icons.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class WingsIconFactory extends AbstractIconFactory<SIcon> {

  /**
   * {@inheritDoc}
   */
  @Override
  protected SIcon createIcon(String urlSpec, Dimension iconSize) {
    if (urlSpec != null) {
      URL imageURL = UrlHelper.createURL(urlSpec);
      if (imageURL != null) {
        ImageIcon imageIcon = new ImageIcon(imageURL);
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(
            (int) iconSize.getWidth(), (int) iconSize.getHeight(),
            Image.SCALE_SMOOTH));
        return new SImageIcon(imageIcon);
      }
    }
    return null;
  }
}
