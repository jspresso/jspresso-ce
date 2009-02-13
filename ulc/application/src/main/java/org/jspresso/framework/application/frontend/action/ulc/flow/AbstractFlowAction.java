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
package org.jspresso.framework.application.frontend.action.ulc.flow;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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

      public void windowClosing(@SuppressWarnings("unused") WindowEvent event) {
        IAction nextAction = getNextAction(optionReverseDictionary.get(alert
            .getValue()));
        if (nextAction != null) {
          actionHandler.execute(nextAction, context);
        }
      }
    });
    alert.show();
    return true;
  }

  /**
   * Gets the action to execute next based on the user selected option.
   * 
   * @param selectedOption
   *          the user selected option.
   * @return the action to execute next.
   */
  protected abstract IAction getNextAction(String selectedOption);
}
