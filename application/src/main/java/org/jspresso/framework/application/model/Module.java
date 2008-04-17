/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jspresso.framework.util.bean.AbstractPropertyChangeCapable;
import org.jspresso.framework.util.lang.ObjectUtils;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

import com.d2s.framework.action.IAction;
import com.d2s.framework.security.ISecurable;

/**
 * A child module is a non-root module (it belongs to a workspace). A child
 * module uses a view to render its projected artifact.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class Module extends AbstractPropertyChangeCapable implements ISecurable {

  private String             description;
  private Collection<String> grantedRoles;
  private String             i18nDescription;
  private String             i18nName;
  private String             iconImageURL;

  private String             name;
  private Module             parent;
  private IViewDescriptor    projectedViewDescriptor;
  private boolean            started;
  private IAction            startupAction;
  private List<Module>       subModules;

  /**
   * Constructs a new <code>Module</code> instance.
   */
  public Module() {
    started = false;
  }

  /**
   * Adds a child module.
   * 
   * @param child
   *            the child module to add. It will fire a "subModules" property
   *            change event.
   * @return <code>true</code> if the module was succesfully added.
   */
  public boolean addSubModule(Module child) {
    if (subModules == null) {
      subModules = new ArrayList<Module>();
    }
    List<Module> oldValue = new ArrayList<Module>(getSubModules());
    if (subModules.add(child)) {
      updateParentsAndFireSubModulesChanged(oldValue, getSubModules());
      return true;
    }
    return false;
  }

  /**
   * Adds a modules module collection. It will fire a "subModules" property
   * change event.
   * 
   * @param children
   *            the modules modules to add.
   * @return <code>true</code> if the modules module collection was
   *         succesfully added.
   */
  public boolean addSubModules(Collection<? extends Module> children) {
    if (subModules == null) {
      subModules = new ArrayList<Module>();
    }
    List<Module> oldValue = new ArrayList<Module>(getSubModules());
    if (subModules.addAll(children)) {
      updateParentsAndFireSubModulesChanged(oldValue, getSubModules());
      return true;
    }
    return false;
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
   * Gets the module's parent module.
   * 
   * @return the parent module or null if none.
   */
  public Module getParent() {
    return parent;
  }

  /**
   * Gets the projectedViewDescriptor.
   * 
   * @return the projectedViewDescriptor.
   */
  public IViewDescriptor getProjectedViewDescriptor() {
    return projectedViewDescriptor;
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
   * Gets the modules modules.
   * 
   * @return the list of modules modules.
   */
  public List<Module> getSubModules() {
    return subModules;
  }

  /**
   * Hash code based on name.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return new HashCodeBuilder(23, 57).append(name).toHashCode();
  }

  /**
   * Gets the started.
   * 
   * @return the started.
   */
  public boolean isStarted() {
    return started;
  }

  /**
   * Removes a child module. It will fire a "subModules" property change event.
   * 
   * @param module
   *            the child module to remove.
   * @return <code>true</code> if the module was succesfully removed.
   */
  public boolean removeSubModule(Module module) {
    if (subModules != null) {
      List<Module> oldValue = new ArrayList<Module>(getSubModules());
      if (subModules.remove(module)) {
        updateParentsAndFireSubModulesChanged(oldValue, getSubModules());
        return true;
      }
      return false;
    }
    return false;
  }

  /**
   * Removes a modules module collection. It will fire a "subModules" property
   * change event.
   * 
   * @param children
   *            the modules modules to remove.
   * @return <code>true</code> if the modules module collection was
   *         succesfully removed.
   */
  public boolean removeSubModules(Collection<Module> children) {
    if (subModules != null) {
      List<Module> oldValue = new ArrayList<Module>(getSubModules());
      if (subModules.removeAll(children)) {
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
   * Sets the parent module. It will fire a "parent" property change event.
   * 
   * @param parent
   *            the parent module to set or null if none.
   */
  public void setParent(Module parent) {
    if (ObjectUtils.equals(this.parent, parent)) {
      return;
    }
    Module oldParent = getParent();
    if (getParent() != null) {
      getParent().removeSubModule(this);
    }
    this.parent = parent;
    if (getParent() != null && !getParent().getSubModules().contains(this)) {
      getParent().addSubModule(this);
    }
    firePropertyChange("parent", oldParent, getParent());
  }

  /**
   * Sets the projectedViewDescriptor.
   * 
   * @param projectedViewDescriptor
   *            the projectedViewDescriptor to set.
   */
  public void setProjectedViewDescriptor(IViewDescriptor projectedViewDescriptor) {
    this.projectedViewDescriptor = projectedViewDescriptor;
  }

  /**
   * Sets the started.
   * 
   * @param started
   *            the started to set.
   */
  public void setStarted(boolean started) {
    this.started = started;
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
   * Sets the modules modules. It will fire a "subModules" property change
   * event.
   * 
   * @param children
   *            the modules modules to set.
   */
  public void setSubModules(List<Module> children) {
    List<Module> oldValue = null;
    if (getSubModules() != null) {
      oldValue = new ArrayList<Module>(getSubModules());
    }
    this.subModules = children;
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
   * This method will set the parent module to the new modules modules and
   * remove the parent of the old removed modules modules. It will fire the
   * "subModules" property change event.
   * 
   * @param oldChildren
   *            the old modules collection property.
   * @param newChildren
   *            the new modules collection property.
   */
  protected void updateParentsAndFireSubModulesChanged(
      List<Module> oldChildren, List<Module> newChildren) {
    if (oldChildren != null) {
      for (Module oldChild : oldChildren) {
        if (newChildren == null || !newChildren.contains(oldChild)) {
          oldChild.setParent(null);
        }
      }
    }
    if (newChildren != null) {
      for (Module newChild : newChildren) {
        if (oldChildren == null || !oldChildren.contains(newChild)) {
          newChild.setParent(this);
        }
      }
    }
    firePropertyChange("subModules", oldChildren, newChildren);
  }
}
