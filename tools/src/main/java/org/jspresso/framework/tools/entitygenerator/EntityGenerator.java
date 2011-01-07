/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.tools.entitygenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.util.freemarker.CompareStrings;
import org.jspresso.framework.util.freemarker.GenerateSqlName;
import org.jspresso.framework.util.freemarker.InstanceOf;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Generates Jspresso powered component java code based on its descriptor.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EntityGenerator {

  private static final String BEAN_FACTORY_SELECTOR   = "beanFactorySelector";
  private static final String APPLICATION_CONTEXT_KEY = "applicationContextKey";
  private static final String COMPONENT_IDS           = "componentIds";
  private static final String EXCLUDE_PATTERN         = "excludePatterns";
  private static final String GENERATE_ANNOTATIONS    = "generateAnnotations";
  private static final String INCLUDE_PACKAGES        = "includePackages";
  private static final String OUTPUT_DIR              = "outputDir";
  private static final String TEMPLATE_NAME           = "templateName";
  private static final String TEMPLATE_RESOURCE_PATH  = "templateResourcePath";

  private String              beanFactorySelector;
  private String              applicationContextKey;
  private String[]            componentIds;
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
   *          the command line arguments.
   */
  @SuppressWarnings("static-access")
  public static void main(String[] args) {
    Options options = new Options();
    options.addOption(OptionBuilder
        .withArgName(BEAN_FACTORY_SELECTOR)
        .hasArg()
        .withDescription(
            "use given selector too lookup the bean ref factory context file."
                + " If not set, defaults to classpath*:beanRefFactory.xml .")
        .create(BEAN_FACTORY_SELECTOR));
    options
        .addOption(OptionBuilder
            .withArgName(APPLICATION_CONTEXT_KEY)
            .isRequired()
            .hasArg()
            .withDescription(
                "use given applicationContextKey as registered in the spring BeanFactoryLocator.")
            .create(APPLICATION_CONTEXT_KEY));
    options.addOption(OptionBuilder
        .withArgName(TEMPLATE_RESOURCE_PATH)
        .isRequired()
        .hasArg()
        .withDescription(
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
    options.addOption(OptionBuilder
        .withArgName(EXCLUDE_PATTERN)
        .hasArgs()
        .withValueSeparator(',')
        .withDescription(
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
            .withArgName(COMPONENT_IDS)
            .hasArgs()
            .withValueSeparator(',')
            .withDescription(
                "generate code for the given component descriptor identifiers in the application context.")
            .create(COMPONENT_IDS));
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
    generator.setBeanFactorySelector(cmd.getOptionValue(BEAN_FACTORY_SELECTOR));
    generator.setApplicationContextKey(cmd
        .getOptionValue(APPLICATION_CONTEXT_KEY));
    generator.setTemplateResourcePath(cmd
        .getOptionValue(TEMPLATE_RESOURCE_PATH));
    generator.setTemplateName(cmd.getOptionValue(TEMPLATE_NAME));
    generator.setOutputDir(cmd.getOptionValue(OUTPUT_DIR));
    generator.setIncludePackages(cmd.getOptionValues(INCLUDE_PACKAGES));
    generator.setExcludePatterns(cmd.getOptionValues(EXCLUDE_PATTERN));
    generator.setGenerateAnnotations(cmd.hasOption(GENERATE_ANNOTATIONS));
    generator.setComponentIds(cmd.getOptionValues(COMPONENT_IDS));
    generator.generateComponents();
  }

  /**
   * Generates the component java source files.
   */
  @SuppressWarnings("unchecked")
  public void generateComponents() {
    ListableBeanFactory appContext = getListableBeanFactory();
    Collection<IComponentDescriptor<?>> componentDescriptors = new LinkedHashSet<IComponentDescriptor<?>>();
    if (componentIds == null) {
      Map<String, IComponentDescriptor<?>> allComponents = appContext
          .getBeansOfType(IComponentDescriptor.class);
      for (Map.Entry<String, IComponentDescriptor<?>> componentEntry : allComponents
          .entrySet()) {
        String className = componentEntry.getValue().getName();
        if (className != null) {
          boolean include = false;
          if (includePackages != null) {
            for (String pkg : includePackages) {
              if (className.startsWith(pkg)) {
                include = true;
              }
            }
          } else {
            include = true;
          }
          if (include) {
            if (excludePatterns != null) {
              for (String excludePattern : excludePatterns) {
                if (include
                    && Pattern.matches(excludePattern, componentEntry
                        .getValue().getName())) {
                  include = false;
                }
              }
            }
            if (include) {
              componentDescriptors.add(componentEntry.getValue());
            }
          }
        }
      }
    } else {
      for (String componentId : componentIds) {
        componentDescriptors.add((IComponentDescriptor<?>) appContext
            .getBean(componentId));
      }
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
    for (IComponentDescriptor<?> componentDescriptor : componentDescriptors) {
      OutputStream out;
      if (outputDir != null) {
        try {
          File outFile = new File(outputDir + "/"
              + componentDescriptor.getName().replace('.', '/') + ".java");
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
      rootContext.put("componentDescriptor", componentDescriptor);
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
   * Sets the beanFactorySelector.
   * 
   * @param beanFactorySelector
   *          the beanFactorySelector to set.
   */
  public void setBeanFactorySelector(String beanFactorySelector) {
    this.beanFactorySelector = beanFactorySelector;
  }

  /**
   * Sets the applicationContextKey.
   * 
   * @param applicationContextKey
   *          the applicationContextKey to set.
   */
  public void setApplicationContextKey(String applicationContextKey) {
    this.applicationContextKey = applicationContextKey;
  }

  /**
   * Sets the componentIds.
   * 
   * @param componentIds
   *          the componentIds to set.
   */
  public void setComponentIds(String[] componentIds) {
    this.componentIds = componentIds;
  }

  /**
   * Sets the excludePatterns.
   * 
   * @param excludePatterns
   *          the excludePatterns to set.
   */
  public void setExcludePatterns(String[] excludePatterns) {
    this.excludePatterns = excludePatterns;
  }

  /**
   * Sets the generateAnnotations.
   * 
   * @param generateAnnotations
   *          the generateAnnotations to set.
   */
  public void setGenerateAnnotations(boolean generateAnnotations) {
    this.generateAnnotations = generateAnnotations;
  }

  /**
   * Sets the includePackages.
   * 
   * @param includePackages
   *          the includePackages to set.
   */
  public void setIncludePackages(String[] includePackages) {
    this.includePackages = includePackages;
  }

  /**
   * Sets the outputDir.
   * 
   * @param outputDir
   *          the outputDir to set.
   */
  public void setOutputDir(String outputDir) {
    this.outputDir = outputDir;
  }

  /**
   * Sets the templateName.
   * 
   * @param templateName
   *          the templateName to set.
   */
  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  /**
   * Sets the templateResourcePath.
   * 
   * @param templateResourcePath
   *          the templateResourcePath to set.
   */
  public void setTemplateResourcePath(String templateResourcePath) {
    this.templateResourcePath = templateResourcePath;
  }

  private ListableBeanFactory getListableBeanFactory() {
    BeanFactoryLocator bfl = SingletonBeanFactoryLocator
        .getInstance(beanFactorySelector);
    BeanFactoryReference bf = bfl.useBeanFactory(applicationContextKey);
    return (ListableBeanFactory) bf.getFactory();
  }
}
