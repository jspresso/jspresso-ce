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
    bindListItem: function (item, state) {
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(item, "title", "value");
      modelController.addTarget(item, "subtitle", "description");
      modelController.addTarget(item, "image", "iconImageUrl");
    }
  },

  construct: function (remotePeerRegistry, actionHandler, commandHandler) {
    this.base(arguments, remotePeerRegistry, actionHandler, commandHandler);
  },

  members: {

    /**
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @return {qx.ui.mobile.core.Widget}
     */
    _createCustomComponent: function (remoteComponent) {
      return null;
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
      } else if (remoteContainer instanceof org.jspresso.framework.gui.remote.RCardContainer) {
        container = this._createCardContainer(remoteContainer);
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
      var selectionList = this.createComponent(remoteNavPage.getSelectionView());
      var navPage = new qx.ui.mobile.page.NavigationPage();
      navPage.setTitle(remoteNavPage.getLabel());
      navPage.addListener("initialize", function (e) {
        navPage.getContent().add(selectionList);
      }, this);
      /** @type {qx.ui.mobile.page.NavigationPage} */
      var nextPage = this.createComponent(remoteNavPage.getNextPage());
      selectionList.addListener("changeSelection", function(evt) {
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
        this.linkNextPageBackButton(nextPage, navPage);
      } else {
        nextPage.setUserData("previousPage", navPage);
      }
      this._addDetailPage(navPage);
      return navPage;
    },

    /**
     * @param nextPage {qx.ui.mobile.page.NavigationPage}
     * @param previousPage {qx.ui.mobile.page.NavigationPage}
     * @param animation {String}
     */
    linkNextPageBackButton: function (nextPage, previousPage, animation) {
      if(typeof animation === undefined) animation = "slide";
      nextPage.setShowBackButton(true);
      nextPage.setBackButtonText(previousPage.getTitle());
      var backButton = nextPage.getLeftContainer().getChildren()[0];
      backButton.setIcon("org/jspresso/framework/mobile/back-mobile.png");
      backButton.setShow("both");
      nextPage.addListener("back", function () {
        previousPage.show({animation: animation,  reverse: true});
      }, this);
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
          this.linkNextPageBackButton(nextPage, compositePage);
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
        if(remoteForm.getLabelsPosition() != "NONE") {
          var label = new qx.ui.mobile.form.Label("<p>" + rComponent.getLabel() + "</p>");
          label.setLabelFor(component.getId());
          row.add(label, {flex:1});
        }
        if(this._isMultiline(rComponent)) {
          row.add(component, {flex:1});
        } else {
          row.add(component);
        }
        form.add(row);
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
          remoteComboBox.getValues().insertAt(0, "");
          cbModel.push(" ");
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
     * @param remoteTree {org.jspresso.framework.gui.remote.RTree}
     */
    _createTree: function (remoteTree) {

      var treeListModel = org.jspresso.framework.state.remote.RemoteCompositeValueState.flatten(remoteTree.getState(), 0);

      var treeList = new qx.ui.mobile.list.List({
        configureItem: function (item, data, row) {
          var state = data.state;
          org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.bindListItem(item, state);
          item.setShowArrow(true);
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

      /** @type {qx.ui.mobile.form.TextField } */
      var textField;
      if (remoteActionField.getShowTextField()) {
        textField = new qx.ui.mobile.form.TextField();
      }
      var actionField = this._decorateWithAsideActions(textField, remoteActionField, true);
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
          var content = textField.getValue();
          if (content && content.length > 0) {
            if (content != state.getValue()) {
              textField.setValue(state.getValue());
              if (e instanceof qx.event.type.Focus) {
                if (e.getRelatedTarget() && (/** @type {qx.ui.mobile.core.Widget } */ e.getRelatedTarget()) == actionField) {
                  return;
                }
              }
              var actionEvent = new org.jspresso.framework.gui.remote.RActionEvent();
              actionEvent.setActionCommand(content);
              this.__actionHandler.execute(mainAction, actionEvent);
            }
          } else {
            state.setValue(null);
          }
        };
        textField.addListener("blur", triggerAction, this);
        // textField.addListener("changeValue", triggerAction, this);

        modelController.addTarget(textField, "value", "value", false, {
          converter: this._modelToViewFieldConverter
        });
        if (remoteActionField.getCharacterAction()) {
          textField.addListener("input", function (event) {
            var actionEvent = new org.jspresso.framework.gui.remote.RActionEvent();
            actionEvent.setActionCommand(textField.getValue());
            this.__actionHandler.execute(remoteActionField.getCharacterAction(), actionEvent);
          }, this);
        }
      }
      return actionField;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param rComponent {org.jspresso.framework.gui.remote.RComponent}
     */
    _createFormattedField: function (rComponent) {
      var formattedField = new qx.ui.mobile.form.TextField();
      this._bindFormattedField(formattedField, rComponent);
      return formattedField;
    },

    /**
     *
     * @param expectedCharCount {Integer}
     * @param component {qx.ui.mobile.core.Widget}
     * @param maxCharCount {Integer}
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @return {undefined}
     */
    _sizeMaxComponentWidth: function (component, remoteComponent, expectedCharCount, maxCharCount) {
      // NO-OP
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
            this.__actionHandler.execute(remoteLabel.getAction());
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
     */
    _createHBoxContainer: function () {
      var hboxContainer = new qx.ui.mobile.container.Composite();
      hboxContainer.setLayout(new qx.ui.mobile.layout.HBox());
      return hboxContainer;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     */
    _createVBoxContainer: function () {
      var hboxContainer = new qx.ui.mobile.container.Composite();
      hboxContainer.setLayout(new qx.ui.mobile.layout.VBox());
      return hboxContainer;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteDateField {org.jspresso.framework.gui.remote.RDateField}
     */
    _createDateField: function (remoteDateField) {
      var dateField = this._createFormattedField(remoteDateField);
      return dateField;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteColorField {org.jspresso.framework.gui.remote.RColorField}
     */
    _createColorField: function (remoteColorField) {
      var colorField = this._createFormattedField(remoteColorField);
      return colorField;
    },

    /**
     *
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteList {org.jspresso.framework.gui.remote.RList}
     */
    _createList: function (remoteList) {
      var listModel = remoteList.getState().getChildren();

      var list = new qx.ui.mobile.list.List({
        configureItem: function (item, data, row) {
          org.jspresso.framework.view.qx.mobile.MobileQxViewFactory.bindListItem(item,  data);
          item.setShowArrow(true);
        }
      });

      list.setModel(listModel);

      list.addListener("changeSelection", function(evt) {
        var selectedIndex = evt.getData();
        remoteList.getState().setLeadingIndex(selectedIndex);
        remoteList.getState().setSelectedIndices([selectedIndex]);
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
    }




  }
});
