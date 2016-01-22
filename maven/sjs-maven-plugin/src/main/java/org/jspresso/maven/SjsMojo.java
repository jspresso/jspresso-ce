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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

/**
 * Goal which performs SJS compilation for a Jspresso project.
 *
 * @author Vincent Vandenschrick
 */
@Mojo(name = "compile-sjs", defaultPhase = LifecyclePhase.GENERATE_SOURCES,
    requiresDependencyResolution = ResolutionScope.COMPILE, threadSafe = true)
public class SjsMojo extends AbstractMojo {

  /**
   * The Maven project.
   */
  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  private MavenProject project;

  /**
   * The directory containing the groovy dsl sources.
   */
  @Parameter(defaultValue = "${basedir}/src/main/dsl", required = true)
  private File srcDir;

  /**
   * The name of the application groovy file that packs the application.
   */
  @Parameter(defaultValue = "application.groovy", required = true)
  private String applicationFileName;

  /**
   * Sets the output directory for generated resources.
   */
  @Parameter(defaultValue = "${project.build.directory}/generated-resources/dsl", required = true)
  private File outputDir;

  /**
   * The target file name for the model Spring beans.
   */
  @Parameter(defaultValue = "dsl-model.xml", required = true)
  private String modelOutputFileName;

  /**
   * The target file name for the view Spring beans.
   */
  @Parameter(defaultValue = "dsl-view.xml", required = true)
  private String viewOutputFileName;

  /**
   * The target file name for the backend Spring beans.
   */
  @Parameter(defaultValue = "dsl-backend.xml", required = true)
  private String backOutputFileName;

  /**
   * The target file name for the frontend Spring beans.
   */
  @Parameter(defaultValue = "dsl-frontend.xml", required = true)
  private String frontOutputFileName;

  /**
   * Triggers thee execution of SJS compilation.
   *
   * @throws MojoExecutionException
   *     whenever an unexpected error occurs when executing mojo.
   */
  @Override
  public void execute() throws MojoExecutionException {
    if (isChangeDetected()) {
      try {
        runSjsCompilation();
      } catch (IOException | DependencyResolutionRequiredException | ScriptException | ResourceException ex) {
        throw new MojoExecutionException(
            "An unexpected exception occurred when running SJS compilation.", ex);
      }
    } else {
      getLog().info("No change detected. Skipping generation.");
    }
    Resource outputResource = new Resource();
    outputResource.setDirectory(outputDir.getPath());
    project.addResource(outputResource);
  }

  private void runSjsCompilation() throws IOException, ResourceException,
      ScriptException, DependencyResolutionRequiredException {
    Properties projectProperties = project.getProperties();
    projectProperties.put("srcDir", srcDir.getAbsolutePath());
    projectProperties.put("outputDir", outputDir.getAbsolutePath());
    projectProperties.put("modelOutputFileName", modelOutputFileName);
    projectProperties.put("viewOutputFileName", viewOutputFileName);
    projectProperties.put("backOutputFileName", backOutputFileName);
    projectProperties.put("frontOutputFileName", frontOutputFileName);

    List<URL> classpath;
    classpath = new ArrayList<>();
    classpath.add(srcDir.toURI().toURL());
    List<String> compileClasspathElements = project
        .getCompileClasspathElements();
    for (String element : compileClasspathElements) {
      if (!element.equals(project.getBuild().getOutputDirectory())) {
        File elementFile = new File(element);
        getLog().debug(
            "Adding element to plugin classpath " + elementFile.getPath());
        URL url = elementFile.toURI().toURL();
        classpath.add(url);
      }
    }
    GroovyScriptEngine gse = new GroovyScriptEngine(
        classpath.toArray(new URL[classpath.size()]));
    Binding binding = new Binding();
    binding.setVariable("project", project);
    binding.setVariable("fail", new FailClosure());
    gse.run(applicationFileName, binding);
  }

  private boolean isChangeDetected() {
    if (!outputDir.exists() || outputDir.list().length == 0) {
      return true;
    }
    long outputLastModified = latestModified(outputDir,
        outputDir.lastModified());
    getLog().info("Scanning for changes : " + srcDir);
    return hasChangedSourceFile(srcDir, outputLastModified);
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

  private boolean hasChangedSourceFile(File source, long maxLastModified) {
    if (source.isDirectory()) {
      File[] files = source.listFiles();
      if (files != null) {
        for (File childSource : files) {
          if (hasChangedSourceFile(childSource, maxLastModified)) {
            return true;
          }
        }
      }
    } else {
      if (source.lastModified() > maxLastModified) {
        getLog().info(
            "Detected a change on source " + source.toString() + ". "
                + new Date(source.lastModified()) + " > "
                + new Date(maxLastModified));
        return true;
      }
    }
    return false;
  }

  //
  // FailClosure
  //
  private class FailClosure extends Closure<Object> {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@code FailClosure} instance.
     */
    public FailClosure() {
      super(SjsMojo.this);
    }

    @Override
    public Object call(Object... args) {
      if (args == null || args.length == 0) {
        throwRuntimeException(new MojoExecutionException("Failed"));
      } else if (args.length == 1) {
        if (args[0] instanceof Throwable) {
          Throwable cause = (Throwable) args[0];
          throwRuntimeException(new MojoExecutionException(cause.getMessage(),
              cause));
        } else {
          throwRuntimeException(new MojoExecutionException(
              String.valueOf(args[0])));
        }
      } else if (args.length == 2) {
        if (args[1] instanceof Throwable) {
          throwRuntimeException(new MojoExecutionException(
              String.valueOf(args[0]), (Throwable) args[1]));
        } else {
          throw new Error(
              "Invalid arguments to fail(Object, Throwable), second argument must be a Throwable");
        }
      } else {
        throw new Error(
            "Too many arguments for fail(), expected one of: fail(), fail(Object) or fail(Object, Throwable)");
      }
      return null;
    }
  }
}
