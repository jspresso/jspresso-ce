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
package org.jspresso.framework.view.descriptor.basic;

import java.util.Collection;
import java.util.Locale;

import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.util.descriptor.DefaultIconDescriptor;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.util.gate.IGateAccessible;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.util.lang.StringUtils;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.descriptor.EBorderType;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * Default implementation of a view descriptor.
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
 */
public abstract class BasicViewDescriptor extends DefaultIconDescriptor
    implements IViewDescriptor {

  private ActionMap          actionMap;
  private String             background;
  private EBorderType        borderType = EBorderType.NONE;
  private String             font;
  private String             foreground;
  private IModelDescriptor   modelDescriptor;
  private Collection<String> grantedRoles;
  private Collection<IGate>  readabilityGates;
  private boolean            readOnly;
  private Collection<IGate>  writabilityGates;

  /**
   * Gets the grantedRoles.
   * 
   * @return the grantedRoles.
   */
  public Collection<String> getGrantedRoles() {
    if (grantedRoles == null && getModelDescriptor() != null) {
      if (getModelDescriptor() instanceof ISecurable) {
        return ((ISecurable) getModelDescriptor()).getGrantedRoles();
      }
    }
    return grantedRoles;
  }

  /**
   * Gets the readabilityGates.
   * 
   * @return the readabilityGates.
   */
  public Collection<IGate> getReadabilityGates() {
    // Gates are handled both on model connector and view connector. It is not
    // necessary to fetch the model gates here. Only component view descriptors
    // use their model gates since they are often backed by a reference property
    // connector.
    // if (readabilityGates == null && getModelDescriptor() != null) {
    // if (getModelDescriptor() instanceof IGateAccessible) {
    // return ((IGateAccessible) getModelDescriptor()).getReadabilityGates();
    // }
    // }
    return readabilityGates;
  }

  /**
   * Gets the writabilityGates.
   * 
   * @return the writabilityGates.
   */
  public Collection<IGate> getWritabilityGates() {
    // Gates are handled both on model connector and view connector. It is not
    // necessary to fetch the model gates here. Only component view descriptors
    // use their model gates since they are often backed by a reference property
    // connector.
    // if (writabilityGates == null && getModelDescriptor() != null) {
    // if (getModelDescriptor() instanceof IGateAccessible) {
    // return ((IGateAccessible) getModelDescriptor()).getWritabilityGates();
    // }
    // }
    return writabilityGates;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isReadOnly() {
    if (!readOnly && getModelDescriptor() != null) {
      if (getModelDescriptor() instanceof IGateAccessible) {
        return ((IGateAccessible) getModelDescriptor()).isReadOnly();
      }
    }
    return readOnly;
  }

  /**
   * Sets the grantedRoles.
   * 
   * @param grantedRoles
   *          the grantedRoles to set.
   */
  public void setGrantedRoles(Collection<String> grantedRoles) {
    this.grantedRoles = StringUtils.ensureSpaceFree(grantedRoles);
  }

  /**
   * Sets the readabilityGates.
   * 
   * @param readabilityGates
   *          the readabilityGates to set.
   */
  public void setReadabilityGates(Collection<IGate> readabilityGates) {
    this.readabilityGates = readabilityGates;
  }

  /**
   * Sets the readOnly.
   * 
   * @param readOnly
   *          the readOnly to set.
   */
  public void setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
  }

  /**
   * Sets the writabilityGates.
   * 
   * @param writabilityGates
   *          the writabilityGates to set.
   */
  public void setWritabilityGates(Collection<IGate> writabilityGates) {
    this.writabilityGates = writabilityGates;
  }

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
  public String getBackground() {
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
  public String getFont() {
    return font;
  }

  /**
   * {@inheritDoc}
   */
  public String getForeground() {
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
        && getModelDescriptor() instanceof IComponentDescriptor<?>) {
      iconImageURL = ((IComponentDescriptor<?>) getModelDescriptor())
          .getIconImageURL();
      setIconImageURL(iconImageURL);
    }
    return iconImageURL;
  }

  /**
   * Gets the modelDescriptor.
   * 
   * @return the modelDescriptor.
   */
  public IModelDescriptor getModelDescriptor() {
    return modelDescriptor;
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
  public void setBackground(String background) {
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
  public void setFont(String font) {
    this.font = font;
  }

  /**
   * Sets the foreground.
   * 
   * @param foreground
   *          the foreground to set.
   */
  public void setForeground(String foreground) {
    this.foreground = foreground;
  }

  /**
   * Sets the modelDescriptor.
   * 
   * @param modelDescriptor
   *          the modelDescriptor to set.
   */
  public void setModelDescriptor(IModelDescriptor modelDescriptor) {
    this.modelDescriptor = modelDescriptor;
  }

}
