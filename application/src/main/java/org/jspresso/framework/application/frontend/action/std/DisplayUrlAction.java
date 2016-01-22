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
package org.jspresso.framework.application.frontend.action.std;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.util.url.UrlHelper;

/**
 * This action opens a browser (or a browser tab) targeted at a URL. The actual
 * URL is a composition of a static parametrized prefix ({@code baseUrl})
 * and a dynamic part taken from the action context parameter.
 *
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
  private String target;

  /**
   * Instantiates a new Display url action.
   */
  public DisplayUrlAction() {
    target = UrlHelper.BLANK_TARGET;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    StringBuilder urlSpec = new StringBuilder();
    if (baseUrl != null) {
      urlSpec.append(baseUrl);
    }
    urlSpec.append((String) getActionParameter(context));

    if (urlSpec.length() > 0) {
      getController(context).displayUrl(urlSpec.toString(), target);
    }
    return true;
  }

  /**
   * Configures the static prefix that is prepended (if not {@code null})
   * to the opened URL.
   *
   * @param baseUrl
   *          the baseUrl to set.
   */
  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  /**
   * Sets the target window.
   *
   * @param target the target
   */
  public void setTarget(String target) {
    this.target = target;
  }
}
