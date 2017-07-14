#!/usr/bin/env groovy
node("aws-ecs-small") {

    stage('Checkout') {
        checkout scm
        def COMMIT_HASH = sh script: "git rev-parse HEAD", returnStdout: true
        def VERSION = sh script: "git describe --tags --always", returnStdout: true
        currentBuild.displayName = "#$BUILD_NUMBER - $VERSION"
    }

    stage('Build') {
        sh "./gradlew -qs jar"
        stash name: "$JOB_NAME-$BUILD_NUMBER", useDefaultExcludes: false
    }
}

timeout(time: 1, unit: 'DAYS') {    
    stage('Promotion'){
        input message: 'Publish to artifactory?'
    }
}

node("aws-ecs-small") {
    stage('Publish') {
        unstash "$JOB_NAME-$BUILD_NUMBER"
        sshagent(['jenkins-master-phabricator-pushable']) {
            withCredentials([usernamePassword(credentialsId: 'artifactory-deployer', passwordVariable: 'ORG_GRADLE_PROJECT_maven_password', usernameVariable: 'ORG_GRADLE_PROJECT_maven_user')]) {
                sh "mkdir ~/.ssh && ssh-keyscan git.traveloka.com >> ~/.ssh/known_hosts"
                sh "./gradlew -qs final -Pmaven_url=https://artifactory.noc.tvlk.cloud/artifactory/libs-release-local -Prelease.scope=$RELEASE_SCOPE"
            }
        }
        def VERSION = sh script: "git describe --tags --always", returnStdout: true
        currentBuild.description = "This is published"
        currentBuild.displayName = "#$BUILD_NUMBER - $VERSION"
    }
}
