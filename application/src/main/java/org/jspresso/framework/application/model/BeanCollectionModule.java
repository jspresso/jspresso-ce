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
 * This type of module keeps a reference on a beans collection. There is no
 * assumption made on wether these beans are actually persistent entites or any
 * other type of java beans.
 * <p>
 * Simple bean collection modules must have their collection of referenced beans
 * initialized somehow. There is no standard built-in action to do so since it
 * is highly dependent on what's needed. So it's rather common to have the
 * module content initialized through a startup action depending on the session
 * state.
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
   * Configures the type of bean element this collection module manages. A bunch
   * of default values are inferred from this element component descriptor. For
   * instance, pageing size (if used) will default to the component one unless
   * explicitely set. Same goes for icon image URL, default ordering properties
   * or even granted roles. The latter means that bean collection modules based
   * on forbidden entities will automatically be excluded from the workspace of
   * the logged-in user.
   * <p>
   * if not explicitely configured, the eement component descriptor can be
   * inferred from the collection view descriptor configured as projected view
   * descriptor.
   * 
   * @param elementComponentDescriptor
   *          the elementComponentDescriptor to set.
   */
  public void setElementComponentDescriptor(
      IComponentDescriptor<Object> elementComponentDescriptor) {
    this.elementComponentDescriptor = elementComponentDescriptor;
  }

  /**
   * This property is not used by the module itself, but by built-in actions
   * that maybe registered on this module. One of tese actions is
   * <code>AddBeanAsSubModuleAction</code>.
   * <p>
   * This property indicates the view to use whenever the user requests a
   * &quot;form-like&quot; view on a collection element. Naturally the
   * configured element view descriptor must be backed by a model matching the
   * type of the module managed beans.
   * 
   * @param elementViewDescriptor
   *          the elementViewDescriptor to set.
   */
  public void setElementViewDescriptor(IViewDescriptor elementViewDescriptor) {
    this.elementViewDescriptor = elementViewDescriptor;
  }

  /**
   * Assigns the list of beans this module manages. The projected view will
   * automatically reflect this change since a &quot;moduleObjects&quot;
   * property change will be fired.
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
