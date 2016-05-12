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
package org.jspresso.framework.application.frontend.action.std;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.view.ICompositeView;
import org.jspresso.framework.view.IMapView;
import org.jspresso.framework.view.IView;

/**
 * Refresh card view having given permId
 * 
 * @author Maxime HAMM
 *
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class RefreshCardViewFrontAction<E, F, G> extends FrontendAction<E, F, G> {

  private String cardViewId = null;
  
  /**
   * Sets the card view id.
   * @param cardViewId The card view id.
   */
  public void setCardViewId(String cardViewId) {
    this.cardViewId = cardViewId;
  }
  
  /**
   * Gets card view id.
   * @param context The context.
   * @return the card view id.
   */
  protected String getCardViewId(Map<String, Object> context) {
    return cardViewId;
  }
  
  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    IMapView<E> cardView = null;
    IView<?> view = getView(context);
    String cardId = getCardViewId(context);
    
    //
    // Search within current view
    IView<?> v = view;
    while (v!=null) {
      if (cardId.equals(v.getDescriptor().getPermId())) {
        cardView = (IMapView<E>) v;
        break;
      }
      v = v.getParent();
    }
    
    // 
    // Search children view
    if (cardView == null) { 
      cardView = (IMapView<E>) searchCard(view, cardId);
    }
    
    // 
    // Refresh card
    if (cardView!=null) {
      getViewFactory(context).refreshCardView(cardView, false, actionHandler, getLocale(context));
    }
    
    return super.execute(actionHandler, context);
  }

  /**
   * Search card view from perm id.
   * @param view The parent view.
   * @param permId The perm id.
   * @return the card view.
   */
  protected IMapView<?> searchCard(IView<?> view, String permId) {
    if (permId.equals(view.getDescriptor().getPermId())) {
      return (IMapView<?>) view;
    }
    if (view instanceof ICompositeView<?>) {
      ICompositeView<?> composite = (ICompositeView<?>)view;
      for (IView<?> v : composite.getChildren()) {
        IMapView<?> card = searchCard(v, permId);
        if (card !=null) {
          return card;
        }
      }
    }
    else if (view instanceof IMapView<?>) {
      IView<?> v = ((IMapView<?>)view).getCurrentView();
      IMapView<?> card = searchCard(v, permId);
      if (card !=null) {
        return card;
      }
    }
    return null;
  }
}
