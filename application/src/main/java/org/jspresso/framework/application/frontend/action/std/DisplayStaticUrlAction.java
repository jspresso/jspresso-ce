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

/**
 * Like its parent, this action opens a URL in a browser (or a browser tab). But
 * instead of taking the URL out of the context, it uses a statically
 * parametrized URL, or rather a key ({@code urlKey}) used to translate
 * the URL based on the logged-in user language. This is particularly useful for
 * linking to static internationalized content, like an online manual. Be aware
 * that once the URL is translated, it is still appended to the
 * {@code baseUrl}.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class DisplayStaticUrlAction<E, F, G> extends DisplayUrlAction<E, F, G> {

  private String urlKey;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    setActionParameter(
        getTranslationProvider(context).getTranslation(urlKey,
            getLocale(context)), context);
    return super.execute(actionHandler, context);
  }

  /**
   * Configures the translation key used to internationalize the URL to open.
   *
   * @param urlKey
   *          the urlKey to set.
   */
  public void setUrlKey(String urlKey) {
    this.urlKey = urlKey;
  }
}
