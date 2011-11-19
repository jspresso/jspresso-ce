/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.application.view.descriptor.basic.BasicWorkspaceViewDescriptor;
import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.security.ISecurityHandler;
import org.jspresso.framework.util.automation.IPermIdSource;
import org.jspresso.framework.util.gui.IIconImageURLProvider;
import org.jspresso.framework.util.lang.StringUtils;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * A workspace is an group of functional application modules. You may decide
 * arbitrarily how to group modules into workspaces but a good approach might be
 * to design the workspaces based on roles (i.e. business activities). This
 * helps to clearly seperates tasks-unrelated modules and eases authorization
 * management since a workspace can be granted or forbidden as a whole by
 * Jspresso security.
 * <p>
 * Workspaces might be graphically represented differently depending on the UI
 * technology used. For instance, the Swing and ULC channels use a MDI UI in
 * which each workspace is represented as an internal frame (document). On the
 * other hand, Flex and Qooxdoo channels represent workspaces stacked in an
 * accordion. Whatever the graphical representation is, there is at most one
 * workspace active at a time for a user session - either the active (focused)
 * internal frame or the expanded accordion section.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class Workspace implements ISecurable, IPermIdSource {

  /**
   * <code>DESCRIPTION</code> is "description".
   */
  public static final String    DESCRIPTION      = "description";

  /**
   * <code>I18N_DESCRIPTION</code> is "i18nDescription".
   */
  public static final String    I18N_DESCRIPTION = "i18nDescription";

  /**
   * <code>I18N_NAME</code> is "i18nName".
   */
  public static final String    I18N_NAME        = "i18nName";

  /**
   * <code>MODULES</code> is "modules".
   */
  public static final String    MODULES          = "modules";

  /**
   * <code>NAME</code> is "name".
   */
  public static final String    NAME             = "name";

  private String                description;
  private Collection<String>    grantedRoles;
  private String                i18nDescription;
  private String                i18nName;
  private String                iconImageURL;
  private IIconImageURLProvider iconImageURLProvider;
  private IAction               itemSelectionAction;

  private List<Module>          modules;
  private String                name;

  private boolean               started;

  private IAction               startupAction;
  private ISecurityHandler      securityHandler;

  private IViewDescriptor       viewDescriptor;

  private String                permId;

  /**
   * Constructs a new <code>Workspace</code> instance.
   */
  public Workspace() {
    started = false;
  }

  /**
   * Equality based on name.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Workspace)) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    Workspace rhs = (Workspace) obj;
    return new EqualsBuilder().append(getName(), rhs.getName()).isEquals();
  }

  /**
   * Gets the workspace description. It may serve for the workspace view.
   * 
   * @return the workspace description.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Gets the grantedRoles.
   * 
   * @return the grantedRoles.
   */
  @Override
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
   * Gets the item selection action that will be attached to the created view
   * that displays the workspace.
   * 
   * @return the item selection action that will be attached to the created view
   *         that displays the workspace
   */
  public IAction getItemSelectionAction() {
    return itemSelectionAction;
  }

  /**
   * Gets the modules modules.
   * 
   * @return the list of modules belonging to this workspace.
   */
  public List<Module> getModules() {
    if (modules == null) {
      return null;
    }
    ISecurityHandler sh = getSecurityHandler();
    if (sh != null) {
      for (Iterator<Module> ite = modules.iterator(); ite.hasNext();) {
        Module nextModule = ite.next();
        if (!sh.isAccessGranted(nextModule)) {
          try {
            sh.pushToSecurityContext(nextModule);
            ite.remove();
          } finally {
            sh.restoreLastSecurityContextSnapshot();
          }
        }
      }
    }
    return modules;
  }

  /**
   * Gets the workspace name. It may serve for the workspace view.
   * 
   * @return the workspace name.
   */
  public String getName() {
    return name;
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
   * Gets the workspace view descriptor. Whenever the view descriptor has not
   * been set, a default one is generated.
   * 
   * @return the viewDescriptor.
   */
  public IViewDescriptor getViewDescriptor() {
    if (viewDescriptor == null) {
      viewDescriptor = new BasicWorkspaceViewDescriptor();
      ((BasicWorkspaceViewDescriptor) viewDescriptor).setName(getName());
      ((BasicWorkspaceViewDescriptor) viewDescriptor)
          .setDescription(getDescription());
      ((BasicWorkspaceViewDescriptor) viewDescriptor)
          .setIconImageURL(getIconImageURL());
      if (iconImageURLProvider == null) {
        iconImageURLProvider = new WorkspaceIconImageURLProvider();
      }
      ((BasicWorkspaceViewDescriptor) viewDescriptor)
          .setIconImageURLProvider(iconImageURLProvider);
      ((BasicWorkspaceViewDescriptor) viewDescriptor)
          .setItemSelectionAction(getItemSelectionAction());
    }
    return viewDescriptor;
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
   * Gets the started.
   * 
   * @return the started.
   */
  public boolean isStarted() {
    return started;
  }

  /**
   * Configures the key used to translate actual internationalized workspace
   * description. The resulting translation will generally be leveraged as a
   * toolTip on the UI side but its use may be extended for online help.
   * 
   * @param description
   *          the workspace description.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Assigns the roles that are authorized to start this workspace. It supports
   * &quot;<b>!</b>&quot; prefix to negate the role(s). Whenever the user is not
   * granted sufficient privileges, the workspace is not installed at all in the
   * application frame. Setting the collection of granted roles to
   * <code>null</code> (default value) disables role based authorization on this
   * workspace.
   * 
   * @param grantedRoles
   *          the grantedRoles to set.
   */
  public void setGrantedRoles(Collection<String> grantedRoles) {
    this.grantedRoles = StringUtils.ensureSpaceFree(grantedRoles);
  }

  /**
   * Stores the internationalized workspace description for use in the UI as
   * toolTip for instance.
   * 
   * @param i18nDescription
   *          the i18nDescription to set.
   * @internal
   */
  public void setI18nDescription(String i18nDescription) {
    this.i18nDescription = i18nDescription;
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
    this.i18nName = i18nName;
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
   * Since a workspace is represented as a tree view of modules, this property
   * can be used to customize an incon image URL provider on the created tree
   * view (see <code>BasicTreeViewDescriptor.iconImageURLProvider</code>).
   * However, the workspace built-in incon image URL provider (
   * <code>WorkspaceIconImageURLProvider</code>) will setup sensible defaults so
   * that it unlikely has to be changed.
   * 
   * @param iconImageURLProvider
   *          the iconImageURLProvider to set.
   */
  public void setIconImageURLProvider(IIconImageURLProvider iconImageURLProvider) {
    this.iconImageURLProvider = iconImageURLProvider;
  }

  /**
   * Configures the action to be installed as item selection action on the
   * rendered module tree view - see
   * <code>BasicTreeViewDescriptor.itemSelectionAction</code>. The configured
   * action will then be executed each time a module selection changes in the
   * workspace.
   * 
   * @param itemSelectionAction
   *          the itemSelectionAction to set.
   */
  public void setItemSelectionAction(IAction itemSelectionAction) {
    this.itemSelectionAction = itemSelectionAction;
  }

  /**
   * Installs a list of module(s) into this workspace. Each module may own
   * sub-modules that form a (potentially complex and dynamic) hierarchy, that
   * is visually rendered as a tree view.
   * 
   * @param modules
   *          the modules modules to set.
   */
  public void setModules(List<Module> modules) {
    this.modules = modules;
    if (this.modules != null) {
      for (Module module : this.modules) {
        module.setSecurityHandler(getSecurityHandler());
      }
    }
  }

  /**
   * Configures the key used to translate actual internationalized workspace
   * name. The resulting translation will be leveraged as the workspace label on
   * the UI side.
   * 
   * @param name
   *          the module's name.
   */
  public void setName(String name) {
    this.name = name;
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
   * Configures an action to be executed the first time the workspace is
   * &quot;started&quot; by the user. The action will execute in the context of
   * the workspace but with no specific module selected. It will help
   * initializing workspace values, notify user, ...
   * 
   * @param startupAction
   *          the startupAction to set.
   */
  public void setStartupAction(IAction startupAction) {
    this.startupAction = startupAction;
  }

  /**
   * Configures the security handler used to secure this module.
   * 
   * @param securityHandler
   *          the security handler.
   * @internal
   */
  public void setSecurityHandler(ISecurityHandler securityHandler) {
    this.securityHandler = securityHandler;
    if (this.modules != null) {
      for (Module module : this.modules) {
        module.setSecurityHandler(getSecurityHandler());
      }
    }
  }

  /**
   * based on i18n name.
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
   * Gets the securityHandler.
   * 
   * @return the securityHandler.
   */
  private ISecurityHandler getSecurityHandler() {
    return securityHandler;
  }

  /**
   * Gets the permId.
   * 
   * @return the permId.
   */
  @Override
  public String getPermId() {
    if (permId != null) {
      return permId;
    }
    return getName();
  }

  /**
   * Sets the permanent identifier to this application element. Permanent
   * identifiers are used by different framework parts, like dynamic security or
   * record/replay controllers to uniquely identify an application element.
   * Permanent identifiers are generated by the SJS build based on the element
   * id but must be explicitely set if Spring XML is used.
   * 
   * @param permId
   *          the permId to set.
   */
  @Override
  public void setPermId(String permId) {
    this.permId = permId;
  }
}
