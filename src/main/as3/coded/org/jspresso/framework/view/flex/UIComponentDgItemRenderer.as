/**
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
 */

package org.jspresso.framework.view.flex {
  import mx.binding.utils.BindingUtils;
  import mx.binding.utils.ChangeWatcher;
  
  import org.jspresso.framework.gui.remote.RComponent;
  import org.jspresso.framework.state.remote.RemoteCompositeValueState;
  import org.jspresso.framework.state.remote.RemoteValueState;

  public class UIComponentDgItemRenderer extends RemoteValueDgItemEditor {
    
    private var _viewFactory:DefaultFlexViewFactory;
    private var _remoteComponent:RComponent;

    private var valueChangeListener:ChangeWatcher;
    
    public function set viewFactory(value:DefaultFlexViewFactory):void {
      _viewFactory = value;
      updateComponents();
    }
    public function get viewFactory():DefaultFlexViewFactory {
      return _viewFactory;
    }
    
    public function set remoteComponent(value:RComponent):void {
      _remoteComponent = value;
      updateComponents();
    }
    public function get remoteComponent():RComponent {
      return _remoteComponent;
    }
    
    private function updateComponents():void {
      if(viewFactory != null && remoteComponent != null) {
        if(state == null || editor == null) {
          state = new RemoteValueState();
          remoteComponent.state = state;
          editor = viewFactory.createComponent(remoteComponent, false);
        }
      }
    }
    
    override public function set data(value:Object):void {
      super.data = value;
      var cellValueState:RemoteValueState;
      if(index != -1 && value is RemoteCompositeValueState) {
        cellValueState = (value as RemoteCompositeValueState).children[index] as RemoteValueState;
      } else if(value is RemoteValueState) {
        cellValueState = value as RemoteValueState;
      }
	    if(valueChangeListener != null) {
	      valueChangeListener.unwatch();
	    }
	    valueChangeListener = BindingUtils.bindSetter(refresh, cellValueState, "value", true);
    }
    
  	protected function refresh(value:Object):void {
      state.value = value;
  	}
  }
}