/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/

def call(String pipelineType){

		figlet pipelineType
  
        stage('Compile') {
             bat "./mvnw.cmd clean compile -e"

        }
        stage('SonarQube') {
			def scannerHome = tool 'sonar-scanner';
			withSonarQubeEnv('sonarqube-server') { 
			bat "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=Sonarqube-jenkins -Dsonar.projectBaseDir=c:/repo/ejemplo-maven/ -Dsonar.sources=src -Dsonar.java.binaries=build" 
					}                    
        }
		
        stage('Test') {
            bat "./mvnw.cmd clean test -e"
        }
        stage('Jar') {
            bat "./mvnw.cmd clean package -e"
        }
        stage('uploadNexus') {
			nexusPublisher nexusInstanceId: 'nexus_server_jesus', nexusRepositoryId: 'test-repo', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: 'C:\\repo\\ejemplo-maven\\build\\DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]
        }
		
        stage('Run') {
						bat "start /min mvnw.cmd spring-boot:run &"
						sleep 30
        }

}

return this;