/*
	forma de invocaciÃ³n de mÃ©todo call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/

def call(String ETAPA){
  
		if(ETAPA=='build'){
			stage('build') {
					//Build & Unit Test
					figlet env.STAGE_NAME
					STAGE = env.STAGE_NAME
					println "Stage: ${env.STAGE_NAME}"
					bat './gradlew clean build'    
			}
		}			
		
		if(ETAPA=='sonar'){
			stage('sonar') {
				figlet env.STAGE_NAME
				STAGE = env.STAGE_NAME
				println "Stage: ${env.STAGE_NAME}"
				def scannerHome = tool 'sonar-scanner';
				withSonarQubeEnv('sonarqube-server') { 
				bat "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=Sonarqube-jenkins -Dsonar.projectBaseDir=c:/repo/ejemplo-gradle/ -Dsonar.sources=src -Dsonar.java.binaries=build" 
				}                    
			}
		}			
		
		if(ETAPA=='run'){
			stage('run') {
				figlet env.STAGE_NAME
				STAGE = env.STAGE_NAME
				println "Stage: ${env.STAGE_NAME}"       
				//bat 'nohup bash gradlew bootRun &'
				bat "start /min gradlew spring-boot:run &"
				println "probar manual en navegador: http://localhost:8081/rest/mscovid/test?msg=testing"
				sleep 20
			}
		}			
		
		if(ETAPA=='testing'){
			stage('testing') {
				figlet env.STAGE_NAME
				STAGE = env.STAGE_NAME
				println "Stage: ${env.STAGE_NAME}"   
				//bat "curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing"
			}
		}			
		
		if(ETAPA=='nexus'){
			stage('nexus') {
				figlet env.STAGE_NAME
				//Código Eliminado para pruebas slack
				STAGE = env.STAGE_NAME
				println "Stage: ${env.STAGE_NAME}"                      
			}
		}			

}

return this;