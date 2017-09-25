// Implement your application backend here using the SJS DSL.

bean('abstractBackController',
    class: 'org.jspresso.framework.application.backend.AbstractBackendController',
    parent: 'abstractBackControllerBase',
    custom: [loginContextName: '${parentArtifactId}']
)
