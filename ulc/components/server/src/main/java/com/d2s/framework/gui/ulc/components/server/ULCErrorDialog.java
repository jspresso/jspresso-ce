/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.server;

import java.util.Locale;

import com.d2s.framework.util.i18n.ITranslationProvider;
import com.d2s.framework.util.ulc.UlcUtil;
import com.ulcjava.base.application.GridBagConstraints;
import com.ulcjava.base.application.ULCButton;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.ULCDialog;
import com.ulcjava.base.application.ULCGridBagLayoutPane;
import com.ulcjava.base.application.ULCHtmlPane;
import com.ulcjava.base.application.ULCLabel;
import com.ulcjava.base.application.ULCScrollPane;
import com.ulcjava.base.application.ULCWindow;
import com.ulcjava.base.application.event.ActionEvent;
import com.ulcjava.base.application.event.serializable.IActionListener;
import com.ulcjava.base.application.util.Dimension;
import com.ulcjava.base.application.util.Insets;
import com.ulcjava.base.application.util.ULCIcon;

/**
 * Dialog used for reporting detailed messages (and errors).
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class ULCErrorDialog extends ULCDialog {

  private static final long    serialVersionUID = -3122747783739141527L;

  /**
   * Factory method for error dialog.
   * 
   * @param sourceComponent
   *          one of the components insinde the owning window.
   * @param translationProvider
   *          the translationProvider for labels.
   * @param locale
   *          the locale used.
   * @return the created error dialog instance.
   */
  public static ULCErrorDialog createInstance(ULCComponent sourceComponent,
      ITranslationProvider translationProvider, Locale locale) {
    ULCErrorDialog errorDialog;
    errorDialog = new ULCErrorDialog(UlcUtil.getVisibleWindow(sourceComponent));
    errorDialog.translationProvider = translationProvider;
    errorDialog.locale = locale;
    errorDialog.initGui();
    return errorDialog;
  }
  private int                  collapsedHeight  = 0;

  private ULCButton            detailsButton;
  private ULCHtmlPane          detailsPane;
  private ULCGridBagLayoutPane detailsPanel;

  private int                  expandedHeight   = 0;
  private ULCLabel             iconLabel;

  private Locale               locale;
  private ULCHtmlPane          messagePane;

  private ITranslationProvider translationProvider;

  private ULCErrorDialog(ULCWindow owner) {
    super(owner, true);
  }

  /**
   * initialize the gui.
   */
  private void initGui() {
    // initialize the gui
    ULCGridBagLayoutPane pane = new ULCGridBagLayoutPane();

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.setAnchor(GridBagConstraints.NORTH);
    gbc.setFill(GridBagConstraints.NONE);
    gbc.setGridHeight(1);
    gbc.setInsets(new Insets(22, 12, 11, 17));
    iconLabel = new ULCLabel();
    pane.add(iconLabel, gbc);

    messagePane = new ULCHtmlPane();
    messagePane.setOpaque(false);
    gbc = new GridBagConstraints();
    gbc.setAnchor(GridBagConstraints.WEST);
    gbc.setFill(GridBagConstraints.BOTH);
    gbc.setGridHeight(1);
    gbc.setGridWidth(3);
    gbc.setGridX(1);
    gbc.setWeightX(0.0);
    gbc.setWeightY(0.00001);
    gbc.setInsets(new Insets(24, 0, 0, 11));
    pane.add(messagePane, gbc);

    gbc = new GridBagConstraints();
    gbc.setFill(GridBagConstraints.NONE);
    gbc.setGridX(1);
    gbc.setGridY(1);
    gbc.setGridWidth(1);
    gbc.setWeightX(1.0);
    gbc.setWeightY(0.0);
    gbc.setAnchor(GridBagConstraints.EAST);
    gbc.setInsets(new Insets(12, 0, 11, 5));
    ULCButton okButton = new ULCButton(translationProvider.getTranslation("ok",
        locale));
    pane.add(okButton, gbc);

    detailsButton = new ULCButton(translationProvider.getTranslation("details",
        locale));
    gbc = new GridBagConstraints();
    gbc.setGridX(2);
    gbc.setWeightX(0.0);
    gbc.setInsets(new Insets(12, 0, 11, 11));
    pane.add(detailsButton, gbc);

    detailsPane = new ULCHtmlPane();
    // detailsPane.setTransferHandler(new DetailsTransferHandler());
    ULCScrollPane detailsScrollPane = new ULCScrollPane(detailsPane);
    detailsScrollPane.setPreferredSize(new Dimension(10, 250));
    detailsPanel = new ULCGridBagLayoutPane();
    detailsPanel.add(detailsScrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0,
        1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(6,
            11, 11, 11), 0, 0));
    gbc = new GridBagConstraints();
    gbc.setFill(GridBagConstraints.BOTH);
    gbc.setGridWidth(3);
    gbc.setGridX(0);
    gbc.setGridY(2);
    gbc.setWeightY(1.0);
    pane.add(detailsPanel, gbc);

    ULCButton button = new ULCButton(translationProvider.getTranslation(
        "copy.name", locale));
    button.addActionListener(new IActionListener() {

      private static final long serialVersionUID = -3638328336723671191L;

      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent ae) {
        // detailsPane.copy();
      }
    });
    gbc = new GridBagConstraints();
    gbc.setAnchor(GridBagConstraints.WEST);
    gbc.setFill(GridBagConstraints.NONE);
    gbc.setGridWidth(1);
    gbc.setGridX(0);
    gbc.setGridY(1);
    gbc.setWeightY(0.0);
    gbc.setWeightX(1.0);
    gbc.setInsets(new Insets(6, 11, 11, 11));
    detailsPanel.add(button, gbc);

    okButton.addActionListener(new IActionListener() {

      private static final long serialVersionUID = 4528742243771716293L;

      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent e) {
        setVisible(false);
      }
    });
    detailsButton.addActionListener(new IActionListener() {

      private static final long serialVersionUID = -6132496169632602467L;

      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent e) {
        setDetailsVisible(!detailsPanel.isVisible());
      }
    });
    getContentPane().add(pane);
  }

  /**
   * Set the details section of the error dialog. If the details are either null
   * or an empty string, then hide the detailsPane button and hide the detail
   * scroll pane. Otherwise, just set the detailsPane section.
   * 
   * @param details
   *          Details to be shown in the detail section of the dialog. This can
   *          be null if you do not want to display the details section of the
   *          dialog.
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
   *          Details to be shown in the detail section of the dialog. This can
   *          be null if you do not want to display the details section of the
   *          dialog.
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
   * Set the detailsPane section to be either visible or invisible. Set the text
   * of the Details button accordingly.
   * 
   * @param b
   *          if true detailsPane section will be visible
   */
  private void setDetailsVisible(boolean b) {
    if (b) {
      collapsedHeight = getHeight();
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
      setSize(getWidth(), height);
    } else {
      detailsPanel.setVisible(false);
      detailsButton.setText(translationProvider.getTranslation("details",
          locale)
          + " >>");
      setSize(getWidth(), collapsedHeight);
    }
    repaint();
  }

  /**
   * Set the error message for the dialog box.
   * 
   * @param message
   *          Message for the error dialog
   */
  public void setMessage(String message) {
    this.messagePane.setText(message);
  }

  /**
   * Specifies the icon to use.
   * 
   * @param messageIcon
   *          the Icon to use. If null, the default error icon will be used
   */
  public void setMessageIcon(ULCIcon messageIcon) {
    iconLabel.setIcon(messageIcon);
  }
}
