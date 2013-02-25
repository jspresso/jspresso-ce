package org.jspresso.maven;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.slf4j.impl.StaticLoggerBinder;

/**
 * Goal which performs SJS compilation for a Jspresso project.
 */
@Mojo(name = "compile-sjs", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class SjsMojo extends AbstractMojo {

  /**
   * The Maven project.
   */
  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  MavenProject   project;

  /**
   * The directory containing the grouvy dsl sources.
   */
  @Parameter(defaultValue = "${basedir}/src/main/dsl", required = true)
  private File   srcDir;

  /**
   * The name of the application groovy file that packs the application.
   */
  @Parameter(defaultValue = "application.groovy", required = true)
  private String applicationFileName;

  /**
   * Sets the output directory for generated resources.
   */
  @Parameter(defaultValue = "${project.build.directory}/generated-resources/dsl", required = true)
  private File   outputDir;

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
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void execute() throws MojoExecutionException {
    // bind slf4j to maven log
    StaticLoggerBinder.getSingleton().setLog(getLog());
    if (isChangeDetected()) {
      try {
        runSjsCompilation();
      } catch (IOException ex) {
        new MojoExecutionException(
            "An unexpected exception occured when running SJS compilation.", ex);
      } catch (ResourceException ex) {
        new MojoExecutionException(
            "An unexpected exception occured when running SJS compilation.", ex);
      } catch (ScriptException ex) {
        new MojoExecutionException(
            "An unexpected exception occured when running SJS compilation.", ex);
      }
    } else {
      getLog().info("No change detected. Skipping generation.");
    }
    project.addCompileSourceRoot(outputDir.getPath());
  }

  private void runSjsCompilation() throws IOException, ResourceException,
      ScriptException {
    Properties projectProperties = project.getProperties();
    projectProperties.put("srcDir", srcDir.getAbsolutePath());
    projectProperties.put("outputDir", outputDir.getAbsolutePath());
    projectProperties.put("modelOutputFileName", modelOutputFileName);
    projectProperties.put("viewOutputFileName", viewOutputFileName);
    projectProperties.put("backOutputFileName", backOutputFileName);
    projectProperties.put("frontOutputFileName", frontOutputFileName);
    GroovyScriptEngine gse = new GroovyScriptEngine(new URL[] {
      srcDir.toURI().toURL()
    });
    Binding binding = new Binding();
    binding.setVariable("project", project);
    gse.run(applicationFileName, binding);
  }

  private boolean isChangeDetected() {
    if (!outputDir.exists()) {
      return true;
    }
    long outputLastModified = latestModified(outputDir,
        outputDir.lastModified());
    getLog().info("Scanning for changes : " + srcDir);
    if (hasChangedSourceFile(srcDir, outputLastModified)) {
      return true;
    }
    return false;
  }

  private long latestModified(File root, long maxLastModified) {
    long latest = maxLastModified;
    if (root.lastModified() > maxLastModified) {
      latest = root.lastModified();
    }
    if (root.isDirectory()) {
      for (File child : root.listFiles()) {
        latest = latestModified(child, latest);
      }
    }
    return latest;
  }

  private boolean hasChangedSourceFile(File source, long maxLastModified) {
    if (source.isDirectory()) {
      for (File childSource : source.listFiles()) {
        if (hasChangedSourceFile(childSource, maxLastModified)) {
          return true;
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
}
