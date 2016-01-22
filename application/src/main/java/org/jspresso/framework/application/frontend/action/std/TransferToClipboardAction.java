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

import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;

/**
 * An action used to transfer textual representation(s) of selected models to
 * the system clipboard.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class TransferToClipboardAction<E, F, G> extends FrontendAction<E, F, G> {

  private IClipboardTransferHandler clipboardTransferHandler;

  /**
   * Retrieves the managed collection from the model connector then registers
   * the selected elements.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    List<?> transferedComponents = getSelectedModels(context);
    getController(context).setClipboardContent(
        getClipboardTransferHandler().toPlainText(transferedComponents),
        getClipboardTransferHandler().toHtml(transferedComponents));
    return super.execute(actionHandler, context);
  }

  /**
   * Gets the clipboardTransferHandler.
   *
   * @return the clipboardTransferHandler.
   */
  protected IClipboardTransferHandler getClipboardTransferHandler() {
    return clipboardTransferHandler;
  }

  /**
   * Sets the clipboardTransferHandler.
   *
   * @param clipboardTransferHandler
   *          the clipboardTransferHandler to set.
   */
  public void setClipboardTransferHandler(
      IClipboardTransferHandler clipboardTransferHandler) {
    this.clipboardTransferHandler = clipboardTransferHandler;
  }

}
