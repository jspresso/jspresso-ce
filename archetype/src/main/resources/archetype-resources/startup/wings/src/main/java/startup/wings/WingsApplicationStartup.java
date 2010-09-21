package ${package}.startup.wings;

import org.jspresso.framework.application.startup.wings.WingsStartup;

/**
 * Wings application startup class.
 */
public class WingsApplicationStartup extends WingsStartup {

  /**
   * Returns the "${rootArtifactId}-wings-context" value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected String getApplicationContextKey() {
    return "${rootArtifactId}-wings-context";
  }

  /**
   * Overrides default bean ref locator.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected String getBeanFactorySelector() {
    return "${packageInPathFormat}/beanRefFactory.xml";
  }
}
