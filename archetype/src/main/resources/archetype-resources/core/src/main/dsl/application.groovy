import org.jspresso.contrib.sjs.domain.Domain;
import org.jspresso.contrib.sjs.front.Front;
import org.jspresso.contrib.sjs.common.ManageModule;

def domainBuilder = new Domain()

domainBuilder.Domain(projectName:'${parentArtifactId}', mute:true, includeDirectory:project.properties['srcDir']) {
  namespace('${package}') {
    include('model.groovy')
  }
}
if(!domainBuilder.isOK()) {
  println domainBuilder.getErrorDomain()
  fail('SJS defined domain is invalid.\n' + domainBuilder.getErrorDomain())
}

def frontendBuilder = new Front(domainBuilder.getReferenceDomain())
frontendBuilder.Front(){
  namespace('${package}'){
    view {
      include('view.groovy')
      spec('mobile') {
        include('view-mobile.groovy')
      }
    }
    frontend {
      include('frontend.groovy')
      spec('mobile') {
        include('frontend-mobile.groovy')
      }
    }
    backend {
      include('backend.groovy')
      spec('mobile') {
        include('backend-mobile.groovy')
      }
    }
  }
}
if(frontendBuilder.getNbrError() != 0) {
  println frontendBuilder.getError()
  fail('SJS defined frontend / views is invalid.\n' + frontendBuilder.getError())
}

domainBuilder.writeDomainFile(project.properties['outputDir'],project.properties['modelOutputFileName'])
frontendBuilder.writeOutputFile('backend',project.properties['outputDir'],project.properties['backOutputFileName'])
frontendBuilder.writeOutputFile('backend/mobile',project.properties['outputDir'],
    'mobile-'+project.properties['backOutputFileName'])
frontendBuilder.writeOutputFile('view',project.properties['outputDir'],project.properties['viewOutputFileName'])
frontendBuilder.writeOutputFile('view/mobile',project.properties['outputDir'],
    'mobile-'+project.properties['viewOutputFileName'])
frontendBuilder.writeOutputFile('frontend',project.properties['outputDir'],project.properties['frontOutputFileName'])
frontendBuilder.writeOutputFile('frontend/mobile',project.properties['outputDir'],
    'mobile-'+project.properties['frontOutputFileName'])

//Export as module
ManageModule manageModule = new ManageModule()
manageModule.exportModule('${parentArtifactId}',domainBuilder,frontendBuilder,project.properties['outputDir'])

