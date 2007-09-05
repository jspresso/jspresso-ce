/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.view.descriptor.basic;

import java.util.Locale;

import com.d2s.framework.application.model.descriptor.ModuleDescriptor;
import com.d2s.framework.application.view.descriptor.ISubModuleDescriptor;
import com.d2s.framework.util.descriptor.DefaultIconDescriptor;
import com.d2s.framework.util.i18n.ITranslationProvider;
import com.d2s.framework.view.descriptor.IViewDescriptor;
import com.d2s.framework.view.descriptor.basic.BasicListViewDescriptor;
import com.d2s.framework.view.descriptor.basic.BasicSimpleTreeLevelDescriptor;

/**
 * This is the default implementation of a simple module view descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicSubModuleDescriptor extends BasicSimpleTreeLevelDescriptor
    implements ISubModuleDescriptor {

  private DefaultIconDescriptor descriptor;
  private IViewDescriptor       viewDescriptor;

  /**
   * Constructs a new <code>BasicSubModuleDescriptor</code> instance.
   */
  public BasicSubModuleDescriptor() {
    descriptor = new DefaultIconDescriptor();
    BasicListViewDescriptor moduleNodeGroupDescriptor = new BasicListViewDescriptor();
    moduleNodeGroupDescriptor.setName(getName());
    moduleNodeGroupDescriptor.setDescription(getDescription());
    moduleNodeGroupDescriptor.setIconImageURL(getIconImageURL());
    moduleNodeGroupDescriptor
        .setModelDescriptor(ModuleDescriptor.MODULE_DESCRIPTOR
            .getPropertyDescriptor("subModules"));
    moduleNodeGroupDescriptor.setRenderedProperty("i18nName");
    setNodeGroupDescriptor(moduleNodeGroupDescriptor);
  }

  /**
   * {@inheritDoc}
   */
  public String getDescription() {
    return descriptor.getDescription();
  }

  /**
   * {@inheritDoc}
   */
  public String getI18nDescription(ITranslationProvider translationProvider,
      Locale locale) {
    return descriptor.getI18nDescription(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   */
  public String getI18nName(ITranslationProvider translationProvider,
      Locale locale) {
    return descriptor.getI18nName(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   */
  public String getIconImageURL() {
    return descriptor.getIconImageURL();
  }

  /**
   * {@inheritDoc}
   */
  public String getName() {
    return descriptor.getName();
  }

  /**
   * Gets the viewDescriptor.
   * 
   * @return the viewDescriptor.
   */
  public IViewDescriptor getViewDescriptor() {
    return viewDescriptor;
  }

  /**
   * Sets the description.
   * 
   * @param description
   *            the description to set.
   */
  public void setDescription(String description) {
    descriptor.setDescription(description);
  }

  /**
   * Sets the iconImageURL.
   * 
   * @param iconImageURL
   *            the iconImageURL to set.
   */
  public void setIconImageURL(String iconImageURL) {
    descriptor.setIconImageURL(iconImageURL);
  }

  /**
   * Sets the name.
   * 
   * @param name
   *            the name to set.
   */
  public void setName(String name) {
    descriptor.setName(name);
  }

  /**
   * Sets the viewDescriptor.
   * 
   * @param viewDescriptor
   *            the viewDescriptor to set.
   */
  public void setViewDescriptor(IViewDescriptor viewDescriptor) {
    this.viewDescriptor = viewDescriptor;
  }
}
