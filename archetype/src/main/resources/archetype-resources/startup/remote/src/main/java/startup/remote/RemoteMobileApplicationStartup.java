package ${package}.startup.remote;

import org.jspresso.framework.application.startup.remote.RemoteStartup;

/**
 * Remote mobile application startup class.
 *
 * @author Vincent Vandenschrick
 */
public class RemoteMobileApplicationStartup extends RemoteStartup {

  /**
   * Returns the "${rootArtifactId}-remote-mobile-context" value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected String getApplicationContextKey() {
    return "${rootArtifactId}-remote-mobile-context";
  }

  /**
   * Returns "${packageInPathFormat}/beanRefFactory.xml".
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected String getBeanFactorySelector() {
    return "${packageInPathFormat}/beanRefFactory.xml";
  }
}
