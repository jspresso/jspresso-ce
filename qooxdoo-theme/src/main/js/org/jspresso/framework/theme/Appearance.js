/* ************************************************************************

   Copyright:

   License:

   Authors:

************************************************************************ */

qx.Theme.define("org.jspresso.framework.theme.Appearance",
{
  extend : qx.theme.simple.Appearance,
  
  appearances :
  {
    "collapsable-panel" :
    {
      include : "groupbox",
      alias : "groupbox",
      style : function(states)
      {
        return {
          padding    : 5,
          allowGrowY : !!states.opened || !!states.horizontal,
          allowGrowX : !!states.opened || !!states.horizontal
        };
      }
    },
    "groupbox/bar/icon" :
    {
      include : "groupbox/legend",
      alias : "groupbox/legend"
    }
  }
});
