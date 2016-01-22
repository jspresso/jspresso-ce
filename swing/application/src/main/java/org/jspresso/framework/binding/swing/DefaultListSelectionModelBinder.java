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
package org.jspresso.framework.binding.swing;

import java.awt.Rectangle;
import java.util.Arrays;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jspresso.framework.util.event.ISelectable;
import org.jspresso.framework.util.event.ISelectionChangeListener;
import org.jspresso.framework.util.event.SelectionChangeEvent;
import org.jspresso.framework.util.gui.IIndexMapper;
import org.jspresso.framework.util.swing.SwingUtil;

/**
 * Default implementation of {@code IListSelectionModelBinder}.
 *
 * @author Vincent Vandenschrick
 */
public class DefaultListSelectionModelBinder implements
    IListSelectionModelBinder {

  private static int[] getSelectedIndices(ListSelectionModel sm) {
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
  @Override
  public void bindSelectionModel(JComponent collectionComponent,
      ISelectable selectable, ListSelectionModel selectionModel,
      IIndexMapper rowMapper) {
    selectionModel.addListSelectionListener(new SelectionModelListener(
        selectable, rowMapper));
    selectable.addSelectionChangeListener(new SelectionChangeListener(
        collectionComponent, selectionModel, rowMapper));
  }

  private static final class SelectionChangeListener implements
      ISelectionChangeListener {

    private final JComponent         collectionComponent;
    private final IIndexMapper       rowMapper;
    private final ListSelectionModel selectionModel;

    /**
     * Constructs a new {@code SelectionChangeListener} instance.
     *
     * @param collectionComponent
     *          the collection component to bind the selection model with.
     * @param selectionModel
     *          the selection model to forward the changes to.
     * @param rowMapper the row mapper.
     */
    public SelectionChangeListener(JComponent collectionComponent,
        ListSelectionModel selectionModel, IIndexMapper rowMapper) {
      this.collectionComponent = collectionComponent;
      this.selectionModel = selectionModel;
      this.rowMapper = rowMapper;
    }

    /**
     * Forwards selectable selection to model selection.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void selectionChange(final SelectionChangeEvent evt) {
      SwingUtil.updateSwingGui(new Runnable() {

        @Override
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
          } else {
            selectionModel.addSelectionInterval(nextRangeMin, nextRangeMax);
          }
          if (evt.getLeadingIndex() >= 0) {
            Rectangle visibleCell = null;
            if (collectionComponent instanceof JTable) {
              visibleCell = ((JTable) collectionComponent).getCellRect(
                  evt.getLeadingIndex(), 0, true);
            } else if (collectionComponent instanceof JList) {
              visibleCell = ((JList<?>) collectionComponent).getCellBounds(
                  evt.getLeadingIndex(), evt.getLeadingIndex());
            }
            if (visibleCell != null) {
              collectionComponent.scrollRectToVisible(visibleCell);
            }
          }
        }
      });
    }
  }

  private static final class SelectionModelListener implements
      ListSelectionListener {

    private final IIndexMapper rowMapper;
    private final ISelectable  viewSelectable;

    /**
     * Constructs a new {@code SelectionModelListener} instance.
     *
     * @param viewSelectable
     *          the selectable to forward the changes to.
     * @param rowMapper the row mapper.
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
    @Override
    public void valueChanged(ListSelectionEvent e) {
      if (e.getValueIsAdjusting()) {
        return;
      }
      ListSelectionModel sm = (ListSelectionModel) e.getSource();
      int[] viewIndices = getSelectedIndices(sm);
      int viewLeadingIndex = sm.getLeadSelectionIndex();
      int[] modelIndices;
      int modelLeadingIndex;
      if (viewIndices.length > 0) {
        if (rowMapper != null) {
          modelIndices = new int[viewIndices.length];
          for (int i = 0; i < viewIndices.length; i++) {
            modelIndices[i] = rowMapper.modelIndex(viewIndices[i]);
          }
          modelLeadingIndex = rowMapper.modelIndex(viewLeadingIndex);
        } else {
          modelIndices = viewIndices;
          modelLeadingIndex = viewLeadingIndex;
        }
      } else {
        modelIndices = viewIndices;
        modelLeadingIndex = viewLeadingIndex;
      }
      viewSelectable.setSelectedIndices(modelIndices, modelLeadingIndex);
    }
  }
}
