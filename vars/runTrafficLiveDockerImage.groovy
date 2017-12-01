def call(String ServerIP, String CredentialsId, String DockerImage) {
    withCredentials([usernamePassword(credentialsId: "${CredentialsId}", usernameVariable: 'DOCKER_USR', passwordVariable: 'DOCKER_PWD')]) {
        sh "ssh -o StrictHostKeyChecking=no jenkins@${ServerIP} sudo docker login -u $DOCKER_USR -p $DOCKER_PWD"
        sh"ssh -o StrictHostKeyChecking=no jenkins@${ServerIP} sudo docker pull ${DockerImage}"
        // If the container is already stopped/not existing we return true anyway to not let the job fails
        sh"ssh -o StrictHostKeyChecking=no jenkins@${ServerIP} sudo systemctl stop docker-tomcat.service || true"
        sh"ssh -o StrictHostKeyChecking=no jenkins@${ServerIP} sudo docker stop tomcat || true"
        sh"ssh -o StrictHostKeyChecking=no jenkins@${ServerIP} sudo docker rm tomcat || true"
        sh"ssh -o StrictHostKeyChecking=no jenkins@${ServerIP} sudo docker create --rm --env-file /docker/tomcat/conf/tomcat.conf \\ " +
                "-v /logs:/logs \\ " +
                "-p 8080:8080/tcp -p 22222:22222/tcp -p 22223:22223/tcp \\ " +
                "--name=tomcat ${DockerImage}"
        sh"ssh -o StrictHostKeyChecking=no jenkins@${ServerIP} sudo docker cp /docker/tomcat/conf/. tomcat:/usr/local/tomcat/conf/"
        sh"ssh -o StrictHostKeyChecking=no jenkins@${ServerIP} sudo docker cp /docker/tomcat/lib/. tomcat:/usr/local/tomcat/lib/"
        sh"ssh -o StrictHostKeyChecking=no jenkins@${ServerIP} sudo docker start tomcat"
    }
}