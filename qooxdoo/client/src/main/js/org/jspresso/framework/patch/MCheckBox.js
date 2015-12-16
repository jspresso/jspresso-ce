qx.Mixin.define("org.jspresso.framework.patch.MCheckBox", {
  members: {
    /**
     * Handler for the execute event.
     *
     * @param e {qx.event.type.Event} The execute event.
     */
    _onExecute: function (e) {
      if (this.getTriState()) {
        if (this.getValue() === false) {
          this.setValue(true);
        } else if (this.getValue() === true) {
          this.setValue(null);
        } else {
          this.setValue(false);
        }
      } else {
        this.setValue(!this.getValue());
      }
    }
  }
});
