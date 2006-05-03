/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.view.descriptor.basic;

import java.util.Locale;

import com.d2s.framework.application.view.descriptor.ISimpleSubModuleDescriptor;
import com.d2s.framework.util.descriptor.DefaultIconDescriptor;
import com.d2s.framework.util.descriptor.IIconDescriptor;
import com.d2s.framework.util.i18n.ITranslationProvider;
import com.d2s.framework.view.descriptor.IListViewDescriptor;
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
public class BasicSimpleSubModuleDescriptor extends
    BasicSimpleTreeLevelDescriptor implements ISimpleSubModuleDescriptor,
    IIconDescriptor {

  private IViewDescriptor       viewDescriptor;
  private DefaultIconDescriptor descriptor;

  /**
   * Constructs a new <code>BasicSimpleSubModuleDescriptor</code> instance.
   */
  public BasicSimpleSubModuleDescriptor() {
    descriptor = new DefaultIconDescriptor();
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
   * Sets the viewDescriptor.
   * 
   * @param viewDescriptor
   *          the viewDescriptor to set.
   */
  public void setViewDescriptor(IViewDescriptor viewDescriptor) {
    this.viewDescriptor = viewDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IListViewDescriptor getNodeGroupDescriptor() {
    if (super.getNodeGroupDescriptor() == null) {
      BasicListViewDescriptor moduleNodeGroupDescriptor = new BasicListViewDescriptor();
      moduleNodeGroupDescriptor.setName(getName());
      moduleNodeGroupDescriptor.setDescription(getDescription());
      moduleNodeGroupDescriptor.setIconImageURL(getIconImageURL());
      moduleNodeGroupDescriptor.setModelDescriptor(MODULE_DESCRIPTOR
          .getPropertyDescriptor("subModules"));
      moduleNodeGroupDescriptor.setRenderedProperty("name");
      setNodeGroupDescriptor(moduleNodeGroupDescriptor);
    }
    return super.getNodeGroupDescriptor();
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
   * {@inheritDoc}
   */
  public String getI18nName(ITranslationProvider translationProvider, Locale locale) {
    return descriptor.getI18nName(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   */
  public String getI18nDescription(ITranslationProvider translationProvider, Locale locale) {
    return descriptor.getI18nDescription(translationProvider, locale);
  }
}
