def call() {
    node{
        withCredentials([usernamePassword(credentialsId: "${config.CredentialsId}", usernameVariable: 'DOCKER_USR', passwordVariable: 'DOCKER_PWD')]) {
            sh "ssh -o StrictHostKeyChecking=no jenkins@${config.ServerIP} sudo docker login -u $DOCKER_USR -p $DOCKER_PWD"
            sh"ssh -o StrictHostKeyChecking=no jenkins@${config.ServerIP} sudo docker pull ${config.DockerImage}"
            sh"ssh -o StrictHostKeyChecking=no jenkins@${config.ServerIP} sudo docker stop tomcat"
            sh"ssh -o StrictHostKeyChecking=no jenkins@${config.ServerIP} sudo docker create --rm --env-file /docker/tomcat/conf/tomcat.conf \
                    -v /logs:/logs \
                    -p 8080:8080/tcp -p 22222:22222/tcp -p 22223:22223/tcp \
                    --name=tomcat ${config.DockerImage}"
            sh"ssh -o StrictHostKeyChecking=no jenkins@${config.ServerIP} sudo docker cp /docker/tomcat/conf/. tomcat:/usr/local/conf/"
            sh"ssh -o StrictHostKeyChecking=no jenkins@${config.ServerIP} sudo docker start tomcat"
        }
    }
}