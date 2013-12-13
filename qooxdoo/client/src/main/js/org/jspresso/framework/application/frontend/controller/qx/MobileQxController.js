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
qx.Class.define("org.jspresso.framework.application.frontend.controller.qx.MobileQxController", {

  extend: org.jspresso.framework.application.frontend.controller.qx.AbstractQxController,

  implement: [
  ],

  statics: {
  },

  construct: function (application, remoteController, userLanguage) {
    this.base(arguments, application, remoteController, userLanguage);

    var busyIndicator = new qx.ui.mobile.dialog.BusyIndicator("Please wait...");
    this.__busyPopup = new qx.ui.mobile.dialog.Popup(busyIndicator);
    this.__busyPopup.setTitle("Loading...");

    this._manager = new qx.ui.mobile.page.Manager();
  },

  members: {
    /** @type {qx.ui.mobile.dialog.Popup} */
    __busyPopup : null,
    _manager    : null,


    _createViewFactory: function () {
      return new org.jspresso.framework.view.qx.MobileQxViewFactory(this, this, this);
    },

    _showBusy: function(busy) {
      if (busy) {
        this.__busyPopup.show();
      } else {
        this.__busyPopup.hide();
      }
    },

    /**
     *
     * @param title {String}
     * @param message {String}
     * @param dialogView {qx.ui.mobile.core.Widget}
     * @param icon {org.jspresso.framework.gui.remote.RIcon}
     * @param buttons {qx.ui.mobile.form.Button[]}
     * @param useCurrent {Boolean}
     * @param dimension {org.jspresso.framework.util.gui.Dimension}
     * @return {undefined}
     */
    _popupDialog: function (title, message, dialogView, icon, buttons, useCurrent, dimension) {
      useCurrent = (typeof useCurrent == 'undefined') ? false : useCurrent;

      var dialogPage = new qx.ui.mobile.page.NavigationPage();
      dialogPage.setTitle(title);

      var dialogMessage = new qx.ui.mobile.embed.Html(message);
      var group = new qx.ui.mobile.form.Group([dialogMessage, dialogView]);

      dialogPage.addListener("initialize", function (e) {
        var content = dialogPage.getContent();
        content.add(dialogMessage);
        content.add(dialogView);
        for (var i = 0; i < buttons.length; i++) {
          content.add(buttons[i]);
        }
      }, this);

      if (useCurrent && this._dialogStack && this._dialogStack.length > 1) {
        /** @type {qx.ui.mobile.page.NavigationPage} */
        var currentDialogPage = this._dialogStack[this._dialogStack.length - 1][0];
        this._dialogStack.pop();
        currentDialogPage.exclude();
        currentDialogPage.destroy();
        this._manager.getDetailContainer().remove(currentDialogPage);
      }
      this._dialogStack.push([dialogPage, null, null]);
      this._manager.addDetail(dialogPage);
      dialogPage.show();
      this._viewFactory.focus(dialogPage);
    },

    /**
     * Close top most dialog.
     */
    _closeDialog: function () {
      if (this._dialogStack && this._dialogStack.length > 1) {
        /** @type {qx.ui.mobile.page.NavigationPage} */
        var topDialog = this._dialogStack.pop()[0];
        topDialog.exclude();
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

      var workspaces = new qx.data.Array();
      for (var i = 0; i < workspaceActions.getActions().length; i++) {
        var workspaceAction = workspaceActions.getActions()[i];
        workspaces.push({
          title: workspaceAction.getName(),
          subtitle: workspaceAction.getDescription(),
          image: workspaceAction.getIcon().getImageUrlSpec(),
          action: workspaceAction
        });
      }
      var workspaceList = new qx.ui.mobile.list.List({
        configureItem : function(item, data, row) {
          item.setTitle(data.title);
          item.setSubtitle(data.subtitle);
          item.setImage(data.image);
          item.setShowArrow(true);
          // Workaround to assign a size to the image that is not known to the resource loader.
          item.getImageWidget()._setStyle("width", "2rem");
          item.getImageWidget()._setStyle("height", "2rem");
        }
      });
      workspaceList.setModel(workspaces);
      workspaceList.addListener("changeSelection", function(evt) {
        var workspaceAction = workspaces[evt.getData()].action;
        this.execute(workspaceAction);
      }, this);

      var workspacesPage = new qx.ui.mobile.page.NavigationPage();
      workspacesPage.addListener("initialize", function (e) {
        var content = workspacesPage.getContent();
        content.add(workspaceList);
      }, this);
      this._manager.addMaster(workspacesPage);
      workspacesPage.show();
    },

    /**
     *
     * @param workspaceName {String}
     * @param workspaceView {org.jspresso.framework.gui.remote.RComponent}
     * @return {undefined}
     */
    _displayWorkspace: function (workspaceName, workspaceView) {
//      if (workspaceView) {
//        var workspaceNavigator = null;
//        if (workspaceView instanceof org.jspresso.framework.gui.remote.RSplitContainer) {
//          var wv = /** @type {org.jspresso.framework.gui.remote.RSplitContainer } */ workspaceView;
//          workspaceNavigator = wv.getLeftTop();
//          workspaceView = wv.getRightBottom();
//        }
//        var workspaceViewUI = this.createComponent(workspaceView);
//        workspaceViewUI.setUserData("workspaceName", workspaceName);
//        this.__workspaceStack.add(workspaceViewUI);
//        if (workspaceNavigator) {
//          var workspaceNavigatorUI = this.createComponent(workspaceNavigator);
//          if (workspaceNavigatorUI instanceof qx.ui.tree.Tree) {
//            workspaceNavigatorUI.setHideRoot(true);
//          }
//          var existingChildren = this.__workspaceAccordionGroup.getChildren();
//          var existingChild;
//          for (var i = 0; i < existingChildren.length; i++) {
//            var child = existingChildren[i];
//            if (child.getUserData("workspaceName") == workspaceName) {
//              existingChild = child;
//            }
//          }
//          if (existingChild) {
//            existingChild.add(workspaceNavigatorUI);
//          }
//        }
//      }
//      var children = this.__workspaceStack.getChildren();
//      var selectedChild;
//      for (var i = 0; i < children.length; i++) {
//        var child = children[i];
//        if (child.getUserData("workspaceName") == workspaceName) {
//          selectedChild = child;
//        }
//      }
//      if (selectedChild) {
//        this.__workspaceStack.setSelection([selectedChild]);
//      }
//
//      children = this.__workspaceAccordionGroup.getChildren();
//      selectedChild = null;
//      for (var i = 0; i < children.length; i++) {
//        var child = children[i];
//        if (child.getUserData("workspaceName") == workspaceName) {
//          selectedChild = child;
//        }
//      }
//      if (selectedChild) {
//        this.__workspaceAccordionGroup.setSelection([selectedChild]);
//      }
    }


  }
});
