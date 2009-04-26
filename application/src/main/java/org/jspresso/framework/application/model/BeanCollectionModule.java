/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.model;

import java.util.Collection;

import org.jspresso.framework.application.model.descriptor.BeanCollectionModuleDescriptor;
import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.util.lang.ObjectUtils;
import org.jspresso.framework.view.descriptor.ICollectionViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicBorderViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicViewDescriptor;

/**
 * A bean collection module is a module dealing with a collection of beans.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BeanCollectionModule extends Module {

  /**
   * <code>MODULE_OBJECTS</code> is "moduleObjects".
   */
  public static final String           MODULE_OBJECTS = "moduleObjects";

  private IComponentDescriptor<Object> elementComponentDescriptor;
  private IViewDescriptor              elementViewDescriptor;
  private Collection<?>                moduleObjects;

  // /**
  // * Equality based on projected object.
  // * <p>
  // * {@inheritDoc}
  // */
  // @Override
  // public boolean equals(Object obj) {
  // if (!(obj instanceof BeanCollectionModule)) {
  // return false;
  // }
  // if (this == obj) {
  // return true;
  // }
  // // BeanCollectionModule rhs = (BeanCollectionModule) obj;
  //
  // // do not rely on object equality (null lists would make it equal)
  // // return new EqualsBuilder().append(getModuleObjects(),
  // // rhs.getModuleObjects()).isEquals();
  //
  // // Do not rely on names since 2 modules with the same name would make theme
  // // equal.
  // // return ObjectUtils.equals(getName(), rhs.getName());
  // // see [ 2194861 ] Bean collection modules equality should not rely on
  // names
  // //Just rely on object identity.
  // }

  /**
   * Gets the elementComponentDescriptor.
   * 
   * @return the elementComponentDescriptor.
   */
  @SuppressWarnings("unchecked")
  public IComponentDescriptor<Object> getElementComponentDescriptor() {
    if (elementComponentDescriptor == null) {
      if (getProjectedViewDescriptor() instanceof ICollectionViewDescriptor
          && getProjectedViewDescriptor().getModelDescriptor() != null) {
        return (IComponentDescriptor<Object>) ((ICollectionDescriptorProvider<?>) getProjectedViewDescriptor()
            .getModelDescriptor()).getCollectionDescriptor()
            .getElementDescriptor();
      }
    }
    return elementComponentDescriptor;
  }

  /**
   * Gets the elementViewDescriptor.
   * 
   * @return the elementViewDescriptor.
   */
  public IViewDescriptor getElementViewDescriptor() {
    return elementViewDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getIconImageURL() {
    String iconImageUrl = super.getIconImageURL();
    if (iconImageUrl == null) {
      iconImageUrl = getElementComponentDescriptor().getIconImageURL();
      setIconImageURL(iconImageUrl);
    }
    return iconImageUrl;
  }

  // /**
  // * Hash code based on projected object.
  // * <p>
  // * {@inheritDoc}
  // */
  // @Override
  // public int hashCode() {
  // return new HashCodeBuilder(23, 53).append(getName()).toHashCode();
  // see the equals comment.
  // }

  /**
   * Gets the module's projected objects.
   * 
   * @return the projected objects.
   */
  public Collection<?> getModuleObjects() {
    return moduleObjects;
  }

  /**
   * Sets the elementComponentDescriptor.
   * 
   * @param elementComponentDescriptor
   *          the elementComponentDescriptor to set.
   */
  public void setElementComponentDescriptor(
      IComponentDescriptor<Object> elementComponentDescriptor) {
    this.elementComponentDescriptor = elementComponentDescriptor;
  }

  /**
   * Sets the elementViewDescriptor.
   * 
   * @param elementViewDescriptor
   *          the elementViewDescriptor to set.
   */
  public void setElementViewDescriptor(IViewDescriptor elementViewDescriptor) {
    this.elementViewDescriptor = elementViewDescriptor;
  }

  /**
   * Sets the module's projected object collection.
   * 
   * @param moduleObjects
   *          the projected object collection.
   */
  public void setModuleObjects(Collection<?> moduleObjects) {
    if (ObjectUtils.equals(this.moduleObjects, moduleObjects)) {
      return;
    }
    Object oldValue = getModuleObjects();
    this.moduleObjects = moduleObjects;
    firePropertyChange(MODULE_OBJECTS, oldValue, getModuleObjects());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IViewDescriptor getViewDescriptor() {
    IViewDescriptor projectedViewDescriptor = getProjectedViewDescriptor();
    BeanCollectionModuleDescriptor moduleDescriptor = getDescriptor();
    ((BasicViewDescriptor) projectedViewDescriptor)
        .setModelDescriptor(moduleDescriptor
            .getPropertyDescriptor(BeanCollectionModule.MODULE_OBJECTS));
    BasicBorderViewDescriptor moduleViewDescriptor = new BasicBorderViewDescriptor();
    moduleViewDescriptor.setCenterViewDescriptor(projectedViewDescriptor);
    moduleViewDescriptor.setModelDescriptor(moduleDescriptor);
    return moduleViewDescriptor;
  }

  /**
   * Gets the module descriptor.
   * 
   * @return the module descriptor.
   */
  protected BeanCollectionModuleDescriptor getDescriptor() {
    return new BeanCollectionModuleDescriptor(getElementComponentDescriptor());
  }
}
