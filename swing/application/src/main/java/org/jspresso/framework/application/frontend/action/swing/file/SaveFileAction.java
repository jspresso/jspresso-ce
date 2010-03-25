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
package org.jspresso.framework.application.frontend.action.swing.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.file.IFileSaveCallback;
import org.jspresso.framework.util.i18n.Messages;
import org.jspresso.framework.util.swing.SwingUtil;

/**
 * Initiates a file save action.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SaveFileAction extends ChooseFileAction {

  private IFileSaveCallback fileSaveCallback;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {

    JFileChooser currentFileChooser = createFileChooser(context);

    int returnVal = currentFileChooser.showSaveDialog(SwingUtil
        .getVisibleWindow(getSourceComponent(context)));
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = currentFileChooser.getSelectedFile();
      if (file != null) {
        if (file.getName() != null && file.getName().indexOf(".") == -1) { //$NON-NLS-1$
          Map<String, List<String>> fileFilter = getFileFilter(context);
          if (fileFilter != null && !fileFilter.isEmpty()) {
            List<String> extensions = fileFilter.values().iterator().next();
            if (extensions != null && !extensions.isEmpty()) {
              file = new File(file.getAbsolutePath() + extensions.get(0));
            }
          }
        }
        if (file.exists()) {
          if (JOptionPane.showConfirmDialog(null, Messages
              .getString("confirm.override.description"), //$NON-NLS-1$
              Messages.getString("confirm.override.name"), //$NON-NLS-1$
              JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
            file = null;
          }
        }
        if (file != null) {
          try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
              fileSaveCallback.fileChosen(fos, actionHandler, context);
              fos.flush();
            } catch (IOException ex) {
              throw new ActionException(ex);
            } finally {
              try {
                fos.close();
              } catch (IOException ex) {
                // NO-OP.
              }
            }
          } catch (FileNotFoundException ex) {
            fileSaveCallback.cancel(actionHandler, context);
          }
        } else {
          fileSaveCallback.cancel(actionHandler, context);
        }
      } else {
        fileSaveCallback.cancel(actionHandler, context);
      }
    } else {
      fileSaveCallback.cancel(actionHandler, context);
    }
    return super.execute(actionHandler, context);
  }

  /**
   * Sets the fileSaveCallback.
   * 
   * @param fileSaveCallback
   *          the fileSaveCallback to set.
   */
  public void setFileSaveCallback(IFileSaveCallback fileSaveCallback) {
    this.fileSaveCallback = fileSaveCallback;
  }
}
