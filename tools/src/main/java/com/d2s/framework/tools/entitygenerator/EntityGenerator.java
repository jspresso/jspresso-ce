/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.tools.entitygenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator;
import org.springframework.context.ApplicationContext;

import com.d2s.framework.model.descriptor.IComponentDescriptor;
import commons.CompareStrings;
import commons.GenerateSqlName;
import commons.InstanceOf;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Generates D2S powered component java code based on its descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EntityGenerator {

  private static final String APPLICATION_CONTEXT_KEY = "applicationContextKey";
  private static final String COMPONENT_NAMES         = "componentNames";
  private static final String EXCLUDE_PATTERN         = "excludePatterns";
  private static final String GENERATE_ANNOTATIONS    = "generateAnnotations";
  private static final String INCLUDE_PACKAGES        = "includePackages";
  private static final String OUTPUT_DIR              = "outputDir";
  private static final String TEMPLATE_NAME           = "templateName";
  private static final String TEMPLATE_RESOURCE_PATH  = "templateResourcePath";

  private String              applicationContextKey;
  private String[]            componentNames;
  private String[]            excludePatterns;
  private boolean             generateAnnotations;
  private String[]            includePackages;
  private String              outputDir;
  private String              templateName;
  private String              templateResourcePath;

  /**
   * Starts Code generation for an component.
   * 
   * @param args
   *            the command line arguments.
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
        .hasArg().withDescription("sets the used component code template.")
        .create(TEMPLATE_NAME));
    options.addOption(OptionBuilder.withArgName(OUTPUT_DIR).hasArg()
        .withDescription("sets the output directory for generated source.")
        .create(OUTPUT_DIR));
    options
        .addOption(OptionBuilder
            .withArgName(INCLUDE_PACKAGES)
            .hasArgs()
            .withValueSeparator(',')
            .withDescription(
                "generate code for the component descriptors declared in the listed packages.")
            .create(INCLUDE_PACKAGES));
    options.addOption(OptionBuilder.withArgName(EXCLUDE_PATTERN).hasArgs()
        .withValueSeparator(',').withDescription(
            "exclude classes whose names match the regular expression.")
        .create(EXCLUDE_PATTERN));
    options
        .addOption(OptionBuilder
            .withArgName(GENERATE_ANNOTATIONS)
            .withDescription(
                "generate java5 annotations (incompatible with XDoclet as of now).")
            .create(GENERATE_ANNOTATIONS));
    options
        .addOption(OptionBuilder
            .withArgName(COMPONENT_NAMES)
            .hasArgs()
            .withValueSeparator(',')
            .withDescription(
                "generate code for the given component descriptor names in the application context.")
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
    generator.setApplicationContextKey(cmd
        .getOptionValue(APPLICATION_CONTEXT_KEY));
    generator.setTemplateResourcePath(cmd
        .getOptionValue(TEMPLATE_RESOURCE_PATH));
    generator.setTemplateName(cmd.getOptionValue(TEMPLATE_NAME));
    generator.setOutputDir(cmd.getOptionValue(OUTPUT_DIR));
    generator.setIncludePackages(cmd.getOptionValues(INCLUDE_PACKAGES));
    generator.setExcludePatterns(cmd.getOptionValues(EXCLUDE_PATTERN));
    generator.setGenerateAnnotations(cmd.hasOption(GENERATE_ANNOTATIONS));
    generator.setComponentNames(cmd.getOptionValues(COMPONENT_NAMES));
    generator.generateComponents();
  }

  /**
   * Generates the component java source files.
   */
  public void generateComponents() {
    ApplicationContext appContext = getApplicationContext();
    if (componentNames == null) {
      String[] allComponentNames = appContext
          .getBeanNamesForType(IComponentDescriptor.class);
      Set<String> filteredComponentNames = new LinkedHashSet<String>();
      if (includePackages != null) {
        for (String componentName : allComponentNames) {
          for (String pkg : includePackages) {
            if (componentName.startsWith(pkg)) {
              boolean include = true;
              if (excludePatterns != null) {
                for (String excludePattern : excludePatterns) {
                  if (include && Pattern.matches(excludePattern, componentName)) {
                    include = false;
                  }
                }
              }
              if (include) {
                filteredComponentNames.add(componentName);
              }
            }
          }
        }
      }
      componentNames = new ArrayList<String>(filteredComponentNames)
          .toArray(new String[0]);
    }
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
    rootContext.put("generateAnnotations", new Boolean(generateAnnotations));
    for (String componentName : componentNames) {
      OutputStream out;
      if (outputDir != null) {
        try {
          File outFile = new File(outputDir + "/"
              + componentName.replace('.', '/') + ".java");
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
      rootContext.put("componentDescriptor", appContext.getBean(componentName));
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

  /**
   * Sets the applicationContextKey.
   * 
   * @param applicationContextKey
   *            the applicationContextKey to set.
   */
  public void setApplicationContextKey(String applicationContextKey) {
    this.applicationContextKey = applicationContextKey;
  }

  /**
   * Sets the componentNames.
   * 
   * @param componentNames
   *            the componentNames to set.
   */
  public void setComponentNames(String[] componentNames) {
    this.componentNames = componentNames;
  }

  /**
   * Sets the excludePatterns.
   * 
   * @param excludePatterns
   *            the excludePatterns to set.
   */
  public void setExcludePatterns(String[] excludePatterns) {
    this.excludePatterns = excludePatterns;
  }

  /**
   * Sets the generateAnnotations.
   * 
   * @param generateAnnotations
   *            the generateAnnotations to set.
   */
  public void setGenerateAnnotations(boolean generateAnnotations) {
    this.generateAnnotations = generateAnnotations;
  }

  /**
   * Sets the includePackages.
   * 
   * @param includePackages
   *            the includePackages to set.
   */
  public void setIncludePackages(String[] includePackages) {
    this.includePackages = includePackages;
  }

  /**
   * Sets the outputDir.
   * 
   * @param outputDir
   *            the outputDir to set.
   */
  public void setOutputDir(String outputDir) {
    this.outputDir = outputDir;
  }

  /**
   * Sets the templateName.
   * 
   * @param templateName
   *            the templateName to set.
   */
  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  /**
   * Sets the templateResourcePath.
   * 
   * @param templateResourcePath
   *            the templateResourcePath to set.
   */
  public void setTemplateResourcePath(String templateResourcePath) {
    this.templateResourcePath = templateResourcePath;
  }

  private ApplicationContext getApplicationContext() {
    BeanFactoryLocator bfl = SingletonBeanFactoryLocator.getInstance();
    BeanFactoryReference bf = bfl.useBeanFactory(applicationContextKey);
    return (ApplicationContext) bf.getFactory();
  }
}
