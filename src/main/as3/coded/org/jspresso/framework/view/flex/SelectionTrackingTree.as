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
  import flash.events.Event;
  
  import mx.controls.Tree;
  import mx.events.CollectionEvent;
  import mx.events.CollectionEventKind;
  import mx.events.PropertyChangeEvent;
  import mx.events.PropertyChangeEventKind;
  
  import org.jspresso.framework.state.remote.RemoteCompositeValueState;
  import org.jspresso.framework.util.array.ArrayUtil;
  
    
  public class SelectionTrackingTree extends Tree {
    
    override protected function collectionChangeHandler(event:Event):void {
      if(event is CollectionEvent) {
        var collEvent:CollectionEvent = event as CollectionEvent;
        if(collEvent.kind == CollectionEventKind.UPDATE) {
          if(collEvent.items) {
            if(collEvent.items.length > 0 && collEvent.items[0] is PropertyChangeEvent) {
              var pcEvent:PropertyChangeEvent = collEvent.items[0] as PropertyChangeEvent;
              if(pcEvent.kind == PropertyChangeEventKind.UPDATE
                 && pcEvent.property == "selectedIndices"
                 && pcEvent.source is RemoteCompositeValueState) {
                var changedState:RemoteCompositeValueState = pcEvent.source as RemoteCompositeValueState;
                var newlySelectedItems:Array = new Array();
                if(changedState.selectedIndices) {
                  for each(var index:int in changedState.selectedIndices) {
                    newlySelectedItems.push(changedState.children[index]);
                  }
                }
                if(!ArrayUtil.areUnorderedArraysEqual(selectedItems, newlySelectedItems)) {
                  if(newlySelectedItems.length > 0 && !isItemOpen(changedState)) {
                    var newOpenItems:Array = new Array().concat(openItems);
                    newOpenItems.push(changedState);
                    openItems = newOpenItems;
                  }
                  selectedItems = newlySelectedItems;
                }
              }
            }
          }
        }
      }
      super.collectionChangeHandler(event);
    }
  }
}