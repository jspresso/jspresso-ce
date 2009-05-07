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
package org.jspresso.framework.gui.remote;

import java.util.Map;

import org.jspresso.framework.util.remote.RemotePeer;

/**
 * This class is the generic server peer of a client GUI action.
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
public class RAction extends RemotePeer {

  private String  acceleratorAsString;
  private String  description;
  private boolean enabled;
  private RIcon   icon;
  private String  mnemonicAsString;
  private String  name;

  /**
   * Constructs a new <code>RAction</code> instance.
   * 
   * @param guid
   *          the guid.
   */
  public RAction(String guid) {
    super(guid);
    enabled = true;
  }

  /**
   * Triggers the action execution.
   * 
   * @param parameter
   *          the action parameter.
   * @param viewStateGuid
   *          the guid to retrieve the view connector the action is triggred on.
   *          This is fundamental for the cell editors.
   * @param context
   *          a pre-initialized context.
   */
  public void actionPerformed(String parameter, String viewStateGuid,
      Map<String, Object> context) {
    // NO-OP
  }

  /**
   * Gets the acceleratorAsString.
   * 
   * @return the acceleratorAsString.
   */
  public String getAcceleratorAsString() {
    return acceleratorAsString;
  }

  /**
   * Gets the description.
   * 
   * @return the description.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Gets the icon.
   * 
   * @return the icon.
   */
  public RIcon getIcon() {
    return icon;
  }

  /**
   * Gets the mnemonicAsString.
   * 
   * @return the mnemonicAsString.
   */
  public String getMnemonicAsString() {
    return mnemonicAsString;
  }

  /**
   * Gets the name.
   * 
   * @return the name.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the enabled.
   * 
   * @return the enabled.
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Sets the acceleratorAsString.
   * 
   * @param acceleratorAsString
   *          the acceleratorAsString to set.
   */
  public void setAcceleratorAsString(String acceleratorAsString) {
    this.acceleratorAsString = acceleratorAsString;
  }

  /**
   * Sets the description.
   * 
   * @param description
   *          the description to set.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Sets the enabled.
   * 
   * @param enabled
   *          the enabled to set.
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * Sets the icon.
   * 
   * @param icon
   *          the icon to set.
   */
  public void setIcon(RIcon icon) {
    this.icon = icon;
  }

  /**
   * Sets the mnemonicAsString.
   * 
   * @param mnemonicAsString
   *          the mnemonicAsString to set.
   */
  public void setMnemonicAsString(String mnemonicAsString) {
    this.mnemonicAsString = mnemonicAsString;
  }

  /**
   * Sets the name.
   * 
   * @param name
   *          the name to set.
   */
  public void setName(String name) {
    this.name = name;
  }
}
