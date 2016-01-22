/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.view.descriptor.basic;

import java.util.Locale;

import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.application.model.descriptor.ModuleDescriptor;
import org.jspresso.framework.util.descriptor.DefaultIconDescriptor;
import org.jspresso.framework.util.descriptor.IIconDescriptor;
import org.jspresso.framework.util.gui.Icon;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicListViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicSimpleTreeLevelDescriptor;

/**
 * This is the default implementation of a simple module view descriptor.
 *
 * @author Vincent Vandenschrick
 * @internal
 */
public class BasicModuleViewDescriptor extends BasicSimpleTreeLevelDescriptor
    implements IIconDescriptor {

  private final DefaultIconDescriptor descriptor;
  private IViewDescriptor       projectedViewDescriptor;

  /**
   * Constructs a new {@code BasicModuleViewDescriptor} instance.
   */
  public BasicModuleViewDescriptor() {
    descriptor = new DefaultIconDescriptor();
    BasicListViewDescriptor moduleNodeGroupDescriptor = new BasicListViewDescriptor();
    moduleNodeGroupDescriptor.setName(getName());
    moduleNodeGroupDescriptor.setDescription(getDescription());
    moduleNodeGroupDescriptor.setIcon(getIcon());
    moduleNodeGroupDescriptor
        .setModelDescriptor(ModuleDescriptor.MODULE_DESCRIPTOR
            .getPropertyDescriptor(Module.SUB_MODULES));
    moduleNodeGroupDescriptor.setRenderedProperty(Module.I18N_NAME);
    setNodeGroupDescriptor(moduleNodeGroupDescriptor);
    setChildDescriptor(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDescription() {
    return descriptor.getDescription();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getI18nDescription(ITranslationProvider translationProvider,
      Locale locale) {
    return descriptor.getI18nDescription(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getI18nName(ITranslationProvider translationProvider,
      Locale locale) {
    return descriptor.getI18nName(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Icon getIcon() {
    return descriptor.getIcon();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return descriptor.getName();
  }

  /**
   * Gets the projectedViewDescriptor.
   *
   * @return the projectedViewDescriptor.
   */
  public IViewDescriptor getProjectedViewDescriptor() {
    return projectedViewDescriptor;
  }

  /**
   * Sets the description.
   *
   * @param description
   *          the description to set.
   */
  public void setDescription(String description) {
    descriptor.setDescription(description);
  }

  /**
   * Sets the iconImageURL.
   *
   * @param iconImageURL
   *          the iconImageURL to set.
   */
  public void setIconImageURL(String iconImageURL) {
    descriptor.setIconImageURL(iconImageURL);
  }

  /**
   * Sets the name.
   *
   * @param name
   *          the name to set.
   */
  public void setName(String name) {
    descriptor.setName(name);
  }

  /**
   * Sets the projectedViewDescriptor.
   *
   * @param projectedViewDescriptor
   *          the projectedViewDescriptor to set.
   */
  public void setProjectedViewDescriptor(IViewDescriptor projectedViewDescriptor) {
    this.projectedViewDescriptor = projectedViewDescriptor;
  }

  /**
   * Gets the lastUpdated.
   *
   * @return the lastUpdated.
   */
  @Override
  public long getLastUpdated() {
    return descriptor.getLastUpdated();
  }

  /**
   * Sets the lastUpdated.
   *
   * @param lastUpdated
   *          the lastUpdated to set.
   * @internal
   */
  public void setLastUpdated(long lastUpdated) {
    descriptor.setLastUpdated(lastUpdated);
  }
}
