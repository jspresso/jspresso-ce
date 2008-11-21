/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.view.descriptor.basic;

import java.awt.Color;
import java.awt.Font;
import java.util.Locale;

import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.descriptor.EBorderType;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * Default implementation of a view descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
 */
public abstract class BasicViewDescriptor extends BasicSubviewDescriptor
    implements IViewDescriptor {

  private ActionMap   actionMap;
  private Color       background;
  private EBorderType borderType = EBorderType.NONE;
  private Font        font;
  private Color       foreground;

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
  public EBorderType getBorderType() {
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
   * {@inheritDoc}
   */
  @Override
  public String getI18nDescription(ITranslationProvider translationProvider,
      Locale locale) {
    if (getDescription() == null) {
      if (getModelDescriptor() != null) {
        return getModelDescriptor().getI18nDescription(translationProvider,
            locale);
      }
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
      if (getModelDescriptor() != null) {
        return getModelDescriptor().getI18nName(translationProvider, locale);
      }
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
      iconImageURL = ((IComponentDescriptor<?>) getModelDescriptor())
          .getIconImageURL();
      setIconImageURL(iconImageURL);
    }
    return iconImageURL;
  }

  /**
   * Sets the actionMap.
   * 
   * @param actionMap
   *          the actionMap to set.
   */
  public void setActionMap(ActionMap actionMap) {
    this.actionMap = actionMap;
  }

  /**
   * Sets the background.
   * 
   * @param background
   *          the background to set.
   */
  public void setBackground(Color background) {
    this.background = background;
  }

  /**
   * Sets the borderType.
   * 
   * @param borderType
   *          the borderType to set.
   */
  public void setBorderType(EBorderType borderType) {
    this.borderType = borderType;
  }

  /**
   * Sets the font.
   * 
   * @param font
   *          the font to set.
   */
  public void setFont(Font font) {
    this.font = font;
  }

  /**
   * Sets the foreground.
   * 
   * @param foreground
   *          the foreground to set.
   */
  public void setForeground(Color foreground) {
    this.foreground = foreground;
  }

}
