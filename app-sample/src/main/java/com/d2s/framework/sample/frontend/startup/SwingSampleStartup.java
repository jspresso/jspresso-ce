/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.sample.frontend.startup;

import com.d2s.framework.application.backend.IBackendController;
import com.d2s.framework.application.frontend.startup.swing.SwingStartup;
import com.d2s.framework.application.module.BeanModule;
import com.d2s.framework.application.module.Module;
import com.d2s.framework.sample.data.AppDataProducer;

/**
 * Swing sample startup class.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SwingSampleStartup extends SwingStartup {

  /**
   * Sets up some test data.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void start() {
    super.start();
    IBackendController backController = (IBackendController) getApplicationContext()
        .getBean("applicationBackController");
    BeanModule companyModule = (BeanModule) ((Module) backController
        .getModuleConnector("company").getConnectorValue()).getSubModules()
        .get(0);
    companyModule.setModuleObjects(new AppDataProducer(getApplicationContext())
        .createTestData());
  }

  /**
   * Returns the "sample-swing-context" value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected String getApplicationContextKey() {
    return "sample-swing-context";
  }
}
