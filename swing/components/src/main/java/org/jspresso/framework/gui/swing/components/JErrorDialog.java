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
package org.jspresso.framework.gui.swing.components;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.TransferHandler;
import javax.swing.plaf.basic.BasicHTML;

import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.util.swing.SwingUtil;

/**
 * Dialog used for reporting detailed messages (and errors).
 *
 * @author Vincent Vandenschrick
 */
public final class JErrorDialog extends JDialog {

  private static final long    serialVersionUID = -3122747783739141527L;

  private int                  collapsedHeight  = 0;
  private JButton              detailsButton;

  private JEditorPane          detailsPane;
  private JPanel               detailsPanel;
  private int                  expandedHeight   = 0;

  private JLabel               iconLabel;
  private Locale               locale;

  private JEditorPane          messagePane;
  private ITranslationProvider translationProvider;

  private JErrorDialog(Dialog owner) {
    super(owner, true);
  }

  private JErrorDialog(Frame owner) {
    super(owner, true);
  }

  /**
   * Factory method for error dialog.
   *
   * @param sourceComponent
   *          one of the components inside the owning window.
   * @param translationProvider
   *          the translationProvider for labels.
   * @param locale
   *          the locale used.
   * @return the created error dialog instance.
   */
  public static JErrorDialog createInstance(Component sourceComponent,
      ITranslationProvider translationProvider, Locale locale) {
    JErrorDialog errorDialog;
    Window window = SwingUtil.getVisibleWindow(sourceComponent);
    if (window instanceof Dialog) {
      errorDialog = new JErrorDialog((Dialog) window);
    } else {
      errorDialog = new JErrorDialog((Frame) window);
    }
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
      StringBuilder html = new StringBuilder("<html>");
      html.append("<b>").append(translationProvider.getTranslation("details", locale)).append(" :</b>");
      html.append("<pre>");
      html.append("    ").append(details.getMessage());
      html.append("</pre>");
      html.append("<div></div>");
      html.append("<b>").append(translationProvider.getTranslation("stacktrace", locale)).append(" :</b>");
      html.append("<pre>");
      for (StackTraceElement el : details.getStackTrace()) {
        html.append("    ").append(el.toString()).append("\n");
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
   *          Message for the error dialog
   */
  public void setMessage(String message) {
    if (BasicHTML.isHTMLString(message)) {
      this.messagePane.setContentType("text/html");
    } else {
      this.messagePane.setContentType("text/plain");
    }
    this.messagePane.setText(message);
  }

  /**
   * Specifies the icon to use.
   *
   * @param messageIcon
   *          the Icon to use. If null, the default error icon will be used
   */
  public void setMessageIcon(Icon messageIcon) {
    iconLabel.setIcon(messageIcon);
  }

  /**
   * initialize the gui.
   */
  private void initGui() {
    // initialize the gui
    GridBagLayout layout = new GridBagLayout();
    this.getContentPane().setLayout(layout);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.NORTH;
    gbc.fill = GridBagConstraints.NONE;
    gbc.gridheight = 1;
    gbc.insets = new Insets(22, 12, 11, 17);
    iconLabel = new JLabel();
    this.getContentPane().add(iconLabel, gbc);

    messagePane = new JEditorPane();
    messagePane.setEditable(false);
    messagePane.setContentType("text/html");
    messagePane.setOpaque(false);
    messagePane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES,
        Boolean.TRUE);
    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridheight = 1;
    gbc.gridwidth = 2;
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 0.0;
    gbc.weighty = 0.00001;
    gbc.insets = new Insets(24, 0, 0, 11);
    this.getContentPane().add(messagePane, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.NONE;
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.weightx = 1.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.insets = new Insets(12, 0, 11, 5);
    JButton okButton = new JButton(translationProvider.getTranslation("ok",
        locale));
    this.getContentPane().add(okButton, gbc);

    detailsButton = new JButton(translationProvider.getTranslation("details",
        locale));
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.weightx = 0.0;
    gbc.insets = new Insets(12, 0, 11, 11);
    this.getContentPane().add(detailsButton, gbc);

    detailsPane = new JEditorPane();
    detailsPane.setContentType("text/html");
    detailsPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES,
        Boolean.TRUE);
    detailsPane.setTransferHandler(new DetailsTransferHandler());
    JScrollPane detailsScrollPane = new JScrollPane(detailsPane);
    detailsScrollPane.setPreferredSize(new Dimension(10, 250));
    detailsPane.setEditable(false);
    detailsPanel = new JPanel(new GridBagLayout());
    detailsPanel.add(detailsScrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0,
        1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(6,
            11, 11, 11), 0, 0));
    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridwidth = 3;
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    this.getContentPane().add(detailsPanel, gbc);

    JButton button = new JButton(translationProvider.getTranslation(
        "copy.name", locale));
    button.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent ae) {
        detailsPane.copy();
      }
    });
    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.fill = GridBagConstraints.NONE;
    gbc.gridwidth = 1;
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weighty = 0.0;
    gbc.weightx = 1.0;
    gbc.insets = new Insets(6, 11, 11, 11);
    detailsPanel.add(button, gbc);

    okButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        setVisible(false);
        dispose();
      }
    });
    detailsButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        setDetailsVisible(!detailsPanel.isVisible());
      }
    });
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
          locale) + "<<");
      detailsPanel.applyComponentOrientation(detailsButton
          .getComponentOrientation());
      detailsPane.setCaretPosition(0);
      setSize(getWidth(), height);
    } else {
      expandedHeight = getHeight();
      detailsPanel.setVisible(false);
      detailsButton.setText(translationProvider.getTranslation("details",
          locale) + ">>");
      messagePane.setSize(0, 0);
      messagePane.setSize(messagePane.getPreferredSize());
      setSize(getWidth(), collapsedHeight);
    }
    invalidate();
    repaint();
  }

  private final class DetailsTransferHandler extends TransferHandler {

    private static final long serialVersionUID = -5398570598349570102L;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSourceActions(JComponent c) {
      return TransferHandler.COPY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Transferable createTransferable(JComponent c) {
      String text = detailsPane.getSelectedText();
      if (text == null || text.equals("")) {
        detailsPane.selectAll();
        text = detailsPane.getSelectedText();
        detailsPane.select(-1, -1);
      }
      return new StringSelection(text);
    }
  }
}
