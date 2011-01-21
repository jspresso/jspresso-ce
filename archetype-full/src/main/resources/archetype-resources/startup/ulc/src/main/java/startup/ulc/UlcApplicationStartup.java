package ${package}.startup.ulc;

import org.jspresso.framework.application.startup.ulc.UlcStartup;

/**
 * ULC application startup class.
 */
public class UlcApplicationStartup extends UlcStartup {

  /**
   * Returns the "${rootArtifactId}-ulc-context" value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected String getApplicationContextKey() {
    return "${rootArtifactId}-ulc-context";
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
