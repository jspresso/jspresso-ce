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
import java.util.Iterator;
import java.util.List;

import javax.security.auth.Subject;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.security.ISubjectAware;
import org.jspresso.framework.security.SecurityHelper;
import org.jspresso.framework.util.bean.AbstractPropertyChangeCapable;
import org.jspresso.framework.util.lang.ObjectUtils;
import org.jspresso.framework.util.lang.StringUtils;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptorProvider;

/**
 * A module is an entry point in the application. Modules are organized in
 * bi-directional, parent-children hierarchy. As such, they can be viewed (and
 * they are materalized in the UI) as trees. Modules can be (re)organized
 * dynamically by changing their parent-children relationship and their owning
 * workspace UI will reflect the change seamlessly, as with any Jspresso model
 * (in fact workspaces and modules are regular beans that are used as model in
 * standard Jspresso views).
 * <p>
 * All module, among other features, are capable of providing a view to be
 * installedin the UI wen they are selected. This makes Jspresso applications
 * really modular and their architecture flexible enough to embed and run a
 * large variety of different module types.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class Module extends AbstractPropertyChangeCapable implements
    ISecurable, IViewDescriptorProvider, ISubjectAware {

  /**
   * <code>SUB_MODULES</code> is "subModules".
   */
  public static final String SUB_MODULES      = "subModules";

  /**
   * <code>PARENT</code> is "parent".
   */
  public static final String PARENT           = "parent";

  /**
   * <code>NAME</code> is "name".
   */
  public static final String NAME             = "name";

  /**
   * <code>I18N_NAME</code> is "i18nName".
   */
  public static final String I18N_NAME        = "i18nName";

  /**
   * <code>DESCRIPTION</code> is "description".
   */
  public static final String DESCRIPTION      = "description";

  /**
   * <code>I18N_DESCRIPTION</code> is "i18nDescription".
   */
  public static final String I18N_DESCRIPTION = "i18nDescription";

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
  private IAction            entryAction;
  private IAction            exitAction;
  private List<Module>       subModules;

  private Subject            subject;

  private boolean            dirty;

  /**
   * Constructs a new <code>Module</code> instance.
   */
  public Module() {
    started = false;
    dirty = false;
  }

  /**
   * Adds a child module.
   * 
   * @param child
   *          the child module to add. It will fire a &quot;subModules&quot;
   *          property change event.
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
   * Adds a modules module collection. It will fire a &quot;subModules&quot;
   * property change event.
   * 
   * @param children
   *          the modules modules to add.
   * @return <code>true</code> if the modules module collection was succesfully
   *         added.
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
    if (grantedRoles == null && projectedViewDescriptor != null) {
      return projectedViewDescriptor.getGrantedRoles();
    }
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
    String dirtyMarker = "";
    if (isDirty()) {
      dirtyMarker = "(*)";
    }
    if (i18nName != null) {
      return dirtyMarker + i18nName;
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
    if (subModules == null) {
      return null;
    }
    Subject subj = getSubject();
    if (subj != null) {
      for (Iterator<Module> ite = subModules.iterator(); ite.hasNext();) {
        if (!SecurityHelper.isSubjectGranted(subj, ite.next())) {
          ite.remove();
        }
      }
    }
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
   * Removes a child module. It will fire a &quot;subModules&quot; property
   * change event.
   * 
   * @param module
   *          the child module to remove.
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
   * Removes a modules module collection. It will fire a &quot;subModules&quot;
   * property change event.
   * 
   * @param children
   *          the modules modules to remove.
   * @return <code>true</code> if the modules module collection was succesfully
   *         removed.
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
   * Configures the key used to translate actual internationalized module
   * description. The resulting translation will generally be leveraged as a
   * tooltip on the UI side but its use may be extended for online help.
   * 
   * @param description
   *          the module's description.
   */
  public void setDescription(String description) {
    if (ObjectUtils.equals(this.description, description)) {
      return;
    }
    String oldValue = getDescription();
    this.description = description;
    firePropertyChange(DESCRIPTION, oldValue, getDescription());
    if (this.i18nDescription == null) {
      setI18nDescription(name);
    }
  }

  /**
   * Assigns the roles that are authorized to start this module. Whenever the
   * user is not granted sufficient privileges, the module is simply not
   * installed in the workspace. Setting the collection of granted roles to
   * <code>null</code> (default value) disables role based authorization on this
   * module.
   * <p>
   * Some specific modules that are component/entity model based i.e.
   * <code>Bean(Collection)Module</code> also inherit their authrorizations from
   * their model.
   * 
   * @param grantedRoles
   *          the grantedRoles to set.
   */
  public void setGrantedRoles(Collection<String> grantedRoles) {
    this.grantedRoles = StringUtils.ensureSpaceFree(grantedRoles);
  }

  /**
   * Stores the internationalized workspace description for use in the UI as
   * tooltip for instance.
   * 
   * @param i18nDescription
   *          the i18nDescription to set.
   * @internal
   */
  public void setI18nDescription(String i18nDescription) {
    if (ObjectUtils.equals(this.i18nDescription, i18nDescription)) {
      return;
    }
    String oldValue = getI18nDescription();
    this.i18nDescription = i18nDescription;
    firePropertyChange(I18N_DESCRIPTION, oldValue, getI18nDescription());
    if (this.description == null) {
      setDescription(i18nDescription);
    }
  }

  /**
   * Stores the internationalized workspace name for use in the UI as workspace
   * label.
   * 
   * @param i18nName
   *          the i18nName to set.
   * @internal
   */
  public void setI18nName(String i18nName) {
    if (ObjectUtils.equals(this.i18nName, i18nName)) {
      return;
    }
    String oldValue = getI18nName();
    this.i18nName = i18nName;
    firePropertyChange(I18N_NAME, oldValue, getI18nName());
    if (this.name == null) {
      setName(i18nName);
    }
  }

  /**
   * Sets the icon image URL used to identify this workspace. Supported URL
   * protocols include :
   * <ul>
   * <li>all JVM supported protocols</li>
   * <li>the <b>jar:/</b> pseudo URL protocol</li>
   * <li>the <b>classpath:/</b> pseudo URL protocol</li>
   * </ul>
   * 
   * @param iconImageURL
   *          the iconImageURL to set.
   */
  public void setIconImageURL(String iconImageURL) {
    this.iconImageURL = iconImageURL;
  }

  /**
   * Configures the key used to translate actual internationalized module name.
   * The resulting translation will be leveraged as the module label on the UI
   * side.
   * 
   * @param name
   *          the module's name.
   */
  public void setName(String name) {
    if (ObjectUtils.equals(this.name, name)) {
      return;
    }
    String oldValue = getName();
    this.name = name;
    firePropertyChange(NAME, oldValue, getName());
    if (this.i18nName == null) {
      setI18nName(name);
    }
  }

  /**
   * Assigns the parent module and potentially move itself out of previous
   * parent children. It will fire a &quot;parent&quot; property change event.
   * 
   * @param parent
   *          the parent module to set or null if none.
   * @internal
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
    firePropertyChange(PARENT, oldParent, getParent());
  }

  /**
   * Configures the view descriptor used to construct the view that will be
   * displayed when this module is selected.
   * 
   * @param projectedViewDescriptor
   *          the projectedViewDescriptor to set.
   */
  public void setProjectedViewDescriptor(IViewDescriptor projectedViewDescriptor) {
    this.projectedViewDescriptor = projectedViewDescriptor;
  }

  /**
   * Sets the started.
   * 
   * @param started
   *          the started to set.
   * @internal
   */
  public void setStarted(boolean started) {
    this.started = started;
  }

  /**
   * Configures an action to be executed the first time the module is
   * &quot;started&quot; by the user. The action will execute in the context of
   * the current workspace, this module being the current selected module. It
   * will help initializing module values, notify user, ....
   * 
   * @param startupAction
   *          the startupAction to set.
   */
  public void setStartupAction(IAction startupAction) {
    this.startupAction = startupAction;
  }

  /**
   * Installs a list of module(s) as sub-modules of this one. It will fire a
   * &quot;subModules&quot; property change event.
   * 
   * @param children
   *          the modules modules to set.
   * @internal
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
   * &quot;subModules&quot; property change event.
   * 
   * @param oldChildren
   *          the old modules collection property.
   * @param newChildren
   *          the new modules collection property.
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
    firePropertyChange(SUB_MODULES, oldChildren, newChildren);
  }

  /**
   * Returns unmodified projected view descriptor.
   * <p>
   * {@inheritDoc}
   */
  public IViewDescriptor getViewDescriptor() {
    return getProjectedViewDescriptor();
  }

  /**
   * {@inheritDoc}
   * 
   * @internal
   */
  public void setSubject(Subject subject) {
    this.subject = subject;
  }

  private Subject getSubject() {
    if (subject != null) {
      return subject;
    }
    if (getParent() != null) {
      return getParent().getSubject();
    }
    return null;
  }

  /**
   * Gets the entryAction.
   * 
   * @return the entryAction.
   */
  public IAction getEntryAction() {
    return entryAction;
  }

  /**
   * Configures an action to be executed every time the module becomes the
   * current selected module (either through a user explicit navigation or a
   * programmatic selection). The action will execute in the context of the
   * current workspace, this module being the current selected module.
   * 
   * @param entryAction
   *          the entryAction to set.
   */
  public void setEntryAction(IAction entryAction) {
    this.entryAction = entryAction;
  }

  /**
   * Gets the exitAction.
   * 
   * @return the exitAction.
   */
  public IAction getExitAction() {
    return exitAction;
  }

  /**
   * Configures an action to be executed every time the module becomes
   * unselected (either through a user explicit navigation or a programmatic
   * deselection). The action will execute in the context of the current
   * workspace, this module being the current selected module (i.e. the action
   * occurs before the module is actually left).
   * 
   * @param exitAction
   *          the exitAction to set.
   */
  public void setExitAction(IAction exitAction) {
    this.exitAction = exitAction;
  }

  /**
   * Gets the dirty.
   * 
   * @return the dirty.
   */
  public boolean isDirty() {
    return dirty;
  }

  /**
   * Sets the dirty.
   * 
   * @param dirty
   *          the dirty to set.
   * @internal
   */
  public void setDirty(boolean dirty) {
    String oldI18nName = getI18nName();
    this.dirty = dirty;
    firePropertyChange(I18N_NAME, oldI18nName, getI18nName());
  }
}
