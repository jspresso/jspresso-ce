/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.printing.frontend.action;

import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.action.AbstractChainedAction;
import com.d2s.framework.application.printing.model.IReportFactory;
import com.d2s.framework.application.printing.model.descriptor.IReportDescriptor;
import com.d2s.framework.binding.ICollectionConnector;

/**
 * Frontend action to generate a report selected among the model collection
 * connector holding a list of report descriptors.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class ReportAction<E, F, G> extends AbstractChainedAction<E, F, G> {

  private IReportFactory reportFactory;

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    ICollectionConnector viewConnector = (ICollectionConnector) getViewConnector(context);
    int[] selectedIndices = viewConnector.getSelectedIndices();
    ICollectionConnector collectionConnector = (ICollectionConnector) viewConnector
        .getModelConnector();
    if (selectedIndices == null || selectedIndices.length == 0
        || collectionConnector == null) {
      return false;
    }
    IReportDescriptor reportDescriptor = (IReportDescriptor) collectionConnector
        .getChildConnector(selectedIndices[0]).getConnectorValue();
    context.put(ActionContextConstants.ACTION_PARAM, reportFactory
        .createReportInstance(reportDescriptor,
            getTranslationProvider(context), getLocale(context)));
    return super.execute(actionHandler, context);
  }

  /**
   * Sets the reportFactory.
   * 
   * @param reportFactory
   *          the reportFactory to set.
   */
  public void setReportFactory(IReportFactory reportFactory) {
    this.reportFactory = reportFactory;
  }
}
