/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.view.remote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.binding.remote.state.IRemoteStateOwner;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.view.ICompositeView;
import org.jspresso.framework.view.IMapView;
import org.jspresso.framework.view.IView;

/**
 * View state extractor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RemoteViewStateExtractor {

  /**
   * Extracts a view state from a view.
   * 
   * @param view
   *          the view to extract the state of.
   * @return the extracted view state.
   */
  public RemoteViewState extractViewState(IView<RComponent> view) {
    RemoteViewState viewState = createViewState(view);
    viewState.setDescriptor(view.getDescriptor());
    viewState.setPeer(view.getPeer());
    viewState.setValueState(((IRemoteStateOwner) view.getConnector())
        .getState());
    if (view instanceof IMapView<?>) {
      Map<String, RemoteViewState> childrenMapStates = new HashMap<String, RemoteViewState>();
      for (Map.Entry<String, IView<RComponent>> childViewEntry : ((IMapView<RComponent>) view)
          .getChildrenMap().entrySet()) {
        childrenMapStates.put(childViewEntry.getKey(),
            extractViewState(childViewEntry.getValue()));
      }
      ((RemoteMapViewState) viewState).setChildrenMap(childrenMapStates);
    } else if (view instanceof ICompositeView<?>) {
      List<RemoteViewState> childrenStates = new ArrayList<RemoteViewState>();
      for (IView<RComponent> childView : ((ICompositeView<RComponent>) view)
          .getChildren()) {
        childrenStates.add(extractViewState(childView));
      }
    }
    return viewState;
  }

  private RemoteViewState createViewState(IView<RComponent> view) {
    if (view instanceof IMapView<?>) {
      return new RemoteMapViewState();
    } else if (view instanceof ICompositeView<?>) {
      return new RemoteCompositeViewState();
    } else {
      return new RemoteViewState();
    }
  }
}
