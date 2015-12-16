package org.jspresso.framework.view.flex {

import mx.controls.ToolTip;

import org.jspresso.framework.util.html.HtmlUtil;

public class HtmlToolTip extends ToolTip {
  public function HtmlToolTip() {
    super();
  }

  override protected function commitProperties():void {
    super.commitProperties();
    if (HtmlUtil.isHtml(text)) {
      textField.htmlText = HtmlUtil.sanitizeHtml(text);
    }
  }
}
}
