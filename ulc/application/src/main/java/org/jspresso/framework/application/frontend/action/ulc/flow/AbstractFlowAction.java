/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.ulc.flow;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.util.ulc.UlcUtil;
import org.jspresso.framework.view.IIconFactory;

import com.ulcjava.base.application.ULCAlert;
import com.ulcjava.base.application.event.WindowEvent;
import com.ulcjava.base.application.event.serializable.IWindowListener;

/**
 * Base class for ulc actions asking the user to make a decision.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractFlowAction extends AbstractMessageAction {

  /**
   * <code>CANCEL_OPTION</code>.
   */
  protected static final String CANCEL_OPTION = "cancel";
  /**
   * <code>NO_OPTION</code>.
   */
  protected static final String NO_OPTION     = "no";
  /**
   * <code>OK_OPTION</code>.
   */
  protected static final String OK_OPTION     = "ok";
  /**
   * <code>YES_OPTION</code>.
   */
  protected static final String YES_OPTION    = "yes";

  private String                firstOption;
  private String                secondOption;
  private String                thirdOption;

  /**
   * Constructs a new <code>AbstractFlowAction</code> instance.
   * 
   * @param firstOption
   *            the label for the 1st option.
   * @param secondOption
   *            the label for the 2nd option.
   * @param thirdOption
   *            the label for the 3rd option.
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
  public boolean execute(final IActionHandler actionHandler,
      final Map<String, Object> context) {
    ITranslationProvider translationProvider = getTranslationProvider(context);
    Locale locale = getLocale(context);
    final Map<String, String> optionReverseDictionary = new HashMap<String, String>();
    String translatedFirstOption = null;
    if (firstOption != null) {
      translatedFirstOption = translationProvider.getTranslation(firstOption,
          locale);
      optionReverseDictionary.put(translatedFirstOption, firstOption);
    }
    String translatedSecondOption = null;
    if (secondOption != null) {
      translatedSecondOption = translationProvider.getTranslation(secondOption,
          locale);
      optionReverseDictionary.put(translatedSecondOption, secondOption);
    }
    String translatedThirdOption = null;
    if (thirdOption != null) {
      translatedThirdOption = translationProvider.getTranslation(thirdOption,
          locale);
      optionReverseDictionary.put(translatedThirdOption, thirdOption);
    }
    final ULCAlert alert = new ULCAlert(UlcUtil
        .getVisibleWindow(getSourceComponent(context)), getI18nName(
        translationProvider, locale), getMessage(context),
        translatedFirstOption, translatedSecondOption, translatedThirdOption,
        getIconFactory(context).getIcon(getIconImageURL(),
            IIconFactory.LARGE_ICON_SIZE));
    alert.addWindowListener(new IWindowListener() {

      private static final long serialVersionUID = -6049928144066455758L;

      public void windowClosing(@SuppressWarnings("unused")
      WindowEvent event) {
        context.put(ActionContextConstants.NEXT_ACTION,
            getNextAction(optionReverseDictionary.get(alert.getValue())));
        executeNextAction(actionHandler, context);
      }
    });
    alert.show();
    return true;
  }

  /**
   * Calls the super-implementation to execute the next action.
   * 
   * @param actionHandler
   *            the action handler responsible for the action execution.
   * @param context
   *            the action context.
   */
  protected void executeNextAction(IActionHandler actionHandler,
      Map<String, Object> context) {
    super.execute(actionHandler, context);
  }

  /**
   * Gets the action to execute next based on the user selected option.
   * 
   * @param selectedOption
   *            the user selected option.
   * @return the action to execute next.
   */
  protected abstract IAction getNextAction(String selectedOption);
}
