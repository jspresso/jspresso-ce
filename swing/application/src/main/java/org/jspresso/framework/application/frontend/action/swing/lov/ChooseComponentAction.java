/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.swing.lov;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.swing.std.ModalDialogAction;
import org.jspresso.framework.binding.model.IModelValueConnector;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.basic.BasicTableViewDescriptor;


/**
 * A standard List of value action for reference property views. This action
 * should be used in view factories.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ChooseComponentAction extends ModalDialogAction {

  private IDisplayableAction cancelAction;
  private IDisplayableAction okAction;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    List<IDisplayableAction> actions = new ArrayList<IDisplayableAction>();

    okAction.putInitialContext(ActionContextConstants.SOURCE_VIEW_CONNECTOR,
        getViewConnector(context));
    actions.add(okAction);
    actions.add(cancelAction);
    context.put(ActionContextConstants.DIALOG_ACTIONS, actions);

    IModelValueConnector componentsModelConnector = (IModelValueConnector) context
        .get(ActionContextConstants.ACTION_PARAM);
    BasicTableViewDescriptor tableViewDescriptor = new BasicTableViewDescriptor();
    tableViewDescriptor.setModelDescriptor(componentsModelConnector
        .getModelDescriptor());

    IView<JComponent> collectionView = getViewFactory(context).createView(
        tableViewDescriptor, actionHandler, getLocale(context));
    context.put(ActionContextConstants.DIALOG_VIEW, collectionView);

    getMvcBinder(context).bind(collectionView.getConnector(),
        componentsModelConnector);

    return super.execute(actionHandler, context);
  }

  /**
   * Sets the cancelAction.
   * 
   * @param cancelAction
   *            the cancelAction to set.
   */
  public void setCancelAction(IDisplayableAction cancelAction) {
    this.cancelAction = cancelAction;
  }

  /**
   * Sets the okAction.
   * 
   * @param okAction
   *            the okAction to set.
   */
  public void setOkAction(IDisplayableAction okAction) {
    this.okAction = okAction;
  }
}
