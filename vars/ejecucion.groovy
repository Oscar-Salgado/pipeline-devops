/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/

def call(){
  
  pipeline {
    agent any
	
	environment {
	    STAGE = ''
	}
	
	parameters {
	choice choices: ['gradle', 'maven'], description: 'indicar herramienta de construccion', name: 'buildTool'
	}

    stages {
		stage('Pipeline') {
			steps{
				script{
				    try {
						println 'Pipeline'
						
						
						//def ci_or_cd = verifyBranchName()
					
						if (params.buildTool=="gradle") {
								println 'Ejecutar Gradle'
								gradle(verifyBranchName())
						} else {
							println 'Ejecutar Maven'
							maven(verifyBranchName())
						}
						slackSend color: 'good', message: "Información de Ejecución: \n [${env.BUILD_USER}][${env.JOB_NAME}][${params.buildTool}] Ejecución Exitosa!"
					
					} catch (Exception e){
						slackSend color: 'danger', message: "[${env.BUILD_USER}][${env.JOB_NAME}][${params.buildTool}] Ejecución fallida en stage: ${STAGE}"
					    error "Ejecución fallida en stage ${STAGE}"					
					}
				}
			}			
		}
	}
}

}

def verifyBranchName(){
	if (env.GIT_BRANCH.contains('feature-') || env.GIT_BRANCH.contains('develop')){
		return 'CI'
		
	} else {
		return 'CD'
	}
}
return this;












