/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.wings;

import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

import org.jspresso.framework.util.url.UrlHelper;
import org.jspresso.framework.view.AbstractIconFactory;
import org.wings.SIcon;
import org.wings.SImageIcon;


/**
 * A factory to create (and cache) swing icons.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
