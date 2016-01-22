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
package org.jspresso.framework.application.startup.batch;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jspresso.framework.application.launch.batch.BatchLauncher;
import org.jspresso.framework.application.startup.BackendActionStartup;

/**
 * A simple batch process starter. The batch itself is coded as a backend action
 * that is executed by the backend controller.
 *
 * @author Vincent Vandenschrick
 */
public class BatchStartup extends BackendActionStartup implements IBatchStartup {

  /**
   * The constant RETURN_CODE is &quot;RETURN_CODE&quot;.
   */
  public static final String RETURN_CODE = "RETURN_CODE";

  private static final String ACTION_ID             = "action";
  private static final String APP_CONTEXT           = "applicationContext";
  private static final String BEAN_FACTORY_SELECTOR = "beanFactorySelector";
  private static final String BATCH_USER_NAME       = "batchUserName";
  private static final String LOCALE                = "locale";

  private static final Logger LOG = LoggerFactory.getLogger(BatchStartup.class);

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean parseCmdLine(String[] args) {
    Options options = createOptions();
    CommandLineParser parser = new DefaultParser();
    try {
      CommandLine cmd = parser.parse(options, args);
      processCommandLine(cmd);
    } catch (ParseException ex) {
      LOG.error("An error occurred while parsing the command line", ex);
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(BatchLauncher.class.getSimpleName(), options);
      return false;
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void start() {
    startController();
    boolean actionSuccess = executeAction();
    int returnCode;
    Object contextReturnCode = getActionContext().get(RETURN_CODE);
    if (contextReturnCode instanceof Integer) {
      returnCode = (Integer) contextReturnCode;
    } else {
      if (actionSuccess) {
        returnCode = 0;
      } else {
        returnCode = 1;
      }
    }
    System.exit(returnCode);
  }

  /**
   * Creates the CLI options.
   *
   * @return the CLI options.
   */
  @SuppressWarnings("static-access")
  protected Options createOptions() {
    Options options = new Options();
    options.addOption(Option.builder(APP_CONTEXT).argName(APP_CONTEXT).hasArg().required()
                            .desc("use given Spring application context.").build());
    options.addOption(Option.builder(BEAN_FACTORY_SELECTOR).argName(BEAN_FACTORY_SELECTOR).hasArg()
                            .desc("use given Spring bean factory selector.").build());
    options.addOption(Option.builder(LOCALE).argName(LOCALE).hasArg().desc("use given language (defaults to 'en').")
                            .build());
    options.addOption(Option.builder(ACTION_ID).argName(ACTION_ID).hasArg().required()
                            .desc("use the specified backend action.").build());
    options.addOption(Option.builder(BATCH_USER_NAME).argName(BATCH_USER_NAME).hasArg()
                            .desc("use the specified batch user name.").build());
    return options;
  }

  /**
   * Processes the command line.
   *
   * @param cmd
   *     the parsed command line.
   * @throws ParseException
   *     whenever an error occurs parsing the command line.
   */
  protected void processCommandLine(CommandLine cmd) throws ParseException {
    setApplicationContextKey(cmd.getOptionValue(APP_CONTEXT));
    setBeanFactorySelector(cmd.getOptionValue(BEAN_FACTORY_SELECTOR));
    setStartupLocale(LocaleUtils.toLocale(cmd.getOptionValue(LOCALE, "en")));
    setActionBeanId(cmd.getOptionValue(ACTION_ID));
    setUserName(cmd.getOptionValue(BATCH_USER_NAME, "batch"));
  }
}
