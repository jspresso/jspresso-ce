/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.d2s.framework.action.IAction;
import com.d2s.framework.application.view.descriptor.basic.BasicModuleDescriptor;
import com.d2s.framework.security.ISecurable;
import com.d2s.framework.util.IIconImageURLProvider;
import com.d2s.framework.util.bean.AbstractPropertyChangeCapable;
import com.d2s.framework.view.descriptor.IViewDescriptor;

/**
 * A module is a central element in the application architecture. It serves as
 * an entry point on the domain model. Modules are organized as a tree structure
 * since they can (optionally) provide modules. A module can be seen as a window
 * on the business grouping processes forming a business activity (like master
 * data management, customer contract handling, ...). Each module can
 * (optionally) provide a projected object serving as model root for trigerring
 * grouped processes.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class Module extends AbstractPropertyChangeCapable implements ISecurable {

  private String                description;
  private Collection<String>    grantedRoles;
  private String                i18nDescription;
  private String                i18nName;
  private String                iconImageURL;
  private String                name;
  private IAction               startupAction;
  private IIconImageURLProvider iconImageURLProvider;

  private IViewDescriptor       viewDescriptor;

  private List<SubModule>       subModules;

  /**
   * Sets the iconImageURLProvider.
   * 
   * @param iconImageURLProvider
   *            the iconImageURLProvider to set.
   */
  public void setIconImageURLProvider(IIconImageURLProvider iconImageURLProvider) {
    this.iconImageURLProvider = iconImageURLProvider;
  }

  /**
   * Gets the startupAction.
   * 
   * @return the startupAction.
   */
  public IAction getStartupAction() {
    return startupAction;
  }

  /**
   * Sets the startupAction.
   * 
   * @param startupAction
   *            the startupAction to set.
   */
  public void setStartupAction(IAction startupAction) {
    this.startupAction = startupAction;
  }

  /**
   * Gets the module view descriptor. Whenever the view descriptor has not been
   * set, a default one is generated.
   * 
   * @return the viewDescriptor.
   */
  public IViewDescriptor getViewDescriptor() {
    if (viewDescriptor == null) {
      viewDescriptor = new BasicModuleDescriptor();
      ((BasicModuleDescriptor) viewDescriptor).setName(getName());
      ((BasicModuleDescriptor) viewDescriptor).setDescription(getDescription());
      ((BasicModuleDescriptor) viewDescriptor)
          .setIconImageURL(getIconImageURL());
      ((BasicModuleDescriptor) viewDescriptor)
          .setIconImageURLProvider(iconImageURLProvider);
    }
    return viewDescriptor;
  }

  /**
   * Adds a child module.
   * 
   * @param child
   *            the child module to add. It will fire a "subModules" property
   *            change event.
   * @return <code>true</code> if the module was succesfully added.
   */
  public boolean addSubModule(SubModule child) {
    if (subModules == null) {
      subModules = new ArrayList<SubModule>();
    }
    List<SubModule> oldValue = new ArrayList<SubModule>(getSubModules());
    if (subModules.add(child)) {
      updateParentsAndFireSubModulesChanged(oldValue, getSubModules());
      return true;
    }
    return false;
  }

  /**
   * Adds a subModules module collection. It will fire a "subModules" property
   * change event.
   * 
   * @param subModulesToAdd
   *            the subModules modules to add.
   * @return <code>true</code> if the subModules module collection was
   *         succesfully added.
   */
  public boolean addSubModules(Collection<? extends SubModule> subModulesToAdd) {
    if (subModules == null) {
      subModules = new ArrayList<SubModule>();
    }
    List<SubModule> oldValue = new ArrayList<SubModule>(getSubModules());
    if (subModules.addAll(subModulesToAdd)) {
      updateParentsAndFireSubModulesChanged(oldValue, getSubModules());
      return true;
    }
    return false;
  }

  /**
   * Equality based on name.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Module)) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    Module rhs = (Module) obj;
    return new EqualsBuilder().append(getName(), rhs.getName()).isEquals();
  }

  /**
   * Gets the module's description. It may serve for the module's view.
   * 
   * @return the module's description.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Gets the grantedRoles.
   * 
   * @return the grantedRoles.
   */
  public Collection<String> getGrantedRoles() {
    return grantedRoles;
  }

  /**
   * Gets the i18nDescription.
   * 
   * @return the i18nDescription.
   */
  public String getI18nDescription() {
    if (i18nDescription != null) {
      return i18nDescription;
    }
    return getDescription();
  }

  /**
   * Gets the i18nName.
   * 
   * @return the i18nName.
   */
  public String getI18nName() {
    if (i18nName != null) {
      return i18nName;
    }
    return getName();
  }

  /**
   * Gets the iconImageURL.
   * 
   * @return the iconImageURL.
   */
  public String getIconImageURL() {
    return iconImageURL;
  }

  /**
   * Gets the module's name. It may serve for the module's view.
   * 
   * @return the module's name.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the subModules modules.
   * 
   * @return the list of subModules modules.
   */
  public List<SubModule> getSubModules() {
    return subModules;
  }

  /**
   * Hash code based on name.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return new HashCodeBuilder(23, 53).append(name).toHashCode();
  }

  /**
   * Removes a child module. It will fire a "subModules" property change event.
   * 
   * @param subModule
   *            the child module to remove.
   * @return <code>true</code> if the module was succesfully removed.
   */
  public boolean removeSubModule(SubModule subModule) {
    if (subModules != null) {
      List<SubModule> oldValue = new ArrayList<SubModule>(getSubModules());
      if (subModules.remove(subModule)) {
        updateParentsAndFireSubModulesChanged(oldValue, getSubModules());
        return true;
      }
      return false;
    }
    return false;
  }

  /**
   * Removes a subModules module collection. It will fire a "subModules"
   * property change event.
   * 
   * @param childrenToRemove
   *            the subModules modules to remove.
   * @return <code>true</code> if the subModules module collection was
   *         succesfully removed.
   */
  public boolean removeSubModules(Collection<SubModule> childrenToRemove) {
    if (subModules != null) {
      List<SubModule> oldValue = new ArrayList<SubModule>(getSubModules());
      if (subModules.removeAll(childrenToRemove)) {
        updateParentsAndFireSubModulesChanged(oldValue, getSubModules());
        return true;
      }
      return false;
    }
    return false;
  }

  /**
   * Sets the module's description. It may serve for the module's view.
   * 
   * @param description
   *            the module's description.
   */
  public void setDescription(String description) {
    if (ObjectUtils.equals(this.description, description)) {
      return;
    }
    String oldValue = getDescription();
    this.description = description;
    firePropertyChange("description", oldValue, getDescription());
  }

  /**
   * Sets the grantedRoles.
   * 
   * @param grantedRoles
   *            the grantedRoles to set.
   */
  public void setGrantedRoles(Collection<String> grantedRoles) {
    this.grantedRoles = grantedRoles;
  }

  /**
   * Sets the i18nDescription.
   * 
   * @param i18nDescription
   *            the i18nDescription to set.
   */
  public void setI18nDescription(String i18nDescription) {
    if (ObjectUtils.equals(this.i18nDescription, i18nDescription)) {
      return;
    }
    String oldValue = getI18nDescription();
    this.i18nDescription = i18nDescription;
    firePropertyChange("i18nDescription", oldValue, getI18nDescription());
  }

  /**
   * Sets the i18nName.
   * 
   * @param i18nName
   *            the i18nName to set.
   */
  public void setI18nName(String i18nName) {
    if (ObjectUtils.equals(this.i18nName, i18nName)) {
      return;
    }
    String oldValue = getI18nName();
    this.i18nName = i18nName;
    firePropertyChange("i18nName", oldValue, getI18nName());
  }

  /**
   * Sets the iconImageURL.
   * 
   * @param iconImageURL
   *            the iconImageURL to set.
   */
  public void setIconImageURL(String iconImageURL) {
    this.iconImageURL = iconImageURL;
  }

  /**
   * Sets the module's name. It may serve for the module's view.
   * 
   * @param name
   *            the module's name.
   */
  public void setName(String name) {
    if (ObjectUtils.equals(this.name, name)) {
      return;
    }
    String oldValue = getName();
    this.name = name;
    firePropertyChange("name", oldValue, getName());
  }

  /**
   * Sets the subModules modules. It will fire a "subModules" property change
   * event.
   * 
   * @param subModules
   *            the subModules modules to set.
   */
  public void setSubModules(List<SubModule> subModules) {
    List<SubModule> oldValue = null;
    if (getSubModules() != null) {
      oldValue = new ArrayList<SubModule>(getSubModules());
    }
    this.subModules = subModules;
    updateParentsAndFireSubModulesChanged(oldValue, getSubModules());
  }

  /**
   * based on name.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    if (getI18nName() != null) {
      return getI18nName();
    }
    return "";
  }

  /**
   * This method will set the parent module to the new subModules modules and
   * remove the parent of the old removed subModules modules. It will fire the
   * "subModules" property change event.
   * 
   * @param oldChildren
   *            the old subModules collection property.
   * @param newChildren
   *            the new subModules collection property.
   */
  protected void updateParentsAndFireSubModulesChanged(
      List<SubModule> oldChildren, List<SubModule> newChildren) {
    if (oldChildren != null) {
      for (SubModule oldChild : oldChildren) {
        if (newChildren == null || !newChildren.contains(oldChild)) {
          oldChild.setParent(null);
        }
      }
    }
    if (newChildren != null) {
      for (SubModule newChild : newChildren) {
        if (oldChildren == null || !oldChildren.contains(newChild)) {
          newChild.setParent(this);
        }
      }
    }
    firePropertyChange("subModules", oldChildren, newChildren);
  }
}
