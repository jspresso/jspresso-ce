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
package org.jspresso.framework.flex.components {

import mx.events.FlexEvent;

import org.jspresso.framework.view.flex.EnhancedRichTextEditor;

public class JsoRichTextEditor extends EnhancedRichTextEditor {
  private var colorPickerColors:Array = ['0x000000', '0x333333', '0x4D4D4D', '0x666666', '0x808080', '0x999999',
                                         '0xB3B3B3', '0xCCCCCC', '0xE6E6E6', '0xFFFFFF', '0x92000a', '0xf5001a',
                                         '0xf79b2b', '0xffff33', '0x3eff3b', '0x50fffe', '0x5981e7', '0x3100f9',
                                         '0x9a00f9', '0xf900fa', '0xd77e6d', '0xe4989a', '0xf5cc9e', '0xfbff42',
                                         '0xb8d8a9', '0xa5c4c8', '0xa9c0f3', '0xa4c4e7', '0xb5a5d6', '0xd2a5bd',
                                         '0xba5e4e', '0xc9656a', '0xdda872', '0xfbe79b', '0x8eb87b', '0x789da4',
                                         '0x839ddb', '0x799dcb', '0x8b74b5', '0xb17696', '0x9d3f30', '0xae333a',
                                         '0xc68445', '0xd0ae4c', '0x65974c', '0x4b757f', '0x5c79c3', '0x4d77af',
                                         '0x614395', '0x91466e', '0x801f11', '0x93000a', '0xae6019', '0xba9124',
                                         '0x3b771e', '0x1e4e5b', '0x3656ab', '0x225093', '0x371274', '0x701747'];

  public function JsoRichTextEditor() {
    super();

    addEventListener(FlexEvent.CREATION_COMPLETE, this_creationCompleteHandler);
  }

  override protected function commitProperties():void {
    super.commitProperties();

    if (this.colorPicker) {
      this.colorPicker.dataProvider = colorPickerColors;
    }
  }

  protected function this_creationCompleteHandler(event:FlexEvent):void {
    removeEventListener(FlexEvent.CREATION_COMPLETE, this_creationCompleteHandler);

    this.fontFamilyCombo.styleName = "type1comboBox";
    this.fontFamilyCombo.editable = false;

    this.fontSizeCombo.styleName = "type1comboBox";
    this.fontSizeCombo.editable = false;

    this.boldButton.styleName = "type1";
    this.italicButton.styleName = "type1";
    this.underlineButton.styleName = "type1";

    this.alignButtons.setStyle("buttonStyleName", "type1");

    this.bulletButton.styleName = "type1";

    this.colorPicker.width = 42;
  }
}
}
