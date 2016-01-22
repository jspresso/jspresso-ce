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
package org.jspresso.framework.application.view;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.IController;
import org.jspresso.framework.application.backend.session.IApplicationSessionAware;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.view.AbstractActionFactory;

/**
 * An intermediate view factory abstract class used to handle session DI on
 * gates.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public abstract class ControllerAwareActionFactory<E, F, G> extends
    AbstractActionFactory<E, F, G> {

  /**
   * {@inheritDoc}
   */
  @Override
  protected void applyGateDependencyInjection(IGate gate,
      IActionHandler actionHandler) {
    super.applyGateDependencyInjection(gate, actionHandler);
    if (gate instanceof IApplicationSessionAware
        && actionHandler instanceof IController) {
      ((IApplicationSessionAware) gate)
          .setApplicationSession(((IController) actionHandler)
              .getApplicationSession());
    }
  }
}
