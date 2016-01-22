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
import org.jspresso.framework.util.swing.SwingUtil;

/**
 * Initiates a file save action.
 *
 * @author Vincent Vandenschrick
 */
public class SaveFileAction extends ChooseFileAction {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {

    JFileChooser currentFileChooser = createFileChooser(context);

    int returnVal = currentFileChooser.showSaveDialog(SwingUtil
        .getVisibleWindow(getSourceComponent(context)));
    IFileSaveCallback saveCallback = getFileSaveCallback(context);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = currentFileChooser.getSelectedFile();
      if (file != null) {
        if (!file.getName().contains(".")) {
          Map<String, List<String>> fileFilter = getFileFilter(context);
          if (fileFilter != null && !fileFilter.isEmpty()) {
            List<String> extensions = fileFilter.values().iterator().next();
            if (extensions != null && !extensions.isEmpty()) {
              file = new File(file.getAbsolutePath() + extensions.get(0));
            }
          }
        }
        if (file.exists()) {
          if (JOptionPane.showConfirmDialog(
              null,
              getTranslationProvider(context).getTranslation(
                  "confirm.override.description", getLocale(context)),
              getTranslationProvider(context).getTranslation(
                  "confirm.override.name", getLocale(context)),
              JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
            file = null;
          }
        }
        if (file != null) {
          try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
              saveCallback.fileChosen(file.getName(), fos, actionHandler,
                  context);
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
            saveCallback.cancel(actionHandler, context);
          }
        } else {
          saveCallback.cancel(actionHandler, context);
        }
      } else {
        saveCallback.cancel(actionHandler, context);
      }
    } else {
      saveCallback.cancel(actionHandler, context);
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
    super.setFileCallback(fileSaveCallback);
  }

  /**
   * Gets the fileSaveCallback.
   *
   * @param context the action context.
   * @return the fileSaveCallback.
   */
  protected IFileSaveCallback getFileSaveCallback(Map<String, Object> context) {
    return (IFileSaveCallback) super.getFileCallback(context);
  }

  /**
   * Computes a file name to save the file. Queries the file save callback for a
   * file name and defaults to the action default one if none is returned.
   *
   * @param context
   *          the action context.
   * @return the file name to save the file under.
   */
  @Override
  protected String getFileName(Map<String, Object> context) {
    IFileSaveCallback saveCallback = getFileSaveCallback(context);
    if (saveCallback != null) {
      String fileName = saveCallback.getFileName(context);
      if (fileName != null && fileName.length() > 0) {
        return fileName;
      }
    }
    return super.getFileName(context);
  }
}
