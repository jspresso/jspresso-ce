package org.jspresso.framework.view
{
  import mx.controls.Image;
  import mx.controls.treeClasses.TreeItemRenderer;

  public class RemoteValueTreeItemRenderer extends TreeItemRenderer  {

    public function RemoteValueTreeItemRenderer() {
      super();
      var iconImage:Image = new Image();
      iconImage.source = "http://www.iconclinical.com/images/home/img.jpg";
      icon = iconImage;
    }
    
    override public function set data(value:Object):void {
      super.data = value;
//      if (value != null) {
//        text = value[DataGridListData(listData).dataField];
//        if(Number(text) > 100) {
//          setStyle("backgroundColor", 0xFF0000);
//        }
//      } else {
//        // If value is null, clear text.
//        text= "";
//      }
//      super.invalidateDisplayList();
    }
  }
}