def call(String buildStatus = 'SUCCESS', String duration) {
    // build status of null means successful
    buildStatus =  buildStatus ?: 'SUCCESS'

    // Default values
    def colorCode = '#d00000'
    def subject = "${env.JOB_NAME} - # ${env.BUILD_NUMBER} ${buildStatus} after ${duration}"
    def summary = "${subject} (${env.BUILD_URL})"

    // Override default values based on build status
    if (buildStatus == 'SUCCESS') {
        color = 'GREEN'
        colorCode = '#36a64f'
    } else {
        color = 'RED'
        colorCode = '#d00000'
    }

    // Send notifications
    slackSend (color: colorCode, message: summary)
}