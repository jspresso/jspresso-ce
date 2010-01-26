/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.security.auth.Subject;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jspresso.framework.application.backend.action.BackendAction;
import org.jspresso.framework.application.backend.session.IApplicationSession;
import org.jspresso.framework.application.launch.batch.BatchLauncher;
import org.jspresso.framework.application.startup.AbstractBackendStartup;
import org.jspresso.framework.security.UserPrincipal;

/**
 * A simple batch process starter. The batch itself is coded as a backend action
 * that is executed by the backend controller.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BatchStartup extends AbstractBackendStartup implements
    IBatchStartup {

  private String              applicationContextKey;
  private Locale              startupLocale;
  private String              actionBeanId;
  private Map<String, Object> actionContext;
  private String              batchUserName;

  private static final String APP_CONTEXT     = "applicationContext";
  private static final String LOCALE          = "locale";
  private static final String ACTION_ID       = "action";
  private static final String BATCH_USER_NAME = "batchUserName";

  /**
   * {@inheritDoc}
   */
  @Override
  protected String getApplicationContextKey() {
    return applicationContextKey;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Locale getStartupLocale() {
    return startupLocale;
  }

  /**
   * Gets the initial parameterized action context.
   * 
   * @return the initial parameterized action context.
   */
  protected Map<String, Object> getActionContext() {
    if (actionContext == null) {
      actionContext = new HashMap<String, Object>();
    }
    return actionContext;
  }

  /**
   * The bean id of the backend action to execute.
   * 
   * @return bean id of the backend action to execute.
   */
  protected String getActionBeanId() {
    return actionBeanId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void start() {
    super.start();
    IApplicationSession batchSession = getBackendController()
        .getApplicationSession();
    batchSession.setLocale(getStartupLocale());
    batchSession.setSubject(createSubject());
    BackendAction backendAction = getAction();
    Map<String, Object> startupActionContext = new HashMap<String, Object>();
    startupActionContext.putAll(getBackendController()
        .getInitialActionContext());
    startupActionContext.putAll(getActionContext());
    boolean success = getBackendController().execute(backendAction,
        startupActionContext);
    if (!success) {
      System.exit(1);
    }
  }

  /**
   * Retrieves the backend action to execute.
   * 
   * @return the backend action to execute.
   */
  protected BackendAction getAction() {
    return (BackendAction) getApplicationContext().getBean(getActionBeanId());
  }

  /**
   * {@inheritDoc}
   */
  public boolean parseCmdLine(String[] args) {
    Options options = createOptions();
    CommandLineParser parser = new BasicParser();
    try {
      CommandLine cmd = parser.parse(options, args);
      processCommandLine(cmd);
    } catch (ParseException ex) {
      System.err.println(ex.getLocalizedMessage());
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(BatchLauncher.class.getSimpleName(), options);
      return false;
    }
    return true;
  }

  /**
   * Processes the command line.
   * 
   * @param cmd
   *          the parsed command line.
   * @throws ParseException
   *           whenever an error occurs parsing the command line.
   */
  protected void processCommandLine(CommandLine cmd) throws ParseException {
    applicationContextKey = cmd.getOptionValue(APP_CONTEXT);
    startupLocale = new Locale(cmd.getOptionValue(LOCALE, "en"));
    actionBeanId = cmd.getOptionValue(ACTION_ID);
    batchUserName = cmd.getOptionValue(BATCH_USER_NAME, "batch");
  }

  /**
   * Creates the CLI options.
   * 
   * @return the CLI options.
   */
  @SuppressWarnings("static-access")
  protected Options createOptions() {
    Options options = new Options();
    options.addOption(OptionBuilder.withArgName(APP_CONTEXT).hasArg()
        .isRequired().withDescription("use given Spring application context.")
        .create(APP_CONTEXT));
    options.addOption(OptionBuilder.withArgName(LOCALE).hasArg()
        .withDescription("use given language (defaults to 'en').").create(
            LOCALE));
    options.addOption(OptionBuilder.withArgName(ACTION_ID).hasArg()
        .isRequired().withDescription("use the specified backend action.")
        .create(ACTION_ID));
    options.addOption(OptionBuilder.withArgName(BATCH_USER_NAME).hasArg()
        .withDescription("use the specified batch user name.").create(
            BATCH_USER_NAME));
    return options;
  }

  /**
   * Creates a default batch user subject.
   * 
   * @return a default batch user subject.
   */
  protected Subject createSubject() {
    Subject s = new Subject();
    UserPrincipal p = new UserPrincipal(batchUserName);
    s.getPrincipals().add(p);
    return s;
  }
}
