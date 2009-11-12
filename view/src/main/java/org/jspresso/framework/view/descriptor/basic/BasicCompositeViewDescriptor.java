/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.view.descriptor.basic;

import java.util.List;

import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.view.descriptor.ICollectionViewDescriptor;
import org.jspresso.framework.view.descriptor.ICompositeViewDescriptor;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * Default implementation of a composite view descriptor.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class BasicCompositeViewDescriptor extends BasicViewDescriptor
    implements ICompositeViewDescriptor {

  private boolean cascadingModels;

  /**
   * If no model is defined on this composite view descriptor, gets the one from
   * its leading child.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public IModelDescriptor getModelDescriptor() {
    IModelDescriptor modelDescriptor = super.getModelDescriptor();
    if (isCascadingModels() && modelDescriptor == null) {
      List<IViewDescriptor> childViewDescriptors = getChildViewDescriptors();
      if (childViewDescriptors != null && !childViewDescriptors.isEmpty()) {
        modelDescriptor = childViewDescriptors.get(0).getModelDescriptor();
        setModelDescriptor(modelDescriptor);
      }
    }
    return modelDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isCascadingModels() {
    return cascadingModels;
  }

  /**
   * Sets the cascadingModels.
   * 
   * @param cascadingModels
   *          true if this descriptor is cascading its models based on a master
   *          / detail relationship.
   */
  public void setCascadingModels(boolean cascadingModels) {
    this.cascadingModels = cascadingModels;
  }

  /**
   * Performs any default initialization on a child view descriptor.
   * 
   * @param childViewDescriptor
   *          the child view descriptor to initialize.
   * @param isLeading
   *          is it the leading child descriptor (e.g. the 1st one not null) ?
   */
  protected void completeChildDescriptor(IViewDescriptor childViewDescriptor,
      boolean isLeading) {
    IModelDescriptor modelDescriptor = super.getModelDescriptor();
    if (modelDescriptor != null
        && childViewDescriptor instanceof BasicViewDescriptor
        && childViewDescriptor.getModelDescriptor() == null) {
      if (isCascadingModels()) {
        if (isLeading) {
          ((BasicViewDescriptor) childViewDescriptor)
              .setModelDescriptor(modelDescriptor);
        }
      } else {
        if (childViewDescriptor instanceof IPropertyViewDescriptor
            || childViewDescriptor instanceof ICollectionViewDescriptor) {
          if (modelDescriptor instanceof IComponentDescriptorProvider<?>) {
            // we can complete this property view descriptor model based on the
            // surrounding model.
            ((BasicViewDescriptor) childViewDescriptor)
                .setModelDescriptor(((IComponentDescriptorProvider<?>) modelDescriptor)
                    .getComponentDescriptor().getPropertyDescriptor(
                        childViewDescriptor.getName()));
          }
        } else {
          ((BasicViewDescriptor) childViewDescriptor)
              .setModelDescriptor(modelDescriptor);
        }
      }
    }
  }
}
