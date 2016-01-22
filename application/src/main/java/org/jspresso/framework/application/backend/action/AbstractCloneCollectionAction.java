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
package org.jspresso.framework.application.backend.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.binding.ICollectionConnector;

/**
 * An action used duplicate a collection of domain objects. Cloning an entity
 * should result in adding it to the collection the action was triggered on.
 * Components to clone are retrieved from the context using the selected indices
 * of the model collection connector. Actual cloning of components is left to
 * concrete implementations that must implement :
 *
 * <pre>
 * protected abstract Object cloneElement(Object element,
 *     Map&lt;String, Object&gt; context)
 * </pre>
 *
 * @author Vincent Vandenschrick
 */
public abstract class AbstractCloneCollectionAction extends
    AbstractAddCollectionToMasterAction {

  /**
   * Clones an element.
   *
   * @param element
   *          the element to clone.
   * @param context
   *          the action context.
   * @return the cloned element.
   */
  protected abstract Object cloneElement(Object element,
      Map<String, Object> context);

  /**
   * {@inheritDoc}
   */
  @Override
  protected List<?> getAddedComponents(Map<String, Object> context) {
    int[] selectedIndices = getSelectedIndices(context);
    ICollectionConnector collectionConnector = getModelConnector(context);
    if (selectedIndices == null || selectedIndices.length == 0
        || collectionConnector == null) {
      return null;
    }
    List<Object> elementClones = new ArrayList<>();
    for (int selectedIndice : selectedIndices) {
      Object element = collectionConnector
          .getChildConnector(selectedIndice).getConnectorValue();
      elementClones.add(cloneElement(element, context));
    }
    return elementClones;
  }

}
