def call(String ServerIP) {
    sh"ssh -o StrictHostKeyChecking=no jenkins@${ServerIP} yes | sudo docker image prune -a"
}