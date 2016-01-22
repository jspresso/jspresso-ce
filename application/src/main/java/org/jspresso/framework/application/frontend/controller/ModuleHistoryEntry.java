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
package org.jspresso.framework.application.frontend.controller;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import org.jspresso.framework.application.model.Module;

/**
 * A simple structure to represent an entry in the modules navigation history.
 *
 * @author Vincent Vandenschrick
 */
public class ModuleHistoryEntry {

  private String            id;
  private String            name;
  private Reference<Module> moduleRef;
  private String            workspaceName;

  /**
   * Constructs a new {@code ModuleHistoryEntry} instance.
   *
   * @param workspaceName
   *          the workspace name of this history entry.
   * @param module
   *          the module of this history entry.
   * @param name the user-friendly name of this history entry.
   */
  public ModuleHistoryEntry(String workspaceName, Module module, String name) {
    if (workspaceName == null || module == null) {
      throw new IllegalArgumentException(
          "Cannot create a module history entry for null module or workspace.");
    }
    this.workspaceName = workspaceName;
    this.moduleRef = new SoftReference<>(module);
    this.id = Long.toHexString(System.currentTimeMillis());
    this.name = name;
  }

  /**
   * Gets the module.
   *
   * @return the module.
   */
  public Module getModule() {
    return moduleRef.get();
  }

  /**
   * Gets the workspaceName.
   *
   * @return the workspaceName.
   */
  public String getWorkspaceName() {
    return workspaceName;
  }

  /**
   * Gets the module history entry id.
   *
   * @return the module history entry id.
   */
  public String getId() {
    return id;
  }

  /**
   * Gets the module history name.
   *
   * @return the module history name.
   */
  public String getName() {
    return name;
  }

}
