/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.frontend.action.std;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;

/**
 * A simple action to display an static Url content.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class DisplayUrlAction<E, F, G> extends FrontendAction<E, F, G> {

  private String baseUrl;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(
      @SuppressWarnings("unused") IActionHandler actionHandler,
      Map<String, Object> context) {
    StringBuffer urlSpec = new StringBuffer();
    if (baseUrl != null) {
      urlSpec.append(baseUrl);
    }
    urlSpec.append((String) getActionParameter(context));

    if (urlSpec.length() > 0) {
      getController(context).displayUrl(urlSpec.toString());
    }
    return true;
  }

  /**
   * Sets the baseUrl.
   * 
   * @param baseUrl
   *          the baseUrl to set.
   */
  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }
}
