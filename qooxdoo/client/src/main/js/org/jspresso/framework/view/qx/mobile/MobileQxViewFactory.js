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

/**
 * @asset (org/jspresso/framework/mobile/back-mobile.svg)
 * @asset (org/jspresso/framework/mobile/my_location-mobile.svg)
 */
qx.Class.define("org.jspresso.framework.view.qx.mobile.MobileQxViewFactory", {
  extend: org.jspresso.framework.view.qx.AbstractQxViewFactory,

  statics: {
    __GET_CHILDREN_METHOD: "getChildren",
    __CURRENT_PAGE: "currentPage",
    __CARD_NAME: "cardName",
    __EXISTING_CARDS: "existingCards",
    __EXISTING_CARD_NAMES: "existingCardNames",

    refineListItemClasses: function (item, title, subtitle) {
      var htmlRegExp = /.*<\/.*>.*/;
      if (title && title.match(htmlRegExp)) {
        // Title is self formatted
        item.getTitleWidget().removeCssClass("list-item-title");
      }
      if (subtitle && subtitle.match(htmlRegExp)) {
        // Subtitle is self formatted
        item.getSubtitleWidget().removeCssClass("list-item-subtitle");
      }
    },

    bindListItem: function (item, state, selected, displayIcon) {
      var children = state.getChildren();
      var title;
      if (children.length > 1 && !(item instanceof org.jspresso.framework.view.qx.mobile.TreeItemRenderer)) {
        title = children.getItem(1).getValue();
      } else if (state.getValue()) {
        title = state.getValue();
      } else {
        title = "";
      }
      var subtitle = state.getDescription();
      item.setTitle(title);
      item.setSubtitle(subtitle);
      org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.refineListItemClasses(item, title, subtitle);
      if (displayIcon) {
        if (state.getIconImageUrl()) {
          item.setImage(state.getIconImageUrl());
        }
      } else {
        item.setImage(null);
      }
      item.setSelected(selected);
    }
  },

  construct: function (remotePeerRegistry, actionHandler, commandHandler) {
    this.base(arguments, remotePeerRegistry, actionHandler, commandHandler);
  },

  members: {

    /** @type {Array} */
    __monthNames: null,
    /** @type {qx.ui.mobile.form.TextField} */
    __textComponentToBlur: null,

    /**
     * @return {qx.ui.mobile.core.Widget}
     */
    _createEmptyWidget: function () {
      return new qx.ui.mobile.core.Widget();
    },

    /**
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @return {qx.ui.mobile.core.Widget}
     */
    _createCustomComponent: function (remoteComponent) {
      if (remoteComponent instanceof org.jspresso.framework.gui.remote.mobile.RImagePicker) {
        return this._createImagePicker(remoteComponent);
      } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.mobile.RImageCanvas) {
        return this._createImageCanvas(remoteComponent);
      }
      return null;
    },

    _visibleFieldConverter: function (readable, model) {
      return readable ? "visible" : "excluded";
    },

    _bindVisibility: function (remoteComponent, component) {
      if (component instanceof qx.ui.mobile.core.Widget) {
        this.base(arguments, remoteComponent, component);
      }
    },

    /**
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @param registerPeers {Boolean}
     * @return {qx.ui.mobile.core.Widget}
     */
    createComponent: function (remoteComponent, registerPeers) {
      var component = this.base(arguments, remoteComponent, registerPeers);
      if (remoteComponent && component) {
        component = this._decorateWithActions(remoteComponent, component);
      }
      if (!(remoteComponent instanceof org.jspresso.framework.gui.remote.mobile.RMobilePageAware) && !(component
          instanceof qx.ui.mobile.page.Page)) {
        this._bindVisibility(remoteComponent, component);
      }
      return component;
    },

    /**
     * @param component {qx.ui.mobile.core.Widget}
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @return {undefined}
     */
    applyComponentStyle: function (component, remoteComponent) {
      this._applyStyleName(component, remoteComponent.getStyleName());
    },

    /**
     * @param component {qx.ui.mobile.core.Widget}
     * @param styleName {String}
     * @return {undefined}
     */
    _applyStyleName: function (component, styleName) {
      if (styleName) {
        component.addCssClass(styleName);
      }
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
      var actions = this.extractAllActions(remoteComponent.getActionLists());
      if (actions.length > 0) {
        var modelController;
        if (disableActionsWithField && remoteComponent && remoteComponent.getState()) {
          modelController = new qx.data.controller.Object(remoteComponent.getState());
        }
        var maxToolbarActionCount = 2;
        var hBox = new qx.ui.mobile.layout.HBox();
        hBox.setAlignY("middle");
        var actionField = new qx.ui.mobile.container.Composite(hBox);
        var toolBar = this.createToolBarFromActions(actions, maxToolbarActionCount, modelController);
        toolBar.removeCssClass("toolbar");
        var buttons = toolBar.getChildren();
        for (var i = 0; i < buttons.length; i++) {
          buttons[i].removeCssClass("toolbar-button");
        }
        if (component) {
          actionField.add(component, {flex: 1});
          actionField.add(toolBar);
          return actionField;
        } else {
          return toolBar
        }
      }
      return component;
    },

    /**
     * @param extraActions {qx.data.Array}
     * @return {qx.ui.mobile.toolbar.Button}
     */
    _createExtraActionsToolBarButton: function (extraActions) {
      var extraButton = new qx.ui.mobile.toolbar.Button("...");
      //extraButton.removeCssClass("toolbar-button");
      var extraMenu = new qx.ui.mobile.dialog.Menu();
      extraMenu.getSelectionList().setDelegate({
        configureItem: function (item, data, row) {
          item.setTitle(data.getName());
          if (data.getIcon()) {
            item.setImage(
                org.jspresso.framework.view.qx.AbstractQxViewFactory.completeForSVG(data.getIcon().getImageUrlSpec()));
          }
          item.setEnabled(data.getEnabled());
          item.setSelectable(data.getEnabled());
          var atLeastOneAction = false;
          if (data.getHiddenWhenDisabled() && !data.getEnabled()) {
            item.setVisibility("excluded");
          } else {
            item.setVisibility("visible");
            atLeastOneAction = true;
          }
          if (atLeastOneAction) {
            extraButton.setVisibility("visible");
          } else {
            extraButton.setVisibility("excluded");
          }
        }
      });
      for (var i = 0; i < extraActions.getLength(); i++) {
        this._getRemotePeerRegistry().register(extraActions.getItem(i));
      }
      extraMenu.getSelectionList().setModel(extraActions);
      extraMenu.setAnchor(extraButton);
      extraMenu.addListener("changeSelection", function (evt) {
        extraMenu.hide();
        var selectedIndex = evt.getData()["index"];
        if (selectedIndex >= 0) {
          this._getActionHandler().execute(extraActions.getItem(selectedIndex));
          extraMenu.setSelectedIndex(-1);
        }
      }, this);
      this.addButtonListener(extraButton, function (evt) {
        extraMenu.show();
      }, this);
      return extraButton;
    },

    _reworkToolBar: function (toolBar, maxToolbarActionCount, toolBarButtons, actions, extraActions,
                              extraActionsToolBarButton) {
      extraActions.removeAll();
      toolBar.removeAll();
      var countAdded = 0;
      var extraActionFirstIndex = -1;
      for (var i = 0; i < toolBarButtons.length; i++) {
        var toolBarButton = toolBarButtons[i];
        if (toolBarButton.isVisible()) {
          if (countAdded < (maxToolbarActionCount - (extraActionsToolBarButton ? 1 : 0))) {
            toolBar.add(toolBarButton);
            countAdded++;
          } else {
            if (extraActionFirstIndex < 0) {
              extraActionFirstIndex = i;
            }
            extraActions.push(actions[i]);
          }
        }
      }
      if (extraActionsToolBarButton) {
        if (extraActions.getLength() > 1) {
          toolBar.add(extraActionsToolBarButton);
        } else if (extraActionFirstIndex >= 0) {
          toolBar.add(toolBarButtons[extraActionFirstIndex]);
        }
      }
      this.__sizeToolBarContent(toolBar);
    },

    __sizeToolBarContent: function (toolBar) {
      var maxH = 0;
      var children = toolBar.getChildren();
      for (var childI = 0; childI < children.length; childI++) {
        if (children[childI].getContainerElement().offsetHeight > maxH) {
          maxH = children[childI].getContainerElement().offsetHeight;
        }
      }
      if (maxH > 0) {
        for (childI = 0; childI < children.length; childI++) {
          if (children[childI].getVisibility() == "visible") {
            children[childI]._setStyle("height", maxH + "px");
          }
        }
      }
    },

    /**
     * @param actions {Array}
     * @param maxToolbarActionCount {Integer}
     * @param modelController {qx.data.controller.Object}
     * @return {qx.ui.mobile.toolbar.ToolBar}
     */
    createToolBarFromActions: function (actions, maxToolbarActionCount, modelController) {
      var toolBar = new qx.ui.mobile.toolbar.ToolBar();
      toolBar.addCssClass("jspresso-toolbar");
      var toolBarButtons = [];
      var extraActions = new qx.data.Array();
      for (var i = 0; i < actions.length; i++) {
        var toolBarButton = this.createToolBarAction(actions[i]);
        toolBarButtons.push(toolBarButton);
        if (modelController) {
          modelController.addTarget(toolBarButton, "enabled", "writable", false);
        }
      }
      var extraActionsToolBarButton = null;
      if (actions.length > maxToolbarActionCount) {
        extraActionsToolBarButton = this._createExtraActionsToolBarButton(extraActions);
        if (modelController) {
          modelController.addTarget(extraActionsToolBarButton, "enabled", "writable", false);
        }
      }
      this._reworkToolBar(toolBar, maxToolbarActionCount, toolBarButtons, actions, extraActions,
          extraActionsToolBarButton);
      for (var i = 0; i < toolBarButtons.length; i++) {
        toolBarButtons[i].addListener("changeVisibility", function (e) {
          this._reworkToolBar(toolBar, maxToolbarActionCount, toolBarButtons, actions, extraActions,
              extraActionsToolBarButton);
        }, this);
      }
      toolBar.addListener("appear", function (e) {
        qx.event.Timer.once(function () {
              this.__sizeToolBarContent(toolBar);
            }, this,
            org.jspresso.framework.application.frontend.controller.qx.mobile.MobileQxController.ANIMATION_DURATION);
      }, this);
      return toolBar;
    },

    /**
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @param component {qx.ui.mobile.core.Widget}
     */
    _addToolBarActions: function (remoteComponent, component) {
      var maxToolbarActionCount = 3;
      var actions = this.extractAllActions(remoteComponent.getActionLists());
      if (actions.length > 0) {
        var toolBar = this.createToolBarFromActions(actions, maxToolbarActionCount, null);
        if (component instanceof qx.ui.mobile.page.Page) {
          component.addListener("initialize", function (e) {
            component.add(toolBar);
          }, this);
        } else {
          component.add(toolBar);
        }
      }
    },

    /**
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @param component {qx.ui.mobile.core.Widget}
     * @return {qx.ui.mobile.core.Widget}
     */
    _decorateWithActions: function (remoteComponent, component) {
      if (remoteComponent.getFocusGainedAction()) {
        component.addListener("focusin", function (event) {
          this._getActionHandler().execute(remoteComponent.getFocusGainedAction());
        }, this);
      }
      if (remoteComponent instanceof org.jspresso.framework.gui.remote.RTextField || remoteComponent
          instanceof org.jspresso.framework.gui.remote.RDateField || remoteComponent
          instanceof org.jspresso.framework.gui.remote.RNumericComponent || remoteComponent
          instanceof org.jspresso.framework.gui.remote.RLabel || remoteComponent
          instanceof org.jspresso.framework.gui.remote.RTimeField || remoteComponent
          instanceof org.jspresso.framework.gui.remote.RComboBox || remoteComponent
          instanceof org.jspresso.framework.gui.remote.RCheckBox) {
        return this._decorateWithAsideActions(component, remoteComponent, false);
      } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RForm || remoteComponent
          instanceof org.jspresso.framework.gui.remote.mobile.RMobilePage) {
        this._addToolBarActions(remoteComponent, /** @type {qx.ui.mobile.container.Composite} */ component);
        return component;
      } else {
        return component;
      }
    },

    loseFocus: function () {
      if (this.__textComponentToBlur) {
        this.__textComponentToBlur.blur();
      }
    },

    /**
     * @param page {qx.ui.mobile.page.NavigationPage}
     * @param pageAction {org.jspresso.framework.gui.remote.RAction}
     */
    _installNavigationPageMainAction: function (page, pageAction) {
      this._getRemotePeerRegistry().register(pageAction);
      page.setButtonText(pageAction.getName());
      if (pageAction.getIcon()) {
        page.setButtonIcon(org.jspresso.framework.view.qx.AbstractQxViewFactory.completeForSVG(
            pageAction.getIcon().getImageUrlSpec()));
      }
      page.addListener("action", function (event) {
        this._getActionHandler().execute(pageAction);
      }, this);
      page.setShowButton(pageAction.getEnabled());
      pageAction.addListener("changeEnabled", function (evt) {
        page.setShowButton(evt.getData());
      }, this);
    },

    /**
     * @param page {qx.ui.mobile.container.Composite}
     * @param pageAction {org.jspresso.framework.gui.remote.RAction}
     */
    installPageMainAction: function (page, pageAction) {
      if (pageAction) {
        if (page instanceof qx.ui.mobile.page.NavigationPage) {
          this._installNavigationPageMainAction(page, pageAction);
        }
        // The following overrites the page main action in case of a single module workspace.
        /*
        if (page instanceof qx.ui.mobile.page.NavigationPage) {
            this._installNavigationPageMainAction(page, pageAction);
          } else {
            var pages = page.getUserData(org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.__EXISTING_CARDS);
            if (pages) {
              for (var i = 0; i < pages.length; i++) {
                this.installPageMainAction(pages[i], pageAction);
              }
            }
          }
          */
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
        this.linkNextPageBackButton(page, null, backAction, null);
      }
    },

    _getPageEndScroll: function (page) {
      var pageEndScroll = page.getUserData("pageEndScroll");
      if (!pageEndScroll) {
        pageEndScroll = page._getScrollContainer();
      }
      return pageEndScroll;
    },

    /**
     * @param page {qx.ui.mobile.page.NavigationPage}
     * @param pageEndAction {org.jspresso.framework.gui.remote.RAction}
     */
    installPageEndAction: function (page, pageEndAction) {
      if (pageEndAction) {
        page.addListener("initialize", function (e) {
          var pageEndScroll = this._getPageEndScroll(page);
          pageEndScroll.setWaypointsY(["100%"]);
          pageEndScroll.addListener("waypoint", function (e) {
            var data = e.getData();
            if (data.index == 0 && data.axis == "y" && data.direction == "down") {
              this._getActionHandler().execute(pageEndAction, null, function () {
                pageEndScroll.refresh();
              });
            }
          }, this);
        }, this);
      }
    },

    /**
     * @param page {qx.ui.mobile.page.NavigationPage}
     * @param swipeLeftAction {org.jspresso.framework.gui.remote.RAction}
     */
    installSwipeLeftAction: function (page, swipeLeftAction) {
      if (swipeLeftAction) {
        page.addListener("swipe", function (/** @type {qx.event.type.Swipe}*/ e) {
          if (e.getAxis() == "x" && e.getDirection() == "left") {
            this._getActionHandler().execute(swipeLeftAction);
          }
        }, this);
      }
    },

    /**
     * @param page {qx.ui.mobile.page.NavigationPage}
     * @param swipeRightAction {org.jspresso.framework.gui.remote.RAction}
     */
    installSwipeRightAction: function (page, swipeRightAction) {
      if (swipeRightAction) {
        page.addListener("swipe", function (/** @type {qx.event.type.Swipe}*/ e) {
          if (e.getAxis() == "x" && e.getDirection() == "right") {
            this._getActionHandler().execute(swipeRightAction);
          }
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
      this.installSwipeLeftAction(page, remotePage.getSwipeLeftAction());
      this.installSwipeRightAction(page, remotePage.getSwipeRightAction());
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
        var content = remoteContainer.getContent();
        content.setUserData(org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.__CURRENT_PAGE,
            remoteContainer.getUserData(org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.__CURRENT_PAGE));
        container = this.createComponent(content);
      } else if (remoteContainer instanceof org.jspresso.framework.gui.remote.RCardContainer) {
        container = this._createCardContainer(remoteContainer);
      } else if (remoteContainer instanceof org.jspresso.framework.gui.remote.RBorderContainer) {
        container = this._createBorderContainer(remoteContainer);
      } else if (remoteContainer instanceof org.jspresso.framework.gui.remote.REvenGridContainer) {
        container = this._createEvenGridContainer(remoteContainer);
      } else if (remoteContainer instanceof org.jspresso.framework.gui.remote.mobile.RMobileTabContainer) {
        container = this._createTabContainer(remoteContainer);
      }
      if (container instanceof qx.ui.mobile.page.NavigationPage) {
        this._bindDynamicLabel(container, remoteContainer);
        if (remoteContainer instanceof org.jspresso.framework.gui.remote.mobile.RMobilePageAware) {
          this.installPageActions(remoteContainer, container);
        }
        container.setUserData("pageGuid", remoteContainer.getGuid());
      }
      return container;
    },

    _computeLabelProperty: function (component) {
      var labelProperty;
      if (component instanceof qx.ui.mobile.page.NavigationPage) {
        labelProperty = "title";
      } else if (component instanceof qx.ui.mobile.basic.Label) {
        labelProperty = "value";
      }
      return labelProperty;
    },

    __copyPageActions: function (source, target) {
      if (!target.getEnterAction()) {
        target.setEnterAction(source.getEnterAction());
      }
      if (!target.getMainAction()) {
        target.setMainAction(source.getMainAction());
      }
      if (!target.getBackAction()) {
        target.setBackAction(source.getBackAction());
      }
      if (!target.getPageEndAction()) {
        target.setPageEndAction(source.getPageEndAction());
      }
      if (!target.getSwipeLeftAction()) {
        target.setSwipeLeftAction(source.getSwipeLeftAction());
      }
      if (!target.getSwipeRightAction()) {
        target.setSwipeRightAction(source.getSwipeRightAction());
      }
    },

    /**
     * @param remoteCardPage {org.jspresso.framework.gui.remote.mobile.RMobileCardPage}
     * @return {qx.ui.mobile.core.Widget}
     */
    _createMobileCardPage: function (remoteCardPage) {
      var rCardContainer = remoteCardPage.getPages();
      if (rCardContainer instanceof org.jspresso.framework.gui.remote.RCardContainer) {
        var remoteCards = rCardContainer.getCards();
        if (remoteCards) {
          for (var i = 0; i < remoteCards.length; i++) {
            var remoteCard = remoteCards[i];
            if (remoteCard instanceof org.jspresso.framework.gui.remote.mobile.RMobilePageAware) {
              this.__copyPageActions(remoteCardPage, remoteCard);
            }
          }
        }
      }
      /** @type {qx.ui.mobile.container.Composite} */
      var cardContainer = this.createComponent(rCardContainer);
      // Card pages will be added and selected automatically by the card container.
      cardContainer.setUserData("jspressoGuid", remoteCardPage.getGuid());
      return cardContainer;
    },

    /**
     *
     * @param nextPage {qx.ui.mobile.core.Widget}
     * @return {qx.ui.mobile.page.NavigationPage}
     */
    getActualPageToShow: function (nextPage) {
      var pageToShow;
      // Because of MobileCardPage
      if (nextPage instanceof qx.ui.mobile.page.NavigationPage) {
        pageToShow = nextPage;
      } else {
        var currentPage = nextPage.getUserData(
            org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.__CURRENT_PAGE);
        if (currentPage != null) {
          // This is a card container or a border container with a nested page
          pageToShow = this.getActualPageToShow(currentPage);
        } else {
          var pages = nextPage.getUserData(org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.__EXISTING_CARDS);
          if (pages && pages.length > 0) {
            pageToShow = pages[pages.length - 1];
          } else if (nextPage[org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.__GET_CHILDREN_METHOD]) {
            var children = nextPage.getChildren();
            if (children && children.length == 1) {
              pageToShow = this.getActualPageToShow(children[0]);
            }
          }
        }
      }
      return pageToShow;
    },

    /**
     * @param remoteNavPage {org.jspresso.framework.gui.remote.mobile.RMobileNavPage}
     * @return {qx.ui.mobile.core.Widget}
     */
    _createMobileNavPage: function (remoteNavPage) {
      var remoteSelectionComponent = remoteNavPage.getSelectionView();
      var actionLists = remoteNavPage.getActionLists();
      if (!actionLists) {
        actionLists = [];
      }
      var selectionActionLists = remoteNavPage.getSelectionView().getActionLists();
      if (selectionActionLists) {
        actionLists = actionLists.concat(selectionActionLists);
      }
      remoteNavPage.setActionLists(actionLists);
      /** @type {qx.ui.mobile.list.List} */
      var selectionComponent = this.createComponent(remoteSelectionComponent);
      var rHeaderSections = remoteNavPage.getHeaderSections();
      var needsSplittedContent = false;
      if (rHeaderSections && rHeaderSections.length > 0 && this._getActionHandler().isTablet()) {
        needsSplittedContent = true;
      }
      if (needsSplittedContent) {
        var center = false;
        for (var i = 0; (i < rHeaderSections.length) && !center; i++) {
          var rHeaderSection = rHeaderSections[i];
          if (rHeaderSection.getPosition() == "LEFT" || rHeaderSection.getPosition() == "RIGHT") {
            center = true;
          }
        }
        needsSplittedContent = center;
      }
      var navPage;
      if (needsSplittedContent) {
        navPage = new org.jspresso.framework.view.qx.mobile.NoScrollNavigationPage();
      } else {
        navPage = new qx.ui.mobile.page.NavigationPage();
      }
      if (remoteNavPage.getLabel()) {
        navPage.setTitle(remoteNavPage.getLabel());
      } else {
        navPage.setTitle(remoteSelectionComponent.getLabel());
      }

      var headerSections = this._createPageSections(rHeaderSections, navPage);
      var rEditAction = remoteNavPage.getEditAction();
      var rEditorPage = remoteNavPage.getEditorPage();
      if (rEditAction && rEditorPage) {
        var editorPage = this.createComponent(rEditorPage);
        this.linkNextPageBackButton(editorPage, navPage, null, "flip");
        navPage.setUserData("editorPage", editorPage);
        /*
        if (remoteNavPage.getMainAction() == null) {
          remoteNavPage.setMainAction(rEditAction);
        } else {
          var editActionList = new org.jspresso.framework.gui.remote.RActionList();
          editActionList.setActions([rEditAction]);
          var actionLists = [editActionList];
          if (remoteNavPage.getActionLists()) {
            actionLists = actionLists.concat(remoteNavPage.getActionLists());
          }
          remoteNavPage.setActionLists(actionLists);
        }
        */
      }
      navPage.addListener("initialize", function (e) {
        var scroll;
        var content = navPage.getContent();
        var contentLeft = content;
        var contentRight = content;
        var splittedContent;
        if (needsSplittedContent) {
          content.setLayout(new qx.ui.mobile.layout.VBox());
          splittedContent = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.HBox());
          contentLeft = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.VBox());
          contentRight = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.VBox());
          contentLeft.addCssClasses(["jspresso-tablet-content-left", "group"]);
          contentRight.addCssClasses(["jspresso-tablet-content-right", "group"]);
          splittedContent.add(this.decorateWithScrollContainer(contentLeft), {flex: 1});
          splittedContent.add(contentRight, {flex: 1});
        }
        if (remoteNavPage.getHeaderText()) {
          var headerLabel = new qx.ui.mobile.form.Label(remoteNavPage.getHeaderText());
          headerLabel.addCssClasses(["jspresso-header-label"]);
          content.add(headerLabel);
        }
        if (headerSections) {
          for (var i = 0; i < headerSections.length; i++) {
            var contentToAdd;
            var headerSection = headerSections[i];
            if (headerSection.getUserData("position") == "TOP" || headerSection.getUserData("position") == "BOTTOM") {
              contentToAdd = content;
            } else if (headerSection.getUserData("position") == "LEFT" || headerSection.getUserData("position")
                == "RIGHT") {
              contentToAdd = contentLeft;
            }
            if (headerSection instanceof org.jspresso.framework.gui.remote.RComponent) {
              this._addSectionHeader(contentToAdd, headerSection);
            } else {
              contentToAdd.add(headerSection);
            }
          }
        }

        if (needsSplittedContent) {
          content.add(splittedContent, {flex: 1});
        }

        this._addSectionHeader(contentRight, remoteNavPage.getSelectionView());
        if (needsSplittedContent) {
          var selectionScroll = new qx.ui.mobile.container.Scroll();
          //selectionScroll.removeCssClasses(["iscroll", "scroll"]);
          selectionScroll.add(selectionComponent, {flex: 1});
          contentRight.add(selectionScroll, {flex: 1});
          scroll = selectionScroll;
        } else {
          contentRight.add(selectionComponent);
          scroll = navPage._getScrollContainer();
        }
        navPage.setUserData("pageEndScroll", scroll);
        if (!needsSplittedContent) {
          selectionComponent.getModel().addListener("change", function (e) {
            var lastScrollTimestamp = scroll.getUserData("lastScrollTimestamp");
            if (!lastScrollTimestamp || e.getTimeStamp() - lastScrollTimestamp > 2000) {
              var scrollPosition = scroll.getPosition();
              var scrollElt = scroll.getContentElement();
              var selectionComponentElt = selectionComponent.getContentElement();
              var scPosition = qx.bom.element.Location.getRelative(scrollElt, selectionComponentElt, "scroll",
                  "scroll");
              if ((-scPosition.top - scrollPosition[1]) > scrollElt.clientHeight) {
                scroll.setUserData("lastScrollTimestamp", e.getTimeStamp());
                scroll.scrollToWidget(selectionComponent, 500);
              }
            }
          }, this);
        }
      }, this);
      var remoteNextPage = remoteNavPage.getNextPage();
      if (remoteNextPage) {
        /** @type {qx.ui.mobile.page.NavigationPage} */
        var nextPage = this.createComponent(remoteNextPage);
        this.linkNextPageBackButton(nextPage, navPage, null, null);
        selectionComponent.addListener("changeSelection", function (evt) {
          var oldSelectedIndex = selectionComponent.getUserData("oldSelectedIndex");
          var newSelectedIndex = evt.getData();
          selectionComponent.setUserData("oldSelectedIndex", newSelectedIndex);
          // Navigation will be handled by selection
          // change on server-side,triggering a page change.
          if (nextPage instanceof qx.ui.mobile.page.NavigationPage) {
            var pageToShow = this.getActualPageToShow(nextPage);
            if (pageToShow) {
              this._getActionHandler().showDetailPage(pageToShow);
            }
          }
        }, this);
        navPage.addListener("start", function () {
          if (!this._getActionHandler().hasAnimationQueued()) {
            var selectionState = remoteSelectionComponent.getState();
            selectionState.setLeadingIndex(-1);
            selectionState.setSelectedIndices([]);
          }
        }, this);
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
      // Because of MobileCardPage
      if (!(nextPage instanceof qx.ui.mobile.page.NavigationPage)) {
        if (!nextPage.getUserData("previousPage")) {
          var currentPage = nextPage.getUserData(
              org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.__CURRENT_PAGE);
          if (currentPage) {
            this.linkNextPageBackButton(currentPage, previousPage, backAction, animation);
          }
          var existingCards = nextPage.getUserData(
              org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.__EXISTING_CARDS);
          if (existingCards != null) {
            for (var i = 0; i < existingCards.length; i++) {
              if (currentPage != existingCards[i]) {
                this.linkNextPageBackButton(existingCards[i], previousPage, backAction, animation);
              }
            }
            nextPage.setUserData("previousPage", previousPage);
          } else if (nextPage[org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.__GET_CHILDREN_METHOD]) {
            var children = nextPage.getChildren();
            if (children && children.length == 1) {
              this.linkNextPageBackButton(children[0], previousPage, backAction, animation);
            }
          }
        }
        return;
      }

      if (typeof animation === undefined) {
        animation = "slide";
      }

      if (previousPage || backAction) {
        nextPage.setShowBackButton(true);
        var backButton = nextPage.getLeftContainer().getChildren()[0];
        backButton.setShow("both");
        if (backAction && backAction.getName()) {
          nextPage.setBackButtonText(backAction.getName());
        } else {
          backButton.setIcon("org/jspresso/framework/mobile/back-mobile.svg");
        }
        if (backAction) {
          nextPage.addListener("back", function () {
            this._getActionHandler().execute(backAction);
          }, this);
        }
        if (previousPage) {
          nextPage.addListener("back", function () {
            this._getActionHandler().showDetailPage(this.getActualPageToShow(previousPage), animation, true);
          }, this);
        }
      }
    },

    __createPageSectionForNavPage: function (navPage, holdingPage, remoteNavPage) {
      /** @type {qx.ui.mobile.page.NavigationPage} */
      var nextPage = navPage;
      this.linkNextPageBackButton(nextPage, holdingPage, null, null);
      var listModel = new qx.data.Array();
      remoteNavPage.setUserData("next", nextPage);
      if (remoteNavPage.getLabelState()) {
        this._getRemotePeerRegistry().register(remoteNavPage.getLabelState());
      }
      if (remoteNavPage.getToolTipState()) {
        this._getRemotePeerRegistry().register(remoteNavPage.getToolTipState());
      }
      listModel.push(remoteNavPage);
      var list = new qx.ui.mobile.list.List({
        configureItem: function (item, data, row) {
          var section = data;
          var title;
          var subtitle;
          if (section.getLabelState()) {
            title = section.getLabelState().getValue();
          } else {
            title = section.getLabel();
          }
          if (section.getToolTipState()) {
            subtitle = section.getToolTipState().getValue();
          } else {
            subtitle = section.getToolTip();
          }
          item.setTitle(title);
          item.setSubtitle(subtitle);
          org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.refineListItemClasses(item, title, subtitle);
          if (section.getIcon()) {
            item.setImage(org.jspresso.framework.view.qx.AbstractQxViewFactory.completeForSVG(
                section.getIcon().getImageUrlSpec()));
          }
          item.setShowArrow(true);
        }
      });
      list.addCssClass("jspresso-list");
      list.setModel(listModel);
      list.addListener("changeSelection", function (evt) {
        var selectedIndex = evt.getData();
        /** @type {qx.ui.mobile.list.List} */
        var l = evt.getCurrentTarget();
        var pageToShow = this.getActualPageToShow(l.getModel().getItem(selectedIndex).getUserData("next"));
        if (pageToShow && pageToShow.getVisibility() != "visible") {
          this._getActionHandler().showDetailPage(pageToShow);
        }
      }, this);
      list.setUserData("position", remoteNavPage.getPosition());
      return list;
    },

    /**
     *
     * @param rPageSections {org.jspresso.framework.gui.remote.RComponent[]}
     * @param holdingPage {qx.ui.mobile.page.NavigationPage}
     * @return {Array}
     */
    _createPageSections: function (rPageSections, holdingPage) {
      var sections = [];
      if (rPageSections) {
        for (var i = 0; i < rPageSections.length; i++) {
          var remotePageSection = rPageSections[i];
          remotePageSection.setUserData(org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.__CURRENT_PAGE,
              holdingPage);
          var pageSection = this.createComponent(remotePageSection);
          if (pageSection instanceof qx.ui.mobile.page.NavigationPage) {
            var list = this.__createPageSectionForNavPage(pageSection, holdingPage, remotePageSection);
            sections.push(list);
          } else {
            remotePageSection.setUserData("position", remotePageSection.getPosition());
            pageSection.setUserData("position", remotePageSection.getPosition());
            pageSection.setUserData(org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.__CURRENT_PAGE,
                holdingPage);
            sections.push(remotePageSection);
            sections.push(pageSection);
          }
        }
      }
      return sections;
    },

    /**
     * @param remoteCompositePage {org.jspresso.framework.gui.remote.mobile.RMobileCompositePage}
     * @return {qx.ui.mobile.core.Widget}
     */
    _createMobileCompositePage: function (remoteCompositePage) {
      var compositePage = new qx.ui.mobile.page.NavigationPage();
      compositePage.setTitle(remoteCompositePage.getLabel());
      var pageSections = remoteCompositePage.getPageSections();
      var sections = this._createPageSections(pageSections, compositePage);
      var rEditAction = remoteCompositePage.getEditAction();
      var rEditorPage = remoteCompositePage.getEditorPage();
      if (rEditAction && rEditorPage) {
        var editorPage = this.createComponent(rEditorPage);
        this.linkNextPageBackButton(editorPage, compositePage, null, "flip");
        compositePage.setUserData("editorPage", editorPage);
        /*
        if (remoteCompositePage.getMainAction() == null) {
          remoteCompositePage.setMainAction(rEditAction);
        } else {
          var editActionList = new org.jspresso.framework.gui.remote.RActionList();
          editActionList.setActions([rEditAction]);
          var actionLists = [editActionList];
          if (remoteCompositePage.getActionLists()) {
            actionLists = actionLists.concat(remoteCompositePage.getActionLists());
          }
          remoteCompositePage.setActionLists(actionLists);
        }
        */
      }
      compositePage.addListener("initialize", function (e) {
        var content = compositePage.getContent();
        if (remoteCompositePage.getHeaderText()) {
          var headerLabel = new qx.ui.mobile.form.Label(remoteCompositePage.getHeaderText());
          headerLabel.addCssClasses(["jspresso-header-label"]);
          content.add(headerLabel);
        }
        var contentTop = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.VBox());
        var contentCenter = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.VBox());
        var contentBottom = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.VBox());

        contentTop.addCssClasses(["jspresso-tablet-content-top", "group"]);
        contentCenter.addCssClasses(["jspresso-tablet-content-center", "group"]);
        contentBottom.addCssClasses(["jspresso-tablet-content-bottom", "group"]);

        var contentLeft = contentCenter;
        var contentRight = contentCenter;
        var needsSplittedContent = pageSections && pageSections.length > 1 && this._getActionHandler().isTablet();
        if (needsSplittedContent) {
          var left = false;
          var right = false;
          for (var i = 0; (i < sections.length) && !(left && right); i++) {
            var section = sections[i];
            if (section.getUserData("position") == "LEFT") {
              left = true;
            } else if (section.getUserData("position") == "RIGHT") {
              right = true;
            }
          }
          needsSplittedContent = (left && right);
        }

        if (needsSplittedContent) {
          var splittedContent = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.HBox());
          contentLeft = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.VBox());
          contentRight = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.VBox());
          contentLeft.addCssClasses(["jspresso-tablet-content-left", "group"]);
          contentRight.addCssClasses(["jspresso-tablet-content-right", "group"]);
          splittedContent.add(contentLeft, {flex: 1});
          splittedContent.add(contentRight, {flex: 1});
          contentCenter.add(splittedContent);
        }
        var contentToAdd = contentTop;
        for (var i = 0; i < sections.length; i++) {
          var section = sections[i];
          if (section.getUserData("position") == "TOP") {
            contentToAdd = contentTop;
          } else if (section.getUserData("position") == "LEFT") {
            contentToAdd = contentLeft;
          } else if (section.getUserData("position") == "RIGHT") {
            contentToAdd = contentRight;
          } else if (section.getUserData("position") == "BOTTOM") {
            contentToAdd = contentBottom;
          }
          if (section instanceof org.jspresso.framework.gui.remote.RComponent) {
            this._addSectionHeader(contentToAdd, section);
          } else {
            contentToAdd.add(section);
          }
        }
        if (contentTop.getChildren().length > 0) {
          content.add(contentTop, {flex: 1});
        }
        if (contentCenter.getChildren().length > 0) {
          content.add(contentCenter, {flex: 1});
        }
        if (contentBottom.getChildren().length > 0) {
          content.add(contentBottom, {flex: 1});
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
      if (button.getShow() == "both") {
        button.setIconPosition("top");
        // The following is for fixing #542
        var layout = button._getChildren()[0]._getLayout();
        var iconWidget = button.getIconWidget();
        var labelWidget = button.getLabelWidget();
        labelWidget.removeCssClass("label");
        layout._setLayoutProperty(iconWidget, "flex", 1);
        layout._setLayoutProperty(labelWidget, "flex", 0);
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
      if (label && icon) {
        button.setShow("both");
      } else if (label) {
        button.setShow("label");
        button.getLabelWidget().setWrap(true);
      } else {
        //button.setGap(0);
        //button.removeCssClass("gap");
        button.setShow("icon");
      }
      button.addCssClass("jspresso-button");
    },

    /**
     * @param button {qx.ui.mobile.form.Button | qx.ui.mobile.menu.Button}
     * @param listener {function}
     * @param that {var}
     */
    addButtonListener: function (button, listener, that) {
      this.addComponentThresholdListener(button, "tap", listener, that);
    },

    _addSectionHeader: function (container, remoteSection) {
      if (remoteSection && (remoteSection.getBorderType() == "TITLED" || remoteSection.getBorderType()
          == "TITLED_ACTIONS") && remoteSection.getLabel()) {
        var header = new qx.ui.mobile.form.Row();
        header.addCssClasses(["form-row-group-title", "jspresso-form-row-group-title"]);
        var sectionLabel = new qx.ui.mobile.basic.Label(remoteSection.getLabel());
        header.add(sectionLabel);
        this._bindDynamicLabel(sectionLabel, remoteSection);
        this._bindVisibility(remoteSection, header);
        container.add(header);
      }
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteForm {org.jspresso.framework.gui.remote.RForm}
     */
    _createForm: function (remoteForm) {
      var form = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.VBox());
      form.addCssClasses(["form", "single"]);
      var top = new qx.ui.mobile.form.Row();
      top.addCssClass("form-row-group-first");
      form.add(top);
      for (var i = 0; i < remoteForm.getElements().length; i++) {
        var rComponent = remoteForm.getElements()[i];

        var component = /** @type {qx.ui.mobile.core.Widget} */ this.createComponent(rComponent);

        var row = new qx.ui.mobile.container.Composite();
        row.addCssClass("form-row");
        row.addCssClass("form-row-content");
        if (remoteForm.getLabelsPosition() == "ABOVE") {
          row.setLayout(new qx.ui.mobile.layout.VBox().set({alignX: 'left'}));
        } else {
          row.setLayout(new qx.ui.mobile.layout.HBox());
        }
        component.bind("visibility", row, "visibility");
        if (remoteForm.getLabelsPosition() != "NONE") {
          var label = new qx.ui.mobile.form.Label("<p>" + rComponent.getLabel() + "</p>");
          component.bind("visibility", label, "visibility");
          // Changes label color when disabled
          // label.setLabelFor(component.getId());
          if (remoteForm.getLabelsPosition() == "ASIDE") {
            label.addCssClass("jspresso-form-label-aside");
          }
          // to align form elements to the left
          row.add(label/*, {flex:1}*/);
        } else {
          if (rComponent instanceof org.jspresso.framework.gui.remote.RCheckBox) {
            var wrapper = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.HBox());
            component.addCssClass("checkbox-form");
            wrapper.add(component);
            wrapper.add(new qx.ui.mobile.form.Label(rComponent.getLabel()));
            component = wrapper;
          }
        }

        if (rComponent instanceof org.jspresso.framework.gui.remote.RImageComponent) {
          var layout = new qx.ui.mobile.layout.HBox().set({alignY: "middle"});
          if (remoteForm.getLabelsPosition() == "NONE") {
            layout.setAlignX("center");
          } else {
            layout.setAlignX("left");
          }
          var wrapper = new qx.ui.mobile.container.Composite(layout);
          wrapper.add(component, {flex: 1});
          component = wrapper;
        }

        component.addCssClass("jspresso-form-element");
        if (this.isFixedWidth(rComponent)) {
          row.add(component);
        } else {
          if (remoteForm.getLabelsPosition() == "ASIDE") {
            component.addCssClass("jspresso-form-element-grow-aside");
          }
          row.add(component, {flex: 1});
        }
        if (this.isMultiline(rComponent)) {
          form.add(row, {flex: 1});
        } else {
          form.add(row);
        }
        if (i < remoteForm.getElements().length - 1) {
          var separator = new qx.ui.mobile.form.Row();
          separator.addCssClass("form-separation-row");
          form.add(separator);
          row.bind("visibility", separator, "visibility");
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
      textField.addListener("input", function (event) {
        this.__textComponentToBlur = textField;
        if (remoteTextField.getCharacterAction()) {
          textField.setUserData(org.jspresso.framework.view.qx.AbstractQxViewFactory._INPUT_TIMESTAMP,
              event.getTimeStamp());
          qx.event.Timer.once(function (e) {
            if (e.getTimeStamp() - textField.getUserData(
                org.jspresso.framework.view.qx.AbstractQxViewFactory._INPUT_TIMESTAMP)
                >= org.jspresso.framework.view.qx.AbstractQxViewFactory._INPUT_THRESHOLD) {
              var actionEvent = new org.jspresso.framework.gui.remote.RActionEvent();
              actionEvent.setActionCommand(textField.getValue());
              this._getActionHandler().execute(remoteTextField.getCharacterAction(), actionEvent);
            }
          }, this, org.jspresso.framework.view.qx.AbstractQxViewFactory._INPUT_THRESHOLD);
        }
      }, this);
      this.__bindReadOnlyBorder(state, textField);
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
      comboBox.setNullable(false);
      comboBox.setPlaceholder(remoteComboBox.getLabel());
      comboBox.setClearButtonLabel(this._getActionHandler().translate("Clear"));
      comboBox.setDialogTitle(remoteComboBox.getLabel());
      var cbModel = new qx.data.Array();
      for (var i = 0; i < remoteComboBox.getValues().length; i++) {
        var value = remoteComboBox.getValues()[i];
        var tr = remoteComboBox.getTranslations()[i];
        if (value == "") {
          // tr = remoteComboBox.getLabel();
          // tr = String.fromCharCode(0x00A0);
          comboBox.setNullable(true);
        } else {
          cbModel.push(tr);
        }
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
      comboBox.addListener("changeSelection", function (evt) {
        comboBox.setValue(evt.getData()["item"]);
      }, this);
      var state = remoteComboBox.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(comboBox, "value", "value", true, {
        converter: function (modelValue, model, source, target) {
          if (!modelValue) {
            return "";
          }
          var offset = 0;
          if (comboBox.getNullable()) {
            offset = 1;
          }
          return model.getItem(remoteComboBox.getValues().indexOf(modelValue) - offset);
        }
      }, {
        converter: function (modelValue, model, source, target) {
          if (!modelValue) {
            return "";
          }
          var offset = 0;
          if (comboBox.getNullable()) {
            offset = 1;
          }
          return remoteComboBox.getValues()[comboBox.getModel().indexOf(modelValue) + offset];
        }
      });
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
     * @param remoteMap {org.jspresso.framework.gui.remote.RMap}
     */
    _createMap: function (remoteMap) {
      var map = new org.jspresso.framework.view.qx.mobile.MapNavigationPage();
      map.setUserData("pageGuid", remoteMap.getGuid());
      map.setButtonIcon("org/jspresso/framework/mobile/my_location-mobile.svg");
      map.addListener("action", function (event) {
        map.moveToCurrentPosition();
      }, this);
      var state = remoteMap.getState();
      var childrenStates = state.getChildren();
      var mapContentState = childrenStates.getItem(0);
      var updateMap = function () {
        var mapContent = mapContentState.getValue();
        if (mapContent != null) {
          mapContent = JSON.parse(mapContent);
          var markers = mapContent["markers"];
          var routes = mapContent["routes"];
          if (markers || routes) {
            map.showMap();
            map.drawMapContent(markers, routes, remoteMap.getDefaultZoom());
          }
        } else {
          map.hideMap();
        }
      };
      map.addListener("initialize", updateMap);
      if (remoteMap.getPermId()) {
        map.addListener("changeZoom", function (event) {
          this.notifyMapChanged(remoteMap, event.getData());
        }, this);
      }
      mapContentState.addListener("changeValue", updateMap, this);
      map.setTitle(remoteMap.getLabel());
      map.setShowButton(true);
      this._addDetailPage(map);
      return map;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteTree {org.jspresso.framework.gui.remote.mobile.RMobileTree}
     */
    _createTree: function (remoteTree) {

      var treeListModel = org.jspresso.framework.state.remote.RemoteCompositeValueState.flatten(remoteTree.getState(),
          0);

      var treeList = new qx.ui.mobile.list.List({
        configureItem: function (item, data, row) {
          var state = data.state;
          org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.bindListItem(item, state, false,
              remoteTree.getDisplayIcon());
          item.setShowArrow(remoteTree.getShowArrow() && row > 0);
          item.setLevel(data.level);
        },
        createItemRenderer: function () {
          return new org.jspresso.framework.view.qx.mobile.TreeItemRenderer();
        }
      });
      treeList.addCssClass("jspresso-list");
      treeList.setModel(treeListModel);
      var selections = [];
      treeList.addListener("changeSelection", function (evt) {
        var futureDeselections = [];
        var localLevel = 0;
        for (var i = 1; i < treeListModel.length; i++) {
          var lowerNode = treeListModel.getItem(i);
          if (lowerNode.level > localLevel) {
            var currentNode = treeListModel.getItem(i - 1);
            if (currentNode.state.getSelectedIndices() != null && currentNode.state.getSelectedIndices().length > 0) {
              futureDeselections.push(currentNode.state);
            }
          }
          localLevel = lowerNode.level;
        }
        var selectedIndex = evt.getData();
        var currentNode = treeListModel.getItem(selectedIndex);
        var localIndex = 0;
        var futureSelections = [];
        for (var i = selectedIndex - 1; i >= 0; i--) {
          var upperNode = treeListModel.getItem(i);
          if (upperNode.level < currentNode.level) {
            futureSelections = [{
              state: upperNode.state,
              selection: [localIndex]
            }].concat(futureSelections);
            var j = futureDeselections.indexOf(upperNode.state);
            if (j >= 0) {
              futureDeselections[j] = null;
            }
            currentNode = upperNode;
            localIndex = 0;
          } else {
            localIndex++;
          }
        }
        for (var i = 0; i < futureDeselections.length; i++) {
          if (futureDeselections[i]) {
            futureDeselections[i].setLeadingIndex(-1);
            futureDeselections[i].setSelectedIndices(null);
          }
        }
        for (var i = 0; i < futureSelections.length; i++) {
          if (futureSelections[i]) {
            futureSelections[i].state.setLeadingIndex(futureSelections[i].selection[0]);
            futureSelections[i].state.setSelectedIndices(futureSelections[i].selection);
          }
        }
      }, this);

      this.addComponentThresholdListener(treeList, "changeSelection", function (evt) {
        if (remoteTree.getRowAction()) {
          this._getActionHandler().execute(remoteTree.getRowAction());
        }
      }, this);
      return treeList;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteCardContainer {org.jspresso.framework.gui.remote.RCardContainer}
     */
    _createCardContainer: function (remoteCardContainer) {
      var showAnimation = true;
      if (remoteCardContainer instanceof org.jspresso.framework.gui.remote.mobile.RMobileCardContainer) {
        showAnimation = false;
      }
      var cardContainer = this._createCardContainerComponent(showAnimation);
      cardContainer.setUserData(org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.__EXISTING_CARD_NAMES, []);
      cardContainer.setUserData(org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.__EXISTING_CARDS, []);

      for (var i = 0; i < remoteCardContainer.getCardNames().length; i++) {
        var rCardComponent = remoteCardContainer.getCards()[i];
        var cardName = remoteCardContainer.getCardNames()[i];

        this.addCard(cardContainer, rCardComponent, cardName);
      }

      /** @type {org.jspresso.framework.state.remote.RemoteValueState} */
      var state = remoteCardContainer.getState();
      state.addListener("changeValue", function (e) {
        var selectedCardName = e.getData();
        var cards = cardContainer.getUserData(
            org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.__EXISTING_CARDS);
        if (cards) {
          var selectedCard;
          for (var i = 0; i < cards.length; i++) {
            var child = cards[i];
            if (child.getUserData(org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.__CARD_NAME)
                == selectedCardName) {
              selectedCard = child;
            }
          }
          if (selectedCard) {
            this._selectCard(cardContainer, selectedCard);
          }
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
      this._addSectionHeader(borderContainer, remoteBorderContainer);
      var holdingPage = remoteBorderContainer.getUserData(
          org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.__CURRENT_PAGE);
      var north = remoteBorderContainer.getNorth();
      if (north) {
        var child = this.createComponent(north);
        if (child instanceof qx.ui.mobile.page.NavigationPage) {
          if (holdingPage) {
            child = this.__createPageSectionForNavPage(child, holdingPage, north);
            borderContainer.add(child);
          } else {
            borderContainer.setUserData(org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.__CURRENT_PAGE,
                child);
          }
        } else {
          this._addSectionHeader(borderContainer, north);
          borderContainer.add(child);
        }
      }
      var center = remoteBorderContainer.getCenter();
      if (center) {
        var child = this.createComponent(center);
        if (child instanceof qx.ui.mobile.page.NavigationPage) {
          if (holdingPage) {
            child = this.__createPageSectionForNavPage(child, holdingPage, center);
            borderContainer.add(child);
          } else {
            borderContainer.setUserData(org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.__CURRENT_PAGE,
                child);
          }
        } else {
          this._addSectionHeader(borderContainer, center);
          borderContainer.add(child, {flex: 1});
        }
      }
      var south = remoteBorderContainer.getSouth();
      if (south) {
        var child = this.createComponent(south);
        if (child instanceof qx.ui.mobile.page.NavigationPage) {
          if (holdingPage) {
            child = this.__createPageSectionForNavPage(child, holdingPage, south);
            borderContainer.add(child);
          } else {
            borderContainer.setUserData(org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.__CURRENT_PAGE,
                child);
          }
        } else {
          this._addSectionHeader(borderContainer, south);
          borderContainer.add(child);
        }
      }
      return borderContainer;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteEvenGridContainer {org.jspresso.framework.gui.remote.REvenGridContainer}
     */
    _createEvenGridContainer: function (remoteEvenGridContainer) {
      var evenGridContainer = new qx.ui.mobile.container.Composite();
      var evenGridLayout = new qx.ui.mobile.layout.VBox();
      evenGridContainer.setLayout(evenGridLayout);
      this._addSectionHeader(evenGridContainer, remoteEvenGridContainer);
      var holdingPage = remoteEvenGridContainer.getUserData(
          org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.__CURRENT_PAGE);

      for (var i = 0; i < remoteEvenGridContainer.getCells().length; i++) {
        var remoteCellComponent = remoteEvenGridContainer.getCells()[i];
        var cellComponent = this.createComponent(remoteCellComponent);
        if (cellComponent instanceof qx.ui.mobile.page.NavigationPage) {
          if (holdingPage) {
            cellComponent = this.__createPageSectionForNavPage(cellComponent, holdingPage, remoteCellComponent);
            evenGridContainer.add(cellComponent);
          } else {
            evenGridContainer.setUserData(org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.__CURRENT_PAGE,
                cellComponent);
          }
        } else {
          this._addSectionHeader(evenGridContainer, north);
          evenGridContainer.add(cellComponent);
        }
      }
      return evenGridContainer;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteTabContainer {org.jspresso.framework.gui.remote.mobile.RMobileTabContainer}
     */
    _createTabContainer: function (remoteTabContainer) {
      // view remoteTabContainer may have to be retrieved for late update
      // of cards.
      this._getRemotePeerRegistry().register(remoteTabContainer);
      var tabContainer;
      if (remoteTabContainer.getCarouselMode()) {
        tabContainer = new org.jspresso.framework.view.qx.mobile.EnhancedCarousel();
        tabContainer.setHeight(null);
        tabContainer.addCssClass("jspresso-carousel");
      } else {
        tabContainer = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.VBox());
        var tabBar = new qx.ui.mobile.tabbar.TabBar();
        tabContainer.setUserData("tabBar", tabBar);
        tabContainer.setUserData("tabBarChildren", []);
        tabContainer.add(tabBar);
      }
      for (var i = 0; i < remoteTabContainer.getTabs().length; i++) {
        /** @type {org.jspresso.framework.gui.remote.RComponent} */
        var remoteTab = remoteTabContainer.getTabs()[i];
        var tabComponent = this.createComponent(remoteTab);

        if (remoteTabContainer.getCarouselMode()) {
          tabContainer.add(tabComponent);
        } else {
          var tab = new qx.ui.mobile.tabbar.TabButton(remoteTab.getLabel(), remoteTab.getIcon());
          tab.setView(tabComponent);
          tabContainer.getUserData("tabBar").add(tab);
          tabContainer.getUserData("tabBarChildren").push(tab);
          tabContainer.add(tabComponent);
        }
      }
      if (remoteTabContainer.getCarouselMode()) {
        remoteTabContainer.addListener("changeSelectedIndex", function (event) {
          tabContainer.setCurrentIndex(event.getData());
        });
        tabContainer.addListener("changeCurrentIndex", function (event) {
          var index = event.getData();
          remoteTabContainer.setSelectedIndex(index);
          var command = new org.jspresso.framework.application.frontend.command.remote.RemoteSelectionCommand();
          command.setTargetPeerGuid(remoteTabContainer.getGuid());
          command.setPermId(remoteTabContainer.getPermId());
          command.setLeadingIndex(index);
          this._getCommandHandler().registerCommand(command);
        }, this);
        tabContainer.setCurrentIndex(remoteTabContainer.getSelectedIndex());
      } else {
        var tabBar = tabContainer.getUserData("tabBar");
        remoteTabContainer.addListener("changeSelectedIndex", function (event) {
          tabBar.setSelection(tabContainer.getUserData("tabBarChildren")[event.getData()]);
        });
        tabBar.addListener("changeSelection", function (event) {
          var index = tabContainer.getUserData("tabBarChildren").indexOf(event.getData());
          remoteTabContainer.setSelectedIndex(index);
          var command = new org.jspresso.framework.application.frontend.command.remote.RemoteSelectionCommand();
          command.setTargetPeerGuid(remoteTabContainer.getGuid());
          command.setPermId(remoteTabContainer.getPermId());
          command.setLeadingIndex(index);
          this._getCommandHandler().registerCommand(command);
        }, this);
        tabBar.setSelection(tabContainer.getUserData("tabBarChildren")[remoteTabContainer.getSelectedIndex()])
      }

      return tabContainer;
    },

    /**
     * @param navigationPage {qx.ui.mobile.page.NavigationPage}
     * @return {undefined}
     */
    _addDetailPage: function (navigationPage) {
      /** @type {org.jspresso.framework.application.frontend.controller.qx.mobile.MobileQxController} */
      this._getActionHandler().addDetailPage(navigationPage);
    },

    /**
     *
     * @return {undefined}
     * @param rCardComponent  {org.jspresso.framework.gui.remote.RComponent}
     * @param cardContainer {qx.ui.mobile.container.Composite}
     * @param cardName {String}
     */
    addCard: function (cardContainer, rCardComponent, cardName) {
      if (rCardComponent instanceof org.jspresso.framework.gui.remote.mobile.RMobilePageAwareContainer) {
        this.addCard(cardContainer, rCardComponent.getContent(), cardName);
      } else if (rCardComponent instanceof org.jspresso.framework.gui.remote.RBorderContainer
          && rCardComponent.getCenter() instanceof org.jspresso.framework.gui.remote.mobile.RMobilePage) {
        this.addCard(cardContainer, rCardComponent.getCenter(), cardName);
      } else {
        var existingCards = cardContainer.getUserData(
            org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.__EXISTING_CARDS);
        var existingCardNames = cardContainer.getUserData(
            org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.__EXISTING_CARD_NAMES);
        var existingCard = existingCardNames.indexOf(cardName) >= 0;
        if (!existingCard) {
          existingCardNames.push(cardName);
          var remoteMobileCardPage = this._getRemotePeerRegistry().getRegistered(
              cardContainer.getUserData("jspressoGuid"));
          if (remoteMobileCardPage && rCardComponent
              instanceof org.jspresso.framework.gui.remote.mobile.RMobilePageAware) {
            this.__copyPageActions(remoteMobileCardPage, rCardComponent);
          }
          var cardComponent = this.createComponent(rCardComponent);
          if (rCardComponent instanceof org.jspresso.framework.gui.remote.mobile.RMobilePage) {
            if (cardContainer.getUserData("position")) {
              // the card view is actually a page section... Should add the card page as a section.
              var holdingPage = cardContainer.getUserData(
                  org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.__CURRENT_PAGE);
              cardComponent = this.__createPageSectionForNavPage(cardComponent, holdingPage, rCardComponent);
              cardContainer.add(cardComponent);
            }
            // Do not actually add the card to the card container since it's added to the manager.
            // cardContainer.add(cardComponent);
          } else {
            cardContainer.add(cardComponent);
          }
          existingCards.push(cardComponent);
          cardComponent.setUserData(org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.__CARD_NAME, cardName);
          this._selectCard(cardContainer, cardComponent);
          this.linkNextPageBackButton(cardComponent, cardContainer.getUserData("previousPage"), null, null);
        } else {
          this._selectCard(cardContainer, existingCards[existingCardNames.indexOf(cardName)]);
        }
      }
    },

    /**
     * @param showAnimation {Boolean}
     * @return {qx.ui.mobile.core.Widget}
     */
    _createCardContainerComponent: function (showAnimation) {
      var cardLayout = new qx.ui.mobile.layout.Card();
      cardLayout.setShowAnimation(showAnimation);
      return new qx.ui.mobile.container.Composite(cardLayout);
    },

    /**
     *
     * @return {undefined}
     * @param cardContainer {qx.ui.mobile.container.Composite}
     * @param selectedCard  {qx.ui.mobile.core.Widget}
     */
    _selectCard: function (cardContainer, selectedCard) {
      var isSection = cardContainer.getUserData("position");
      if (selectedCard && !(selectedCard instanceof qx.ui.mobile.page.NavigationPage)) {
        selectedCard.show();
      }
      if (!isSection) {
        var pageToShow = this.getActualPageToShow(selectedCard);
        var pageToHide = cardContainer.getUserData(
            org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.__CURRENT_PAGE);
        if (pageToHide && pageToHide != pageToShow) {
          pageToHide.setVisibility("excluded");
        }
        // Put selectedCard as current page and not pageToShow since selectedCard can be a nested card container.
        cardContainer.setUserData(org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.__CURRENT_PAGE,
            selectedCard);
        if (pageToShow) {
          this._getActionHandler().showPage(pageToShow);
        }
      }
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
        textField.addListener("input", function (event) {
          this.__textComponentToBlur = textField;
          if (remoteActionField.getCharacterAction()) {
            textField.setUserData(org.jspresso.framework.view.qx.AbstractQxViewFactory._INPUT_TIMESTAMP,
                event.getTimeStamp());
            qx.event.Timer.once(function (e) {
              if (e.getTimeStamp() - textField.getUserData(
                  org.jspresso.framework.view.qx.AbstractQxViewFactory._INPUT_TIMESTAMP)
                  >= org.jspresso.framework.view.qx.AbstractQxViewFactory._INPUT_THRESHOLD) {
                var actionEvent = new org.jspresso.framework.gui.remote.RActionEvent();
                actionEvent.setActionCommand(textField.getValue());
                this._getActionHandler().execute(remoteActionField.getCharacterAction(), actionEvent);
              }
            }, this, org.jspresso.framework.view.qx.AbstractQxViewFactory._INPUT_THRESHOLD);
          }
        }, this);
        textField.addListener("tap", function (event) {
          if (textField.getReadOnly() && state.getWritable()) {
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
          var remoteLabelAction = remoteLabel.getAction();
          this._getRemotePeerRegistry().register(remoteLabelAction);
          modelController.addTarget(atom, "label", "value", false, {
            converter: function (modelValue, model) {
              if (modelValue) {
                var htmlContent;
                if (org.jspresso.framework.util.html.HtmlUtil.isHtml(modelValue)) {
                  htmlContent = modelValue;
                } else {
                  var executeAction = "'executeAction(\"" + remoteLabelAction.getGuid() + "\");' ";
                  htmlContent = "<u " + "onMouseUp=" + executeAction + "onPointerUp=" + executeAction + "onTouchEnd="
                      + executeAction + ">" + modelValue + "</u>";
                }
                htmlContent = org.jspresso.framework.util.html.HtmlUtil.bindActionToHtmlContent(htmlContent,
                    remoteLabel.getAction());
                return htmlContent;
              }
              return modelValue;
            }
          });
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
        this.setIcon(atom, remoteLabel.getIcon());
      }
      return atom;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteDateField {org.jspresso.framework.gui.remote.RDateField}
     */
    _createDateField: function (remoteDateField) {
      var dateField = this._createFormattedField(remoteDateField);
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
      var state = remoteDateField.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(datePickerButton, "enabled", "writable", false);
      datePickerButton.removeCssClass("button");
      //datePickerButton.removeCssClass("gap");
      var datePicker = new org.jspresso.framework.view.qx.mobile.DatePicker(this.__monthNames);
      var datePickerPopup = new org.jspresso.framework.view.qx.mobile.PickerPopup(datePicker, datePickerButton);
      datePickerPopup.setConfirmButtonCaption(this._getActionHandler().translate("ok"));
      datePickerPopup.setCancelButtonCaption(this._getActionHandler().translate("cancel"));
      datePickerButton.addListener("tap", function (e) {
        var current = remoteDateField.getState().getValue();
        if (current) {
          if (current instanceof Date) {
            current = org.jspresso.framework.util.format.DateUtils.fromDate(current);
          }
        } else {
          current = org.jspresso.framework.util.format.DateUtils.fromDate(new Date());
          current.setHour(0);
          current.setMinute(0);
          current.setSecond(0);
        }
        datePicker.setSelectedIndex(2, datePicker.getYearIndex(current.getYear()));
        datePicker.setSelectedIndex(1, current.getMonth());
        datePicker.setSelectedIndex(0, current.getDate() - 1);
        datePickerPopup.show();
      }, this);
      var dateFieldWithPicker = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.HBox());
      dateFieldWithPicker.add(dateField, {flex: 1});
      dateFieldWithPicker.add(datePickerButton, {flex: 0});
      datePickerPopup.addListener("confirmSelection", function (e) {
        var current = remoteDateField.getState().getValue();
        if (current) {
          if (current instanceof Date) {
            current = org.jspresso.framework.util.format.DateUtils.fromDate(current);
          }
        } else {
          current = org.jspresso.framework.util.format.DateUtils.fromDate(new Date());
          current.setHour(0);
          current.setMinute(0);
          current.setSecond(0);
        }
        var date = parseInt(e.getData()[0].item.title);
        var month = e.getData()[1].index;
        var year = parseInt(e.getData()[2].item.title);
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
      var timePickerButton = this.createButton("...", null, null);
      var state = remoteTimeField.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(timePickerButton, "enabled", "writable", false);
      timePickerButton.removeCssClass("button");
      //timePickerButton.removeCssClass("gap");
      var secondsAware = remoteTimeField.getSecondsAware();
      var millisecondsAware = remoteTimeField.getMillisecondsAware();
      var timePicker = new org.jspresso.framework.view.qx.mobile.TimePicker(secondsAware, millisecondsAware);
      var timePickerPopup = new org.jspresso.framework.view.qx.mobile.PickerPopup(timePicker, timePickerButton);
      timePickerPopup.setConfirmButtonCaption(this._getActionHandler().translate("ok"));
      timePickerPopup.setCancelButtonCaption(this._getActionHandler().translate("cancel"));
      timePickerButton.addListener("tap", function (e) {
        var current = remoteTimeField.getState().getValue();
        if (current) {
          if (current instanceof Date) {
            current = org.jspresso.framework.util.format.DateUtils.fromDate(current);
          }
        } else {
          current = org.jspresso.framework.util.format.DateUtils.fromDate(new Date());
          current.setHour(0);
          current.setMinute(0);
          current.setSecond(0);
          current.setMillSecond(0);
        }
        timePicker.setSelectedIndex(0, current.getHour());
        timePicker.setSelectedIndex(1, current.getMinute());
        if (secondsAware) {
          timePicker.setSelectedIndex(2, current.getSecond());
        }
        if (millisecondsAware) {
          timePicker.setSelectedIndex(3, current.getMillisecond());
        }
        timePickerPopup.show();
      }, this);
      var timeFieldWithPicker = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.HBox());
      timeFieldWithPicker.add(timeField, {flex: 1});
      timeFieldWithPicker.add(timePickerButton, {flex: 0});
      timePickerPopup.addListener("confirmSelection", function (e) {
        var current = remoteTimeField.getState().getValue();
        if (current) {
          if (current instanceof Date) {
            current = org.jspresso.framework.util.format.DateUtils.fromDate(current);
          }
        } else {
          current = org.jspresso.framework.util.format.DateUtils.fromDate(new Date());
        }
        var hour = parseInt(e.getData()[0].item.title);
        var minute = parseInt(e.getData()[1].item.title);
        var second = 0;
        if (e.getData().length > 2) {
          second = parseInt(e.getData()[2].item.title);
        }
        var millisecond = 0;
        if (e.getData().length > 3) {
          millisecond = parseInt(e.getData()[3].item.title);
        }

        var dateDto = new org.jspresso.framework.util.lang.DateDto();
        dateDto.setYear(current.getYear());
        dateDto.setMonth(current.getMonth());
        dateDto.setDate(current.getDate());
        dateDto.setHour(hour);
        dateDto.setMinute(minute);
        dateDto.setSecond(second);
        dateDto.setMillisecond(millisecond);
        remoteTimeField.getState().setValue(dateDto);
      }, this);
      return timeFieldWithPicker;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteColorField {org.jspresso.framework.gui.remote.RColorField}
     */
    _createColorField: function (remoteColorField) {
      var colorField = this._createFormattedField(remoteColorField);
      colorField.addCssClass("jspresso-color-field");
      return colorField;
    },

    /**
     *
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteList {org.jspresso.framework.gui.remote.mobile.RMobileList}
     */
    _createList: function (remoteList) {
      /** @type {org.jspresso.framework.state.remote.RemoteCompositeValueState} */
      var state = remoteList.getState();
      var listModel = state.getChildren();

      var rendererType;
      var singleSelectionMode = remoteList.getSelectionMode() == "SINGLE_SELECTION" || remoteList.getSelectionMode()
          == "SINGLE_CUMULATIVE_SELECTION";
      if (singleSelectionMode) {
        rendererType = qx.ui.mobile.list.renderer.Default;
      } else {
        rendererType = org.jspresso.framework.view.qx.mobile.SelectableListItemRenderer;
      }

      var list = new org.jspresso.framework.view.qx.mobile.EnhancedList({
        configureItem: function (item, data, row) {
          var selectedIndices = state.getSelectedIndices();
          var selected = selectedIndices != null && selectedIndices.indexOf(row) >= 0;
          org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.bindListItem(item, data, selected,
              remoteList.getDisplayIcon());
          if (remoteList instanceof org.jspresso.framework.gui.remote.mobile.RMobileList) {
            item.setShowArrow(remoteList.getShowArrow());
          }
        },
        createItemRenderer: function () {
          return new rendererType();
        }
      });
      list.addCssClass("jspresso-list");

      list.setModel(listModel);

      list.addListener("changeSelection", function (evt) {
        var selectedIndex = evt.getData();
        var stateSelectedIndices = state.getSelectedIndices();
        var item = listModel.getItem(selectedIndex);
        if (remoteList.getSelectionMode() == "SINGLE_SELECTION" || remoteList.getSelectionMode()
            == "SINGLE_CUMULATIVE_SELECTION") {
          if (!stateSelectedIndices || !qx.lang.Array.equals(stateSelectedIndices, [selectedIndex])) {
            state.setLeadingIndex(selectedIndex);
            state.setSelectedIndices([selectedIndex]);
          }
        } else {
          var selectedIndices = stateSelectedIndices;
          if (selectedIndices == null) {
            selectedIndices = [];
          } else {
            selectedIndices = selectedIndices.concat([]);
          }
          var selected = selectedIndices.indexOf(selectedIndex);
          var leadingIndex;
          if (list.getUserData("tapEvent") && selected >= 0) {
            selectedIndices.splice(selected, 1);
            if (selectedIndices.length > 0) {
              leadingIndex = selectedIndices[0];
            } else {
              leadingIndex = -1;
            }
          } else if (selected < 0) {
            selectedIndices.push(selectedIndex);
            selectedIndices.sort();
            leadingIndex = selectedIndex;
          }
          if (!stateSelectedIndices || !qx.lang.Array.equals(stateSelectedIndices, selectedIndices)) {
            state.setLeadingIndex(leadingIndex);
            state.setSelectedIndices(selectedIndices);
          }
          if (item) {
            item._applyEventPropagation(item.getValue(), undefined, "value");
          }
        }
      }, this);

      var rowAction = remoteList.getRowAction();
      if (rowAction) {
        this._getRemotePeerRegistry().register(rowAction)
        this.addComponentThresholdListener(list, "changeSelection", function (evt) {
          // Since the client won't be notified yet about selection change and action enablement,
          // force it enabled on client side and let the server decide...
          rowAction.setEnabled(true);
          this._getActionHandler().execute(rowAction);
        }, this);
      }

      state.addListener("changeSelectedIndices", function (e) {
        if (!list.getUserData("tapEvent")) {
          /** @type {Array} */
          var stateSelection = e.getTarget().getSelectedIndices();
          if (singleSelectionMode) {
            if (stateSelection && stateSelection.length > 0) {
              list.fireDataEvent("changeSelection", stateSelection[0]);
            }
          } else {
            // For multi selection sync
            list.render();
          }
        }
      }, this);

      return list;
    },

    /**
     *
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteRepeater {org.jspresso.framework.gui.remote.mobile.RMobileList}
     */
    _createRepeater: function (remoteRepeater) {
      var repeaterContainer = new org.jspresso.framework.view.qx.mobile.RepeaterContainer(remoteRepeater.getState());
      if (remoteRepeater.getRowAction()) {
        this._getRemotePeerRegistry().register(remoteRepeater.getRowAction())
      }
      var repeater = new org.jspresso.framework.view.qx.ViewRepeater(repeaterContainer, remoteRepeater, this,
          this._getActionHandler());
      repeater.setDataProvider(remoteRepeater.getState().getChildren());
      remoteRepeater.assignPeer(repeater);

      return repeaterContainer;
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
      var pageToShow = this.getActualPageToShow(component);
      if (pageToShow && pageToShow.getVisibility() != "visible") {
        this._getActionHandler().showDetailPage(pageToShow);
      }
    },

    /**
     * @param component {qx.ui.mobile.page.NavigationPage}
     * @return {undefined}
     */
    edit: function (component) {
      var editorPage = component.getUserData("editorPage");
      if (editorPage) {
        this._getActionHandler().showDetailPage(editorPage, "flip");
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
      this.__bindReadOnlyBorder(state, formattedField);
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
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteIntegerField {org.jspresso.framework.gui.remote.RIntegerField}
     */
    _createIntegerField: function (remoteIntegerField) {
      var integerField = this._createFormattedField(remoteIntegerField);
      return integerField;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteDecimalField {org.jspresso.framework.gui.remote.RDecimalField}
     */
    _createDecimalField: function (remoteDecimalField) {
      var decimalField = this._createFormattedField(remoteDecimalField);
      return decimalField;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remotePercentField {org.jspresso.framework.gui.remote.RPercentField}
     */
    _createPercentField: function (remotePercentField) {
      var percentField = this._createFormattedField(remotePercentField);
      return percentField;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteDurationField {org.jspresso.framework.gui.remote.RDurationField}
     */
    _createDurationField: function (remoteDurationField) {
      var durationField = this._createFormattedField(remoteDurationField);
      return durationField;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteDateField {org.jspresso.framework.gui.remote.RDateField}
     */
    _createDateTimeField: function (remoteDateField) {
      var dateTimeField = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.VBox());
      var oldType = remoteDateField.getType();
      try {
        remoteDateField.setType("DATE");
        dateTimeField.add(this._createDateField(remoteDateField), {flex: 1});
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
      remoteTimeField.setMillisecondsAware(remoteDateField.getMillisecondsAware());
      remoteTimeField.useDateDto(true);
      dateTimeField.add(this.createComponent(remoteTimeField, false), {flex: 1});
      var dateTimeFieldWrapper = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.HBox());
      dateTimeFieldWrapper.add(dateTimeField, {flex: 1});
      return dateTimeFieldWrapper;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteActionComponent {org.jspresso.framework.gui.remote.RActionComponent}
     */
    _createActionComponent: function (remoteActionComponent) {
      var list = new org.jspresso.framework.view.qx.mobile.EnhancedList({
        configureItem: function (item, data, row) {
          item.setShowArrow(false);
          item.setTitle(data.getName());
          if (data.getIcon()) {
            item.setImage(
                org.jspresso.framework.view.qx.AbstractQxViewFactory.completeForSVG(data.getIcon().getImageUrlSpec()));
          }
          item.setEnabled(data.getEnabled());
          item.setSelectable(data.getEnabled());
          if (data.getHiddenWhenDisabled() && !data.getEnabled()) {
            item.setVisibility("excluded");
          } else {
            item.setVisibility("visible");
          }
        }
      });
      list.addCssClass("jspresso-list");

      var actionList = remoteActionComponent.getActionList();
      var listModel = [];
      if (actionList) {
        listModel = actionList.getActions();
      } else {
        listModel = [remoteActionComponent.getAction()];
      }
      for (var i = 0; i < listModel.length; i++) {
        this._getRemotePeerRegistry().register(listModel[i]);
      }
      list.setModel(new qx.data.Array(listModel));

      this.addComponentThresholdListener(list, "changeSelection", function (evt) {
        var selectedIndex = evt.getData();
        this._getActionHandler().execute(listModel[selectedIndex]);
      }, this);
      return list;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteImageComponent {org.jspresso.framework.gui.remote.RImageComponent}
     */
    _createImageComponent: function (remoteImageComponent) {
      var imageComponent = new qx.ui.mobile.basic.Image();
      qx.bom.element.Style.set(imageComponent.getContentElement(), "max-width", "100%");

      var state = remoteImageComponent.getState();
      state.addListener("changeValue", function (e) {
        var imageUrl = e.getData();
        if (imageUrl) {
          if (imageUrl.indexOf("://") >= 0) {
            imageComponent.setSource(imageUrl);
          }
        } else {
          imageComponent.setSource(null);
        }
      }, this);
      imageComponent.setSource(state.getValue());
      if (remoteImageComponent.getAction() != null) {
        this._getRemotePeerRegistry().register(remoteImageComponent.getAction());
        this.addComponentThresholdListener(imageComponent, "tap", function (e) {
          var element = imageComponent.getContentElement();
          var elementRect = element.getBoundingClientRect();
          var actionEvent = new org.jspresso.framework.gui.remote.RImageActionEvent();
          actionEvent.setWidth(element.width);
          actionEvent.setHeight(element.height);
          actionEvent.setX(~~(e.getDocumentLeft() - elementRect.left));
          actionEvent.setY(~~(e.getDocumentTop() - elementRect.top));
          /*
          alert("w: " + actionEvent.getWidth() + "; " + "h: " + actionEvent.getHeight() + "; " + "x: "
              + actionEvent.getX() + "; " + "y: " + actionEvent.getY() + "; e.x: " + element.x + "; e.y: " + element.y
              + "; d.x: " + e.getDocumentLeft() + "; d.y: " + e.getDocumentTop() + "; r.x: " + elementRect.left
              + "; r.y: " + elementRect.right);
          */
          this._getActionHandler().execute(remoteImageComponent.getAction(), actionEvent);
        }, this);
      }
      var wrapper = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.HBox().set({
        alignX: "center",
        alignY: "middle"
      }));
      wrapper.addCssClass("jspresso-cropper");
      wrapper.add(imageComponent);
      return wrapper;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteRadioBox {org.jspresso.framework.gui.remote.RRadioBox}
     */
    _createRadioBox: function (remoteRadioBox) {
      var radioBox = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.VBox());
      var radioGroup = new qx.ui.mobile.form.RadioGroup();
      for (var i = 0; i < remoteRadioBox.getValues().length; i++) {
        var rb = new qx.ui.mobile.form.RadioButton();
        rb.addCssClass("radiobutton-form");
        rb.setModel(remoteRadioBox.getValues()[i]);
        radioGroup.add(rb);
        var option = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.HBox());
        option.add(rb);
        option.add(new qx.ui.mobile.form.Label(remoteRadioBox.getTranslations()[i]));
        radioBox.add(option);
      }

      var state = remoteRadioBox.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(radioGroup, "modelSelection", "value", false, {
        converter: function (modelValue, model) {
          return [modelValue];
        }
      });
      radioGroup.getModelSelection().addListener("change", function (e) {
        var modelSelection = e.getTarget();
        if (modelSelection.length > 0) {
          state.setValue(modelSelection.getItem(0));
        } else {
          state.setValue(null);
        }
      });
      modelController.addTarget(radioGroup, "enabled", "writable", false);
      var radioBoxWrapper = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.HBox());
      radioBoxWrapper.add(radioBox, {flex: 1});
      return radioBoxWrapper;
    },

    /**
     * @param remoteImagePicker {org.jspresso.framework.gui.remote.mobile.RImagePicker}
     * @return {qx.ui.mobile.core.Widget}
     */
    _createImagePicker: function (remoteImagePicker) {
      var state = remoteImagePicker.getState();
      var imageChooser = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.VBox());
      var imagePickerBar = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.HBox());
      var imagePicker = new org.jspresso.framework.view.qx.mobile.ImagePicker(remoteImagePicker.getSubmitUrl(),
          remoteImagePicker.getLabel(), remoteImagePicker.getImageSize(), remoteImagePicker.getFormatName());
      imagePicker.addCssClass("jspresso-even-width");
      var clearButton = new qx.ui.mobile.form.Button(this._getActionHandler().translate("Clear"));
      clearButton.addListener("tap", function (e) {
        state.setValue(null);
      }, this);
      clearButton.addCssClass("jspresso-even-width");
      imagePickerBar.add(imagePicker, {flex: 1});
      imagePickerBar.add(clearButton, {flex: 1});
      var imagePreview = this._createImageComponent(remoteImagePicker);
      imagePreview.addCssClass("group");
      imageChooser.add(imagePickerBar, {flex: 1});
      imageChooser.add(imagePreview, {flex: 1});
      imageChooser.addCssClass("jspresso-image-chooser");

      var syncPicker = function (imageData) {
        if (imageData) {
          imagePicker.setLabel(this._getActionHandler().translate("Replace") + "...");
          clearButton.setVisibility("visible");
        } else {
          imagePicker.setLabel(this._getActionHandler().translate("Choose") + "...");
          clearButton.setVisibility("excluded");
        }
      };

      state.addListener("changeValue", function (e) {
        var imageData = e.getData();
        syncPicker.call(this, imageData);
      }, this);
      imagePicker.addListener("imagePicked", function (imagePickedEvent) {
        state.setValue(imagePickedEvent.getData()['url']);
      }, this);
      syncPicker.call(this, state.getValue());
      return imageChooser;
    },

    /**
     * @param remoteImageCanvas {org.jspresso.framework.gui.remote.mobile.RImageCanvas}
     * @return {qx.ui.mobile.core.Widget}
     */
    _createImageCanvas: function (remoteImageCanvas) {
      var height;
      var imageCanvas = new org.jspresso.framework.view.qx.mobile.ImageCanvas(remoteImageCanvas.getImageSize(),
          this._getActionHandler().translate("Clear"));
      imageCanvas.addCssClass("jspresso-image-canvas");
      var state = remoteImageCanvas.getState();
      state.addListener("changeValue", function (e) {
        var imageUrl = e.getData();
        if (imageUrl) {
          if (imageUrl.indexOf("://") >= 0) {
            //imageCanvas.clear();
            imageCanvas.setImage(imageUrl);
          }
        } else {
          imageCanvas.clear();
        }
      }, this);
      imageCanvas.setImage(state.getValue());
      imageCanvas.addListener("drawn", function (e) {
        var image = imageCanvas.getImage(remoteImageCanvas.getFormatName());
        state.setValue(image.src);
      }, this);
      return imageCanvas;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param component {qx.ui.mobile.core.Widget}
     */
    decorateWithScrollContainer: function (component) {
      var scrollContainer = new qx.ui.mobile.container.Scroll();
      // scrollContainer.setFixedHeight(true);
      // scrollContainer.setShowScrollIndicator(false);
      scrollContainer.add(component, {
        flex: 1
      });
      return scrollContainer;
    },

    __bindReadOnlyBorder: function (state, textInput) {
      state.addListener("changeWritable", function (event) {
        if (!event.getData()) {
          textInput.addCssClass("jspresso-border-none");
        } else {
          textInput.removeCssClass("jspresso-border-none");
        }
      }, this);
      if (!state.getWritable()) {
        textInput.addCssClass("jspresso-border-none");
      }
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteTextArea {org.jspresso.framework.gui.remote.RTextArea}
     */
    _createTextArea: function (remoteTextArea) {
      var textArea = new qx.ui.mobile.form.TextArea();
      textArea.addCssClass("jspresso-textarea");
      if (remoteTextArea.getPreferredSize() && remoteTextArea.getPreferredSize().getHeight()) {
        textArea._setStyle("height", remoteTextArea.getPreferredSize().getHeight() + "px");
      }
      this._bindTextArea(remoteTextArea, textArea);
      return textArea;
    },

    /**
     * @param remoteTextArea {org.jspresso.framework.gui.remote.RTextArea}
     * @param textArea {qx.ui.mobile.form.TextArea}
     */
    _bindTextArea: function (remoteTextArea, textArea) {
      var state = remoteTextArea.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(textArea, "value", "value", true, {
        converter: this._modelToViewFieldConverter
      }, {
        converter: this._viewToModelFieldConverter
      });
      modelController.addTarget(textArea, "readOnly", "writable", false, {
        converter: this._readOnlyFieldConverter
      });
      textArea.addListener("input", function (event) {
        this.__textComponentToBlur = textArea;
      }, this);
      this.__bindReadOnlyBorder(state, textArea);
    },

    /**
     * @return {Boolean}
     * @param rComponent {org.jspresso.framework.gui.remote.RComponent}
     */
    isMultiline: function (rComponent) {
      return rComponent instanceof org.jspresso.framework.gui.remote.RTable || rComponent
          instanceof org.jspresso.framework.gui.remote.RTextArea || rComponent
          instanceof org.jspresso.framework.gui.remote.RList || rComponent
          instanceof org.jspresso.framework.gui.remote.RHtmlArea || (rComponent
              instanceof org.jspresso.framework.gui.remote.RImageComponent && !rComponent.isScrollable());
    },

    /**
     * @return {Boolean}
     * @param rComponent {org.jspresso.framework.gui.remote.RComponent}
     */
    isFixedWidth: function (rComponent) {
      return rComponent instanceof org.jspresso.framework.gui.remote.RCheckBox || rComponent
          instanceof org.jspresso.framework.gui.remote.RLabel || (rComponent
              instanceof org.jspresso.framework.gui.remote.RActionField && !rComponent.isShowTextField());
    },


    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteHtmlArea {org.jspresso.framework.gui.remote.RHtmlArea}
     */
    _createHtmlEditor: function (remoteHtmlArea) {
      return this._createTextArea(remoteHtmlArea);
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteHtmlArea {org.jspresso.framework.gui.remote.RHtmlArea}
     */
    _createHtmlText: function (remoteHtmlArea) {
      var htmlText = new qx.ui.mobile.basic.Label();
      var state = remoteHtmlArea.getState();
      var modelController = new qx.data.controller.Object(state);
      if (remoteHtmlArea.getAction()) {
        this._getRemotePeerRegistry().register(remoteHtmlArea.getAction());
      }
      modelController.addTarget(htmlText, "value", "value", false, {
        converter: function (modelValue, model) {
          return org.jspresso.framework.util.html.HtmlUtil.bindActionToHtmlContent(modelValue,
              remoteHtmlArea.getAction());
        }
      });
      return htmlText;
    }
  }
});
