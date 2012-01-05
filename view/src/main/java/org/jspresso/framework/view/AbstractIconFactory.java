/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual icon class created.
 */
public abstract class AbstractIconFactory<E> implements IIconFactory<E> {

  private Map<String, Map<Dimension, E>> iconStore;

  private Dimension                      largeIconSize;
  private Dimension                      mediumIconSize;
  private Dimension                      smallIconSize;
  private Dimension                      tinyIconSize;

  private IIconSet                       iconSet;

  /**
   * Constructs a new <code>AbstractIconFactory</code> instance.
   */
  protected AbstractIconFactory() {
    iconStore = new HashMap<String, Map<Dimension, E>>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
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
   * Gets the largeIconSize.
   * 
   * @return the largeIconSize.
   */
  @Override
  public Dimension getLargeIconSize() {
    if (largeIconSize == null) {
      largeIconSize = new Dimension(48, 48);
    }
    return largeIconSize;
  }

  /**
   * Gets the mediumIconSize.
   * 
   * @return the mediumIconSize.
   */
  @Override
  public Dimension getMediumIconSize() {
    if (mediumIconSize == null) {
      mediumIconSize = new Dimension(32, 32);
    }
    return mediumIconSize;
  }

  /**
   * Gets the smallIconSize.
   * 
   * @return the smallIconSize.
   */
  @Override
  public Dimension getSmallIconSize() {
    if (smallIconSize == null) {
      smallIconSize = new Dimension(24, 24);
    }
    return smallIconSize;
  }

  /**
   * Gets the tinyIconSize.
   * 
   * @return the tinyIconSize.
   */
  @Override
  public Dimension getTinyIconSize() {
    if (tinyIconSize == null) {
      tinyIconSize = new Dimension(16, 16);
    }
    return tinyIconSize;
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
   * Sets the smallIconSize.
   * 
   * @param smallIconSize
   *          the smallIconSize to set.
   */
  public void setSmallIconSize(Dimension smallIconSize) {
    this.smallIconSize = smallIconSize;
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
   * Gets the iconSet.
   * 
   * @return the iconSet.
   */
  @Override
  public IIconSet getIconSet() {
    return iconSet;
  }

  /**
   * Configures the icon set to use.
   * 
   * @param iconSet the iconSet to set.
   */
  public void setIconSet(IIconSet iconSet) {
    this.iconSet = iconSet;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getBackwardIconImageURL() {
    return getIconSet().getBackwardIconImageURL();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCancelIconImageURL() {
    return getIconSet().getCancelIconImageURL();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDownIconImageURL() {
    return getIconSet().getDownIconImageURL();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getErrorIconImageURL() {
    return getIconSet().getErrorIconImageURL();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getForbiddenIconImageURL() {
    return getIconSet().getForbiddenIconImageURL();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getForwardIconImageURL() {
    return getIconSet().getForwardIconImageURL();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getInfoIconImageURL() {
    return getIconSet().getInfoIconImageURL();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getNoIconImageURL() {
    return getIconSet().getNoIconImageURL();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getOkYesIconImageURL() {
    return getIconSet().getOkYesIconImageURL();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getQuestionIconImageURL() {
    return getIconSet().getQuestionIconImageURL();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getUpIconImageURL() {
    return getIconSet().getUpIconImageURL();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getWarningIconImageURL() {
    return getIconSet().getWarningIconImageURL();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public E getBackwardIcon(Dimension iconSize) {
    return getIcon(getBackwardIconImageURL(), iconSize);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public E getCancelIcon(Dimension iconSize) {
    return getIcon(getCancelIconImageURL(), iconSize);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public E getDownIcon(Dimension iconSize) {
    return getIcon(getDownIconImageURL(), iconSize);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public E getErrorIcon(Dimension iconSize) {
    return getIcon(getErrorIconImageURL(), iconSize);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public E getForbiddenIcon(Dimension iconSize) {
    return getIcon(getForbiddenIconImageURL(), iconSize);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public E getForwardIcon(Dimension iconSize) {
    return getIcon(getForwardIconImageURL(), iconSize);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public E getInfoIcon(Dimension iconSize) {
    return getIcon(getInfoIconImageURL(), iconSize);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public E getNoIcon(Dimension iconSize) {
    return getIcon(getNoIconImageURL(), iconSize);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public E getOkYesIcon(Dimension iconSize) {
    return getIcon(getOkYesIconImageURL(), iconSize);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public E getQuestionIcon(Dimension iconSize) {
    return getIcon(getQuestionIconImageURL(), iconSize);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public E getUpIcon(Dimension iconSize) {
    return getIcon(getUpIconImageURL(), iconSize);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public E getWarningIcon(Dimension iconSize) {
    return getIcon(getWarningIconImageURL(), iconSize);
  }

}
