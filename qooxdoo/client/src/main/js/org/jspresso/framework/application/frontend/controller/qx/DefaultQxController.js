/**
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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
qx.Class.define("org.jspresso.framework.application.frontend.controller.qx.DefaultQxController", {

  extend: org.jspresso.framework.application.frontend.controller.qx.AbstractQxController,

  implement: [
  ],

  statics: {
  },

  construct: function (application, remoteController, userLanguage) {
    this.base(arguments, application, remoteController, userLanguage);
    this._application.getRoot().set({
      blockerColor: '#bfbfbf',
      blockerOpacity: 0.5
    });
  },

  members: {

    /** @type {qx.ui.form.RadioGroup} */
    __workspaceAccordionGroup: null,
    /** @type {qx.ui.container.Stack} */
    __workspaceStack: null,


    _createViewFactory: function () {
      return new org.jspresso.framework.view.qx.DefaultQxViewFactory(this, this, this);
    },

    _showBusy: function(busy) {
      if (busy) {
        this._application.getRoot().setGlobalCursor("wait");
      } else {
        this._application.getRoot().setGlobalCursor("default");
      }
    },

    /**
     *
     * @param title {String}
     * @param message {String}
     * @param dialogView {qx.ui.core.Widget}
     * @param icon {org.jspresso.framework.gui.remote.RIcon}
     * @param buttons {qx.ui.form.Button[]}
     * @param useCurrent {Boolean}
     * @param dimension {org.jspresso.framework.util.gui.Dimension}
     * @return {undefined}
     */
    _popupDialog: function (title, message, dialogView, icon, buttons, useCurrent, dimension) {
      useCurrent = (typeof useCurrent == 'undefined') ? false : useCurrent;

      var buttonBox = new qx.ui.container.Composite();
      buttonBox.setLayout(new qx.ui.layout.HBox(10, "right"));

      var dialogBox = new qx.ui.container.Composite();
      dialogBox.setLayout(new qx.ui.layout.VBox(10, null, new qx.ui.decoration.Decorator().set({
        width: 1
      })));

      if (message) {
        var messageLabel = new qx.ui.basic.Label(message);
        messageLabel.setRich(org.jspresso.framework.util.html.HtmlUtil.isHtml(message));
        dialogBox.add(messageLabel);
      }
      if (dimension != null) {
        dialogView.setWidth(dimension.getWidth());
        dialogView.setHeight(dimension.getHeight());
      }
      dialogBox.add(dialogView, {flex: 1});
      for (var i = 0; i < buttons.length; i++) {
        buttonBox.add(buttons[i]);
      }
      dialogBox.add(buttonBox);

      /**
       * @type {qx.ui.window.Window}
       */
      var dialog;
      var newDialog = true;
      if (useCurrent && this._dialogStack && this._dialogStack.length > 1) {
        dialog = this._dialogStack[this._dialogStack.length - 1][0];
        dialog.removeAll();
        newDialog = false;
      } else {
        var dialogParent;
        //        if(this._dialogStack && this._dialogStack.length > 1) {
        //          dialogParent = this._dialogStack[_dialogStack.length -1];
        //        } else {
        //          dialogParent = this._application.getRoot();
        //        }
        dialogParent = this._application.getRoot();
        dialog = new qx.ui.window.Window();
        dialog.setLayout(new qx.ui.layout.Grow());
        dialog.set({
          modal: true,
          showClose: false,
          showMaximize: false,
          showMinimize: false
        });
        dialogParent.add(dialog);
        this._dialogStack.push([dialog, null, null]);
      }
      dialog.setCaption(title);
      this._viewFactory.setIcon(dialog, icon);
      if (buttons.length > 0) {
        dialog.addListener("keypress", function (e) {
          if (e.getKeyIdentifier() == "Enter" && !qx.ui.core.FocusHandler.getInstance().isFocused(buttons[0])
              && !(qx.ui.core.FocusHandler.getInstance().getFocusedWidget() instanceof qx.ui.form.AbstractField)) {
            buttons[0].focus();
            buttons[0].execute(); // and call the default button's
          }
        });
      }

      if (this.__workspaceStack && this.__workspaceStack.getBounds()) {
        dialog.setMaxWidth(Math.floor(this.__workspaceStack.getBounds().width * 90 / 100));
        dialog.setMaxHeight(Math.floor(this.__workspaceStack.getBounds().height * 90 / 100));
      }
      dialog.add(dialogBox);
      dialog.open();
      if (newDialog) {
        dialog.center();
      }
      this._viewFactory.focus(dialogBox);
    },

    /**
     * Close top most dialog.
     */
    _closeDialog: function () {
      if (this._dialogStack && this._dialogStack.length > 1) {
        /** @type {qx.ui.window.Window} */
        var topDialog = this._dialogStack.pop()[0];
        this._application.getRoot().remove(topDialog);
        topDialog.close();
        topDialog.destroy();
      }
    },

    /**
     * @param workspaceNames {String[]}
     * @param workspaceActions {org.jspresso.framework.gui.remote.RActionList}
     * @param exitAction {org.jspresso.framework.gui.remote.RAction}
     * @param navigationActions {org.jspresso.framework.gui.remote.RActionList[]}
     * @param actions {org.jspresso.framework.gui.remote.RActionList[]}
     * @param secondaryActions {org.jspresso.framework.gui.remote.RActionList[]}
     * @param helpActions {org.jspresso.framework.gui.remote.RActionList[]}
     * @param size {org.jspresso.framework.util.gui.Dimension}
     * @return {undefined}
     *
     */
    _initApplicationFrame: function (workspaceNames, workspaceActions, exitAction, navigationActions, actions,
                                     secondaryActions, helpActions, size) {
      //this._application.getRoot().removeAll();

      var applicationFrame = new qx.ui.container.Composite(new qx.ui.layout.VBox());

      this.__statusBar = new qx.ui.basic.Label();
      this.__statusBar.setVisibility("excluded");
      this._decorateApplicationFrame(applicationFrame, exitAction, navigationActions, actions, helpActions);

      var workspaceAccordion = new qx.ui.container.Composite(new qx.ui.layout.VBox(5));
      this.__workspaceAccordionGroup = new qx.ui.form.RadioGroup();
      this.__workspaceAccordionGroup.setAllowEmptySelection(false);
      for (var i = 0; i < workspaceActions.getActions().length; i++) {
        var workspacePanel = new collapsablepanel.Panel(workspaceActions.getActions()[i].getName());
        if (i == 0) {
          workspacePanel.setValue(true);
        } else {
          workspacePanel.setValue(false);
        }
        workspacePanel.setUserData("workspaceName", workspaceNames[i]);
        workspacePanel.setGroup(this.__workspaceAccordionGroup);
        workspacePanel.setUserData("rAction", workspaceActions.getActions()[i]);
        workspacePanel.addListener("changeValue", function (event) {
          this.execute(event.getTarget().getUserData("rAction"));
        }, this);
        this._viewFactory.setIcon(workspacePanel.getChildControl("bar"), workspaceActions.getActions()[i].getIcon());
        workspaceAccordion.add(workspacePanel, {flex: 1});
      }

      this.__workspaceStack = new qx.ui.container.Stack();

      var splitContainer = new qx.ui.splitpane.Pane("horizontal");
      splitContainer.add(workspaceAccordion, 0.15);
      splitContainer.add(this.__workspaceStack, 0.85);

      applicationFrame.add(splitContainer, {flex: 1});
      if (secondaryActions && secondaryActions.length > 0) {
        var secondaryToolBar = new qx.ui.container.SlideBar();
        secondaryToolBar.add(this._viewFactory.createToolBarFromActionLists(secondaryActions));
        applicationFrame.add(secondaryToolBar);
      }
      if (size) {
        if (size.getWidth() > 0) {
          applicationFrame.setMinWidth(size.getWidth());
        }
        if (size.getHeight() > 0) {
          applicationFrame.setMinHeight(size.getHeight());
        }
      }
      var scrollContainer = new qx.ui.container.Scroll();
      scrollContainer.add(applicationFrame);

      this._application.getRoot().add(scrollContainer, {edge: 0})
    },

    /**
     *
     * @param workspaceName {String}
     * @param workspaceView {org.jspresso.framework.gui.remote.RComponent}
     * @return {undefined}
     */
    _displayWorkspace: function (workspaceName, workspaceView) {
      if (workspaceView) {
        var workspaceNavigator = null;
        if (workspaceView instanceof org.jspresso.framework.gui.remote.RSplitContainer) {
          var wv = /** @type {org.jspresso.framework.gui.remote.RSplitContainer } */ workspaceView;
          workspaceNavigator = wv.getLeftTop();
          workspaceView = wv.getRightBottom();
        }
        var workspaceViewUI = this.createComponent(workspaceView);
        workspaceViewUI.setUserData("workspaceName", workspaceName);
        this.__workspaceStack.add(workspaceViewUI);
        if (workspaceNavigator) {
          var workspaceNavigatorUI = this.createComponent(workspaceNavigator);
          if (workspaceNavigatorUI instanceof qx.ui.tree.Tree) {
            workspaceNavigatorUI.setHideRoot(true);
          }
          var existingChildren = this.__workspaceAccordionGroup.getChildren();
          var existingChild;
          for (var i = 0; i < existingChildren.length; i++) {
            var child = existingChildren[i];
            if (child.getUserData("workspaceName") == workspaceName) {
              existingChild = child;
            }
          }
          if (existingChild) {
            existingChild.add(workspaceNavigatorUI);
          }
        }
      }
      var children = this.__workspaceStack.getChildren();
      var selectedChild;
      for (var i = 0; i < children.length; i++) {
        var child = children[i];
        if (child.getUserData("workspaceName") == workspaceName) {
          selectedChild = child;
        }
      }
      if (selectedChild) {
        this.__workspaceStack.setSelection([selectedChild]);
      }

      children = this.__workspaceAccordionGroup.getChildren();
      selectedChild = null;
      for (var i = 0; i < children.length; i++) {
        var child = children[i];
        if (child.getUserData("workspaceName") == workspaceName) {
          selectedChild = child;
        }
      }
      if (selectedChild) {
        this.__workspaceAccordionGroup.setSelection([selectedChild]);
      }
    }



  }
});
