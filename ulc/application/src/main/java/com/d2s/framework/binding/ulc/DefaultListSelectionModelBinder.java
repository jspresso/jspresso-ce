/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.ulc;

import java.util.Arrays;

import com.d2s.framework.util.IIndexMapper;
import com.d2s.framework.util.event.ISelectable;
import com.d2s.framework.util.event.ISelectionChangeListener;
import com.d2s.framework.util.event.SelectionChangeEvent;
import com.ulcjava.base.application.ULCListSelectionModel;
import com.ulcjava.base.application.event.ListSelectionEvent;
import com.ulcjava.base.application.event.serializable.IListSelectionListener;

/**
 * Default implementation of <code>IListSelectionModelBinder</code>.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultListSelectionModelBinder implements
    IListSelectionModelBinder {

  private static int[] getSelectedIndices(ULCListSelectionModel sm) {
    int iMin = sm.getMinSelectionIndex();
    int iMax = sm.getMaxSelectionIndex();

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

  /**
   * {@inheritDoc}
   */
  public void bindSelectionModel(ISelectable selectable,
      ULCListSelectionModel selectionModel, IIndexMapper rowMapper) {
    selectionModel.addListSelectionListener(new SelectionModelListener(
        selectable, rowMapper));
    selectable.addSelectionChangeListener(new SelectionChangeListener(
        selectionModel, rowMapper));
  }

  private static final class SelectionChangeListener implements
      ISelectionChangeListener {

    private IIndexMapper          rowMapper;
    private ULCListSelectionModel selectionModel;

    /**
     * Constructs a new <code>SelectionChangeListener</code> instance.
     * 
     * @param selectionModel
     *            the selection model to forward the changes to.
     * @param rowMapper
     */
    public SelectionChangeListener(ULCListSelectionModel selectionModel,
        IIndexMapper rowMapper) {
      this.selectionModel = selectionModel;
      this.rowMapper = rowMapper;
    }

    /**
     * Forwards selectable's selection to model selection.
     * <p>
     * {@inheritDoc}
     */
    public void selectionChange(SelectionChangeEvent evt) {
      if (evt.getNewSelection() == null || evt.getNewSelection().length == 0) {
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

      int nextRangeMin = viewIndices[0];
      int nextRangeMax = nextRangeMin;
      boolean firstSelection = true;
      for (int index : viewIndices) {
        if (index == nextRangeMax + 1) {
          // contiguous selection
          nextRangeMax = index;
        } else if (index != nextRangeMax) {
          if (firstSelection) {
            selectionModel.setSelectionInterval(nextRangeMin, nextRangeMax);
            firstSelection = false;
          } else {
            selectionModel.addSelectionInterval(nextRangeMin, nextRangeMax);
          }
          nextRangeMin = index;
          nextRangeMax = nextRangeMin;
        }
      }
      if (firstSelection) {
        selectionModel.setSelectionInterval(nextRangeMin, nextRangeMax);
        firstSelection = false;
      } else {
        selectionModel.addSelectionInterval(nextRangeMin, nextRangeMax);
      }
    }
  }

  private static final class SelectionModelListener implements
      IListSelectionListener {

    private static final long serialVersionUID = 5463656526630255326L;

    private IIndexMapper      rowMapper;
    private ISelectable       viewSelectable;

    /**
     * Constructs a new <code>SelectionModelListener</code> instance.
     * 
     * @param viewSelectable
     *            the selectable to forward the changes to.
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
    public void valueChanged(ListSelectionEvent e) {
      ULCListSelectionModel sm = (ULCListSelectionModel) e.getSource();
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
}
