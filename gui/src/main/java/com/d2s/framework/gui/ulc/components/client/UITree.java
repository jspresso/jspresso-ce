/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.client;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTree;

import com.d2s.framework.gui.ulc.components.shared.TreeConstants;
import com.ulcjava.base.shared.internal.Anything;

/**
 * This is the client half object of the ULCTree extension.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class UITree extends com.ulcjava.base.client.UITree {

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object createBasicObject(Anything a) {
    JTree tree = (JTree) super.createBasicObject(a);
    tree.addMouseListener(new PreparePopupListener());
    return tree;
  }

  private final class PreparePopupListener extends MouseAdapter {

    /**
     * {@inheritDoc}
     */
    @Override
    public void mousePressed(MouseEvent evt) {
      maybePreparePopup(evt);
    }

    private void maybePreparePopup(MouseEvent evt) {
      if (evt.getButton() != MouseEvent.BUTTON1) {
        JTree tree = (JTree) evt.getSource();
        int row = tree.getRowForLocation(evt.getX(), evt.getY());
        Anything rowAnything = new Anything();
        rowAnything.put(TreeConstants.ROW_KEY, row);
        sendULC(TreeConstants.PREPARE_POPUP_REQUEST, rowAnything);
      }
    }
  }

}
