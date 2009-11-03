/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
import org.jspresso.framework.util.collection.ObjectEqualityMap;


/**
 * An action used duplicate a collection of map domain objects. Cloning a map
 * should result in adding it to the collection the action was triggered on.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class CloneMapCollectionAction extends AddMapToMasterAction {

  /**
   * Clones a map.
   * 
   * @param element
   *            the map to clone.
   * @param context
   *            the action context.
   * @return the cloned map.
   */
  protected Object cloneElement(Object element, Map<String, Object> context) {
    return new ObjectEqualityMap<Object, Object>((Map<?, ?>) element);
  }

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
    List<Object> elementClones = new ArrayList<Object>();
    for (int i = 0; i < selectedIndices.length; i++) {
      Object element = collectionConnector
          .getChildConnector(selectedIndices[i]).getConnectorValue();
      elementClones.add(cloneElement(element, context));
    }
    return elementClones;
  }
}
