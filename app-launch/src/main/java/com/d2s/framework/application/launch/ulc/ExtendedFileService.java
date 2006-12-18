/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.launch.ulc;

import java.io.File;

import javax.swing.JOptionPane;

import com.d2s.framework.util.i18n.Messages;
import com.ulcjava.base.shared.FileChooserConfig;
import com.ulcjava.base.shared.FileChooserConfig.FileFilterConfig;
import com.ulcjava.base.trusted.AllPermissionsFileService;

/**
 * A file service that handles automatic extension mgt and overriding
 * confirmation.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ExtendedFileService extends AllPermissionsFileService {

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
