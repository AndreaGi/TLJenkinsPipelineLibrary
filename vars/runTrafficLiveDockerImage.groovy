def call(String ServerIP, String CredentialsId, String DockerImage, String ImageTag) {
    withCredentials([usernamePassword(credentialsId: "${CredentialsId}", usernameVariable: 'DOCKER_USR', passwordVariable: 'DOCKER_PWD')]) {
        sh "ssh -o StrictHostKeyChecking=no jenkins@${ServerIP} sudo docker login -u $DOCKER_USR -p $DOCKER_PWD"
        sh"ssh -o StrictHostKeyChecking=no jenkins@${ServerIP} sudo docker pull ${DockerImage}"
        sh"ssh -o StrictHostKeyChecking=no jenkins@${ServerIP} sudo \"sed -i 's|FROM.*|FROM andreadeltek/trafficlive:${ImageTag}|g' /docker/tomcat/Dockerfile \" "
        // If the container is already stopped/not existing we return true anyway to not let the job fails
        sh"ssh -o StrictHostKeyChecking=no jenkins@${ServerIP} sudo systemctl stop docker-tomcat.service || true"
        sh"ssh -o StrictHostKeyChecking=no jenkins@${ServerIP} sudo docker stop tomcat || true"
        sh"ssh -o StrictHostKeyChecking=no jenkins@${ServerIP} sudo docker rm tomcat || true"
        sh"ssh -o StrictHostKeyChecking=no jenkins@${ServerIP} sudo docker build -t trafficlive/tomcat:${ImageTag} /docker/tomcat"
        sh"ssh -o StrictHostKeyChecking=no jenkins@${ServerIP} sudo \"sed -i 's|trafficlive/tomcat:.*|trafficlive/tomcat:${ImageTag}|g' /etc/systemd/system/docker-tomcat.service \" "
        sh"ssh -o StrictHostKeyChecking=no jenkins@${ServerIP} sudo systemctl daemon-reload"
        sh"ssh -o StrictHostKeyChecking=no jenkins@${ServerIP} sudo systemctl start docker-tomcat.service "
    }
}