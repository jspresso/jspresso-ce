/*
 * Copyright (c) 2005-2018 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.view;

import java.util.Locale;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.util.automation.IPermIdSource;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * A default debug UI plugin used to compute technical tooltips.
 *
 * @author Vincent Vandenschrick
 */
public class DefaultUIDebugPlugin implements IUIDebugPlugin {

  /**
   * {@inheritDoc}
   */
  public String computeTechnicalDescription(String originalDescription, IViewDescriptor viewDescriptor,
                                            IActionHandler actionHandler, Locale locale) {
    if (actionHandler.isLiveDebugUI()) {
      return appendPermIdDescription(originalDescription, viewDescriptor, actionHandler, locale);
    }
    return originalDescription;
  }

  /**
   * {@inheritDoc}
   */
  public String computeTechnicalDescription(String originalDescription, IAction action,
                                            IActionHandler actionHandler, Locale locale) {
    if (actionHandler.isLiveDebugUI()) {
      return appendPermIdDescription(originalDescription, action, actionHandler, locale);
    }
    return originalDescription;
  }

  /**
   * {@inheritDoc}
   */
  protected String appendPermIdDescription(String originalDescription, IPermIdSource permIdSource,
                                            ITranslationProvider translationProvider, Locale locale) {
    String description = originalDescription;
    if (permIdSource.getPermId() != null) {
      if (description == null) {
        description = "";
      } else {
        description = description + " ";
      }
      description = description + "(Descriptor PermId -> [" + permIdSource.getPermId() + "])";
    }
    return description;
  }
}
