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
 *
 * @asset(org/jspresso/framework/mobile/back-mobile.png)
 */

qx.Class.define("org.jspresso.framework.view.qx.mobile.MobileQxViewFactory", {
  extend: org.jspresso.framework.view.qx.AbstractQxViewFactory,

  statics: {
    bindListItem: function (item, state, selected) {
      var children = state.getChildren();
      if(children.length > 1) {
        item.setTitle(children.getItem(1).getValue());
      } else {
        item.setTitle(state.getValue());
      }
      item.setSubtitle(state.getDescription());
      item.setImage(state.getIconImageUrl());
      item.setSelected(selected);
    }
  },

  construct: function (remotePeerRegistry, actionHandler, commandHandler) {
    this.base(arguments, remotePeerRegistry, actionHandler, commandHandler);
  },

  members: {

    /** @type {array} */
    __monthNames: null,

    /**
     * @return {qx.ui.mobile.core.Widget}
     */
    _createEmptyWidget: function () {
      return new qx.ui.core.Widget();
    },

    /**
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @return {qx.ui.mobile.core.Widget}
     */
    _createCustomComponent: function (remoteComponent) {
      return null;
    },

    /**
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @param registerPeers {Boolean}
     * @return {qx.ui.mobile.core.Widget}
     */
    createComponent: function (remoteComponent, registerPeers) {
      var component = this.base(arguments, remoteComponent, registerPeers);
      component = this._decorateWithActions(remoteComponent, component);
      return component;
    },

    /**
     * @return {qx.ui.mobile.form.Button}
     * @param remoteAction {org.jspresso.framework.gui.remote.RAction}
     */
    createAction: function (remoteAction) {
      var button = this.createButton(remoteAction.getName(), remoteAction.getDescription(), remoteAction.getIcon());
      this._bindButton(button, remoteAction);
      return button;
    },

    /**
     *
     * @return {qx.ui.mobile.form.Button}
     * @param remoteAction {org.jspresso.framework.gui.remote.RAction}
     */
    createToolBarAction: function (remoteAction) {
      var button = this.createToolBarButton(remoteAction.getName(), remoteAction.getDescription(),
          remoteAction.getIcon());
      this._bindButton(button, remoteAction);
      return button;
    },

    /**
     * @param component {qx.ui.mobile.core.Widget}
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @param disableActionsWithField {Boolean}
     * @returns {qx.ui.mobile.core.Widget}
     */
    _decorateWithAsideActions: function (component, remoteComponent, disableActionsWithField) {
      var actions = this._extractAllActions(remoteComponent.getActionLists());
      if (actions.length > 0) {
        var modelController;
        if (disableActionsWithField && remoteComponent && remoteComponent.getState()) {
          modelController = new qx.data.controller.Object(remoteComponent.getState());
        }
        var maxToolbarActionCount = 2;
        var hBox = new qx.ui.mobile.layout.HBox();
        hBox.setAlignY("middle");
        var actionField = new qx.ui.mobile.container.Composite(hBox);
        var toolBar = this._createToolBarFromActions(actions, maxToolbarActionCount, modelController);
        toolBar.removeCssClass("toolbar");
        var buttons = toolBar.getChildren();
        for (var i = 0; i < buttons.length; i++) {
          buttons[i].removeCssClass("toolbar-button");
        }
        if(component) {
          actionField.add(component, {flex:1});
          actionField.add(toolBar);
          return actionField;
        } else {
          return toolBar
        }
      }
      return component;
    },

    /**
     * @param extraActions {Array}
     * @return {qx.ui.mobile.toolbar.Button}
     */
    _createExtraActionsToolBarButton: function (extraActions) {
      var extraButton = new qx.ui.mobile.toolbar.Button("...");
      var extraMenu = new qx.ui.mobile.dialog.Menu();
      extraMenu.getSelectionList().setDelegate({
        configureItem: function (item, data, row) {
          item.setTitle(data.getName());
          if(data.getIcon()) {
            item.setImage(data.getIcon().getImageUrlSpec());
          }
        }
      });
      extraMenu.getSelectionList().setModel(new qx.data.Array(extraActions));
      extraMenu.setAnchor(extraButton);
      extraMenu.addListener("changeSelection", function(evt){
        var selectedIndex = evt.getData();
        this._getActionHandler().execute(extraActions[selectedIndex]);
      }, this);
      this.addButtonListener(extraButton, function(evt) {
        extraMenu.show();
      }, this);
      return extraButton;
    },

    /**
     * @param actionLists {Array}
     * @return {Array}
     */
    _extractAllActions: function (actionLists) {
      var allActions = [];
      if (actionLists && actionLists.length > 0) {
        for (var i = 0; i < actionLists.length; i++) {
          var actionList = actionLists[i];
          var actions = actionList.getActions();
          if (actions) {
            for (var j = 0; j < actions.length; j++) {
              allActions.push(actions[j]);
            }
          }
        }
      }
      return allActions;
    },

    /**
     * @param actions {Array}
     * @param maxToolbarActionCount {Integer}
     * @param modelController {qx.data.controller.Object}
     * @return {qx.ui.mobile.toolbar.ToolBar}
     */
    _createToolBarFromActions: function (actions, maxToolbarActionCount, modelController) {
      var extraActions = [];
      var toolBar = new qx.ui.mobile.toolbar.ToolBar();
      var actionComponent;
      for (var i = 0; i < actions.length; i++) {
        if (i < maxToolbarActionCount - 1 || actions.length == maxToolbarActionCount) {
          actionComponent = this.createToolBarAction(actions[i]);
          if(modelController) {
            modelController.addTarget(actionComponent, "enabled", "writable", false);
          }
          toolBar.add(actionComponent);
        } else {
          extraActions.push(actions[i]);
        }
      }
      if (extraActions.length > 0) {
        actionComponent = this._createExtraActionsToolBarButton(extraActions);
        if(modelController) {
          modelController.addTarget(actionComponent, "enabled", "writable", false);
        }
        toolBar.add(actionComponent);
      }
      return toolBar;
    },

    /**
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @param component {qx.ui.mobile.core.Widget}
     */
    _addToolBarActions: function (remoteComponent, component) {
      var maxToolbarActionCount = 4;
      var actions = this._extractAllActions(remoteComponent.getActionLists());
      if (actions.length > 0) {
        var toolBar = this._createToolBarFromActions(actions, maxToolbarActionCount, null);
        component.add(toolBar);
      }
    },

    /**
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @param component {qx.ui.mobile.core.Widget}
     * @return {qx.ui.mobile.core.Widget}
     */
    _decorateWithActions: function (remoteComponent, component) {
      if (remoteComponent instanceof org.jspresso.framework.gui.remote.RTextField || remoteComponent
          instanceof org.jspresso.framework.gui.remote.RDateField || remoteComponent
          instanceof org.jspresso.framework.gui.remote.RNumericComponent || remoteComponent
          instanceof org.jspresso.framework.gui.remote.RLabel || remoteComponent
          instanceof org.jspresso.framework.gui.remote.RTimeField || remoteComponent
          instanceof org.jspresso.framework.gui.remote.RComboBox || remoteComponent
          instanceof org.jspresso.framework.gui.remote.RCheckBox) {
        return this._decorateWithAsideActions(component, remoteComponent, false);
      } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RForm
          || remoteComponent instanceof org.jspresso.framework.gui.remote.mobile.RMobilePage) {
        this._addToolBarActions(remoteComponent, /** @type {qx.ui.mobile.container.Composite} */ component);
        return component;
      } else {
        return component;
      }
    },

    /**
     * @param page {qx.ui.mobile.page.NavigationPage}
     * @param pageAction {org.jspresso.framework.gui.remote.RAction}
     */
    installPageMainAction: function (page, pageAction) {
      if(pageAction) {
        page.setButtonText(pageAction.getName());
        if(pageAction.getIcon()) {
          page.setButtonIcon(pageAction.getIcon().getImageUrlSpec());
        }
        page.addListener("action", function(event) {
          this._getActionHandler().execute(pageAction);
        }, this);
        page.setShowButton(true);
      }
    },

    /**
     * @param page {qx.ui.mobile.page.NavigationPage}
     * @param enterAction {org.jspresso.framework.gui.remote.RAction}
     */
    installPageEnterAction: function (page, enterAction) {
      if (enterAction) {
        page.addListener("start", function () {
          this._getActionHandler().execute(enterAction);
        }, this);
      }
    },

    /**
     * @param page {qx.ui.mobile.page.NavigationPage}
     * @param backAction {org.jspresso.framework.gui.remote.RAction}
     */
    installPageBackAction: function (page, backAction) {
      if (backAction) {
        this.linkNextPageBackButton(page,  null, backAction);
      }
    },

    /**
     * @param page {qx.ui.mobile.page.NavigationPage}
     * @param pageEndAction {org.jspresso.framework.gui.remote.RAction}
     */
    installPageEndAction: function (page, pageEndAction) {
      if (pageEndAction) {
        page.addListener("initialize", function (e) {
          page._getScrollContainer().addListener("pageEnd", function(e) {
            this._getActionHandler().execute(pageEndAction);
          }, this);
        }, this);
      }
    },

    /**
     *
     * @param remotePage {org.jspresso.framework.gui.remote.mobile.RMobilePageAware}
     * @param page {qx.ui.mobile.page.NavigationPage}
     */
    installPageActions: function (remotePage, page) {
      this.installPageEnterAction(page, remotePage.getEnterAction());
      this.installPageMainAction(page, remotePage.getMainAction());
      this.installPageBackAction(page, remotePage.getBackAction());
      this.installPageEndAction(page, remotePage.getPageEndAction());
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteContainer {org.jspresso.framework.gui.remote.RContainer}
     */
    _createContainer: function (remoteContainer) {
      var container;
      if (remoteContainer instanceof org.jspresso.framework.gui.remote.mobile.RMobileCardPage) {
        container = this._createMobileCardPage(remoteContainer);
      } else if (remoteContainer instanceof org.jspresso.framework.gui.remote.mobile.RMobileNavPage) {
        container = this._createMobileNavPage(remoteContainer);
      } else if (remoteContainer instanceof org.jspresso.framework.gui.remote.mobile.RMobileCompositePage) {
        container = this._createMobileCompositePage(remoteContainer);
      } else if (remoteContainer instanceof org.jspresso.framework.gui.remote.mobile.RMobilePageAwareContainer) {
        container = this.createComponent(remoteContainer.getContent());
      } else if (remoteContainer instanceof org.jspresso.framework.gui.remote.RCardContainer) {
        container = this._createCardContainer(remoteContainer);
      } else if (remoteContainer instanceof org.jspresso.framework.gui.remote.RBorderContainer) {
        container = this._createBorderContainer(remoteContainer);
      }
      if(  remoteContainer instanceof org.jspresso.framework.gui.remote.mobile.RMobilePageAware
          && container instanceof qx.ui.mobile.page.NavigationPage) {
        this.installPageActions(remoteContainer, container);
      }
      return container;
    },

    /**
     * @param remoteCardPage {org.jspresso.framework.gui.remote.mobile.RMobileCardPage}
     * @return {qx.ui.mobile.core.Widget}
     */
    _createMobileCardPage: function (remoteCardPage) {
      /** @type {qx.ui.mobile.container.Composite} */
      var cardContainer = this.createComponent(remoteCardPage.getPages());
      // Card pages will be added and selected automatically by the card container.
      return cardContainer;
    },

    /**
     * @param remoteNavPage {org.jspresso.framework.gui.remote.mobile.RMobileNavPage}
     * @return {qx.ui.mobile.core.Widget}
     */
    _createMobileNavPage: function (remoteNavPage) {
      /** @type {qx.ui.mobile.list.List} */
      var selectionComponent = this.createComponent(remoteNavPage.getSelectionView());
      var headerComponent;
      if (remoteNavPage.getHeaderView()) {
        headerComponent = this.createComponent(remoteNavPage.getHeaderView());
      }
      var navPage = new qx.ui.mobile.page.NavigationPage();
      navPage.setTitle(remoteNavPage.getLabel());
      navPage.addListener("initialize", function (e) {
        if (headerComponent) {
          navPage.getContent().add(headerComponent);
        }
        navPage.getContent().add(selectionComponent);
      }, this);
      if (remoteNavPage.getNextPage()) {
        /** @type {qx.ui.mobile.page.NavigationPage} */
        var nextPage = this.createComponent(remoteNavPage.getNextPage());
        selectionComponent.addListener("changeSelection", function(evt) {
          // Because of MobileCardPage
          if(nextPage instanceof qx.ui.mobile.page.NavigationPage) {
            nextPage.show();
          } else {
            var cardPage = nextPage.getUserData("currentPage");
            if(cardPage) {
              cardPage.show();
            }
          }
        }, this);
        // Because of MobileCardPage
        if(nextPage instanceof qx.ui.mobile.page.NavigationPage) {
          this.linkNextPageBackButton(nextPage, navPage, remoteNavPage.getNextPage().getBackAction());
        } else {
          nextPage.setUserData("previousPage", navPage);
        }
      }
      this._addDetailPage(navPage);
      return navPage;
    },

    /**
     * @param nextPage {qx.ui.mobile.page.NavigationPage}
     * @param previousPage {qx.ui.mobile.page.NavigationPage}
     * @param backAction {org.jspresso.framework.gui.remote.RAction}
     * @param animation {String}
     */
    linkNextPageBackButton: function (nextPage, previousPage, backAction, animation) {
      if(typeof animation === undefined) animation = "slide";

      nextPage.setShowBackButton(true);
      var backButton = nextPage.getLeftContainer().getChildren()[0];
      if(backAction && backAction.getName()) {
        nextPage.setBackButtonText(backAction.getName());
        backButton.setShow("label");
      } else {
        backButton.setIcon("org/jspresso/framework/mobile/back-mobile.png");
        backButton.setShow("icon");
      }
      if(backAction) {
        nextPage.addListener("back", function () {
          this._getActionHandler().execute(backAction);
        }, this);
      }
      if(previousPage) {
        nextPage.addListener("back", function () {
          previousPage.show({animation: animation,  reverse: true});
        }, this);
      }
    },

    /**
     * @param remoteCompositePage {org.jspresso.framework.gui.remote.mobile.RMobileCompositePage}
     * @return {qx.ui.mobile.core.Widget}
     */
    _createMobileCompositePage: function (remoteCompositePage) {
      var compositePage = new qx.ui.mobile.page.NavigationPage();
      compositePage.setTitle(remoteCompositePage.getLabel());
      var sections = [];
      for(var i = 0; i < remoteCompositePage.getPageSections().length; i++) {
        var remotePageSection = remoteCompositePage.getPageSections()[i];
        if(remotePageSection instanceof org.jspresso.framework.gui.remote.mobile.RMobilePage) {
          /** @type {qx.ui.mobile.page.NavigationPage} */
          var nextPage = this.createComponent(remotePageSection);
          this.linkNextPageBackButton(nextPage, compositePage, remotePageSection.getBackAction());
          var listModel = new qx.data.Array();
          listModel.push({
            section: remotePageSection,
            next: nextPage
          });
          var list = new qx.ui.mobile.list.List({
            configureItem: function (item, data, row) {
              var section = data.section;
              item.setTitle(section.getLabel());
              item.setSubtitle(section.getToolTip());
              if(section.getIcon()) {
                item.setImage(section.getIcon().getImageUrlSpec());
              }
              item.setShowArrow(true);
            }
          });
          list.setModel(listModel);
          list.addListener("changeSelection", function(evt) {
            var selectedIndex = evt.getData();
            /** @type {qx.ui.mobile.list.List} */
            var l = evt.getCurrentTarget();
            l.getModel().getItem(selectedIndex).next.show();
          }, this);
          sections.push(list);
        } else {
          var pageSection = this.createComponent(remotePageSection);
          sections.push(pageSection);
        }
      }
      if(remoteCompositePage.getEditAction() && remoteCompositePage.getEditorPage()) {
        var editorPage = this.createComponent(remoteCompositePage.getEditorPage());
        this.linkNextPageBackButton(editorPage, compositePage,
            remoteCompositePage.getEditorPage().getBackAction(), "flip");
        compositePage.setUserData("editorPage", editorPage);
        if (remoteCompositePage.getMainAction() == null) {
          remoteCompositePage.setMainAction(remoteCompositePage.getEditAction());
        } else {
          var editActionList = new org.jspresso.framework.gui.remote.RActionList();
          editActionList.setActions([remoteCompositePage.getEditAction()]);
          var actionLists = [editActionList];
          if(remoteCompositePage.getActionLists()) {
            actionLists = actionLists.concat(remoteCompositePage.getActionLists());
          }
          remoteCompositePage.setActionLists(actionLists);
        }
      }
      compositePage.addListener("initialize", function (e) {
        for(var i = 0; i < sections.length; i++) {
          compositePage.getContent().add(sections[i]);
        }
      }, this);
      this._addDetailPage(compositePage);
      return compositePage;
    },

    /**
     *
     * @return {qx.ui.mobile.form.Button}
     * @param toolTip {String}
     * @param label {String}
     * @param icon {org.jspresso.framework.gui.remote.RIcon}
     */
    createButton: function (label, toolTip, icon) {
      var button = new qx.ui.mobile.form.Button();
      this._completeButton(button, label, toolTip, icon);
      return button;
    },

    /**
     *
     * @return {qx.ui.mobile.toolbar.Button}
     * @param toolTip {String}
     * @param label {String}
     * @param icon {org.jspresso.framework.gui.remote.RIcon}
     */
    createToolBarButton: function (label, toolTip, icon) {
      var button = new qx.ui.mobile.toolbar.Button();
      this._completeButton(button, label, toolTip, icon);
      if(button.getShow() == "both") {
        button.setIconPosition("top");
      }
      return button;
    },

    /**
     *
     * @return {undefined}
     * @param icon {org.jspresso.framework.gui.remote.RIcon}
     * @param button {qx.ui.mobile.form.Button}
     * @param label {String}
     * @param toolTip {String}
     */
    _completeButton: function (button, label, toolTip, icon) {
      this.setIcon(button, icon);
      if (label) {
        button.setLabel(label);
      }
      if(label && icon) {
        button.setShow("both");
      } else if(label) {
        button.setShow("label");
      } else {
        button.setGap(0);
        var padding = "14px";
        button._setStyle("padding-left", padding);
        button._setStyle("padding-right", padding);
        button.setShow("icon");
      }
    },

    /**
     * @param button {qx.ui.mobile.form.Button | qx.ui.mobile.menu.Button}
     * @param listener {function}
     * @param that {var}
     */
    addButtonListener: function (button, listener, that) {
      button.addListener("tap", listener, that);
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteForm {org.jspresso.framework.gui.remote.RForm}
     */
    _createForm: function (remoteForm) {
      var form = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.VBox());
      form.addCssClass("form");
      var top = new qx.ui.mobile.form.Row();
      top.addCssClass("form-row-group-first");
      form.add(top);
      for (var i = 0; i < remoteForm.getElements().length; i++) {
        var rComponent = remoteForm.getElements()[i];

        var component = /** @type {qx.ui.mobile.core.Widget} */ this.createComponent(rComponent);

        var row = new qx.ui.mobile.container.Composite();
        row.addCssClass("form-row");
        row.addCssClass("form-row-content");
        if(remoteForm.getLabelsPosition() == "ABOVE"
            || this._isMultiline(rComponent)) {
          row.setLayout(new qx.ui.mobile.layout.VBox());
        } else {
          row.setLayout(new qx.ui.mobile.layout.HBox())
        }
        if(remoteForm.getLabelsPosition() != "NONE"
            || rComponent instanceof org.jspresso.framework.gui.remote.RCheckBox) {
          var label = new qx.ui.mobile.form.Label("<p>" + rComponent.getLabel() + "</p>");
          // Changes label color when disabled
          //label.setLabelFor(component.getId());
          label._setStyle("width", "30%");
          row.add(label, {flex:1});
        }
        component._setStyle("font-weight", "normal");
        if(this._isFixedWidth(rComponent)) {
          row.add(component);
        } else {
          component._setStyle("width", "70%");
          row.add(component, {flex:1});
        }
        if(this._isMultiline(rComponent)) {
          form.add(row, {flex:1});
        } else {
          form.add(row);
        }
      }
      var bottom = new qx.ui.mobile.form.Row();
      bottom.addCssClass("form-row-group-last");
      form.add(bottom);
      return form;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteTextField  {org.jspresso.framework.gui.remote.RTextField}
     */
    _createTextField: function (remoteTextField) {
      var textField = new qx.ui.mobile.form.TextField();
      if (remoteTextField.getMaxLength() > 0) {
        textField.setMaxLength(remoteTextField.getMaxLength());
      }
      textField.setPlaceholder(remoteTextField.getLabel());
      this._bindTextField(remoteTextField, textField);
      return textField;
    },

    /**
     * @param remoteTextField  {org.jspresso.framework.gui.remote.RTextField}
     * @param textField {qx.ui.mobile.form.TextField}
     */
    _bindTextField: function (remoteTextField, textField) {
      var state = remoteTextField.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(textField, "value", "value", true, {
        converter: this._modelToViewFieldConverter
      }, {
        converter: this._viewToModelFieldConverter
      });
      modelController.addTarget(textField, "readOnly", "writable", false, {
        converter: this._readOnlyFieldConverter
      });
      if (remoteTextField.getCharacterAction()) {
        textField.addListener("input", function (event) {
          var actionEvent = new org.jspresso.framework.gui.remote.RActionEvent();
          actionEvent.setActionCommand(textField.getValue());
          this._getActionHandler().execute(remoteTextField.getCharacterAction(), actionEvent);
        }, this);
      }
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remotePasswordField {org.jspresso.framework.gui.remote.RPasswordField}
     */
    _createPasswordField: function (remotePasswordField) {
      var passwordField = new qx.ui.mobile.form.PasswordField();
      if (remotePasswordField.getMaxLength() > 0) {
        passwordField.setMaxLength(remotePasswordField.getMaxLength());
      }
      passwordField.setPlaceholder(remotePasswordField.getLabel());
      this._bindTextField(remotePasswordField, passwordField);
      return passwordField;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteComboBox {org.jspresso.framework.gui.remote.RComboBox}
     */
    _createComboBox: function (remoteComboBox) {
      var comboBox = new qx.ui.mobile.form.SelectBox();
      comboBox.setPlaceholder(remoteComboBox.getLabel());
      var cbModel = new qx.data.Array();
      for (var i = 0; i < remoteComboBox.getValues().length; i++) {
        if (i == 0 && remoteComboBox.getValues()[i].length > 0) {
          qx.lang.Array.insertAt(remoteComboBox.getValues(), "", 0);
          cbModel.insertAt(0, " ");
        }
        cbModel.push(remoteComboBox.getTranslations()[i]);
      }
      comboBox.setModel(cbModel);
      this._bindComboBox(remoteComboBox, comboBox);
      return comboBox;
    },

    /**
     * @param remoteComboBox  {org.jspresso.framework.gui.remote.RComboBox}
     * @param comboBox {qx.ui.mobile.form.SelectBox}
     */
    _bindComboBox: function (remoteComboBox, comboBox) {
      // To workaround the fact that value change is not notified correctly.
      comboBox.addListener("changeSelection", function(evt) {
        comboBox.setValue(evt.getData()["item"]);
      }, this);
      var state = remoteComboBox.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(comboBox, "value", "value", true,
          {
            converter: function (modelValue, model, source, target) {
              return model.getItem(remoteComboBox.getValues().indexOf(modelValue));
            }
          },
          {
            converter: function (modelValue, model, source, target) {
              return remoteComboBox.getValues()[comboBox.getModel().indexOf(modelValue)];
            }
          }
      );
      modelController.addTarget(comboBox, "enabled", "writable", false);
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteCheckBox {org.jspresso.framework.gui.remote.RCheckBox}
     */
    _createCheckBox: function (remoteCheckBox) {
      var checkBox = new qx.ui.mobile.form.CheckBox();
      this._bindCheckBox(remoteCheckBox, checkBox);
      return checkBox;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteTree {org.jspresso.framework.gui.remote.mobile.RMobileTree}
     */
    _createTree: function (remoteTree) {

      var treeListModel = org.jspresso.framework.state.remote.RemoteCompositeValueState.flatten(remoteTree.getState(), 0);

      var treeList = new qx.ui.mobile.list.List({
        configureItem: function (item, data, row) {
          var state = data.state;
          org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.bindListItem(item, state, false);
          if(remoteTree instanceof org.jspresso.framework.gui.remote.mobile.RMobileTree) {
            item.setShowArrow(remoteTree.getShowArrow());
          }
          item.getImageWidget()._setStyle("margin-left", data.level + "rem");
        }
      });
      treeList.setModel(treeListModel);
      var selections = [];
      treeList.addListener("changeSelection", function(evt) {
        var futureDeselections = [];
        var localLevel = 0;
        for(var i = 1; i < treeListModel.length; i++) {
          var lowerNode = treeListModel.getItem(i);
          if(lowerNode.level > localLevel) {
            var currentNode = treeListModel.getItem(i-1);
            if(currentNode.state.getSelectedIndices() != null && currentNode.state.getSelectedIndices().length > 0) {
              futureDeselections.push(currentNode.state);
            }
          }
          localLevel = lowerNode.level;
        }
        var selectedIndex = evt.getData();
        var currentNode = treeListModel.getItem(selectedIndex);
        var localIndex = 0;
        var futureSelections = [];
        for(var i = selectedIndex-1; i >= 0; i--) {
          var upperNode = treeListModel.getItem(i);
          if(upperNode.level < currentNode.level) {
            futureSelections = [{state: upperNode.state, selection: [localIndex]}].concat(futureSelections);
            var j = futureDeselections.indexOf(upperNode.state);
            if(j >= 0) {
              futureDeselections[j] = null;
            }
            currentNode = upperNode;
            localIndex = 0;
          } else {
            localIndex++;
          }
        }
        for(var i = 0; i < futureDeselections.length; i++) {
          if(futureDeselections[i]) {
            futureDeselections[i].setLeadingIndex(-1);
            futureDeselections[i].setSelectedIndices(null);
          }
        }
        for(var i = 0; i < futureSelections.length ; i++) {
          if(futureSelections[i]) {
            futureSelections[i].state.setLeadingIndex(futureSelections[i].selection[0]);
            futureSelections[i].state.setSelectedIndices(futureSelections[i].selection);
          }
        }
      }, this);
      return treeList;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteCardContainer {org.jspresso.framework.gui.remote.RCardContainer}
     */
    _createCardContainer: function (remoteCardContainer) {
      var cardContainer = this._createCardContainerComponent();
      cardContainer.setUserData("existingCards", []);

      for (var i = 0; i < remoteCardContainer.getCardNames().length; i++) {
        var rCardComponent = remoteCardContainer.getCards()[i];
        var cardName = remoteCardContainer.getCardNames()[i];

        this.addCard(cardContainer, rCardComponent, cardName);
      }

      /** @type {org.jspresso.framework.state.remote.RemoteValueState} */
      var state = remoteCardContainer.getState();
      state.addListener("changeValue", function (e) {
        var selectedCardName = e.getData();
        var children = cardContainer.getChildren();
        var selectedCard;
        for (var i = 0; i < children.length; i++) {
          var child = children[i];
          if (child.getUserData("cardName") == selectedCardName) {
            selectedCard = child;
          }
        }
        if (selectedCard) {
          this._selectCard(cardContainer, selectedCard);
        }
      }, this);
      return cardContainer;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteBorderContainer {org.jspresso.framework.gui.remote.RBorderContainer}
     */
    _createBorderContainer: function (remoteBorderContainer) {
      var borderContainer = new qx.ui.mobile.container.Composite();
      var borderLayout = new qx.ui.mobile.layout.VBox();
      borderContainer.setLayout(borderLayout);
      if (remoteBorderContainer.getNorth()) {
        var child = this.createComponent(remoteBorderContainer.getNorth());
        borderContainer.add(child);
      }
      if (remoteBorderContainer.getCenter()) {
        var child = this.createComponent(remoteBorderContainer.getCenter());
        borderContainer.add(child, {flex:1});
      }
      if (remoteBorderContainer.getSouth()) {
        var child = this.createComponent(remoteBorderContainer.getSouth());
        borderContainer.add(child);
      }
      return borderContainer;
    },

    /**
     * @param cardComponent {qx.ui.mobile.page.NavigationPage}
     * @return {undefined}
     */
    _addDetailPage: function (cardComponent) {
      /** @type {org.jspresso.framework.application.frontend.controller.qx.mobile.MobileQxController} */
      this._getActionHandler().addDetailPage(cardComponent);
    },

    /**
     *
     * @return {undefined}
     * @param rCardComponent  {org.jspresso.framework.gui.remote.RCardContainer}
     * @param cardContainer {qx.ui.mobile.container.Composite}
     * @param cardName {String}
     */
    addCard: function (cardContainer, rCardComponent, cardName) {
      if(rCardComponent instanceof org.jspresso.framework.gui.remote.mobile.RMobilePage) {
        var children = cardContainer.getChildren();
        var existingCards = cardContainer.getUserData("existingCards");
        var existingCard = existingCards.indexOf(cardName) >= 0;
        if (!existingCard) {
          existingCards.push(cardName);
          var cardComponent = this.createComponent(rCardComponent);
          // Do not actually add the card to the card container since it's added to the manager.
          // cardContainer.add(cardComponent);
          this.linkNextPageBackButton(cardComponent, cardContainer.getUserData("previousPage"));
          this._selectCard(cardContainer, cardComponent);
        }
      }
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     */
    _createCardContainerComponent: function () {
      return new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.Card());
    },

    /**
     *
     * @return {undefined}
     * @param cardContainer {qx.ui.mobile.container.Composite}
     * @param selectedCard  {qx.ui.mobile.core.Widget}
     */
    _selectCard: function (cardContainer, selectedCard) {
      cardContainer.setUserData("currentPage", selectedCard);
      selectedCard.show();
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     */
    _createDefaultComponent: function () {
      return new qx.ui.mobile.core.Widget();
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteActionField {org.jspresso.framework.gui.remote.RActionField}
     */
    _createActionField: function (remoteActionField) {

      /** @type {qx.ui.mobile.form.TextField} */
      var textField;
      if (remoteActionField.getShowTextField()) {
        textField = new qx.ui.mobile.form.TextField();
        textField.setPlaceholder(remoteActionField.getLabel());
      }
      var toDecorate = textField;
      if (!textField) {
        toDecorate = new qx.ui.mobile.core.Widget();
      }
      var actionField = this._decorateWithAsideActions(toDecorate, remoteActionField, true);
      var state = remoteActionField.getState();
      var modelController = new qx.data.controller.Object(state);
      var mainAction = remoteActionField.getActionLists()[0].getActions()[0];
      if (textField) {
        if (remoteActionField.getFieldEditable()) {
          modelController.addTarget(textField, "readOnly", "writable", false, {
            converter: this._readOnlyFieldConverter
          });
        } else {
          textField.setReadOnly(true);
        }
        var triggerAction = function (e) {
          var content = e.getData();
          if (content && content.length > 0) {
            if (content != state.getValue()) {
              textField.setValue(state.getValue());
              var actionEvent = new org.jspresso.framework.gui.remote.RActionEvent();
              actionEvent.setActionCommand(content);
              this._getActionHandler().execute(mainAction, actionEvent);
            }
          } else {
            state.setValue(null);
          }
        };
        textField.addListener("changeValue", triggerAction, this);

        modelController.addTarget(textField, "value", "value", false, {
          converter: this._modelToViewFieldConverter
        });
        if (remoteActionField.getCharacterAction()) {
          textField.addListener("input", function (event) {
            var actionEvent = new org.jspresso.framework.gui.remote.RActionEvent();
            actionEvent.setActionCommand(textField.getValue());
            this._getActionHandler().execute(remoteActionField.getCharacterAction(), actionEvent);
          }, this);
        }
        textField.addListener("tap", function (event) {
          if (textField.getReadOnly() && mainAction.getEnabled()) {
            var actionEvent = new org.jspresso.framework.gui.remote.RActionEvent();
            this._getActionHandler().execute(mainAction, actionEvent);
          }
        }, this);
      }
      return actionField;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param rComponent {org.jspresso.framework.gui.remote.RComponent}
     */
    _createFormattedField: function (rComponent) {
      var formattedField = new qx.ui.mobile.form.TextField();
      formattedField.setPlaceholder(rComponent.getLabel());
      this._bindFormattedField(formattedField, rComponent);
      return formattedField;
    },

    /**
     * @return {undefined}
     * @param component {qx.ui.mobile.core.Widget}
     * @param alignment {String}
     */
    _configureHorizontalAlignment: function (component, alignment) {
      // NO-OP
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteLabel {org.jspresso.framework.gui.remote.RLabel}
     */
    _createLabel: function (remoteLabel) {
      var atom = new qx.ui.mobile.basic.Atom();
      var label = atom.getLabelWidget();
      var state = remoteLabel.getState();
      if (state) {
        var modelController = new qx.data.controller.Object(state);
        if (remoteLabel instanceof org.jspresso.framework.gui.remote.RLink && remoteLabel.getAction()) {
          this.__remotePeerRegistry.register(remoteLabel.getAction());
          atom.setRich(true);
          modelController.addTarget(atom, "label", "value", false, {
            converter: function (modelValue, model) {
              if (modelValue) {
                return "<u><a href='javascript:'>" + modelValue + "</a></u>";
              }
              return modelValue;
            }
          });
          atom.addListener("tap", function (event) {
            this._getActionHandler().execute(remoteLabel.getAction());
          }, this);
        } else {
          modelController.addTarget(atom, "label", "value", false, {
            converter: function (modelValue, model) {
              return modelValue;
            }
          });
        }
      } else {
        atom.setLabel(remoteLabel.getLabel());
      }
      this._configureHorizontalAlignment(label, remoteLabel.getHorizontalAlignment());
      if (remoteLabel.getIcon()) {
        atom.setIcon(remoteLabel.getIcon().getImageUrlSpec());
      }
      return atom;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteDateField {org.jspresso.framework.gui.remote.RDateField}
     */
    _createDateField: function (remoteDateField) {
      var dateField = this._createFormattedField(remoteDateField);
      //dateField._setStyle("width", "initial");
      if (!this.__monthNames) {
        this.__monthNames = [];
        for (var i = 1; i <= 9; i++) {
          this.__monthNames.push(this._getActionHandler().translate("m_0" + i));
        }
        for (var i = 10; i <= 12; i++) {
          this.__monthNames.push(this._getActionHandler().translate("m_" + i));
        }
      }
      var datePickerButton = this.createButton("...", null, null);
      datePickerButton.removeCssClass("button");
      var datePicker = new org.jspresso.framework.view.qx.mobile.DatePicker(datePickerButton, this.__monthNames);
      datePickerButton.addListener("tap", function(e) {
        var current = remoteDateField.getState().getValue();
        if (current) {
          if (current instanceof Date) {
            current = org.jspresso.framework.util.format.DateUtils.fromDate(new Date());
          }
        } else {
          current = org.jspresso.framework.util.format.DateUtils.fromDate(new Date());
          current.setHour(0);
          current.setMinute(0);
          current.setSecond(0);
        }
        datePicker.setSelectedIndex(2, datePicker.getYearIndex(current.getYear()));
        datePicker.setSelectedIndex(1, current.getMonth());
        datePicker.setSelectedIndex(0, current.getDate()-1);
        datePicker.show();
      }, this);
      var dateFieldWithPicker = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.HBox());
      dateFieldWithPicker.add(dateField, {flex:1});
      dateFieldWithPicker.add(datePickerButton, {flex:0});
      datePicker.addListener("confirmSelection", function(e) {
        var current = remoteDateField.getState().getValue();
        if (current) {
          if (current instanceof Date) {
            current = org.jspresso.framework.util.format.DateUtils.fromDate(new Date());
          }
        } else {
          current = org.jspresso.framework.util.format.DateUtils.fromDate(new Date());
          current.setHour(0);
          current.setMinute(0);
          current.setSecond(0);
        }
        var date = new Number(e.getData()[0].item);
        var month = e.getData()[1].index;
        var year = new Number(e.getData()[2].item);
        var dateDto = new org.jspresso.framework.util.lang.DateDto();
        dateDto.setYear(year);
        dateDto.setMonth(month);
        dateDto.setDate(date);
        dateDto.setHour(current.getHour());
        dateDto.setMinute(current.getMinute());
        dateDto.setSecond(current.getSecond());
        remoteDateField.getState().setValue(dateDto);
      }, this);
      return dateFieldWithPicker;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteTimeField {org.jspresso.framework.gui.remote.RTimeField}
     */
    _createTimeField: function (remoteTimeField) {
      var timeField = this._createFormattedField(remoteTimeField);
      //timeField._setStyle("width", "initial");
      return timeField;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteColorField {org.jspresso.framework.gui.remote.RColorField}
     */
    _createColorField: function (remoteColorField) {
      var colorField = this._createFormattedField(remoteColorField);
      colorField._setStyle("width", "initial");
      return colorField;
    },

    /**
     *
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteList {org.jspresso.framework.gui.remote.mobile.RMobileList}
     */
    _createList: function (remoteList) {
      var listModel = remoteList.getState().getChildren();

      var rendererType;
      if (remoteList.getSelectionMode() == "SINGLE_SELECTION"
          || remoteList.getSelectionMode() == "SINGLE_CUMULATIVE_SELECTION") {
        rendererType = qx.ui.mobile.list.renderer.Default;
      } else {
        rendererType = org.jspresso.framework.view.qx.mobile.CheckBoxListItemRenderer;
      }

      var list = new qx.ui.mobile.list.List({
        configureItem: function (item, data, row) {
          var selectedIndices = remoteList.getState().getSelectedIndices();
          var selected = selectedIndices != null && selectedIndices.indexOf(row) >= 0;
          org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.bindListItem(item,  data, selected);
          if(remoteList instanceof org.jspresso.framework.gui.remote.mobile.RMobileList) {
            item.setShowArrow(remoteList.getShowArrow());
          }
        },
        createItemRenderer : function() {
          return new rendererType();
        }
      });

      list.setModel(listModel);

      list.addListener("changeSelection", function(evt) {
        var selectedIndex = evt.getData();
        var state = remoteList.getState();
        if (remoteList.getSelectionMode() == "SINGLE_SELECTION"
            || remoteList.getSelectionMode() == "SINGLE_CUMULATIVE_SELECTION") {
          state.setLeadingIndex(selectedIndex);
          state.setSelectedIndices([selectedIndex]);
        } else {
          var selectedIndices = state.getSelectedIndices();
          if(selectedIndices == null) {
            selectedIndices = [];
          } else {
            selectedIndices = selectedIndices.concat([]);
          }
          var selected = selectedIndices.indexOf(selectedIndex);
          if(selected >= 0) {
            selectedIndices.splice(selected, 1);
            if(selectedIndices.length > 0) {
              state.setLeadingIndex(selectedIndices[0]);
            } else {
              state.setLeadingIndex(-1);
            }
          } else {
            selectedIndices.push(selectedIndex);
            selectedIndices.sort();
            state.setLeadingIndex(selectedIndex);
          }
          state.setSelectedIndices(selectedIndices);
        }
      }, this);
      return list;
    },

    /**
     *
     * @param remoteCheckBox {org.jspresso.framework.gui.remote.RCheckBox}
     * @param checkBox {qx.ui.mobile.form.CheckBox}
     */
    _bindCheckBox: function (remoteCheckBox, checkBox) {
      var state = remoteCheckBox.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(checkBox, "value", "value", true);
      modelController.addTarget(checkBox, "enabled", "writable", false);
    },

    /**
     * @param component {qx.ui.mobile.page.NavigationPage}
     * @return {undefined}
     */
    focus: function (component) {
      if (component) {
        component.show();
      }
    },

    /**
     * @param component {qx.ui.mobile.page.NavigationPage}
     * @return {undefined}
     */
    edit: function (component) {
      var editorPage = component.getUserData("editorPage");
      if (editorPage) {
        editorPage.show({animation: "flip"});
      }
    },

    /**
     * @param formattedField {qx.ui.mobile.form.TextField}
     * @param rComponent {org.jspresso.framework.gui.remote.RComponent}
     */
    _bindFormattedField: function (formattedField, rComponent) {
      var format = this._createFormat(rComponent);
      var state = rComponent.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(formattedField, "value", "value", true, {
        converter: function (modelValue, model) {
          if (modelValue == null) {
            return "";
          }
          var formattedValue = modelValue;
          if (format) {
            formattedValue = format.format(modelValue);
          }
          return formattedValue;
        }
      }, {
        converter: function (viewValue, model) {
          var parsedValue = viewValue;
          if (viewValue == null || viewValue.length == 0) {
            parsedValue = null;
          } else if (format) {
            try {
              parsedValue = format.parse(viewValue);
            } catch (ex) {
              // restore old value.
              parsedValue = state.getValue();
              if (parsedValue) {
                formattedField.setValue(format.format(parsedValue));
              } else {
                formattedField.setValue("");
              }
            }
          }
          return parsedValue;
        }
      });
      modelController.addTarget(formattedField, "readOnly", "writable", false, {
        converter: this._readOnlyFieldConverter
      });
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteNumericComponent {org.jspresso.framework.gui.remote.RNumericComponent}
     */
    _createNumericComponent: function (remoteNumericComponent) {
      var numericComponent = this.base(arguments, remoteNumericComponent);
      var maxChars = -1;
      if (remoteNumericComponent.getMinValue() != null && remoteNumericComponent.getMaxValue() != null) {
        var formatter = this._createFormat(remoteNumericComponent);
        maxChars = Math.max(formatter.format(remoteNumericComponent.getMaxValue()).length,
            formatter.format(remoteNumericComponent.getMinValue()).length);
        if (numericComponent instanceof qx.ui.mobile.form.TextField) {
          numericComponent.setMaxLength(maxChars);
        }
      }
      this._configureHorizontalAlignment(numericComponent, remoteNumericComponent.getHorizontalAlignment());
      return numericComponent;
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteIntegerField {org.jspresso.framework.gui.remote.RIntegerField}
     */
    _createIntegerField: function (remoteIntegerField) {
      var integerField = this._createFormattedField(remoteIntegerField);
      return integerField;
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteDecimalField {org.jspresso.framework.gui.remote.RDecimalField}
     */
    _createDecimalField: function (remoteDecimalField) {
      var decimalField = this._createFormattedField(remoteDecimalField);
      return decimalField;
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remotePercentField {org.jspresso.framework.gui.remote.RPercentField}
     */
    _createPercentField: function (remotePercentField) {
      var percentField = this._createFormattedField(remotePercentField);
      return percentField;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteDateField {org.jspresso.framework.gui.remote.RDateField}
     */
    _createDateTimeField: function (remoteDateField) {
      var dateTimeField = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.HBox())
      var oldType = remoteDateField.getType();
      try {
        remoteDateField.setType("DATE");
        dateTimeField.add(this._createDateField(remoteDateField), {flex:1});
      } catch (e) {
        throw e;
      } finally {
        remoteDateField.setType(oldType);
      }

      var remoteTimeField = new org.jspresso.framework.gui.remote.RTimeField();
      remoteTimeField.setBackground(remoteDateField.getBackground());
      remoteTimeField.setBorderType(remoteDateField.getBorderType());
      remoteTimeField.setFont(remoteDateField.getFont());
      remoteTimeField.setForeground(remoteDateField.getForeground());
      remoteTimeField.setGuid(remoteDateField.getGuid());
      remoteTimeField.setState(remoteDateField.getState());
      remoteTimeField.setToolTip(remoteDateField.getToolTip());
      remoteTimeField.setSecondsAware(remoteDateField.getSecondsAware());
      remoteTimeField.useDateDto(true);
      dateTimeField.add(this.createComponent(remoteTimeField, false), {flex:1});
      return dateTimeField;
    },

    /**
     * @return {Boolean}
     * @param rComponent {org.jspresso.framework.gui.remote.RComponent}
     */
    _isFixedWidth: function (rComponent) {
      return rComponent instanceof org.jspresso.framework.gui.remote.RCheckBox || rComponent
          instanceof org.jspresso.framework.gui.remote.RLabel;
    }

  }
});
