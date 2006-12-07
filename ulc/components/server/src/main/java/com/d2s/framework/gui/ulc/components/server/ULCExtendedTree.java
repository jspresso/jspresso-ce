/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.server;

import java.util.List;
import java.util.Map;

import com.d2s.framework.gui.ulc.components.server.event.ExtendedTreeExpansionEvent;
import com.d2s.framework.gui.ulc.components.server.event.IExtendedTreeWillExpandListener;
import com.d2s.framework.gui.ulc.components.shared.ExtendedInternalFrameConstants;
import com.d2s.framework.gui.ulc.components.shared.ExtendedTreeConstants;
import com.ulcjava.base.application.ULCPopupMenu;
import com.ulcjava.base.application.tree.ITreeModel;
import com.ulcjava.base.application.tree.ITreeNode;
import com.ulcjava.base.application.tree.TreePath;
import com.ulcjava.base.shared.internal.Anything;

/**
 * ULC tree extended to take care of will events.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCExtendedTree extends com.ulcjava.base.application.ULCTree {

  private static final long     serialVersionUID = -7089246786443892657L;

  private ITreePathPopupFactory popupFactory;

  /**
   * Constructs a new <code>ULCExtendedTree</code> instance.
   */
  public ULCExtendedTree() {
    super();
  }

  /**
   * Constructs a new <code>ULCExtendedTree</code> instance.
   * 
   * @param model
   *          the tree model.
   */
  public ULCExtendedTree(ITreeModel model) {
    super(model);
  }

  /**
   * Constructs a new <code>ULCExtendedTree</code> instance.
   * 
   * @param root
   *          the root node.
   */
  public ULCExtendedTree(ITreeNode root) {
    super(root);
  }

  /**
   * Constructs a new <code>ULCExtendedTree</code> instance.
   * 
   * @param children
   *          the list of nodes.
   */
  public ULCExtendedTree(List children) {
    super(children);
  }

  /**
   * Constructs a new <code>ULCExtendedTree</code> instance.
   * 
   * @param children
   *          the map of children nodes.
   */
  public ULCExtendedTree(Map children) {
    super(children);
  }

  /**
   * Constructs a new <code>ULCExtendedTree</code> instance.
   * 
   * @param children
   *          the array of children nodes.
   */
  public ULCExtendedTree(Object[] children) {
    super(children);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String typeString() {
    return "com.d2s.framework.gui.ulc.components.client.UIExtendedTree";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void handleEvent(int listenerType, int eventId, Anything args) {
    if (listenerType == ExtendedTreeConstants.EXTENDED_TREE_EXPANSION_EVENT) {
      TreePath treePath = getPathForRow(args.get(ExtendedTreeConstants.ROW_KEY,
          0));
      distributeToListeners(new ExtendedTreeExpansionEvent(this, eventId,
          treePath));
    } else {
      super.handleEvent(listenerType, eventId, args);
    }
  }

  /**
   * Adds an extended listener.
   * 
   * @param listener
   *          the listener.
   */
  public void addTreeWillExpandListener(IExtendedTreeWillExpandListener listener) {
    internalAddListener(ExtendedTreeConstants.EXTENDED_TREE_EXPANSION_EVENT,
        listener);
  }

  /**
   * Removes an extended listener.
   * 
   * @param listener
   *          the listener.
   */
  public void removeExtendedTreeWillExpandListener(
      IExtendedTreeWillExpandListener listener) {
    internalRemoveListener(
        ExtendedInternalFrameConstants.EXTENDED_INTERNAL_FRAME_EVENT, listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleRequest(String request, Anything args) {
    if (request.equals(ExtendedTreeConstants.PREPARE_POPUP_REQUEST)) {
      handlePreparePopup(args.get(ExtendedTreeConstants.ROW_KEY, 0));
    } else {
      super.handleRequest(request, args);
    }
  }

  private void handlePreparePopup(int row) {
    if (popupFactory != null) {
      ULCPopupMenu popupMenu = popupFactory
          .createPopupForTreepath(getPathForRow(row));
      setComponentPopupMenu(popupMenu);
    }
  }

  /**
   * Sets the popupFactory.
   * 
   * @param popupFactory
   *          the popupFactory to set.
   */
  public void setPopupFactory(ITreePathPopupFactory popupFactory) {
    this.popupFactory = popupFactory;
  }
}
