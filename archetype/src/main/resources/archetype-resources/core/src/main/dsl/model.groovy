import org.jspresso.contrib.sjs.domain.Domain;

def domainBuilder = new Domain()
domainBuilder.Project('${parentArtifactId}',
  namespace:'${package}'){
    Domain{
      // Implement your domain here using the SJS DSL.
    }
  }
domainBuilder.writeDomainFile(project.properties['outputDir'],project.properties['outputFileName'])