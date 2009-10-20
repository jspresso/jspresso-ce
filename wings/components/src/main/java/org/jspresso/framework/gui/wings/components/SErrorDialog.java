/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.gui.wings.components;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.wings.SButton;
import org.wings.SComponent;
import org.wings.SDialog;
import org.wings.SDimension;
import org.wings.SFrame;
import org.wings.SGridBagLayout;
import org.wings.SIcon;
import org.wings.SLabel;
import org.wings.SPanel;
import org.wings.SScrollPane;


/**
 * Dialog used for reporting detailed messages (and errors).
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class SErrorDialog extends SDialog {

  private static final long    serialVersionUID = -3122747783739141527L;

  private int                  collapsedHeight  = 0;
  private SButton              detailsButton;

  private SLabel               detailsPane;
  private SPanel               detailsPanel;
  private int                  expandedHeight   = 0;

  private SLabel               iconLabel;
  private Locale               locale;

  private SLabel               messagePane;
  private ITranslationProvider translationProvider;

  private SErrorDialog(SFrame owner) {
    super(owner, true);
    setDraggable(true);
  }

  /**
   * Factory method for error dialog.
   * 
   * @param sourceComponent
   *            one of the components insinde the owning window.
   * @param translationProvider
   *            the translationProvider for labels.
   * @param locale
   *            the locale used.
   * @return the created error dialog instance.
   */
  public static SErrorDialog createInstance(SComponent sourceComponent,
      ITranslationProvider translationProvider, Locale locale) {
    SErrorDialog errorDialog;
    errorDialog = new SErrorDialog(sourceComponent.getParentFrame());
    errorDialog.translationProvider = translationProvider;
    errorDialog.locale = locale;
    errorDialog.initGui();
    return errorDialog;
  }

  /**
   * Set the details section of the error dialog. If the details are either null
   * or an empty string, then hide the detailsPane button and hide the detail
   * scroll pane. Otherwise, just set the detailsPane section.
   * 
   * @param details
   *            Details to be shown in the detail section of the dialog. This
   *            can be null if you do not want to display the details section of
   *            the dialog.
   */
  public void setDetails(String details) {
    if (details == null || details.equals("")) {
      setDetailsVisible(false);
      detailsButton.setVisible(false);
    } else {
      this.detailsPane.setText(details);
      setDetailsVisible(false);
      detailsButton.setVisible(true);
    }
  }

  /**
   * Set the details section of the error dialog. If the details are either null
   * or an empty string, then hide the detailsPane button and hide the detail
   * scroll pane. Otherwise, just set the detailsPane section.
   * 
   * @param details
   *            Details to be shown in the detail section of the dialog. This
   *            can be null if you do not want to display the details section of
   *            the dialog.
   */
  public void setDetails(Throwable details) {
    String exceptionAsDetails = null;
    if (details != null) {
      StringBuffer html = new StringBuffer("<html>");
      html.append("<b>" + translationProvider.getTranslation("details", locale)
          + " :</b>");
      html.append("<pre>");
      html.append("    " + details.getMessage());
      html.append("</pre>");
      html.append("<div></div>");
      html
          .append("<b>"
              + translationProvider.getTranslation("stacktrace", locale)
              + " :</b>");
      html.append("<pre>");
      for (StackTraceElement el : details.getStackTrace()) {
        html.append("    " + el.toString() + "\n");
      }
      html.append("</pre></html>");
      exceptionAsDetails = html.toString();
    }
    setDetails(exceptionAsDetails);
  }

  /**
   * Set the error message for the dialog box.
   * 
   * @param message
   *            Message for the error dialog
   */
  public void setMessage(String message) {
    this.messagePane.setText(message);
  }

  /**
   * Specifies the icon to use.
   * 
   * @param messageIcon
   *            the Icon to use. If null, the default error icon will be used
   */
  public void setMessageIcon(SIcon messageIcon) {
    iconLabel.setIcon(messageIcon);
  }

  /**
   * initialize the gui.
   */
  private void initGui() {
    // initialize the gui
    SPanel pane = new SPanel(new SGridBagLayout());

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.NORTH;
    gbc.fill = GridBagConstraints.NONE;
    gbc.gridheight = 1;
    gbc.insets = new Insets(22, 12, 11, 17);
    iconLabel = new SLabel();
    pane.add(iconLabel, gbc);

    messagePane = new SLabel();
    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridheight = 1;
    gbc.gridwidth = 3;
    gbc.gridx = 1;
    gbc.weightx = 0.0;
    gbc.weighty = 0.00001;
    gbc.insets = new Insets(24, 0, 0, 11);
    pane.add(messagePane, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.NONE;
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.weightx = 1.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.insets = new Insets(12, 0, 11, 5);
    SButton okButton = new SButton(translationProvider.getTranslation("ok",
        locale));
    pane.add(okButton, gbc);

    detailsButton = new SButton(translationProvider.getTranslation("details",
        locale));
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.weightx = 0.0;
    gbc.insets = new Insets(12, 0, 11, 11);
    pane.add(detailsButton, gbc);

    detailsPane = new SLabel();
    SScrollPane detailsScrollPane = new SScrollPane(detailsPane);
    detailsScrollPane.setMode(SScrollPane.MODE_COMPLETE);
    detailsScrollPane.setPreferredSize(new SDimension(10, 250));
    detailsPanel = new SPanel(new SGridBagLayout());
    detailsPanel.add(detailsScrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0,
        1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(6,
            11, 11, 11), 0, 0));
    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridwidth = 3;
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.weighty = 1.0;
    pane.add(detailsPanel, gbc);

    SButton button = new SButton(translationProvider.getTranslation(
        "copy.name", locale));
    button.addActionListener(new ActionListener() {

      private static final long serialVersionUID = -3638328336723671191L;

      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent ae) {
        // detailsPane.copy();
      }
    });
    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.NONE;
    gbc.gridwidth = 1;
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weighty = 0.0;
    gbc.weightx = 1.0;
    gbc.insets = new Insets(6, 11, 11, 11);
    detailsPanel.add(button, gbc);

    okButton.addActionListener(new ActionListener() {

      private static final long serialVersionUID = 4528742243771716293L;

      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent e) {
        setVisible(false);
      }
    });
    detailsButton.addActionListener(new ActionListener() {

      private static final long serialVersionUID = -6132496169632602467L;

      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent e) {
        setDetailsVisible(!detailsPanel.isVisible());
      }
    });
    add(pane);
  }

  /**
   * Set the detailsPane section to be either visible or invisible. Set the text
   * of the Details button accordingly.
   * 
   * @param b
   *            if true detailsPane section will be visible
   */
  private void setDetailsVisible(boolean b) {
    if (b) {
      collapsedHeight = 200;
      int height;
      if (expandedHeight == 0) {
        height = collapsedHeight + 300;
      } else {
        height = expandedHeight;
      }
      detailsPanel.setVisible(true);
      detailsButton.setText(translationProvider.getTranslation("details",
          locale)
          + " <<");
      setPreferredSize(new SDimension(null, height + "px"));
    } else {
      detailsPanel.setVisible(false);
      detailsButton.setText(translationProvider.getTranslation("details",
          locale)
          + " >>");
      setPreferredSize(new SDimension(null, collapsedHeight + "px"));
    }
  }
}
