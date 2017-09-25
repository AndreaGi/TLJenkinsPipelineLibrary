def call(String ServerIP) {
    sh"ssh -o StrictHostKeyChecking=no jenkins@${ServerIP} sudo yes | docker image prune -a"
}