/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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

import java.util.ArrayList;
import java.util.List;

import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.ITableViewDescriptor;

/**
 * This descriptor is used to implement a table view. This is certainly the most
 * commonly used collection descriptor in Jspresso. A table view displays a
 * collection of components (one row per component in the collection) detailed
 * by a set of properties (one column per displayed component property).
 * <p>
 * The table view will automatically adapt its columuns depending on the
 * underlying property descriptors, e.g. :
 * <ul>
 * <li>columns for read-only properties won't be editable</li>
 * <li>columns that are assigned writability gates will compute the editability
 * of their cells based on each cell's gates</li>
 * <li>columns will adapt their renderer/editor based on the underlying property
 * type, e.g. a calendar component will be used for dates</li>
 * <li>column titles will be filled with property names translations based on
 * the user locale</li>
 * <li>mandatory properties will be visually indicated</li>
 * <li>...</li>
 * </ul>
 * A table view provides sensible defaults regarding its configuration, but it
 * can be refined using either the simple <code>renderedProperties</code> or the
 * more advanced yet lot more powerful <code>columnViewDescriptors</code>
 * properties.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicTableViewDescriptor extends BasicCollectionViewDescriptor
    implements ITableViewDescriptor {

  private List<IPropertyViewDescriptor> columnViewDescriptors;
  private boolean                       horizontallyScrollable;
  private List<String>                  renderedProperties;
  private boolean                       sortable;
  private IDisplayableAction            sortingAction;

  /**
   * Constructs a new <code>BasicTableViewDescriptor</code> instance.
   */
  public BasicTableViewDescriptor() {
    horizontallyScrollable = true;
    sortable = true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<IPropertyViewDescriptor> getColumnViewDescriptors() {
    ICollectionDescriptorProvider<?> modelDescriptor = ((ICollectionDescriptorProvider<?>) getModelDescriptor());
    IComponentDescriptor<?> rowModelDescriptor = modelDescriptor
        .getCollectionDescriptor().getElementDescriptor();
    List<IPropertyViewDescriptor> declaredPropertyViewDescriptors = columnViewDescriptors;
    if (declaredPropertyViewDescriptors == null) {
      List<String> viewRenderedProperties = getRenderedProperties();
      if (modelDescriptor instanceof ICollectionPropertyDescriptor<?>
          && ((ICollectionPropertyDescriptor<?>) modelDescriptor)
              .getReverseRelationEnd() != null) {
        viewRenderedProperties
            .remove(((ICollectionPropertyDescriptor<?>) modelDescriptor)
                .getReverseRelationEnd().getName());
      }
      declaredPropertyViewDescriptors = new ArrayList<IPropertyViewDescriptor>();
      for (String renderedProperty : viewRenderedProperties) {
        BasicPropertyViewDescriptor columnDescriptor = new BasicPropertyViewDescriptor();
        columnDescriptor.setName(renderedProperty);
        columnDescriptor.setModelDescriptor(rowModelDescriptor
            .getPropertyDescriptor(renderedProperty));
        declaredPropertyViewDescriptors.add(columnDescriptor);
      }
    }
    List<IPropertyViewDescriptor> actualPropertyViewDescriptors = new ArrayList<IPropertyViewDescriptor>();
    for (IPropertyViewDescriptor propertyViewDescriptor : declaredPropertyViewDescriptors) {
      IModelDescriptor columnModelDescriptor = propertyViewDescriptor.getModelDescriptor();
      if (columnModelDescriptor == null) {
        if (propertyViewDescriptor.getName() != null) {
          columnModelDescriptor = rowModelDescriptor
              .getPropertyDescriptor(propertyViewDescriptor.getName());
        }
      }
      // Collection properties are not supported as columns
      if (columnModelDescriptor != null
          && !(columnModelDescriptor instanceof ICollectionPropertyDescriptor<?>)) {
        actualPropertyViewDescriptors.addAll(PropertyViewDescriptorHelper
            .explodeComponentReferences(propertyViewDescriptor,
                rowModelDescriptor));
      }
    }
    return actualPropertyViewDescriptors;
  }

  /**
   * Gets the sortingAction.
   * 
   * @return the sortingAction.
   */
  @Override
  public IDisplayableAction getSortingAction() {
    return sortingAction;
  }

  /**
   * Gets the horizontallyScrollable.
   * 
   * @return the horizontallyScrollable.
   */
  @Override
  public boolean isHorizontallyScrollable() {
    return horizontallyScrollable;
  }

  /**
   * Gets the sortable.
   * 
   * @return the sortable.
   */
  @Override
  public boolean isSortable() {
    return sortable;
  }

  /**
   * This property allows for configuring the columns of the table view in a
   * very customizable manner, thus overriding the model descriptor defaults.
   * Each property view descriptor copntained in the list describes a table
   * column that will be rendered in the UI accordingly.
   * <p>
   * For instance, a writable property can be made specifically read-only on
   * this table view by specifying its column property view descriptor
   * read-only. In that case, the model remains untouched and only the view is
   * impacted.
   * <p>
   * Following the same scheme, you can assign a list of writability gates on a
   * column to introduce dynamic cell editability on the view without modifying
   * the model.
   * <p>
   * A last, yet important, example of column view descriptor usage is the
   * role-based column set configuration. Whenever you want a column to be
   * available only for certain user roles (profiles), you can configure a
   * column property view descriptor with a list of granted roles. If the user
   * doesn't have the column(s)required role, the forbidden columns simply won't
   * be displayed. This allows for high authorization-based versatility.
   * <p>
   * There are many other usages of defining column property view descriptors
   * all of them being linked to customizing the table columns without impacting
   * the model.
   * 
   * @param columnViewDescriptors
   *          the columnViewDescriptors to set.
   */
  public void setColumnViewDescriptors(
      List<IPropertyViewDescriptor> columnViewDescriptors) {
    this.columnViewDescriptors = columnViewDescriptors;
  }

  /**
   * This property allows to define the table horizontal scrolling behaviour.
   * Whenever it is set to false, the corresponding table UI component will
   * adapt its columns to fit the available horizontal space.
   * <p>
   * Default value is <code>true</code>, i.e. table columns will have their
   * default size and tha table will scroll horizontally as needed.
   * 
   * @param horizontallyScrollable
   *          the horizontallyScrollable to set.
   */
  public void setHorizontallyScrollable(boolean horizontallyScrollable) {
    this.horizontallyScrollable = horizontallyScrollable;
  }

  /**
   * This is somehow a shortcut to using the <code>columnViewDescriptors</code>
   * property. Instead of providing a full-blown list of property view
   * descriptors to configure the table columns, you just pass-in a list of
   * property names. Table columns are then created from this list, keeping
   * model defaults for all column characteristics.
   * <p>
   * Whenever the property value is <code>null</code> (default), the column list
   * is determined from the collection element component descriptor
   * <code>renderedProperties</code> property.
   * 
   * @param renderedProperties
   *          the renderedProperties to set.
   */
  public void setRenderedProperties(List<String> renderedProperties) {
    this.renderedProperties = renderedProperties;
  }

  /**
   * This property allows to define the table horizontal sorting behaviour.
   * Whenever it is set to false, the corresponding table UI component will not
   * allow manual sorting of its rows.
   * <p>
   * Default value is <code>true</code>, i.e. table allows for its rows to be
   * sorted.
   * 
   * @param sortable
   *          the sortable to set.
   */
  public void setSortable(boolean sortable) {
    this.sortable = sortable;
  }

  /**
   * Configures the action to be activated when a sort is triggered by the user.
   * It should be used with caution and rarely be overriden from the default.
   * 
   * @param sortingAction
   *          the sortingAction to set.
   */
  public void setSortingAction(IDisplayableAction sortingAction) {
    this.sortingAction = sortingAction;
  }

  /**
   * Gets the renderedProperties.
   * 
   * @return the renderedProperties.
   */
  private List<String> getRenderedProperties() {
    if (renderedProperties == null) {
      renderedProperties = ((ICollectionDescriptorProvider<?>) getModelDescriptor())
          .getCollectionDescriptor().getElementDescriptor()
          .getRenderedProperties();
    }
    return renderedProperties;
  }

}
