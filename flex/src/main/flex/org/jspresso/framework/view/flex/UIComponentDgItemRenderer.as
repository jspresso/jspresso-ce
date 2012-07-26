/**
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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
  import mx.controls.DataGrid;
  import mx.controls.listClasses.BaseListData;
  import mx.controls.listClasses.IDropInListItemRenderer;
  
  import org.jspresso.framework.gui.remote.RComponent;
  import org.jspresso.framework.state.remote.RemoteCompositeValueState;
  import org.jspresso.framework.state.remote.RemoteValueState;

  public class UIComponentDgItemRenderer extends RemoteValueDgItemEditor implements IDropInListItemRenderer {
    
    private var _viewFactory:DefaultFlexViewFactory;
    private var _remoteComponent:RComponent;
    private var _toolTipIndex:int;
    private var _backgroundIndex:int;
    private var _foregroundIndex:int;
    private var _listData:BaseListData;

    private var _valueChangeListener:ChangeWatcher;
    
    public function UIComponentDgItemRenderer() {
      //default constructor.
    }

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
    
    public function set toolTipIndex(value:int):void {
      _toolTipIndex = value;
    }
    public function get toolTipIndex():int {
      return _toolTipIndex;
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
	    if(_valueChangeListener != null) {
        _valueChangeListener.reset(cellValueState);
        refresh(cellValueState.value);
      } else {
        _valueChangeListener = BindingUtils.bindSetter(refresh, cellValueState, "value", true);
      }
      if(remoteComponent && toolTipIndex >= 0) {
        var toolTipValue:Object = ((data as RemoteCompositeValueState).children[toolTipIndex] as RemoteValueState).value;
        if(toolTipValue != null) {
          toolTip = toolTipValue.toString();
        } else {
          toolTip = null;
        }
      }
      
      if(listData.owner is DataGrid) {
        var dg:DataGrid = listData.owner as DataGrid;
        if(backgroundIndex >= 0) {
          var backgroundValue:Object = ((data as RemoteCompositeValueState).children[backgroundIndex] as RemoteValueState).value;
          if(backgroundValue) {
            setStyle("backgroundColor", backgroundValue);
            setStyle("backgroundAlpha", DefaultFlexViewFactory.getAlphaFromArgb(backgroundValue as String));
          } else {
            setStyle("backgroundColor", null);
            setStyle("backgroundAlpha", null);
          }
        }
        if(foregroundIndex >= 0) {
          var foregroundValue:Object = ((data as RemoteCompositeValueState).children[foregroundIndex] as RemoteValueState).value;
          if(foregroundValue) {
            setStyle("color", foregroundValue);
            setStyle("alpha", DefaultFlexViewFactory.getAlphaFromArgb(foregroundValue as String));
          } else {
            setStyle("color", null);
            setStyle("alpha", null);
          }
        }
      }
    }
    
  	protected function refresh(value:Object):void {
      state.value = value;
  	}

    public function get listData():BaseListData {
      return _listData;
    }
    
    public function set listData(value:BaseListData):void {
      _listData = value;
      invalidateProperties();
    }

    public function get backgroundIndex():int {
      return _backgroundIndex;
    }
    
    public function set backgroundIndex(value:int):void {
      _backgroundIndex = value;
    }
    
    public function get foregroundIndex():int {
      return _foregroundIndex;
    }
    
    public function set foregroundIndex(value:int):void {
      _foregroundIndex = value;
    }
  }
}