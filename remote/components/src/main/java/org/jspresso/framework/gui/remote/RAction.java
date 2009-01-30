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

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.binding.ICollectionConnectorProvider;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.descriptor.ICollectionDescriptor;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.util.remote.RemotePeer;
import org.jspresso.framework.view.action.IDisplayableAction;

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

  private String           name;
  private String           description;
  private RIcon            icon;
  private String           mnemonicAsString;
  private String           acceleratorAsString;
  private boolean          enabled;

  private IAction          action;
  private IActionHandler   actionHandler;
  private IModelDescriptor modelDescriptor;
  private RComponent       sourceComponent;
  private IValueConnector  viewConnector;

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
   * Gets the name.
   * 
   * @return the name.
   */
  public String getName() {
    return name;
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

  /**
   * Gets the description.
   * 
   * @return the description.
   */
  public String getDescription() {
    return description;
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
   * Gets the icon.
   * 
   * @return the icon.
   */
  public RIcon getIcon() {
    return icon;
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
   * Gets the mnemonicAsString.
   * 
   * @return the mnemonicAsString.
   */
  public String getMnemonicAsString() {
    return mnemonicAsString;
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
   * Gets the acceleratorAsString.
   * 
   * @return the acceleratorAsString.
   */
  public String getAcceleratorAsString() {
    return acceleratorAsString;
  }

  /**
   * Sets the action context.
   * 
   * @param anAction
   *          the Jspresso action.
   * @param anActionHandler
   *          the action handler.
   * @param aSourceComponent
   *          the source component.
   * @param aModelDescriptor
   *          the model descriptor.
   * @param aViewConnector
   *          the view connector.
   */
  public void setContext(IDisplayableAction anAction,
      IActionHandler anActionHandler, RComponent aSourceComponent,
      IModelDescriptor aModelDescriptor, IValueConnector aViewConnector) {
    this.action = anAction;
    this.actionHandler = anActionHandler;
    this.sourceComponent = aSourceComponent;
    this.modelDescriptor = aModelDescriptor;
    if (aModelDescriptor instanceof ICollectionDescriptor) {
      this.viewConnector = ((ICollectionConnectorProvider) aViewConnector)
          .getCollectionConnector();
    } else {
      this.viewConnector = aViewConnector;
    }
  }

  /**
   * Triggers the action execution on the action handler. The following initial
   * action context is filled in : <li>
   * <code>ActionContextConstants.SOURCE_COMPONENT</code> <li>
   * <code>ActionContextConstants.VIEW_CONNECTOR</code> <li>
   * <code>ActionContextConstants.MODEL_CONNECTOR</code> <li>
   * <code>ActionContextConstants.MODEL_DESCRIPTOR</code> <li>
   * <code>ActionContextConstants.SELECTED_INDICES</code> <li>
   * <code>ActionContextConstants.LOCALE</code>
   * 
   * @param parameter
   *          the action parameter.
   */
  public void actionPerformed(String parameter) {
    if (actionHandler != null) {
      Map<String, Object> actionContext = actionHandler.createEmptyContext();
      actionContext.put(ActionContextConstants.MODEL_DESCRIPTOR,
          modelDescriptor);
      actionContext.put(ActionContextConstants.SOURCE_COMPONENT,
          sourceComponent);
      actionContext.put(ActionContextConstants.VIEW_CONNECTOR, viewConnector);
      if (viewConnector instanceof ICollectionConnectorProvider
          && ((ICollectionConnectorProvider) viewConnector)
              .getCollectionConnector() != null) {
        actionContext.put(ActionContextConstants.SELECTED_INDICES,
            ((ICollectionConnectorProvider) viewConnector)
                .getCollectionConnector().getSelectedIndices());
      }
      actionContext.put(ActionContextConstants.ACTION_COMMAND, parameter);
      // actionContext.put(ActionContextConstants.ACTION_WIDGET, e.getSource());
      if (action.getInitialContext() != null) {
        actionContext.putAll(action.getInitialContext());
      }
      actionHandler.execute(action, actionContext);
    }
  }
}
