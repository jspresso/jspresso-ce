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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.text.html.HTMLEditorKit;

/**
 * A rich HTML text editor based on Swing JEditorPane.
 *
 * @author Vincent Vandenschrick
 */
public class JHTMLEditor extends JPanel {

  private static final long serialVersionUID = 2630154668370585110L;

  private final ResourceBundle    bundle;
  private final JTextPane         editorPane;
  private final JToolBar          toolBar;
  private final JScrollPane       scrollPane;

  /**
   * Constructs a new {@code JHTMLEditor} instance.
   *
   * @param locale
   *          the locale to create the JHTMLEditor for.
   */
  public JHTMLEditor(Locale locale) {
    setFocusable(true);
    bundle = ResourceBundle.getBundle(getClass().getName(), locale);
    setLayout(new BorderLayout());
    editorPane = new JTextPane();
    editorPane.setEditorKit(new HTMLEditorKit());

    Action[] actions = editorPane.getActions();
    Map<String, Action> editorActions = new LinkedHashMap<>();
    for (Action action : actions) {
      editorActions.put((String) action.getValue(Action.NAME), action);
    }

    toolBar = createToolBar(editorActions);

    scrollPane = new JScrollPane();
    scrollPane.setViewportView(editorPane);
    add(scrollPane, BorderLayout.CENTER);
    add(toolBar, BorderLayout.SOUTH);
  }

  /**
   * Gets the editorPane.
   *
   * @return the editorPane.
   */
  public JTextPane getEditorPane() {
    return editorPane;
  }

  /**
   * Delegates to the internal editor pane.
   * @return the HTML text.
   * @see javax.swing.JEditorPane#getText()
   */
  public String getText() {
    return editorPane.getText();
  }

  /**
   * Delegates to the internal editor pane.
   * @return true if the component is editable.
   * @see javax.swing.text.JTextComponent#isEditable()
   */
  public boolean isEditable() {
    return editorPane.isEditable();
  }

  /**
   * Delegates to the internal editor pane.
   * @param b
   *          editable.
   * @see javax.swing.text.JTextComponent#setEditable(boolean)
   */
  public void setEditable(boolean b) {
    editorPane.setEditable(b);
    int scCount = toolBar.getComponentCount();
    for (int i = 0; i < scCount; i++) {
      toolBar.getComponentAtIndex(i).setEnabled(b);
    }
    toolBar.setEnabled(b);
  }

  /**
   * Delegates to the internal editor pane.
   * @param htmlText
   *          the HTML text.
   * @see javax.swing.JEditorPane#setText(java.lang.String)
   */
  public void setText(String htmlText) {
    editorPane.setText(htmlText);
  }

  private JButton createActionButton(Map<String, Action> editorActions,
      String actionName, String iconImage) {
    Action action = createDisplayableAction(editorActions, actionName,
        iconImage);
    JButton b = new JButton();
    b.setAction(action);
    b.setText(null);
    b.setPreferredSize(new Dimension(22, 22));
    return b;
  }

  private Action createDisplayableAction(Map<String, Action> editorActions,
      String actionName) {
    return createDisplayableAction(editorActions, actionName, null);
  }

  private Action createDisplayableAction(Map<String, Action> editorActions,
      String actionName, String iconImage) {
    Action actionAdapter = new DisplayableActionAdapter(
        editorActions.get(actionName));
    if (iconImage != null) {
      actionAdapter.putValue(Action.SMALL_ICON, new ImageIcon(getClass()
          .getResource(iconImage)));
    }
    actionAdapter.putValue(Action.SHORT_DESCRIPTION,
        bundle.getString(actionName));
    return actionAdapter;
  }

  @SuppressWarnings("unchecked")
  private JToolBar createToolBar(Map<String, Action> editorActions) {
    JToolBar tb = new JToolBar();

    tb.add(createActionButton(editorActions, "font-bold", "bold.gif"));
    tb.add(createActionButton(editorActions, "font-italic", "italic.gif"));
    tb.add(createActionButton(editorActions, "font-underline", "underline.gif"));

    tb.addSeparator();

    tb.add(createActionButton(editorActions, "left-justify", "left.gif"));
    tb.add(createActionButton(editorActions, "center-justify", "center.gif"));
    tb.add(createActionButton(editorActions, "right-justify", "right.gif"));

    tb.addSeparator();

    tb.add(createActionButton(editorActions, "InsertUnorderedList",
        "unsortedList.png"));
    tb.add(createActionButton(editorActions, "InsertUnorderedListItem",
        "unsortedList.png"));
    tb.add(createActionButton(editorActions, "InsertOrderedList",
        "enumList.png"));
    tb.add(createActionButton(editorActions, "InsertOrderedListItem",
        "enumList.png"));

    tb.addSeparator();

    List<Action> fontActions = new ArrayList<>();
    fontActions.add(createDisplayableAction(editorActions,
        "font-family-SansSerif"));
    fontActions.add(createDisplayableAction(editorActions,
        "font-family-Monospaced"));
    fontActions
        .add(createDisplayableAction(editorActions, "font-family-Serif"));
    JComboBox<Action> fontCb = new JComboBox<>(fontActions.toArray(new Action[fontActions.size()]));
    tb.add(fontCb);
    fontCb.addActionListener(new ActionListener() {

      @SuppressWarnings("unchecked")
      @Override
      public void actionPerformed(ActionEvent e) {
        ((Action) ((JComboBox<Action>) e.getSource()).getSelectedItem())
            .actionPerformed(e);
      }
    });

    List<Action> fontSizeActions = new ArrayList<>();
    fontSizeActions.add(createDisplayableAction(editorActions, "font-size-10"));
    fontSizeActions.add(createDisplayableAction(editorActions, "font-size-12"));
    fontSizeActions.add(createDisplayableAction(editorActions, "font-size-18"));
    fontSizeActions.add(createDisplayableAction(editorActions, "font-size-24"));
    fontSizeActions.add(createDisplayableAction(editorActions, "font-size-48"));
    JComboBox<Action> fontSizeCb = new JComboBox<>(fontSizeActions.toArray(new Action[fontSizeActions.size()]));
    fontSizeCb.addActionListener(new ActionListener() {

      @SuppressWarnings("unchecked")
      @Override
      public void actionPerformed(ActionEvent e) {
        ((Action) ((JComboBox<Action>) e.getSource()).getSelectedItem())
            .actionPerformed(e);
      }
    });
    tb.add(fontSizeCb);

    return tb;
  }

  private static class DisplayableActionAdapter implements Action {

    private final Action delegate;

    /**
     * Constructs a new {@code DisplayableAction} instance.
     *
     * @param delegate
     *          the action delegate.
     */
    public DisplayableActionAdapter(Action delegate) {
      this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
      delegate.actionPerformed(e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
      delegate.addPropertyChangeListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValue(String key) {
      return delegate.getValue(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled() {
      return delegate.isEnabled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putValue(String key, Object value) {
      delegate.putValue(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
      delegate.removePropertyChangeListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnabled(boolean b) {
      delegate.setEnabled(b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
      return (String) getValue(Action.SHORT_DESCRIPTION);
    }
  }

  /**
   * Delegates to internal scroll pane.
   * @param policy the scroll policy.
   * @see javax.swing.JScrollPane#setVerticalScrollBarPolicy(int)
   */
  @SuppressWarnings("MagicConstant")
  public void setVerticalScrollBarPolicy(int policy) {
    scrollPane.setVerticalScrollBarPolicy(policy);
  }

  /**
   * Delegates to internal scroll pane.
   * @param policy the scroll policy.
   * @see javax.swing.JScrollPane#setHorizontalScrollBarPolicy(int)
   */
  @SuppressWarnings("MagicConstant")
  public void setHorizontalScrollBarPolicy(int policy) {
    scrollPane.setHorizontalScrollBarPolicy(policy);
  }
}
