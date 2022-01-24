pipeline {
    agent any 
    stages {
        stage('Build') { 
            steps {
                sh 'mvn -f pom.xml -DoutputDirectory=~/ranking -B -DskipTests clean package'
                
            }
        }
        stage('Deploy') { 
            steps {
				// sh 'PM2_HOME=\'/home/ubuntu/.pm2\' pm2 delete ranking'
            	dir('/home/ubuntu/ranking-microsservico') {
            		echo 'Destruindo instancia atual'
            		sh './destroyRankingInstance.sh'
            		echo 'Instancia destruida com sucesso'
            		
            		echo 'Deletando .jar antigo'
            		sh './removeRankingJar.sh'
            		echo 'Jar deletado com sucesso'
            		
            		echo 'Copiando .jar novo para pasta definitiva'
            		sh 'cp /var/lib/jenkins/workspace/ranking/target/ranking-microsservico*.jar /home/ubuntu/ranking-microsservico/ranking.jar'
            		echo 'Jar copiado com sucesso'
            		
            		echo 'Subindo instancia nova'
					sh './runRanking.sh'
					echo 'Instancia nova executada com sucesso'
				}
            }
        }
    }
    // post {
	//	success {
	//		echo 'Esteira finalizada com sucesso!'
     //   }
	//	failure {
	//		echo 'Esteira falhou...'
     //   }
    // }
}
