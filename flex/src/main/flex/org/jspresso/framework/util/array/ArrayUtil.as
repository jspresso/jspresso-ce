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

package org.jspresso.framework.util.array {
  import mx.collections.IList;
  import mx.core.ClassFactory;
  import mx.events.CollectionEvent;
  import mx.events.CollectionEventKind;
  import mx.events.PropertyChangeEvent;
  import mx.utils.ObjectUtil;
  
  import org.jspresso.framework.util.remote.IRemotePeer;
  import org.jspresso.framework.util.remote.registry.IRemotePeerRegistry;
  
  public class ArrayUtil {
    
    public static function areUnorderedArraysEqual(a1:Array, a2:Array):Boolean {
      if((a1 && !a2) || (a2 && !a1)) {
        return false;
      } else if(a1.length != a2.length) {
        return false;
      } else {
        for each (var e:Object in a1) {
          if(arrayIndexOf(a2,e) < 0) {
            return false;
          }
        }
      }
      return true;
    }

    public static function arrayContains(arr:Array, element:Object):Boolean {
      return arrayIndexOf(arr, element) >= 0;
    }
    
    public static function arrayIndexOf(arr:Array, element:Object):int {
      for(var i:int = 0; i < arr.length; i++) {
        var arrElement:Object = arr[i];
        if(element is IRemotePeer) {
          if(arrElement is IRemotePeer) {
            if(ObjectUtil.compare((arrElement as IRemotePeer).guid, (element as IRemotePeer).guid) == 0) {
              return i;
            }
          }
        } else if(ObjectUtil.compare(element, arrElement) == 0) {
          return i;
        }
      }
      return -1;
    }

    public static function mirrorCollectionViews(source:IList,
                                                 target:IList,
                                                 targetElementFactory:ClassFactory,
                                                 remotePeerRegistry:IRemotePeerRegistry = null):void {
      for(var i:int = 0; i < source.length; i++) {
        var element:Object = cacheCreate(source[i], targetElementFactory);
        target.addItem(element);
        attachItemUpdateListener(element, target);
      }
      source.addEventListener(CollectionEvent.COLLECTION_CHANGE,
        function(event:CollectionEvent):void {
          var item:Object;
          if(event.kind == CollectionEventKind.ADD) {
            for each (item in event.items) {
              var addedElement:Object = cacheCreate(item, targetElementFactory);
              target.addItem(addedElement);
              attachItemUpdateListener(addedElement, target);
            }
          } else if(event.kind == CollectionEventKind.REMOVE) {
            for each (item in event.items) {
              var removedElement:Object;
              if(item is IRemotePeer) {
                removedElement = item;
              } else {
                removedElement = cacheCreate(item, targetElementFactory);
              }
              target.removeItemAt(arrayIndexOf(target.toArray(),removedElement));
            }
          } else if(event.kind == CollectionEventKind.REPLACE) {
            for each (item in event.items) {
              var oldElement:Object;
              var oldItem:Object = (item as PropertyChangeEvent).oldValue;
              if(oldItem is IRemotePeer) {
                oldElement = oldItem;
              } else {
                oldElement = cacheCreate(oldItem, targetElementFactory);
              }
              var newElement:Object = cacheCreate((item as PropertyChangeEvent).newValue, targetElementFactory);
              target.setItemAt(newElement, arrayIndexOf(target.toArray(), oldElement));
              attachItemUpdateListener(newElement, target);
            }
          } else if(event.kind == CollectionEventKind.RESET) {
            // could be finer.
            target.removeAll();
            for each (item in (event.currentTarget as IList).toArray()) {
              var resetElement:Object =cacheCreate((item as PropertyChangeEvent).oldValue, targetElementFactory);
              target.addItem(resetElement);
              attachItemUpdateListener(resetElement, target);
            }
          }
        });
    }
    
    private static function cacheCreate(delegate:Object, targetElementFactory:ClassFactory, remotePeerRegistry:IRemotePeerRegistry = null):Object {
      var target:Object = null;
      if(delegate is IRemotePeer && remotePeerRegistry) {
        target = remotePeerRegistry.getRegistered((delegate as IRemotePeer).guid);
      }
      if(target == null) {
        target = targetElementFactory.newInstance();
        target['delegate'] = delegate;
        if(target is IRemotePeer && remotePeerRegistry) {
          remotePeerRegistry.register(target as IRemotePeer);
        }
      }
      return target;
    }
    
    public static function attachItemUpdateListener(element:Object, collection:IList):void {
      var itemUpdated:Function = function(pce:PropertyChangeEvent):void {
        collection.itemUpdated(pce.source, pce.property, pce.oldValue, pce.newValue);
      };
      element.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, itemUpdated);
    }
  }
}