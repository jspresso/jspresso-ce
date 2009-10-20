/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.view;

import java.util.HashMap;
import java.util.Map;

import org.jspresso.framework.util.gui.Dimension;

/**
 * A factory for icons.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual icon class created.
 */
public abstract class AbstractIconFactory<E> implements IIconFactory<E> {

  private String                         backwardIconImageURL;

  private String                         cancelIconImageURL;
  private String                         errorIconImageURL;
  private String                         forbiddenIconImageURL;

  private String                         forwardIconImageURL;
  private Map<String, Map<Dimension, E>> iconStore;
  private String                         infoIconImageURL;

  private String                         noIconImageURL;

  private String                         upIconImageURL;
  private String                         downIconImageURL;

  private String                         okYesIconImageURL;
  private String                         warningIconImageURL;

  private Dimension                      tinyIconSize;
  private Dimension                      smallIconSize;
  private Dimension                      mediumIconSize;
  private Dimension                      largeIconSize;

  /**
   * Constructs a new <code>AbstractIconFactory</code> instance.
   */
  protected AbstractIconFactory() {
    iconStore = new HashMap<String, Map<Dimension, E>>();
  }

  /**
   * {@inheritDoc}
   */
  public E getBackwardIcon(Dimension iconSize) {
    return getIcon(backwardIconImageURL, iconSize);
  }

  /**
   * Gets the backwardIconImageURL.
   * 
   * @return the backwardIconImageURL.
   */
  public String getBackwardIconImageURL() {
    return backwardIconImageURL;
  }

  /**
   * {@inheritDoc}
   */
  public E getCancelIcon(Dimension iconSize) {
    return getIcon(cancelIconImageURL, iconSize);
  }

  /**
   * Gets the cancelIconImageURL.
   * 
   * @return the cancelIconImageURL.
   */
  public String getCancelIconImageURL() {
    return cancelIconImageURL;
  }

  /**
   * {@inheritDoc}
   */
  public E getErrorIcon(Dimension iconSize) {
    return getIcon(errorIconImageURL, iconSize);
  }

  /**
   * Gets the errorIconImageURL.
   * 
   * @return the errorIconImageURL.
   */
  public String getErrorIconImageURL() {
    return errorIconImageURL;
  }

  /**
   * {@inheritDoc}
   */
  public E getForbiddenIcon(Dimension iconSize) {
    return getIcon(forbiddenIconImageURL, iconSize);
  }

  /**
   * {@inheritDoc}
   */
  public E getUpIcon(Dimension iconSize) {
    return getIcon(upIconImageURL, iconSize);
  }

  /**
   * {@inheritDoc}
   */
  public E getDownIcon(Dimension iconSize) {
    return getIcon(downIconImageURL, iconSize);
  }

  /**
   * Gets the forbiddenIconImageURL.
   * 
   * @return the forbiddenIconImageURL.
   */
  public String getForbiddenIconImageURL() {
    return forbiddenIconImageURL;
  }

  /**
   * {@inheritDoc}
   */
  public E getForwardIcon(Dimension iconSize) {
    return getIcon(forwardIconImageURL, iconSize);
  }

  /**
   * Gets the forwardIconImageURL.
   * 
   * @return the forwardIconImageURL.
   */
  public String getForwardIconImageURL() {
    return forwardIconImageURL;
  }

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

  /**
   * {@inheritDoc}
   */
  public E getInfoIcon(Dimension iconSize) {
    return getIcon(infoIconImageURL, iconSize);
  }

  /**
   * Gets the infoIconImageURL.
   * 
   * @return the infoIconImageURL.
   */
  public String getInfoIconImageURL() {
    return infoIconImageURL;
  }

  /**
   * {@inheritDoc}
   */
  public E getNoIcon(Dimension iconSize) {
    return getIcon(noIconImageURL, iconSize);
  }

  /**
   * Gets the noIconImageURL.
   * 
   * @return the noIconImageURL.
   */
  public String getNoIconImageURL() {
    return noIconImageURL;
  }

  /**
   * {@inheritDoc}
   */
  public E getOkYesIcon(Dimension iconSize) {
    return getIcon(okYesIconImageURL, iconSize);
  }

  /**
   * Gets the okYesIconImageURL.
   * 
   * @return the okYesIconImageURL.
   */
  public String getOkYesIconImageURL() {
    return okYesIconImageURL;
  }

  /**
   * {@inheritDoc}
   */
  public E getWarningIcon(Dimension iconSize) {
    return getIcon(warningIconImageURL, iconSize);
  }

  /**
   * Gets the warningIconImageURL.
   * 
   * @return the warningIconImageURL.
   */
  public String getWarningIconImageURL() {
    return warningIconImageURL;
  }

  /**
   * Sets the backwardIconImageURL.
   * 
   * @param backwardIconImageURL
   *          the backwardIconImageURL to set.
   */
  public void setBackwardIconImageURL(String backwardIconImageURL) {
    this.backwardIconImageURL = backwardIconImageURL;
  }

  /**
   * Sets the cancelIconImageURL.
   * 
   * @param cancelIconImageURL
   *          the cancelIconImageURL to set.
   */
  public void setCancelIconImageURL(String cancelIconImageURL) {
    this.cancelIconImageURL = cancelIconImageURL;
  }

  /**
   * Sets the errorIconImageURL.
   * 
   * @param errorIconImageURL
   *          the errorIconImageURL to set.
   */
  public void setErrorIconImageURL(String errorIconImageURL) {
    this.errorIconImageURL = errorIconImageURL;
  }

  /**
   * Sets the forbiddenIconImageURL.
   * 
   * @param forbiddenIconImageURL
   *          the forbiddenIconImageURL to set.
   */
  public void setForbiddenIconImageURL(String forbiddenIconImageURL) {
    this.forbiddenIconImageURL = forbiddenIconImageURL;
  }

  /**
   * Sets the forwardIconImageURL.
   * 
   * @param forwardIconImageURL
   *          the forwardIconImageURL to set.
   */
  public void setForwardIconImageURL(String forwardIconImageURL) {
    this.forwardIconImageURL = forwardIconImageURL;
  }

  /**
   * Sets the infoIconImageURL.
   * 
   * @param infoIconImageURL
   *          the infoIconImageURL to set.
   */
  public void setInfoIconImageURL(String infoIconImageURL) {
    this.infoIconImageURL = infoIconImageURL;
  }

  /**
   * Sets the noIconImageURL.
   * 
   * @param noIconImageURL
   *          the noIconImageURL to set.
   */
  public void setNoIconImageURL(String noIconImageURL) {
    this.noIconImageURL = noIconImageURL;
  }

  /**
   * Sets the okYesIconImageURL.
   * 
   * @param okYesIconImageURL
   *          the okYesIconImageURL to set.
   */
  public void setOkYesIconImageURL(String okYesIconImageURL) {
    this.okYesIconImageURL = okYesIconImageURL;
  }

  /**
   * Sets the warningIconImageURL.
   * 
   * @param warningIconImageURL
   *          the warningIconImageURL to set.
   */
  public void setWarningIconImageURL(String warningIconImageURL) {
    this.warningIconImageURL = warningIconImageURL;
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
   * Gets the upIconImageURL.
   * 
   * @return the upIconImageURL.
   */
  public String getUpIconImageURL() {
    return upIconImageURL;
  }

  /**
   * Sets the upIconImageURL.
   * 
   * @param upIconImageURL
   *          the upIconImageURL to set.
   */
  public void setUpIconImageURL(String upIconImageURL) {
    this.upIconImageURL = upIconImageURL;
  }

  /**
   * Gets the downIconImageURL.
   * 
   * @return the downIconImageURL.
   */
  public String getDownIconImageURL() {
    return downIconImageURL;
  }

  /**
   * Sets the downIconImageURL.
   * 
   * @param downIconImageURL
   *          the downIconImageURL to set.
   */
  public void setDownIconImageURL(String downIconImageURL) {
    this.downIconImageURL = downIconImageURL;
  }

  /**
   * Gets the tinyIconSize.
   * 
   * @return the tinyIconSize.
   */
  public Dimension getTinyIconSize() {
    if (tinyIconSize == null) {
      tinyIconSize = new Dimension(16, 16);
    }
    return tinyIconSize;
  }

  /**
   * Sets the tinyIconSize.
   * 
   * @param tinyIconSize
   *          the tinyIconSize to set.
   */
  public void setTinyIconSize(Dimension tinyIconSize) {
    this.tinyIconSize = tinyIconSize;
  }

  /**
   * Gets the smallIconSize.
   * 
   * @return the smallIconSize.
   */
  public Dimension getSmallIconSize() {
    if (smallIconSize == null) {
      smallIconSize = new Dimension(24, 24);
    }
    return smallIconSize;
  }

  /**
   * Sets the smallIconSize.
   * 
   * @param smallIconSize
   *          the smallIconSize to set.
   */
  public void setSmallIconSize(Dimension smallIconSize) {
    this.smallIconSize = smallIconSize;
  }

  /**
   * Gets the mediumIconSize.
   * 
   * @return the mediumIconSize.
   */
  public Dimension getMediumIconSize() {
    if (mediumIconSize == null) {
      mediumIconSize = new Dimension(32, 32);
    }
    return mediumIconSize;
  }

  /**
   * Sets the mediumIconSize.
   * 
   * @param mediumIconSize
   *          the mediumIconSize to set.
   */
  public void setMediumIconSize(Dimension mediumIconSize) {
    this.mediumIconSize = mediumIconSize;
  }

  /**
   * Gets the largeIconSize.
   * 
   * @return the largeIconSize.
   */
  public Dimension getLargeIconSize() {
    if (largeIconSize == null) {
      largeIconSize = new Dimension(48, 48);
    }
    return largeIconSize;
  }

  /**
   * Sets the largeIconSize.
   * 
   * @param largeIconSize
   *          the largeIconSize to set.
   */
  public void setLargeIconSize(Dimension largeIconSize) {
    this.largeIconSize = largeIconSize;
  }

}
