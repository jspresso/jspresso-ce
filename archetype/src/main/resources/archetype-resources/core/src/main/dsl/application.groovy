import org.jspresso.contrib.sjs.domain.Domain;
import org.jspresso.contrib.sjs.front.Front;

def domainBuilder = new Domain()
domainBuilder.Project('${parentArtifactId}', mute:true) {
  namespace('${package}') {
    Domain {
      include('src/main/dsl/model.groovy')
      // Implement your domain here using the SJS DSL.
    }
  }
}
if(!domainBuilder.isOK()) return -1;

def frontendBuilder = new Front(domainBuilder.getReferenceDomain())
frontendBuilder.Front(){
  namespace('${package}'){
    include('src/main/dsl/view.groovy')
    include('src/main/dsl/frontend.groovy')
  }
}
if(frontendBuilder.getNbrError() != 0) {
  return -1;
  println frontendBuilder.getError()
}

domainBuilder.writeDomainFile(project.properties['outputDir'],project.properties['modelOutputFileName'])
frontendBuilder.writeViewFile(project.properties['outputDir'],project.properties['viewOutputFileName'])
frontendBuilder.writeFrontEndFile(project.properties['outputDir'],project.properties['frontOutputFileName'])
