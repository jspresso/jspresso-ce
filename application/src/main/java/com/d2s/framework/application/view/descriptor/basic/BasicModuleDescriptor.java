/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.view.descriptor.basic;

import java.util.Locale;

import com.d2s.framework.action.IAction;
import com.d2s.framework.application.view.descriptor.IModuleDescriptor;
import com.d2s.framework.util.i18n.ITranslationProvider;
import com.d2s.framework.view.descriptor.basic.BasicTreeViewDescriptor;

/**
 * This is a default implementation of a simple module view descriptor.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicModuleDescriptor extends BasicTreeViewDescriptor implements
    IModuleDescriptor {

  private IAction startupAction;

  /**
   * Constructs a new <code>BasicModuleDescriptor</code> instance.
   */
  public BasicModuleDescriptor() {
    BasicSubModuleDescriptor subModuleDescriptor = new BasicSubModuleDescriptor();
    subModuleDescriptor.setChildDescriptor(subModuleDescriptor);
    setRootSubtreeDescriptor(subModuleDescriptor);
  }

  /**
   * {@inheritDoc}
   */
  public IAction getStartupAction() {
    return startupAction;
  }

  /**
   * Sets the startupAction.
   * 
   * @param startupAction
   *            the startupAction to set.
   */
  public void setStartupAction(IAction startupAction) {
    this.startupAction = startupAction;
  }
  
  /**
   * TODO Comment needed.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public String getI18nName(ITranslationProvider translationProvider,
      Locale locale) {
    // TODO Auto-generated method stub
    return super.getI18nName(translationProvider, locale);
  }
}
