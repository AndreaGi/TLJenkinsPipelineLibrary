def call(String ServerIP) {
    sh"ssh -o StrictHostKeyChecking=no jenkins@${ServerIP} sudo docker rmi -f $(docker images -q)"
}
