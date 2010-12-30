qx.Mixin.define("org.jspresso.framework.patch.MCheckBox",
{
  members :
  {
    /**
     * Handler for the execute event.
     *
     * @param e {qx.event.type.Event} The execute event.
     */
    _onExecute : function(e) {
      if(this.isTriState()) {
        if(this.getValue() === false) {
          this.toggleValue();
        } else if(this.getValue() === true) {
          this.setValue(null);
        } else {
          this.setValue(false);
        }
      } else {
        this.toggleValue();
      }
    }
  }
}); 