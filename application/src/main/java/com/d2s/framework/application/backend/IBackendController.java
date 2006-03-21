/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend;

import com.d2s.framework.application.IController;
import com.d2s.framework.application.backend.session.IApplicationSession;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.model.descriptor.IModelDescriptor;

/**
 * This interface establishes the contract of the backend controllers. Backend
 * controllers are controllers which act on the application domain model (as
 * oposed to frontend controllers which act on the application view).
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IBackendController extends IController {

  /**
   * Asks this backend controller to perform any necessary action upon startup.
   * One of this action should be to construct the root connector based on the
   * root model descriptor.
   */
  void start();

  /**
   * Given a module identifier, this method returns the composite connector used
   * as model connector for the associated module.
   * 
   * @param moduleId
   *          the modulen identifier.
   * @return the associated module connector.
   */
  ICompositeValueConnector getModuleConnector(String moduleId);

  /**
   * Creates a model connector out of a model descriptor. It should be either a
   * bean connector or a bean collection connector depending on the type of
   * model descriptor.
   * 
   * @param modelDescriptor
   *          the model descriptor to create the connector for.
   * @return the created model connector.
   */
  IValueConnector createModelConnector(IModelDescriptor modelDescriptor);

  /**
   * Gets the applicationSession for this backend controller.
   * 
   * @return the current controller application session.
   */
  IApplicationSession getApplicationSession();

}
