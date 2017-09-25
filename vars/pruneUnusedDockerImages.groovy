def call(String ServerIP) {
    sh"ssh -o StrictHostKeyChecking=no jenkins@${ServerIP} sudo docker image prune -a -f"
}