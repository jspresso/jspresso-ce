/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.frontend.action.wings.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.file.IFileOpenCallback;
import org.wings.SFileChooser;
import org.wings.SOptionPane;


/**
 * Initiates a file open action.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class OpenFileAction extends ChooseFileAction {

  private IFileOpenCallback fileOpenCallback;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(final IActionHandler actionHandler,
      final Map<String, Object> context) {
    SFileChooser fileChooser = new SFileChooser();
    SOptionPane
        .showInputDialog(getSourceComponent(context), getTranslationProvider(
            context)
            .getTranslation("open.file.description", getLocale(context)),
            getTranslationProvider(context).getTranslation("open.file.name",
                getLocale(context)), fileChooser, new OpenFileActionListener(
                fileChooser, getController(context), context));
    return super.execute(actionHandler, context);
  }

  /**
   * Sets the fileOpenCallback.
   * 
   * @param fileOpenCallback
   *            the fileOpenCallback to set.
   */
  public void setFileOpenCallback(IFileOpenCallback fileOpenCallback) {
    this.fileOpenCallback = fileOpenCallback;
  }

  private final class OpenFileActionListener implements ActionListener {

    private IActionHandler      actionHandler;
    private Map<String, Object> context;
    private SFileChooser        fileChooser;

    private OpenFileActionListener(SFileChooser fileChooser,
        IActionHandler actionHandler, Map<String, Object> context) {
      this.fileChooser = fileChooser;
      this.actionHandler = actionHandler;
      this.context = context;
    }

    /**
     * {@inheritDoc}
     */
    public void actionPerformed(ActionEvent e) {
      try {
        if (fileOpenCallback != null) {
          if (fileChooser.getFileName() != null
              && SOptionPane.OK_ACTION.equals(e.getActionCommand())) {
            fileOpenCallback.fileChosen(new FileInputStream(fileChooser
                .getSelectedFile()), actionHandler,
                context);
          } else {
            fileOpenCallback.cancel(actionHandler, context);
          }
        }
      } catch (IOException ex) {
        throw new ActionException(ex);
      }
    }

  }
}
