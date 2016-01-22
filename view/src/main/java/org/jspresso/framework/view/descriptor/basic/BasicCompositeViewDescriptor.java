/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
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

import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.view.descriptor.ICollectionViewDescriptor;
import org.jspresso.framework.view.descriptor.ICompositeViewDescriptor;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * This is the abstract base class for all composite views. A composite view is
 * a view that arranges a collection of nested views in a predefined layout.
 * Nested views can also be composite views which makes it possible to build
 * complex UIs out of simple combinations. Composite views are also the
 * foundation of master-detail views by allowing a nested view to take its model
 * out of the selection of the previous one. This behaviour can be activated
 * using the {@code cascadingModels} property that &quot;cascades&quot; the
 * view models based on the selected elements. Whenever this behaviour is not
 * activated, all nested views share the same model than their parent composite
 * unless specified otherwise.
 *
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
  @Override
  public boolean isCascadingModels() {
    return cascadingModels;
  }

  /**
   * Enables the model &quot;cascading&quot; behaviour. This allows for instance
   * to link 2 nested tables where the 2nd table model is the selected row of
   * the first table (or {@code null} if selection is empty). Using
   * {@code cascadingModel=true} is only necessary when tracking view
   * selection on the master nested view. You don't need it if, for instance,
   * the master nested view is a single model view like a component view. In the
   * latter case, you can bind a table detail view just by adding it to the same
   * composite without having to set {@code cascadingModel=true}.
   * <p>
   * Default value is {@code false}, i.e. al nested views share the same
   * model than the outer composite unless explicitly specified differently.
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
   * @param previousChildDescriptor
   *          null if it is the leading child descriptor or the previous one ?
   */
  protected void completeChildDescriptor(IViewDescriptor childViewDescriptor,
      IViewDescriptor previousChildDescriptor) {
    IModelDescriptor modelDescriptor = super.getModelDescriptor();
    if (modelDescriptor != null
        && childViewDescriptor instanceof BasicViewDescriptor
        && childViewDescriptor.getModelDescriptor() == null) {
      if (isCascadingModels()) {
        if (previousChildDescriptor == null) {
          ((BasicViewDescriptor) childViewDescriptor)
              .setModelDescriptor(modelDescriptor);
        } else {
          IModelDescriptor previousModelDescriptor = previousChildDescriptor
              .getModelDescriptor();
          if (previousModelDescriptor instanceof ICollectionPropertyDescriptor<?>) {
            ((BasicViewDescriptor) childViewDescriptor)
                .setModelDescriptor(((ICollectionPropertyDescriptor<?>) previousModelDescriptor)
                    .getReferencedDescriptor().getElementDescriptor());
          } else if (previousModelDescriptor instanceof IReferencePropertyDescriptor<?>) {
            ((BasicViewDescriptor) childViewDescriptor)
                .setModelDescriptor(((IReferencePropertyDescriptor<?>) previousModelDescriptor)
                    .getReferencedDescriptor());
          }
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
          if (modelDescriptor instanceof IComponentDescriptorProvider<?>) {
            ((BasicViewDescriptor) childViewDescriptor)
                .setModelDescriptor(((IComponentDescriptorProvider<?>) modelDescriptor)
                    .getComponentDescriptor());
          } else {
            ((BasicViewDescriptor) childViewDescriptor)
                .setModelDescriptor(modelDescriptor);
          }
        }
      }
    }
  }
}
