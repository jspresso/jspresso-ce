/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.ulc.flow;

import com.d2s.framework.view.IIconFactory;
import com.d2s.framework.view.action.IAction;
import com.d2s.framework.view.action.IActionHandler;
import com.ulcjava.base.application.ULCAlert;
import com.ulcjava.base.application.UlcUtilities;
import com.ulcjava.base.application.event.WindowEvent;
import com.ulcjava.base.application.event.serializable.IWindowListener;

/**
 * Base class for ulc actions asking the user to make a decision.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractFlowAction extends AbstractMessageAction {

  /**
   * <code>OK_OPTION</code>.
   */
  protected static final String OK_OPTION     = "OK_OPTION";
  /**
   * <code>CANCEL_OPTION</code>.
   */
  protected static final String CANCEL_OPTION = "CANCEL_OPTION";
  /**
   * <code>YES_OPTION</code>.
   */
  protected static final String YES_OPTION    = "YES_OPTION";
  /**
   * <code>NO_OPTION</code>.
   */
  protected static final String NO_OPTION     = "NO_OPTION";

  private String                firstOption;
  private String                secondOption;
  private String                thirdOption;

  /**
   * Constructs a new <code>AbstractFlowAction</code> instance.
   * 
   * @param firstOption
   *          the label for the 1st option.
   * @param secondOption
   *          the label for the 2nd option.
   * @param thirdOption
   *          the label for the 3rd option.
   */
  protected AbstractFlowAction(String firstOption, String secondOption,
      String thirdOption) {
    this.firstOption = firstOption;
    this.secondOption = secondOption;
    this.thirdOption = thirdOption;
  }

  /**
   * Creates a ULCAlert for the user to act on the execution flow.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void execute(final IActionHandler actionHandler) {
    final ULCAlert alert = new ULCAlert(UlcUtilities
        .getRoot(getSourceComponent()), getName(), getMessage(), firstOption,
        secondOption, thirdOption, getIconFactory().getIcon(getIconImageURL(),
            IIconFactory.LARGE_ICON_SIZE));
    alert.addWindowListener(new IWindowListener() {

      private static final long serialVersionUID = -6049928144066455758L;

      public void windowClosing(@SuppressWarnings("unused")
      WindowEvent event) {
        setNextAction(getNextAction(alert.getValue()));
        executeNextAction(actionHandler);
      }
    });
    alert.show();
  }

  /**
   * Gets the action to execute next based on the user selected option.
   * 
   * @param selectedOption
   *          the user selected option.
   * @return the action to execute next.
   */
  protected abstract IAction getNextAction(String selectedOption);

  /**
   * Calls the super-implementation to execute the next action.
   * 
   * @param actionHandler
   *          the action handler responsible for the action execution.
   */
  protected void executeNextAction(IActionHandler actionHandler) {
    super.execute(actionHandler);
  }
}
