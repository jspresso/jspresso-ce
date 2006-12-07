/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.echo;

import java.util.Arrays;

import nextapp.echo2.app.event.ChangeEvent;
import nextapp.echo2.app.event.ChangeListener;
import nextapp.echo2.app.list.ListSelectionModel;

import com.d2s.framework.util.IIndexMapper;
import com.d2s.framework.util.event.ISelectable;
import com.d2s.framework.util.event.ISelectionChangeListener;
import com.d2s.framework.util.event.SelectionChangeEvent;
import com.d2s.framework.util.swing.SwingUtil;

/**
 * Default implementation of <code>IListSelectionModelBinder</code>.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultListSelectionModelBinder implements
    IListSelectionModelBinder {

  /**
   * {@inheritDoc}
   */
  public void bindSelectionModel(ISelectable selectable,
      ListSelectionModel selectionModel, IIndexMapper rowMapper) {
    selectionModel.addChangeListener(new SelectionModelListener(selectable,
        rowMapper));
    selectable.addSelectionChangeListener(new SelectionChangeListener(
        selectionModel, rowMapper));
  }

  private static final class SelectionChangeListener implements
      ISelectionChangeListener {

    private ListSelectionModel selectionModel;
    private IIndexMapper       rowMapper;

    /**
     * Constructs a new <code>SelectionChangeListener</code> instance.
     * 
     * @param selectionModel
     *          the selection model to forward the changes to.
     * @param rowMapper
     */
    public SelectionChangeListener(ListSelectionModel selectionModel,
        IIndexMapper rowMapper) {
      this.selectionModel = selectionModel;
      this.rowMapper = rowMapper;
    }

    /**
     * Forwards selectable's selection to model selection.
     * <p>
     * {@inheritDoc}
     */
    public void selectionChange(final SelectionChangeEvent evt) {
      SwingUtil.updateSwingGui(new Runnable() {

        public void run() {
          if (evt.getNewSelection() == null
              || evt.getNewSelection().length == 0) {
            selectionModel.clearSelection();
            return;
          }
          int[] viewIndices;
          if (rowMapper != null) {
            int[] modelIndices = evt.getNewSelection().clone();
            viewIndices = new int[modelIndices.length];
            for (int i = 0; i < modelIndices.length; i++) {
              viewIndices[i] = rowMapper.viewIndex(modelIndices[i]);
            }
          } else {
            viewIndices = evt.getNewSelection().clone();
          }

          Arrays.sort(viewIndices);
          int[] selectedIndices = getSelectedIndices(selectionModel);
          Arrays.sort(selectedIndices);
          if (Arrays.equals(viewIndices, selectedIndices)) {
            return;
          }
          for (int index : viewIndices) {
            selectionModel.setSelectedIndex(index, true);
          }
        }
      });
    }
  }

  private static final class SelectionModelListener implements ChangeListener {

    private static final long serialVersionUID = -2095104121255697027L;

    private ISelectable       viewSelectable;
    private IIndexMapper      rowMapper;

    /**
     * Constructs a new <code>SelectionModelListener</code> instance.
     * 
     * @param viewSelectable
     *          the selectable to forward the changes to.
     * @param rowMapper
     */
    public SelectionModelListener(ISelectable viewSelectable,
        IIndexMapper rowMapper) {
      this.viewSelectable = viewSelectable;
      this.rowMapper = rowMapper;
    }

    /**
     * Tracks changes in model selections to forward to a selectable.
     * <p>
     * {@inheritDoc}
     */
    public void stateChanged(ChangeEvent e) {
      ListSelectionModel sm = (ListSelectionModel) e.getSource();
      int[] selectedIndices = getSelectedIndices(sm);
      int[] modelIndices;
      if (selectedIndices.length > 0) {
        if (rowMapper != null) {
          int[] viewIndices = selectedIndices;
          modelIndices = new int[viewIndices.length];
          for (int i = 0; i < viewIndices.length; i++) {
            modelIndices[i] = rowMapper.modelIndex(viewIndices[i]);
          }
        } else {
          modelIndices = selectedIndices;
        }
      } else {
        modelIndices = selectedIndices;
      }
      viewSelectable.setSelectedIndices(modelIndices);
    }
  }

  private static int[] getSelectedIndices(ListSelectionModel sm) {
    int iMin = sm.getMinSelectedIndex();
    int iMax = sm.getMaxSelectedIndex();

    if ((iMin < 0) || (iMax < 0)) {
      return new int[0];
    }

    int[] rvTmp = new int[1 + (iMax - iMin)];
    int n = 0;
    for (int i = iMin; i <= iMax; i++) {
      if (sm.isSelectedIndex(i)) {
        rvTmp[n++] = i;
      }
    }
    int[] rv = new int[n];
    System.arraycopy(rvTmp, 0, rv, 0, n);
    return rv;
  }
}
