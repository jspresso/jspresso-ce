/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.test.app;

import java.util.Locale;

import com.d2s.framework.application.backend.IBackendController;
import com.d2s.framework.application.frontend.IFrontendController;
import com.d2s.framework.test.model.AbstractModelTest;
import com.d2s.framework.view.projection.BeanProjection;
import com.d2s.framework.view.projection.Projection;
import com.ulcjava.base.application.IApplication;

/**
 * Swing view testing.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class UlcAppExample extends AbstractModelTest implements IApplication {

  /**
   * Tests Swing view construction.
   */
  public void start() {
    Locale locale = Locale.FRENCH;

    try {
      setUp();
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    IFrontendController frontController = (IFrontendController) getApplicationContext()
        .getBean("testFrontController");

    IBackendController backController = (IBackendController) getApplicationContext()
        .getBean("testBackController");
    BeanProjection departmentsProjection = (BeanProjection) ((Projection) backController
        .getRootProjectionConnector("company").getConnectorValue())
        .getChildren().get(0);
    departmentsProjection.setProjectedObjects(new AppDataProducer(
        getApplicationContext()).createTestData());
    frontController.start(backController, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String getApplicationContextKey() {
    return "com.d2s.framework.test.view";
  }

  /**
   * {@inheritDoc}
   */
  public void stop() {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  public void activate() {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  public void passivate() {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  public void handleMessage(@SuppressWarnings("unused")
  String message) {
    // NO-OP
  }
}
