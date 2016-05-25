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
package org.jspresso.framework.view.flex {

import flash.display.DisplayObject;
import flash.events.MouseEvent;

import mx.collections.ICollectionView;
import mx.collections.IList;
import mx.collections.IViewCursor;
import mx.collections.ListCollectionView;
import mx.core.Container;
import mx.core.UIComponent;
import mx.core.UIComponent;
import mx.events.CollectionEvent;
import mx.events.CollectionEventKind;

import org.jspresso.framework.action.IActionHandler;

import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.gui.remote.RRepeater;
import org.jspresso.framework.state.remote.RemoteCompositeValueState;
import org.jspresso.framework.state.remote.RemoteValueState;

public class ViewRepeater {

  private var _container:Container;
  private var _remoteRepeater:RRepeater;
  private var _viewFactory:DefaultFlexViewFactory;
  private var _actionHandler:IActionHandler;
  private var _dataProvider:ListCollectionView;

  private var _componentTank:Object;

  public function ViewRepeater(container:Container, remoteRepeater:RRepeater, viewFactory:DefaultFlexViewFactory,
                               actionHandler:IActionHandler) {
    _container = container;
    _remoteRepeater = remoteRepeater;
    _viewFactory = viewFactory;
    _actionHandler = actionHandler;
    _componentTank = {};
  }


  public function set dataProvider(value:ListCollectionView):void {
    if (_dataProvider) {
      _dataProvider.removeEventListener(CollectionEvent.COLLECTION_CHANGE, rebindDataProvider);
      _dataProvider = null;
    }

    _dataProvider = value;

    if (_dataProvider) {
      _dataProvider.addEventListener(CollectionEvent.COLLECTION_CHANGE, rebindDataProvider);
    }
    rebindDataProvider();
  }

  private function rebindDataProvider(collectionEvent:CollectionEvent = null):void {
    if (collectionEvent && collectionEvent.kind == CollectionEventKind.UPDATE) {
      return;
    }
    _container.removeAllChildren();
    for (var i:int = 0; i < _dataProvider.length; i++) {
      var state:RemoteValueState = _dataProvider.getItemAt(i) as RemoteValueState;
      var component:UIComponent = _componentTank[state.guid];
      if (!component) {
        var stateMapping:Object = {};
        mapStates(_remoteRepeater.viewPrototype, state, stateMapping);
        _remoteRepeater.transferToState(stateMapping);
        component = _viewFactory.createComponent(_remoteRepeater.repeated, false);
        _componentTank[state.guid] = component;
        component.percentWidth = 100;
        component.addEventListener(MouseEvent.CLICK, function (evt:MouseEvent):void {
          var index:int = _container.getChildIndex(evt.currentTarget as DisplayObject);
          if (index >= 0) {
            (_remoteRepeater.state as RemoteCompositeValueState).leadingIndex = index;
            (_remoteRepeater.state as RemoteCompositeValueState).selectedIndices = [index];
          } else {
            (_remoteRepeater.state as RemoteCompositeValueState).leadingIndex = -1;
            (_remoteRepeater.state as RemoteCompositeValueState).selectedIndices = [];
          }
        });
        if (_remoteRepeater.rowAction) {
          component.doubleClickEnabled = true;
          component.addEventListener(MouseEvent.DOUBLE_CLICK, function (evt:MouseEvent):void {
            _actionHandler.execute(_remoteRepeater.rowAction)
          });
        }
      }
      _container.addChild(component);
    }
  }

  private function mapStates(fromState:RemoteValueState, toState:RemoteValueState, stateMapping:Object):void {
    stateMapping[fromState.guid] = toState;
    if (fromState is RemoteCompositeValueState) {
      var fromChildren:ListCollectionView = (fromState as RemoteCompositeValueState).children;
      for (var i:int = 0; i < fromChildren.length; i++) {
        var toChildren:ListCollectionView = (toState as RemoteCompositeValueState).children;
        if (i < toChildren.length) {
          mapStates(fromChildren.getItemAt(i) as RemoteValueState, toChildren.getItemAt(i) as RemoteValueState,
                    stateMapping);
        }
      }
    }
  }
}
}
