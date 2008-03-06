/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import java.awt.Color;
import java.awt.Font;
import java.util.Collection;
import java.util.Locale;

import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.util.i18n.ITranslationProvider;
import com.d2s.framework.view.action.ActionMap;
import com.d2s.framework.view.descriptor.IViewDescriptor;

/**
 * Default implementation of a view descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class BasicViewDescriptor extends BasicSubviewDescriptor
    implements IViewDescriptor {

  private ActionMap          actionMap;
  private Color              background;
  private int                borderType = NONE;
  private Font               font;
  private Color              foreground;

  private Collection<String> grantedRoles;

  /**
   * Gets the actionMap.
   * 
   * @return the actionMap.
   */
  public ActionMap getActionMap() {
    return actionMap;
  }

  /**
   * {@inheritDoc}
   */
  public Color getBackground() {
    return background;
  }

  /**
   * {@inheritDoc}
   */
  public int getBorderType() {
    return borderType;
  }

  /**
   * {@inheritDoc}
   */
  public Font getFont() {
    return font;
  }

  /**
   * {@inheritDoc}
   */
  public Color getForeground() {
    return foreground;
  }

  /**
   * Gets the grantedRoles.
   * 
   * @return the grantedRoles.
   */
  public Collection<String> getGrantedRoles() {
    return grantedRoles;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getI18nDescription(ITranslationProvider translationProvider,
      Locale locale) {
    if (getDescription() == null) {
      return getModelDescriptor().getI18nDescription(translationProvider,
          locale);
    }
    return super.getI18nDescription(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getI18nName(ITranslationProvider translationProvider,
      Locale locale) {
    if (getName() == null) {
      return getModelDescriptor().getI18nName(translationProvider, locale);
    }
    return super.getI18nName(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getIconImageURL() {
    String iconImageURL = super.getIconImageURL();
    if (iconImageURL == null
        && getModelDescriptor() instanceof IComponentDescriptor) {
      iconImageURL = ((IComponentDescriptor<?>) getModelDescriptor()).getIconImageURL();
      setIconImageURL(iconImageURL);
    }
    return iconImageURL;
  }

  /**
   * Sets the actionMap.
   * 
   * @param actionMap
   *            the actionMap to set.
   */
  public void setActionMap(ActionMap actionMap) {
    this.actionMap = actionMap;
  }

  /**
   * Sets the background.
   * 
   * @param background
   *            the background to set.
   */
  public void setBackground(Color background) {
    this.background = background;
  }

  /**
   * Sets the borderType.
   * 
   * @param borderType
   *            the borderType to set.
   */
  public void setBorderType(int borderType) {
    this.borderType = borderType;
  }

  /**
   * Sets the font.
   * 
   * @param font
   *            the font to set.
   */
  public void setFont(Font font) {
    this.font = font;
  }

  /**
   * Sets the foreground.
   * 
   * @param foreground
   *            the foreground to set.
   */
  public void setForeground(Color foreground) {
    this.foreground = foreground;
  }

  /**
   * Sets the grantedRoles.
   * 
   * @param grantedRoles
   *            the grantedRoles to set.
   */
  public void setGrantedRoles(Collection<String> grantedRoles) {
    this.grantedRoles = grantedRoles;
  }

}
