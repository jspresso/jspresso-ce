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
package org.jspresso.maven;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.classworlds.ClassWorld;
import org.codehaus.plexus.classworlds.realm.ClassRealm;

import org.jspresso.framework.tools.entitygenerator.EntityGenerator;

/**
 * Goal which generates entities for a Jspresso project.
 *
 * @author Vincent Vandenschrick
 */
@Mojo(name = "generate-entities", defaultPhase = LifecyclePhase.GENERATE_SOURCES,
    requiresDependencyResolution = ResolutionScope.COMPILE, threadSafe = true)
public class EntityGeneratorMojo extends AbstractMojo {

  /**
   * The Maven project.
   */
  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  private MavenProject project;

  /**
   * The source directory containing dsl files needed for change detection.
   */
  @Parameter(required = false)
  private File[] sourceDirs;

  /**
   * Uses given selector too lookup the bean ref factory context file. If not set, defaults to beanRefFactory.xml.
   */
  @Parameter(required = true)
  private String beanFactorySelector;

  /**
   * Uses given applicationContextKey as registered in the spring BeanFactoryLocator.
   */
  @Parameter(required = true)
  private String applicationContextKey;

  /**
   * Generates code for the given component descriptor identifiers in the application context.
   */
  @Parameter(required = false)
  private String[] componentIds;

  /**
   * Excludes classes whose names match the regular expression.
   */
  @Parameter(required = false)
  private String[] excludePatterns;

  /**
   * Generates java5 annotations (incompatible with XDoclet as of now).
   */
  @Parameter(defaultValue = "false", required = false)
  private boolean generateAnnotations;

  /**
   * Generates code for the component descriptors declared in the listed packages.
   */
  @Parameter(property = "includePackages", required = false)
  private String[] includePackages;

  /**
   * Configures a maximum size for the generated SQL mapping names.
   */
  @Parameter(defaultValue = "-1", property = "maxSqlNameSize", required = false)
  private int maxSqlNameSize;

  /**
   * Sets the output directory for generated source.
   */
  @Parameter(defaultValue = "${project.build.directory}/generated-sources/entitygenerator", required = true)
  private File outputDir;

  /**
   * Sets the file extension for generated source.
   */
  @Parameter(defaultValue = "java", required = true)
  private String fileExtension;

  /**
   * Prepends a prefix to generated class names.
   */
  @Parameter(required = false)
  private String classnamePrefix;

  /**
   * Appends a suffix to generated class names.
   */
  @Parameter(required = false)
  private String classnameSuffix;

  /**
   * Sets the used component code template.
   */
  @Parameter(defaultValue = "HibernateXdocletEntity.ftl", required = true)
  private String templateName;

  /**
   * Sets the path to lookup the template from.
   */
  @Parameter(defaultValue = "/org/jspresso/framework/tools/entitygenerator", required = true)
  private String templateResourcePath;

  /**
   * Triggers thee execution of EntityGenerator.
   *
   * @throws MojoExecutionException
   *     whenever an unexpected error occurs when executing mojo.
   */
  @Override
  public void execute() throws MojoExecutionException {
    if (sourceDirs == null) {
      sourceDirs = new File[]{new File(project.getBasedir(), "src/main/resources"), new File(project.getBasedir(),
          "target/generated-resources/dsl")};
    }
    if (isChangeDetected()) {
      ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
      //create a new class loading space
      ClassWorld world = new ClassWorld();
      try {
        try {
          //use the existing ContextClassLoader in a realm of the class loading space
          getLog().debug("Creating new class realm for entity generator execution");
          ClassRealm entityGeneratorRealm = world.newRealm(getClass().getName(),
              contextClassLoader);
          setupPluginClasspath(entityGeneratorRealm);
          //make the child realm the ContextClassLoader
          Thread.currentThread().setContextClassLoader(entityGeneratorRealm);
          runEntityGenerator();
        } finally {
          getLog().debug("Disposing entity generator class realm");
          world.disposeRealm(getClass().getName());
          Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
      } catch (Exception ex) {
        throw new MojoExecutionException(ex.toString(), ex);
      }
    } else {
      getLog().info("No change detected. Skipping generation.");
    }
    project.addCompileSourceRoot(outputDir.getPath());
  }

  private void runEntityGenerator() {
    EntityGenerator generator = new EntityGenerator();
    generator.setBeanFactorySelector(beanFactorySelector);
    generator.setApplicationContextKey(applicationContextKey);
    generator.setTemplateResourcePath(templateResourcePath);
    generator.setTemplateName(templateName);
    generator.setOutputDir(outputDir.getAbsolutePath());
    generator.setFileExtension(fileExtension);
    generator.setClassnamePrefix(classnamePrefix);
    generator.setClassnameSuffix(classnameSuffix);
    generator.setIncludePackages(includePackages);
    generator.setExcludePatterns(excludePatterns);
    generator.setGenerateAnnotations(generateAnnotations);
    generator.setComponentIds(componentIds);
    generator.setMaxSqlNameSize(maxSqlNameSize);
    generator.generateComponents();
  }

  private boolean isChangeDetected() {
    if (!outputDir.exists() || outputDir.list().length == 0) {
      return true;
    }
    long outputLastModified = latestModified(outputDir, outputDir.lastModified());
    for (File sourceDir : sourceDirs) {
      getLog().info("Scanning for changes : " + sourceDir.getAbsolutePath());
      if (hasChangedModelDslFile(sourceDir, outputLastModified)) {
        return true;
      }
    }
    return false;
  }

  private long latestModified(File root, long maxLastModified) {
    long latest = maxLastModified;
    if (root.lastModified() > maxLastModified) {
      latest = root.lastModified();
    }
    if (root.isDirectory()) {
      File[] files = root.listFiles();
      if (files != null) {
        for (File child : files) {
          latest = latestModified(child, latest);
        }
      }
    }
    return latest;
  }

  private boolean hasChangedModelDslFile(File source, long maxLastModified) {
    if (source.isDirectory()) {
      File[] files = source.listFiles();
      if (files != null) {
        for (File childSource : files) {
          if (hasChangedModelDslFile(childSource, maxLastModified)) {
            return true;
          }
        }
      }
    } else if (source.getName().toLowerCase().contains("model")) {
      if (source.lastModified() > maxLastModified) {
        getLog().info("Detected a change on resource " + source.toString() + ". " + new Date(source.lastModified())
            + " > " + new Date(maxLastModified));
        return true;
      }
    }
    return false;
  }

  private void setupPluginClasspath(ClassRealm entityGeneratorRealm) throws MojoExecutionException {
    try {
      for (File sourceDir : sourceDirs) {
        entityGeneratorRealm.addURL(sourceDir.toURI().toURL());
        getLog().debug("Adding element to plugin classpath " + sourceDir.getPath());
      }
      List<String> compileClasspathElements = project.getCompileClasspathElements();
      for (String element : compileClasspathElements) {
        if (!element.equals(project.getBuild().getOutputDirectory()) && !(element.contains("log4j"))) {
          File elementFile = new File(element);
          getLog().debug("Adding element to plugin classpath " + elementFile.getPath());
          URL url = elementFile.toURI().toURL();
          entityGeneratorRealm.addURL(url);
        }
      }
    } catch (Exception ex) {
      throw new MojoExecutionException(ex.toString(), ex);
    }
  }

}
