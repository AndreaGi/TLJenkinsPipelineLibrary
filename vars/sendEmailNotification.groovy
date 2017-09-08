def call(currentBuild, recipients) {
    def previousBuild = currentBuild.getPreviousBuild()
    if( previousBuild && (previousBuild.result=="FAILURE" && currentBuild.result == "SUCCESS") ||
            (previousBuild.result == "SUCCESS" && currentBuild.result == "FAILURE") ){
        step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: recipients, sendToIndividuals: true])
    }
}