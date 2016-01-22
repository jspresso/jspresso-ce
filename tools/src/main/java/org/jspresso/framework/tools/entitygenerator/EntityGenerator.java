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
package org.jspresso.framework.tools.entitygenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.regex.Pattern;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.hibernate.type.BasicTypeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator;
import org.springframework.context.ApplicationContext;

import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.util.freemarker.CompactString;
import org.jspresso.framework.util.freemarker.CompareStrings;
import org.jspresso.framework.util.freemarker.DedupSqlName;
import org.jspresso.framework.util.freemarker.GenerateSqlName;
import org.jspresso.framework.util.freemarker.InstanceOf;
import org.jspresso.framework.util.freemarker.ReduceSqlName;

/**
 * Generates Jspresso powered component java code based on its descriptor.
 *
 * @author Vincent Vandenschrick
 */
@SuppressWarnings("UseOfSystemOutOrSystemErr")
public class EntityGenerator {

  private static final Logger LOG                     = LoggerFactory
                                                          .getLogger(EntityGenerator.class);

  private static final String BEAN_FACTORY_SELECTOR   = "beanFactorySelector";
  private static final String APPLICATION_CONTEXT_KEY = "applicationContextKey";
  private static final String COMPONENT_IDS           = "componentIds";
  private static final String EXCLUDE_PATTERN         = "excludePatterns";
  private static final String GENERATE_ANNOTATIONS    = "generateAnnotations";
  private static final String INCLUDE_PACKAGES        = "includePackages";
  private static final String MAX_SQL_NAME_SIZE       = "maxSqlNameSize";
  private static final String OUTPUT_DIR              = "outputDir";
  private static final String FILE_EXTENSION          = "fileExtension";
  private static final String CLASSNAME_PREFIX        = "classnamePrefix";
  private static final String CLASSNAME_SUFFIX        = "classnameSuffix";
  private static final String TEMPLATE_NAME           = "templateName";
  private static final String TEMPLATE_RESOURCE_PATH  = "templateResourcePath";

  private String              beanFactorySelector;
  private String              applicationContextKey;
  private String[]            componentIds;
  private String[]            excludePatterns;
  private boolean             generateAnnotations;
  private String[]            includePackages;
  private String              outputDir;
  private String              fileExtension;
  private String              classnamePrefix;
  private String              classnameSuffix;
  private int                 maxSqlNameSize          = -1;
  private String              templateName;
  private String              templateResourcePath;

  /**
   * Starts Code generation for an component.
   *
   * @param args
   *          the command line arguments.
   */
  @SuppressWarnings("static-access")
  public static void main(String... args) {
    Options options = new Options();
    options.addOption(Option.builder(BEAN_FACTORY_SELECTOR)
        .argName(BEAN_FACTORY_SELECTOR)
        .hasArg()
        .desc(
            "uses given selector too lookup the bean ref factory context file."
                + " If not set, defaults to classpath*:beanRefFactory.xml .")
        .build());
    options
        .addOption(Option.builder(APPLICATION_CONTEXT_KEY)
            .argName(APPLICATION_CONTEXT_KEY)
            .required()
            .hasArg()
            .desc(
                "uses given applicationContextKey as registered in the spring BeanFactoryLocator.")
            .build());
    options.addOption(Option.builder(TEMPLATE_RESOURCE_PATH)
        .argName(TEMPLATE_RESOURCE_PATH)
        .required()
        .hasArg()
        .desc(
            "sets the resource path of the directory containing the templates.")
        .build());
    options.addOption(Option.builder(TEMPLATE_NAME).argName(TEMPLATE_NAME).required()
        .hasArg().desc("sets the used component code template.")
        .build());
    options.addOption(Option.builder(OUTPUT_DIR).argName(OUTPUT_DIR).hasArg()
        .desc("sets the output directory for generated source.")
        .build());
    options.addOption(Option.builder().argName(FILE_EXTENSION).hasArg()
        .desc("sets the file extension for generated source.")
        .build());
    options.addOption(Option.builder(CLASSNAME_PREFIX).argName(CLASSNAME_PREFIX).hasArg()
        .desc("prepends a prefix to generated class names.")
        .build());
    options.addOption(Option.builder(CLASSNAME_SUFFIX).argName(CLASSNAME_SUFFIX).hasArg()
        .desc("appends a suffix to generated class names.")
        .build());
    options.addOption(Option.builder(MAX_SQL_NAME_SIZE).argName(MAX_SQL_NAME_SIZE).hasArg()
        .desc("limits the size of the generated SQL names.")
        .build());
    options
        .addOption(Option.builder(INCLUDE_PACKAGES)
            .argName(INCLUDE_PACKAGES)
            .hasArgs()
            .valueSeparator(',')
            .desc(
                "generates code for the component descriptors declared in the listed packages.")
            .build());
    options.addOption(Option.builder(EXCLUDE_PATTERN)
        .argName(EXCLUDE_PATTERN)
        .hasArgs()
        .valueSeparator(',')
        .desc(
            "excludes classes whose names match the regular expression.")
        .build());
    options
        .addOption(Option.builder(GENERATE_ANNOTATIONS)
            .argName(GENERATE_ANNOTATIONS)
            .desc(
                "generates java5 annotations (incompatible with XDoclet as of now).")
            .build());
    options
        .addOption(Option.builder(COMPONENT_IDS)
            .argName(COMPONENT_IDS)
            .hasArgs()
            .valueSeparator(',')
            .desc(
                "generates code for the given component descriptor identifiers in the application context.")
            .build());
    CommandLineParser parser = new DefaultParser();
    CommandLine cmd;
    try {
      cmd = parser.parse(options, args);
    } catch (ParseException ex) {
      LOG.error("Error parsing command line", ex);
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
    generator.setOutputDir(cmd.getOptionValue(OUTPUT_DIR, "."));
    generator.setFileExtension(cmd.getOptionValue(FILE_EXTENSION, "java"));
    generator.setClassnamePrefix(cmd.getOptionValue(CLASSNAME_PREFIX));
    generator.setClassnameSuffix(cmd.getOptionValue(CLASSNAME_SUFFIX));
    generator.setIncludePackages(cmd.getOptionValues(INCLUDE_PACKAGES));
    generator.setExcludePatterns(cmd.getOptionValues(EXCLUDE_PATTERN));
    generator.setGenerateAnnotations(cmd.hasOption(GENERATE_ANNOTATIONS));
    generator.setComponentIds(cmd.getOptionValues(COMPONENT_IDS));
    String msns = cmd.getOptionValue(MAX_SQL_NAME_SIZE);
    if (msns != null && msns.length() > 0) {
      generator.setMaxSqlNameSize(Integer.parseInt(msns));
    }
    generator.generateComponents();
  }

  /**
   * Generates the component java source files.
   */
  @SuppressWarnings({
      "rawtypes"
      , "ConstantConditions"})
  public void generateComponents() {
    LOG.debug("Loading Spring context {}.", applicationContextKey);
    BeanFactoryReference bfr = getBeanFactoryReference();
    try {
      ApplicationContext appContext = (ApplicationContext) bfr.getFactory();
      LOG.debug("Spring context {} loaded.", applicationContextKey);
      Collection<IComponentDescriptor<?>> componentDescriptors = new LinkedHashSet<>();
      if (componentIds == null) {
        LOG.debug("Retrieving components from Spring context.");
        Map<String, IComponentDescriptor> allComponents = appContext
            .getBeansOfType(IComponentDescriptor.class);
        LOG.debug("{} components retrieved.",
            allComponents.size());
        LOG.debug("Filtering components to generate.");
        for (Map.Entry<String, IComponentDescriptor> componentEntry : allComponents
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
      LOG.debug("{} components filtered.",
          componentDescriptors.size());
      LOG.debug("Initializing Freemarker template");
      Version version = new Version(2, 3, 23);
      Configuration cfg = new Configuration(version);
      cfg.setClassForTemplateLoading(getClass(), templateResourcePath);
      BeansWrapper wrapper = new DefaultObjectWrapper(version);
      cfg.setObjectWrapper(new DefaultObjectWrapper(version));
      Template template;
      try {
        template = cfg.getTemplate(templateName);
      } catch (IOException ex) {
        LOG.error("Error while loading the template", ex);
        return;
      }
      Map<String, Object> rootContext = new HashMap<>();

      rootContext.put("generateSQLName", new GenerateSqlName());
      rootContext.put("dedupSQLName", new DedupSqlName(false));
      rootContext.put("reduceSQLName", new ReduceSqlName(maxSqlNameSize, new DedupSqlName(true)));
      rootContext.put("instanceof", new InstanceOf(wrapper));
      rootContext.put("compareStrings", new CompareStrings(wrapper));
      rootContext.put("compactString", new CompactString());
      rootContext.put("componentTranslationsDescriptor", appContext.getBean("componentTranslationsDescriptor"));
      rootContext.put("generateAnnotations",
          generateAnnotations);
      rootContext.put("hibernateTypeRegistry", new BasicTypeRegistry());
      if (classnamePrefix == null) {
        classnamePrefix = "";
      }
      if (classnameSuffix == null) {
        classnameSuffix = "";
      }
      LOG.debug("Freemarker template initialized");
      for (IComponentDescriptor<?> componentDescriptor : componentDescriptors) {
        OutputStream out = null;
        if (outputDir != null) {
          String cDescName = componentDescriptor.getName();
          int lastDotIndex = cDescName.lastIndexOf('.');
          if (lastDotIndex >= 0) {
            cDescName = cDescName.substring(0, lastDotIndex + 1)
                + classnamePrefix + cDescName.substring(lastDotIndex + 1);
          } else {
            cDescName = classnamePrefix + cDescName;
          }
          cDescName = cDescName + classnameSuffix;
          try {
            File outFile = new File(outputDir + "/"
                + cDescName.replace('.', '/') + "." + fileExtension);
            if (!outFile.exists()) {
              LOG.debug("Creating " + outFile.getName());
              if (!outFile.getParentFile().exists()) {
                //noinspection ResultOfMethodCallIgnored
                outFile.getParentFile().mkdirs();
              }
              //noinspection ResultOfMethodCallIgnored
              outFile.createNewFile();
              out = new FileOutputStream(outFile);
            } else if (componentDescriptor.getLastUpdated() > outFile
                .lastModified()) {
              out = new FileOutputStream(outFile);
            } else {
              LOG.debug("No change detected for {} : {} <= {}",
                  componentDescriptor.getName(),
                  new Date(componentDescriptor.getLastUpdated()),
                  new Date(outFile.lastModified()));
            }
          } catch (IOException ex) {
            LOG.error("Error while writing output", ex);
            return;
          }
        } else {
          out = System.out;
        }
        if (out != null) {
          LOG.info("Generating source code for {}",
              componentDescriptor.getName());
          rootContext.put("componentDescriptor", componentDescriptor);
          try {
            template.process(rootContext, new OutputStreamWriter(out));
            out.flush();
            if (out != System.out) {
              out.close();
            }
          } catch (TemplateException ex) {
            LOG.error("Error while processing the template", ex);
            return;
          } catch (IOException ex) {
            LOG.error("Error while writing output", ex);
            return;
          }
        } else {
          LOG.debug("Source code for {} is up to date. Skipping generation.",
              componentDescriptor.getName());
        }
        LOG.debug("Finished generating Source code for {}.",
            componentDescriptor.getName());
      }
    } finally {
      bfr.release();
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
  public void setComponentIds(String... componentIds) {
    this.componentIds = componentIds;
  }

  /**
   * Sets the excludePatterns.
   *
   * @param excludePatterns
   *          the excludePatterns to set.
   */
  public void setExcludePatterns(String... excludePatterns) {
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
  public void setIncludePackages(String... includePackages) {
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
   * Sets the classnamePrefix.
   *
   * @param classnamePrefix
   *          the classnamePrefix to set.
   */
  public void setClassnamePrefix(String classnamePrefix) {
    this.classnamePrefix = classnamePrefix;
  }

  /**
   * Sets the classnameSuffix.
   *
   * @param classnameSuffix
   *          the classnameSuffix to set.
   */
  public void setClassnameSuffix(String classnameSuffix) {
    this.classnameSuffix = classnameSuffix;
  }

  /**
   * Sets the maxSqlNameSize.
   *
   * @param maxSqlNameSize
   *          the maxSqlNameSize to set.
   */
  public void setMaxSqlNameSize(int maxSqlNameSize) {
    this.maxSqlNameSize = maxSqlNameSize;
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

  private BeanFactoryReference getBeanFactoryReference() {
    BeanFactoryLocator bfl = SingletonBeanFactoryLocator
        .getInstance(beanFactorySelector);
    BeanFactoryReference bf = bfl.useBeanFactory(applicationContextKey);
    return bf;
  }

  /**
   * Sets the fileExtension.
   *
   * @param fileExtension
   *          the fileExtension to set.
   */
  public void setFileExtension(String fileExtension) {
    this.fileExtension = fileExtension;
  }
}
