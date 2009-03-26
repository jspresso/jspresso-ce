package ${package}.startup.remote;

import org.jspresso.framework.application.startup.remote.RemoteStartup;

/**
 * Remote application startup class.
 */
public class RemoteApplicationStartup extends RemoteStartup {

  /**
   * Returns the "${rootArtifactId}-remote-context" value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected String getApplicationContextKey() {
    return "${rootArtifactId}-remote-context";
  }
}
