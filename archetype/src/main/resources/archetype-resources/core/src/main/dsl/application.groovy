import org.jspresso.contrib.sjs.domain.Domain;
import org.jspresso.contrib.sjs.front.Front;

def domainBuilder = new Domain()
domainBuilder.Project('${parentArtifactId}', mute:true) {
  namespace('${package}') {
    Domain {
      include(project.properties['srcDir']+'/src/main/dsl/model.groovy')
      // Implement your domain here using the SJS DSL.
    }
  }
}
if(!domainBuilder.isOK()) {
  println domainBuilder.getErrorDomain()
  fail('SJS defined domain is invalid.')
}

def frontendBuilder = new Front(domainBuilder.getReferenceDomain())
frontendBuilder.Front(){
  namespace('${package}'){
    include(project.properties['srcDir']+'/src/main/dsl/view.groovy')
    include(project.properties['srcDir']+'/src/main/dsl/frontend.groovy')
  }
}
if(frontendBuilder.getNbrError() != 0) {
  println frontendBuilder.getError()
  fail('SJS defined frontend / views is invalid.')
}

domainBuilder.writeDomainFile(project.properties['outputDir'],project.properties['modelOutputFileName'])
frontendBuilder.writeViewFile(project.properties['outputDir'],project.properties['viewOutputFileName'])
frontendBuilder.writeFrontEndFile(project.properties['outputDir'],project.properties['frontOutputFileName'])
