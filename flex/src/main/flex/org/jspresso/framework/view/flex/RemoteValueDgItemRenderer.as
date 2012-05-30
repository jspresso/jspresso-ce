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

  import flash.events.TextEvent;
  
  import mx.binding.utils.BindingUtils;
  import mx.binding.utils.ChangeWatcher;
  import mx.controls.DataGrid;
  import mx.controls.listClasses.BaseListData;
  import mx.controls.listClasses.ListData;
  import mx.controls.listClasses.ListItemRenderer;
  import mx.events.FlexEvent;
  import mx.formatters.Formatter;
  
  import org.jspresso.framework.action.IActionHandler;
  import org.jspresso.framework.gui.remote.RAction;
  import org.jspresso.framework.state.remote.RemoteCompositeValueState;
  import org.jspresso.framework.state.remote.RemoteValueState;
  import org.jspresso.framework.util.html.HtmlUtil;

  public class RemoteValueDgItemRenderer extends ListItemRenderer implements IColumnIndexProvider {
    
    private var _valueChangeListener:ChangeWatcher;
    private var _listData:BaseListData;
    private var _formatter:Formatter;
    private var _index:int;
    private var _selectable:Boolean;
    private var _action:RAction;
    private var _actionHandler:IActionHandler;
    
    public function RemoteValueDgItemRenderer() {
      _index = -1;
      addEventListener(FlexEvent.CREATION_COMPLETE, function(event:FlexEvent):void {
        label.selectable = _selectable;
      });
    }
    
  	override public function set listData(value:BaseListData):void {
   	  updateLabel(data, value);
   	  if(value) {
     	  if(value is ListData) {
     	    super.listData = value;
     	  } else {
    	    super.listData = new ListData(value.label,
    	     null,
           null,
           value.uid,
           value.owner,
           value.rowIndex,
           value.columnIndex);
    	  }
   	  } else {
   	    super.listData = value;
  	  }
  	  _listData = value;
  	}
  	
    public function set index(value:int):void {
      _index = value;
    }
    public function get index():int {
      return _index;
    }
    
    public function set selectable(value:Boolean):void {
      _selectable = value;
      if(label) {
        label.selectable = true;
      }
    }
    public function get selectable():Boolean {
      return _selectable;
    }
    
  	protected function set listDataIcon(value:Class):void {
  	  if(super.listData != null) {
  	    (super.listData as ListData).icon = value;
  	  }
  	}

  	override public function get listData():BaseListData {
  	  return _listData;
  	}

  	public function set formatter(value:Formatter):void {
  	  _formatter = value;
  	}

  	override public function set data(value:Object):void	{
  	  updateLabel(value, listData);
  	  if(listData && super.listData) {
  	    super.listData.label = listData.label;
  	  }
  	  super.data = value;
  	}

  	protected function updateLabel(rendererData:Object, rendererListData:BaseListData):void {
  	  if(rendererData && rendererListData) {
  	    var cellValueState:RemoteValueState;
  	    if(rendererListData.owner is DataGrid) {
  	      cellValueState = ((rendererData as RemoteCompositeValueState).children[index] as RemoteValueState); 
  	    } else {
  	      cellValueState = ((rendererData as RemoteCompositeValueState).children[1] as RemoteValueState);
  	    }
  	    if(_valueChangeListener != null) {
          _valueChangeListener.reset(cellValueState);
          refresh(cellValueState.value);
        } else {
          _valueChangeListener = BindingUtils.bindSetter(refresh, cellValueState, "value", true);
        }
    	  rendererListData.label = computeLabel(cellValueState.value);
  	  }
  	}
  	
  	protected function computeLabel(cellValue:Object):String {
 	    var cellLabel:String;
	    if(cellValue != null) {
	      if(_formatter != null) {
	        cellLabel = _formatter.format(cellValue);
	      } else {
  	      cellLabel = cellValue.toString();
  	    }
  	  } else {
  	    cellLabel = null;
  	  }
      return cellLabel
  	}
  	
  	protected function refresh(cellValue:Object):void {
      listData.label = computeLabel(cellValue);
  	  if(listData && super.listData) {
  	    super.listData.label = listData.label;
  	  }
      invalidateProperties();
  	}
  	
  	protected override function commitProperties():void {
  	  super.commitProperties();
  	  var cellText:String = label.text;
      if(_action != null) {
        if(HtmlUtil.isHtml(cellText)) {
          cellText = HtmlUtil.convertHtmlEntities(cellText);
        }
        label.htmlText = "<u><a href='event:action'>" + cellText + "</a></u>";
      } else {
        if(HtmlUtil.isHtml(cellText)) {
          label.htmlText = HtmlUtil.convertHtmlEntities(cellText);
        }
      }
      if(index == 1 || index == -1) {
        if(data) {
          toolTip = (data as RemoteCompositeValueState).value as String;
        } else {
          toolTip = null;
        }
      } else {
        if(HtmlUtil.isHtml(cellText)) {
          // unconditional
          toolTip = cellText;
        } else {
          if(toolTip) {
            toolTip = cellText;
          } else {
            toolTip = null;
          }
        }
      }
  	}

    public function set action(value:RAction):void {
      _action = value;
      if(_action != null) {
        addEventListener(FlexEvent.CREATION_COMPLETE, function(event:FlexEvent):void {
          //label.selectable = true;
          label.addEventListener(flash.events.TextEvent.LINK, function(evt:TextEvent):void {
            // To ensure that the row is selected before the action gets executed.
            callLater(_actionHandler.execute, [_action]);
          });
        });
      }
    }
    
    public function set actionHandler(value:IActionHandler):void
    {
      _actionHandler = value;
    }


  }
}