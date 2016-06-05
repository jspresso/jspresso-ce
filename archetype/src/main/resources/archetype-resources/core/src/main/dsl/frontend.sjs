// Implement your application frontend here using the SJS DSL.

/*
 * The workspaces and modules
 */

// Describe your workspaces and modules here.


bean 'viewFactoryBase', parent:'abstractViewFactory',
    custom: [
      defaultActionMapRenderingOptions:'LABEL_ICON'
    ]

controller '${parentArtifactId}.name',
    icon:'icon.png',
    context:'${parentArtifactId}',
    language:'en',
    workspaces:[
      /*Reference your workspaces here.*/
    ]
