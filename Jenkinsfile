node{
   stage('SCM Checkout'){
     git 'https://github.com/atul0401/eclipse-projects'
   }
   stage('Compile-Package'){
      // Get maven home path
      def mvnHome =  tool name: 'maven', type: 'maven'   
      sh "${mvnHome}/bin/mvn -f /var/lib/jenkins/workspace/apitesting/ready-api-testserver-samples/pom.xml package"
   }
   
   stage('SonarQube Analysis') {
        def mvnHome =  tool name: 'maven', type: 'maven'
        withSonarQubeEnv('sonar') { 
          sh "${mvnHome}/bin/mvn -f /var/lib/jenkins/workspace/apitesting/ready-api-testserver-samples/pom.xml  sonar:sonar"
        }
    }
    }
