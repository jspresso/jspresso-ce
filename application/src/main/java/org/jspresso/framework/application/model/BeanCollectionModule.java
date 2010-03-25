/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.application.model.descriptor.BeanCollectionModuleDescriptor;
import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.util.collection.ESort;
import org.jspresso.framework.util.lang.ObjectUtils;
import org.jspresso.framework.view.descriptor.ICollectionViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicBorderViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicViewDescriptor;

/**
 * A bean collection module is a module dealing with a collection of beans.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BeanCollectionModule extends Module {

  private Map<String, ESort>           orderingProperties;
  private Integer                      pageSize;

  /**
   * <code>MODULE_OBJECTS</code> is "moduleObjects".
   */
  public static final String           MODULE_OBJECTS = "moduleObjects";

  private IComponentDescriptor<Object> elementComponentDescriptor;
  private IViewDescriptor              elementViewDescriptor;
  private List<?>                      moduleObjects;

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

  /**
   * Gets the module's projected objects.
   * 
   * @return the projected objects.
   */
  public List<?> getModuleObjects() {
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
  public void setModuleObjects(List<?> moduleObjects) {
    if (ObjectUtils.equals(this.moduleObjects, moduleObjects)) {
      return;
    }
    Object oldValue = getModuleObjects();
    this.moduleObjects = moduleObjects;
    firePropertyChange(MODULE_OBJECTS, oldValue, getModuleObjects());
  }

  /**
   * Adds an element to the module's projected object collection.
   * 
   * @param element
   *          the element to add.
   */
  public void addToModuleObjects(Object element) {
    List<Object> newModuleObjects;
    if (getModuleObjects() != null) {
      newModuleObjects = new ArrayList<Object>(getModuleObjects());
    } else {
      newModuleObjects = new ArrayList<Object>();
    }
    newModuleObjects.add(element);
    setModuleObjects(newModuleObjects);
  }

  /**
   * Adds an element to the module's projected object collection at the
   * specified index. If the index is out of the list bounds, the element is
   * simply added at the end of the list.
   * 
   * @param index
   *          the index to add the events element at.
   * @param element
   *          the element to add.
   */
  public void addToModuleObjects(int index, Object element) {
    List<Object> newModuleObjects;
    if (getModuleObjects() != null) {
      newModuleObjects = new ArrayList<Object>(getModuleObjects());
    } else {
      newModuleObjects = new ArrayList<Object>();
    }
    if (index < 0) {
      newModuleObjects.add(0, element);
    } else if (index >= newModuleObjects.size()) {
      newModuleObjects.add(element);
    } else {
      newModuleObjects.add(index, element);
    }
    setModuleObjects(newModuleObjects);
  }

  /**
   * Removes an element from the the module's projected object collection.
   * 
   * @param element
   *          the element to remove.
   */
  public void removeFromModuleObjects(Object element) {
    if (getModuleObjects() != null) {
      List<Object> newModuleObjects;
      newModuleObjects = new ArrayList<Object>(getModuleObjects());
      newModuleObjects.remove(element);
      setModuleObjects(newModuleObjects);
    }
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

    // TEST
    // BasicListViewDescriptor subModulesListViewDescriptor = new
    // BasicListViewDescriptor();
    // subModulesListViewDescriptor.setRenderedProperty("name");
    // subModulesListViewDescriptor.setModelDescriptor(moduleDescriptor
    // .getPropertyDescriptor(Module.SUB_MODULES));
    // subModulesListViewDescriptor.setPreferredWidth(new Integer(300));
    // subModulesListViewDescriptor.setPreferredHeight(new Integer(300));
    // moduleViewDescriptor.setEastViewDescriptor(subModulesListViewDescriptor);
    // TEST

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

  /**
   * Gets the orderingProperties.
   * 
   * @return the orderingProperties.
   */
  public Map<String, ESort> getOrderingProperties() {
    if (orderingProperties == null) {
      return getElementComponentDescriptor().getOrderingProperties();
    }
    return orderingProperties;
  }

  /**
   * Sets the orderingProperties.
   * 
   * @param orderingProperties
   *          the orderingProperties to set.
   */
  public void setOrderingProperties(Map<String, ESort> orderingProperties) {
    this.orderingProperties = orderingProperties;
  }

  /**
   * Gets the pageSize.
   * 
   * @return the pageSize.
   */
  public Integer getPageSize() {
    if (pageSize == null) {
      return getElementComponentDescriptor().getPageSize();
    }
    return pageSize;
  }

  /**
   * Sets the pageSize.
   * 
   * @param pageSize
   *          the pageSize to set.
   */
  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<String> getGrantedRoles() {
    Collection<String> grantedRoles = super.getGrantedRoles();
    if (grantedRoles == null && elementViewDescriptor != null) {
      grantedRoles = elementViewDescriptor.getGrantedRoles();
    }
    if (grantedRoles == null && elementComponentDescriptor != null) {
      grantedRoles = elementComponentDescriptor.getGrantedRoles();
    }
    return grantedRoles;
  }
}
