/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.descriptor.basic;

import java.util.List;

import org.jspresso.framework.view.descriptor.ITabViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;


/**
 * Default implementation of a tab view descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicTabViewDescriptor extends BasicCompositeViewDescriptor
    implements ITabViewDescriptor {

  private List<IViewDescriptor> viewDescriptors;

  /**
   * {@inheritDoc}
   */
  public List<IViewDescriptor> getChildViewDescriptors() {
    return viewDescriptors;
  }

  /**
   * Sets the viewDescriptors.
   * 
   * @param viewDescriptors
   *            the viewDescriptors to set.
   */
  public void setViewDescriptors(List<IViewDescriptor> viewDescriptors) {
    this.viewDescriptors = viewDescriptors;
  }
}
