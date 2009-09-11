qx.Mixin.define("org.jspresso.framework.patch.MSelectBox",
{
   construct : function()
   {
     this.addListener("changeSelection", function(e)
     {
       var sel = e.getData();
       this.fireDataEvent("changeModelSelection",
                          [(sel && sel.length) ? sel[0].getModel()
                                               : null]);
     }, this);
   },
   events :
   {
     "changeModelSelection" : "qx.event.type.Data"
   }
}); 