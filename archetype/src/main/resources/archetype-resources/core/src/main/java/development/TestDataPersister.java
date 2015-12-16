package ${package}.development;

#if(${persistence_hibernate_or_mongo}=="hibernate")
import org.jspresso.framework.application.startup.development.AbstractHibernateTestDataPersister;
#elseif(${persistence_hibernate_or_mongo}=="mongo")
import org.jspresso.framework.application.startup.development.AbstractMongoTestDataPersister;
#end
import org.springframework.beans.factory.BeanFactory;

/**
 * Persists some test data for the application.
 */
#if(${persistence_hibernate_or_mongo}=="hibernate")
public class TestDataPersister extends AbstractHibernateTestDataPersister {
#elseif(${persistence_hibernate_or_mongo}=="mongo")
public class TestDataPersister extends AbstractMongoTestDataPersister {
#end

  /**
   * Constructs a new <code>TestDataPersister</code> instance.
   * 
   * @param beanFactory
   *            the spring bean factory to use.
   */
  public TestDataPersister(BeanFactory beanFactory) {
    super(beanFactory);
  }

  /**
   * Creates some test data using the passed in Spring application context.
   */
  @Override
  public void createAndPersistTestData() {
    
    // Create some entities...
    // MyEntity myEntity = createEntityInstance(MyEntity.class);
    
    // ...and save them.
    //saveOrUpdate(myEntity);
  }
}
