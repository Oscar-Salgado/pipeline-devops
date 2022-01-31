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
	string description: 'Ingresar nombre de stage a ejecutar, sí deja el input vacío se ejecutarán todos los stage.', name: 'etapa'
	}

    stages {
		stage('Pipeline') {
			steps{
				script{
				    try {
						println 'Pipeline'
						
						println params.buildTool
						//echo "$env.BUILD_USER" 
						//echo "$env.BUILD_USER_ID" 
						
						if (params.buildTool=="gradle") {
								println 'Ejecutar Gradle'
								//def ejecucion = load 'gradle.groovy'
								//ejecucion.call()
								figlet params.etapa
								gradle(etapa)
						} else {
							println 'Ejecutar Maven'
							//def ejecucion = load 'maven.groovy'
							//ejecucion.call()
							maven(params.etapa)
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

return this;
