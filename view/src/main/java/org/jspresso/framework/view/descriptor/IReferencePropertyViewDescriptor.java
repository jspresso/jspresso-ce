/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.descriptor;

import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * A property view descriptor used to refine reference property views.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IReferencePropertyViewDescriptor extends
    IPropertyViewDescriptor {

  /**
   * Returns an optional custom LOV action.
   * 
   * @return an optional custom LOV action.
   */
  IDisplayableAction getLovAction();

}
