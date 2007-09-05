/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.wings.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import org.wings.SFileChooser;
import org.wings.SOptionPane;

import com.d2s.framework.action.ActionException;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.file.IFileOpenCallback;

/**
 * Initiates a file open action.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class OpenFileAction extends ChooseFileAction {

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
                .getSelectedFile()), fileChooser.getFileName(), actionHandler,
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
   *          the fileOpenCallback to set.
   */
  public void setFileOpenCallback(IFileOpenCallback fileOpenCallback) {
    this.fileOpenCallback = fileOpenCallback;
  }
}
