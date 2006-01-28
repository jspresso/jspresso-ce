/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.tools.entitygenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Generates D2S powered entity java code based on its descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EntityGenerator {

  private static final String APPLICATION_CONTEXT_KEY = "applicationContextKey";
  private static final String TEMPLATE_RESOURCE_PATH  = "templateResourcePath";
  private static final String TEMPLATE_NAME           = "templateName";
  private static final String OUTPUT_DIR              = "outputDir";
  private static final String COMPONENT_NAMES         = "componentNames";

  /**
   * Generates D2S powered entity java code based on its descriptor.
   * 
   * @param applicationContextKey
   *          the applicationContextKey as registered in the spring
   *          BeanFactoryLocator.
   * @param templateResourcePath
   *          the resource path of the directory containg the templates.
   * @param templateName
   *          the used entity code template.
   * @param outputDir
   *          the root directory where generated sources are generated.
   * @param entityNames
   *          the entity descriptor names in the application context.
   */
  public void generateEntity(String applicationContextKey,
      String templateResourcePath, String templateName, String outputDir,
      String[] entityNames) {
    Configuration cfg = new Configuration();
    cfg.setClassForTemplateLoading(getClass(), templateResourcePath);
    BeansWrapper wrapper = new DefaultObjectWrapper();
    cfg.setObjectWrapper(new DefaultObjectWrapper());
    Template template = null;
    try {
      template = cfg.getTemplate(templateName);
    } catch (IOException ex) {
      ex.printStackTrace();
      return;
    }
    Map<String, Object> rootContext = new HashMap<String, Object>();

    rootContext.put("generateSQLName", new GenerateSqlName());
    rootContext.put("instanceof", new InstanceOf(wrapper));
    rootContext.put("compareStrings", new CompareStrings(wrapper));
    for (String entityName : entityNames) {
      OutputStream out;
      if (outputDir != null) {
        try {
          File outFile = new File(outputDir + "/"
              + entityName.replace('.', '/') + ".java");
          if (!outFile.exists()) {
            if (!outFile.getParentFile().exists()) {
              outFile.getParentFile().mkdirs();
            }
            outFile.createNewFile();
          }
          out = new FileOutputStream(outFile);
        } catch (IOException ex) {
          ex.printStackTrace();
          return;
        }
      } else {
        out = System.out;
      }
      rootContext.put("componentDescriptor", getApplicationContext(
          applicationContextKey).getBean(entityName));
      try {
        template.process(rootContext, new OutputStreamWriter(out));
        out.flush();
        if (out != System.out) {
          out.close();
        }
      } catch (TemplateException ex) {
        ex.printStackTrace();
        return;
      } catch (IOException ex) {
        ex.printStackTrace();
        return;
      }
    }
  }

  private BeanFactory getApplicationContext(String applicationContextKey) {
    BeanFactoryLocator bfl = SingletonBeanFactoryLocator.getInstance();
    BeanFactoryReference bf = bfl.useBeanFactory(applicationContextKey);
    return bf.getFactory();
  }

  /**
   * Starts Code generation for an entity.
   * 
   * @param args
   *          the command line arguments.
   */
  @SuppressWarnings("static-access")
  public static void main(String[] args) {
    Options options = new Options();
    options
        .addOption(OptionBuilder
            .withArgName(APPLICATION_CONTEXT_KEY)
            .isRequired()
            .hasArg()
            .withDescription(
                "use given applicationContextKey as registered in the spring BeanFactoryLocator.")
            .create(APPLICATION_CONTEXT_KEY));
    options.addOption(OptionBuilder.withArgName(TEMPLATE_RESOURCE_PATH)
        .isRequired().hasArg().withDescription(
            "sets the resource path of the directory containg the templates.")
        .create(TEMPLATE_RESOURCE_PATH));
    options.addOption(OptionBuilder.withArgName(TEMPLATE_NAME).isRequired()
        .hasArg().withDescription("sets the used entity code template.")
        .create(TEMPLATE_NAME));
    options.addOption(OptionBuilder.withArgName(OUTPUT_DIR).hasArg()
        .withDescription("sets the output directory for generated source.")
        .create(OUTPUT_DIR));
    options
        .addOption(OptionBuilder
            .withArgName(COMPONENT_NAMES)
            .isRequired()
            .hasArgs()
            .withValueSeparator(',')
            .withDescription(
                "generate code for the given entity descriptor name in the application context.")
            .create(COMPONENT_NAMES));
    CommandLineParser parser = new BasicParser();
    CommandLine cmd = null;
    try {
      cmd = parser.parse(options, args);
    } catch (ParseException ex) {
      System.err.println(ex.getLocalizedMessage());
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(EntityGenerator.class.getSimpleName(), options);
      return;
    }

    EntityGenerator generator = new EntityGenerator();
    generator.generateEntity(cmd.getOptionValue(APPLICATION_CONTEXT_KEY), cmd
        .getOptionValue(TEMPLATE_RESOURCE_PATH), cmd
        .getOptionValue(TEMPLATE_NAME), cmd.getOptionValue(OUTPUT_DIR), cmd
        .getOptionValues(COMPONENT_NAMES));
  }
}
