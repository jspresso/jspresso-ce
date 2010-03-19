/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.launch.ulc;

import java.io.File;

import javax.swing.JOptionPane;

import org.jspresso.framework.util.i18n.Messages;

import com.ulcjava.base.shared.FileChooserConfig;
import com.ulcjava.base.shared.FileChooserConfig.FileFilterConfig;
import com.ulcjava.base.trusted.AllPermissionsFileService;

/**
 * A file service that handles automatic extension mgt and overriding
 * confirmation.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ExtendedFileService extends AllPermissionsFileService {

  // private static JFileChooser createFileChooser(
  // FileChooserConfig fileChooserConfig) {
  // JFileChooser fileChooser = new JFileChooser();
  //
  // if (fileChooserConfig.getSelectedFile() != null) {
  // File file = new File(fileChooserConfig.getSelectedFile());
  // fileChooser.setSelectedFile(file);
  // }
  // if (fileChooserConfig.getCurrentDirectory() != null) {
  // File dir = new File(fileChooserConfig.getCurrentDirectory());
  // fileChooser.setCurrentDirectory(dir);
  // }
  // if (fileChooserConfig.getDialogTitle() != null) {
  // fileChooser.setDialogTitle(fileChooserConfig.getDialogTitle());
  // }
  // if (fileChooserConfig.getApproveButtonToolTipText() != null) {
  // fileChooser.setApproveButtonToolTipText(fileChooserConfig
  // .getApproveButtonToolTipText());
  // }
  // if (fileChooserConfig.getApproveButtonMnemonic() != 0) {
  // fileChooser.setApproveButtonMnemonic(fileChooserConfig
  // .getApproveButtonMnemonic());
  // }
  // fileChooser.setFileHidingEnabled(fileChooserConfig.isFileHidingEnabled());
  // fileChooser.setFileSelectionMode(fileChooserConfig.getFileSelectionMode());
  // fileChooser.setDialogType(fileChooserConfig.getDialogType());
  // fileChooser.setAcceptAllFileFilterUsed(fileChooserConfig
  // .isAcceptAllFileFilterUsed());
  //
  // for (int i = 0; i < fileChooserConfig.getFileFilterConfigs().length; i++) {
  // FileChooserConfig.FileFilterConfig fileFilterConfig = fileChooserConfig
  // .getFileFilterConfigs()[i];
  //
  // final String[] allowedExtensions = fileFilterConfig
  // .getAllowedExtensions();
  // final String description = fileFilterConfig.getDescription();
  // FileFilter fileFilter = new FileFilter() {
  //
  // @Override
  // public boolean accept(File file) {
  // if (file.isDirectory()) {
  // return true;
  // }
  // for (int j = 0; j < allowedExtensions.length; j++) {
  // if (file.getName().endsWith(allowedExtensions[j])) {
  // return true;
  // }
  // }
  // return false;
  // }
  //
  // @Override
  // public String getDescription() {
  // return description;
  // }
  // };
  //
  // fileChooser.addChoosableFileFilter(fileFilter);
  // }
  //
  // fileChooser.setMultiSelectionEnabled(false);
  // fileChooser.putClientProperty("FileChooser.useShellFolder", Boolean.FALSE);
  //
  // return fileChooser;
  // }
  //
  // private String baseChooseFile(final FileChooserConfig fileChooserConfig) {
  // String chosenFile = AccessController
  // .doPrivileged(new PrivilegedAction<String>() {
  //
  // public String run() {
  // JFileChooser fileChooser = createFileChooser(fileChooserConfig);
  //
  // int fileChooserResult = fileChooser.showDialog(null,
  // fileChooserConfig.getApproveButtonText());
  //
  // switch (fileChooserResult) {
  // case JFileChooser.APPROVE_OPTION:
  // return fileChooser.getSelectedFile().getAbsolutePath();
  // case JFileChooser.CANCEL_OPTION:
  // case JFileChooser.ERROR_OPTION:
  // default:
  // return null;
  // }
  // }
  // });
  // return chosenFile;
  // }

  /**
   * {@inheritDoc}
   */
  @Override
  public String chooseFile(final FileChooserConfig fileChooserConfig) {
    String filePath = super.chooseFile(fileChooserConfig);

    if (fileChooserConfig.getDialogType() == FileChooserConfig.SAVE_DIALOG) {
      if (filePath != null) {
        if (filePath.indexOf(".", filePath.lastIndexOf(File.separator)) == -1) { //$NON-NLS-1$
          FileFilterConfig[] fileFilterConfigs = fileChooserConfig
              .getFileFilterConfigs();
          if (fileFilterConfigs != null && fileFilterConfigs.length > 0) {
            String[] allowedExtensions = fileFilterConfigs[0]
                .getAllowedExtensions();
            if (allowedExtensions != null && allowedExtensions.length > 0) {
              filePath += fileFilterConfigs[0].getAllowedExtensions()[0];
            }
          }
        }
        if (new File(filePath).exists()) {
          if (JOptionPane.showConfirmDialog(null, Messages
              .getString("confirm.override.description"), //$NON-NLS-1$
              Messages.getString("confirm.override.name"), //$NON-NLS-1$
              JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
            return null;
          }
        }
      }
    }
    return filePath;
  }

}
