/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.descriptor.basic;

import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.IReferencePropertyViewDescriptor;

/**
 * Basic implemenation of a reference property view descriptor.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicReferencePropertyViewDescriptor extends
    BasicPropertyViewDescriptor implements IReferencePropertyViewDescriptor {

  private IDisplayableAction lovAction;

  /**
   * {@inheritDoc}
   */
  public IDisplayableAction getLovAction() {
    return lovAction;
  }

  /**
   * Sets the lovAction.
   * 
   * @param lovAction
   *          the lovAction to set.
   */
  public void setLovAction(IDisplayableAction lovAction) {
    this.lovAction = lovAction;
  }

}
