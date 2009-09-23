qx.Mixin.define("org.jspresso.framework.patch.MTable",
{
  members :
  {
  	_updateScrollBarVisibility : function()
    {
      if (!this.getBounds()) {
        return;
      }

      var horBar = qx.ui.table.pane.Scroller.HORIZONTAL_SCROLLBAR;
      var verBar = qx.ui.table.pane.Scroller.VERTICAL_SCROLLBAR;
      var scrollerArr = this._getPaneScrollerArr();

      // Check which scroll bars are needed
      var horNeeded = false;
      var verNeeded = false;

      for (var i=0; i<scrollerArr.length; i++)
      {
        var isLast = (i == (scrollerArr.length - 1));

        // Only show the last vertical scrollbar
        var bars = scrollerArr[i].getNeededScrollBars(horNeeded, !isLast);

        if (bars & horBar) {
          horNeeded = true;
        }

        if (isLast && (bars & verBar)) {
          verNeeded = true;
        }
      }

      var verNeeded = true;  //THIS IS THE NEW LINE

      // Set the needed scrollbars
      for (var i=0; i<scrollerArr.length; i++)
      {
        var isLast = (i == (scrollerArr.length - 1));
        var bHadVerticalScrollBar;

        // Only show the last vertical scrollbar
        scrollerArr[i].setHorizontalScrollBarVisible(horNeeded);

        // If this is the last meta-column...
        if (isLast)
        {
          // ... then get the current (old) use of vertical scroll bar
          bHadVerticalScrollBar = scrollerArr[i].getVerticalScrollBarVisible();
        }

        scrollerArr[i].setVerticalScrollBarVisible(isLast && verNeeded);

        // If this is the last meta-column and the use of a vertical scroll bar
        // has changed...
        if (isLast && verNeeded != bHadVerticalScrollBar)
        {
          // ... then dispatch an event to any awaiting listeners
          this.fireDataEvent("verticalScrollBarChanged", verNeeded);
        }
      }
    }
  }
}); 