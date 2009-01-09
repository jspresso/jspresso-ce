package org.jspresso.framework.view.flex {
  import mx.controls.Image;
  import mx.controls.treeClasses.TreeItemRenderer;
  
  import org.jspresso.framework.state.remote.RemoteCompositeValueState;

  public class RemoteValueTreeItemRenderer extends TreeItemRenderer  {

		private var image:Image;

		public function RemoteValueTreeItemRenderer() {
		  image = new Image();
			addChild(image);
		}

		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			if(data is RemoteCompositeValueState) {
				image.x = icon.x;
				image.y = icon.y;
				image.width = icon.width;
				image.height = icon.height;
				image.source = DefaultFlexViewFactory.computeUrl((data as RemoteCompositeValueState).iconImageUrl);
				
				icon.visible = false;
			}
		}
  }
}