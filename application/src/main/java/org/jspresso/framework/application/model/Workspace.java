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
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jspresso.framework.application.view.descriptor.basic.BasicWorkspaceViewDescriptor;
import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.util.IIconImageURLProvider;
import org.jspresso.framework.view.descriptor.IViewDescriptor;


/**
 * A workspace is a central element in the application architecture. It serves
 * as an entry point on the domain model. Workspaces are organized as a tree
 * structure since they can (optionally) provide modules. A module can be seen
 * as a window on the business grouping processes forming a business activity
 * (like master data management, customer contract handling, ...). Each module
 * can (optionally) provide a projected object serving as model root for
 * trigerring grouped processes.
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
public class Workspace implements ISecurable {

  private String                description;
  private Collection<String>    grantedRoles;
  private String                i18nDescription;
  private String                i18nName;
  private String                iconImageURL;
  private IIconImageURLProvider iconImageURLProvider;
  private List<Module>          modules;

  private String                name;

  private IViewDescriptor       viewDescriptor;

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
   * Gets the modules modules.
   * 
   * @return the list of modules belonging to this workspace.
   */
  public List<Module> getModules() {
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
   * Sets the workspace description. It may serve for the workspace view.
   * 
   * @param description
   *            the workspace description.
   */
  public void setDescription(String description) {
    this.description = description;
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
    this.i18nDescription = i18nDescription;
  }

  /**
   * Sets the i18nName.
   * 
   * @param i18nName
   *            the i18nName to set.
   */
  public void setI18nName(String i18nName) {
    this.i18nName = i18nName;
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
   * Sets the iconImageURLProvider.
   * 
   * @param iconImageURLProvider
   *            the iconImageURLProvider to set.
   */
  public void setIconImageURLProvider(IIconImageURLProvider iconImageURLProvider) {
    this.iconImageURLProvider = iconImageURLProvider;
  }

  /**
   * Sets the modules modules. It will fire a "modules" property change event.
   * 
   * @param modules
   *            the modules modules to set.
   */
  public void setModules(List<Module> modules) {
    this.modules = modules;
  }

  /**
   * Sets the module's name. It may serve for the module's view.
   * 
   * @param name
   *            the module's name.
   */
  public void setName(String name) {
    this.name = name;
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
}
