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
package org.jspresso.framework.view.wings;

import java.awt.event.ActionEvent;
import java.util.Locale;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.binding.ICollectionConnectorProvider;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.descriptor.ICollectionDescriptor;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.view.AbstractActionFactory;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.wings.SComponent;
import org.wings.SIcon;

/**
 * A wings action factory.
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
public class WingsActionFactory extends
    AbstractActionFactory<Action, SComponent, SIcon> {

  /**
   * {@inheritDoc}
   */
  public Action createAction(IDisplayableAction action,
      IActionHandler actionHandler, IView<SComponent> view, Locale locale) {
    return createAction(action, actionHandler, view.getPeer(), view
        .getDescriptor().getModelDescriptor(), view.getConnector(), locale);
  }

  /**
   * {@inheritDoc}
   */
  public Action createAction(IDisplayableAction action,
      IActionHandler actionHandler, SComponent sourceComponent,
      IModelDescriptor modelDescriptor, IValueConnector viewConnector,
      Locale locale) {
    Action wingsAction = new ActionAdapter(action, actionHandler,
        sourceComponent, modelDescriptor, viewConnector, locale);
    attachActionGates(action, modelDescriptor, viewConnector, wingsAction);
    return wingsAction;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setActionEnabled(Action action, boolean enabled) {
    action.setEnabled(enabled);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setActionName(Action action, String name) {
    action.putValue(Action.NAME, name);
  }

  private final class ActionAdapter extends AbstractAction {

    private static final long serialVersionUID = 5819377672533326496L;

    private IAction           action;
    private IActionHandler    actionHandler;
    private IModelDescriptor  modelDescriptor;
    private SComponent        sourceComponent;
    private IValueConnector   viewConnector;

    /**
     * Constructs a new <code>ActionAdapter</code> instance.
     * 
     * @param action
     * @param actionHandler
     * @param sourceComponent
     * @param modelDescriptor
     * @param viewConnector
     * @param locale
     */
    public ActionAdapter(IDisplayableAction action,
        IActionHandler actionHandler, SComponent sourceComponent,
        IModelDescriptor modelDescriptor, IValueConnector viewConnector,
        Locale locale) {
      this.action = action;
      this.actionHandler = actionHandler;
      this.sourceComponent = sourceComponent;
      this.modelDescriptor = modelDescriptor;
      if (modelDescriptor instanceof ICollectionDescriptor) {
        this.viewConnector = ((ICollectionConnectorProvider) viewConnector)
            .getCollectionConnector();
      } else {
        this.viewConnector = viewConnector;
      }
      putValue(Action.NAME, action
          .getI18nName(getTranslationProvider(), locale));
      String i18nDescription = action.getI18nDescription(
          getTranslationProvider(), locale);
      if (i18nDescription != null) {
        putValue(Action.SHORT_DESCRIPTION, i18nDescription + TOOLTIP_ELLIPSIS);
      }
      putValue(Action.SMALL_ICON, getIconFactory().getIcon(
          action.getIconImageURL(), getIconFactory().getTinyIconSize()));
      if (action.getMnemonicAsString() != null) {
        putValue(Action.MNEMONIC_KEY, new Integer(KeyStroke.getKeyStroke(
            action.getMnemonicAsString()).getKeyCode()));
      }
    }

    /**
     * Triggers the action execution on the action handler. The following
     * initial action context is filled in : <li>
     * <code>ActionContextConstants.SOURCE_COMPONENT</code> <li>
     * <code>ActionContextConstants.VIEW_CONNECTOR</code> <li>
     * <code>ActionContextConstants.MODEL_CONNECTOR</code> <li>
     * <code>ActionContextConstants.MODEL_DESCRIPTOR</code> <li>
     * <code>ActionContextConstants.SELECTED_INDICES</code> <li>
     * <code>ActionContextConstants.LOCALE</code>
     * <p>
     * {@inheritDoc}
     */
    public void actionPerformed(ActionEvent e) {
      // "".equals(e.getActionCommand()) prevents the form
      // action to be handled.
      if (actionHandler != null && !"".equals(e.getActionCommand())) {
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
        actionContext.put(ActionContextConstants.ACTION_COMMAND, e
            .getActionCommand());
        actionContext.put(ActionContextConstants.ACTION_WIDGET, e.getSource());
        actionHandler.execute(action, actionContext);
      }
    }
  }
}
