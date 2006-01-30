/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.server;

import java.util.List;
import java.util.Map;

import com.d2s.framework.gui.ulc.components.shared.TreeConstants;
import com.ulcjava.base.application.ULCPopupMenu;
import com.ulcjava.base.application.tree.ITreeModel;
import com.ulcjava.base.application.tree.ITreeNode;
import com.ulcjava.base.shared.internal.Anything;

/**
 * This is an extension to the ULCTree component to be able to handle a per-node
 * popup menu.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCTree extends com.ulcjava.base.application.ULCTree {

  private static final long     serialVersionUID = -6399950249801628549L;

  private ITreePathPopupFactory popupFactory;

  /**
   * Constructs a new <code>ULCTree</code> instance.
   */
  public ULCTree() {
    super();
  }

  /**
   * Constructs a new <code>ULCTree</code> instance.
   * 
   * @param model
   *          the tree model.
   */
  public ULCTree(ITreeModel model) {
    super(model);
  }

  /**
   * Constructs a new <code>ULCTree</code> instance.
   * 
   * @param root
   *          the root node.
   */
  public ULCTree(ITreeNode root) {
    super(root);
  }

  /**
   * Constructs a new <code>ULCTree</code> instance.
   * 
   * @param children
   *          the list of nodes.
   */
  public ULCTree(List children) {
    super(children);
  }

  /**
   * Constructs a new <code>ULCTree</code> instance.
   * 
   * @param children
   *          the map of children nodes.
   */
  public ULCTree(Map children) {
    super(children);
  }

  /**
   * Constructs a new <code>ULCTree</code> instance.
   * 
   * @param children
   *          the array of children nodes.
   */
  public ULCTree(Object[] children) {
    super(children);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String typeString() {
    return "com.d2s.framework.gui.ulc.components.client.UITree";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleRequest(String request, Anything args) {
    if (request.equals(TreeConstants.PREPARE_POPUP_REQUEST)) {
      handlePreparePopup(args.get(TreeConstants.ROW_KEY, 0));
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
