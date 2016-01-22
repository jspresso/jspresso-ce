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
package org.jspresso.framework.view.descriptor.basic;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicListDescriptor;
import org.jspresso.framework.util.gui.Icon;
import org.jspresso.framework.view.descriptor.ESelectionMode;
import org.jspresso.framework.view.descriptor.ICollectionViewDescriptor;
import org.jspresso.framework.view.descriptor.ICollectionViewDescriptorProvider;
import org.jspresso.framework.view.descriptor.ICompositeViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * This is the abstract base descriptor of all views used to display a
 * collection of elements. A collection view must be backed by a collection
 * descriptor model. Most of the time, the model will be a collection property
 * descriptor so that the collection to display is directly inferred from the
 * collection property value through the binding layer.
 *
 * @author Vincent Vandenschrick
 */
public abstract class BasicCollectionViewDescriptor extends BasicViewDescriptor
    implements ICollectionViewDescriptor {

  private IAction          itemSelectionAction;
  private IAction          rowAction;
  private ESelectionMode   selectionMode = ESelectionMode.MULTIPLE_INTERVAL_SELECTION;
  private IViewDescriptor  paginationViewDescriptor;
  private Boolean          autoSelectFirstRow;
  private IModelDescriptor selectionModelDescriptor;

  /**
   * {@inheritDoc}
   */
  @Override
  public Icon getIcon() {
    Icon icon = super.getIcon();
    if (icon == null
        && getModelDescriptor() instanceof ICollectionDescriptorProvider<?>) {
      IComponentDescriptor<?> elementDescriptor = ((ICollectionDescriptorProvider<?>) getModelDescriptor())
          .getCollectionDescriptor().getElementDescriptor();
      if (elementDescriptor != null) {
        icon = elementDescriptor.getIcon();
        setIcon(icon);
      }
    }
    return icon;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IAction getItemSelectionAction() {
    return itemSelectionAction;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IAction getRowAction() {
    return rowAction;
  }

  /**
   * Gets the selectionMode.
   *
   * @return the selectionMode.
   */
  @Override
  public ESelectionMode getSelectionMode() {
    return selectionMode;
  }

  /**
   * Registers an action that is implicitly triggered every time the selection
   * changes on the collection view UI peer. The context of the action execution
   * is the same as if the action was registered in the view action map.
   *
   * @param itemSelectionAction
   *          the itemSelectionAction to set.
   */
  public void setItemSelectionAction(IAction itemSelectionAction) {
    this.itemSelectionAction = itemSelectionAction;
  }

  /**
   * Registers an action that is implicitly triggered every time a row is
   * activated (e.g. double-clicked for current UI channels) on the collection
   * view UI peer. The context of the action execution is the same as if the
   * action was registered in the view action map.
   *
   * @param rowAction
   *          the rowAction to set.
   */
  public void setRowAction(IAction rowAction) {
    this.rowAction = rowAction;
  }

  /**
   * Sets the selection mode of the collection view. This is either a value of
   * the {@code ESelectionMode} enum or its equivalent string
   * representation :
   * <ul>
   * <li>{@code MULTIPLE_INTERVAL_SELECTION} for allowing any type of
   * selection</li>
   * <li>{@code MULTIPLE_INTERVAL_CUMULATIVE_SELECTION} for allowing any
   * type of selection with toggle behaviour</li>
   * <li>{@code SINGLE_INTERVAL_SELECTION} for allowing only contiguous
   * interval selection</li>
   * <li>{@code SINGLE_INTERVAL_CUMULATIVE_SELECTION} for allowing only
   * contiguous interval selection with toggle behaviour</li>
   * <li>{@code SINGLE_SELECTION} for allowing only a single item selection
   * </li>
   * <li>{@code SINGLE_CUMULATIVE_SELECTION} for allowing only a single
   * item selection with toggle behaviour</li>
   * </ul>
   * <p>
   * Default value is {@code ESelectionMode.MULTIPLE_INTERVAL_SELECTION},
   * i.e. any type of selection allowed.
   *
   * @param selectionMode
   *          the selectionMode to set.
   */
  public void setSelectionMode(ESelectionMode selectionMode) {
    this.selectionMode = selectionMode;
  }

  /**
   * Gets the paginationViewDescriptor.
   *
   * @return the paginationViewDescriptor.
   */
  @Override
  public IViewDescriptor getPaginationViewDescriptor() {
    return paginationViewDescriptor;
  }

  /**
   * Configures a view that will control the pagination (if any) on this
   * collection view. When constructed, the collection view will be decorated
   * with the pagination view. The pagination view will be bound to the same
   * model as the one providing the collection of the collection view.
   *
   * @param paginationViewDescriptor
   *          the paginationViewDescriptor to set.
   */
  public void setPaginationViewDescriptor(
      IViewDescriptor paginationViewDescriptor) {
    this.paginationViewDescriptor = paginationViewDescriptor;
  }

  /**
   * Gets the autoSelectFirstRow.
   *
   * @return the autoSelectFirstRow.
   */
  @Override
  public boolean isAutoSelectFirstRow() {
    if (autoSelectFirstRow != null) {
      return autoSelectFirstRow;
    }
    return getSelectionMode() != ESelectionMode.MULTIPLE_INTERVAL_CUMULATIVE_SELECTION
        && getSelectionMode() != ESelectionMode.SINGLE_INTERVAL_CUMULATIVE_SELECTION
        && getSelectionMode() != ESelectionMode.SINGLE_CUMULATIVE_SELECTION;
  }

  /**
   * Configures the default selection that gets applied when the content of the
   * collection view changes. Whenever set to {@code true}, the 1st row
   * will be automatically selected, whereas nothing happens when set to false.
   * <p>
   * The default value depends on the selection mode of the collection view.
   * When a cumulative selection mode is used, {@code autoSelectFirstRow}
   * defaults to {@code false}. It defaults to {@code true} otherwise.
   *
   * @param autoSelectFirstRow
   *          the autoSelectFirstRow to set.
   */
  public void setAutoSelectFirstRow(boolean autoSelectFirstRow) {
    this.autoSelectFirstRow = autoSelectFirstRow;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public void setModelDescriptor(IModelDescriptor modelDescriptor) {
    IModelDescriptor actualModelDescriptor = null;
    if (modelDescriptor instanceof ICollectionDescriptorProvider<?>) {
      actualModelDescriptor = modelDescriptor;
    } else if (modelDescriptor instanceof IComponentDescriptor<?>) {
      actualModelDescriptor = new BasicListDescriptor<>();
      ((BasicListDescriptor<Object>) actualModelDescriptor)
          .setElementDescriptor((IComponentDescriptor<Object>) modelDescriptor);
      ((BasicListDescriptor<Object>) actualModelDescriptor)
          .setName("ROOT_COLLECTION");
    }
    super.setModelDescriptor(actualModelDescriptor);
  }

  /**
   * Dig the view descriptor to extract the main collection view.
   *
   * @param viewDescriptor
   *          the module projected view descriptor.
   * @return the main collection view descriptor.
   */
  public static ICollectionViewDescriptor extractMainCollectionView(
      IViewDescriptor viewDescriptor) {
    ICollectionViewDescriptor mainCollectionView = null;
    if (viewDescriptor instanceof ICollectionViewDescriptorProvider) {
      mainCollectionView = ((ICollectionViewDescriptorProvider) viewDescriptor)
          .getCollectionViewDescriptor();
    } else if (viewDescriptor instanceof ICompositeViewDescriptor
        && ((ICompositeViewDescriptor) viewDescriptor)
            .getChildViewDescriptors() != null) {
      for (int i = 0; mainCollectionView == null
          && i < ((ICompositeViewDescriptor) viewDescriptor)
              .getChildViewDescriptors().size(); i++) {
        mainCollectionView = extractMainCollectionView(((ICompositeViewDescriptor) viewDescriptor)
            .getChildViewDescriptors().get(i));
      }
    }
    return mainCollectionView;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ICollectionViewDescriptor getCollectionViewDescriptor() {
    return this;
  }

  /**
   * Gets the selectionModelDescriptor.
   *
   * @return the selectionModelDescriptor.
   */
  @Override
  public IModelDescriptor getSelectionModelDescriptor() {
    return selectionModelDescriptor;
  }

  /**
   * Sets the selectionModelDescriptor.
   *
   * @param selectionModelDescriptor
   *          the selectionModelDescriptor to set.
   */
  public void setSelectionModelDescriptor(
      IModelDescriptor selectionModelDescriptor) {
    this.selectionModelDescriptor = selectionModelDescriptor;
  }
}
