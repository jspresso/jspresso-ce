package ${package}.startup.ulc.development;

import ${package}.development.TestDataPersister;
import ${package}.startup.ulc.UlcApplicationStartup;

/**
 * ULC development application startup class.
 */
public class UlcDevApplicationStartup extends UlcApplicationStartup {

  /**
   * Sets up some test data before actually starting.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void start() {
    new TestDataPersister(getApplicationContext()).persistTestData();
    super.start();
  }
}
