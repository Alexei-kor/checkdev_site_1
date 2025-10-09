pipeline {
    agent any
    stages {
        stage('compile') {
            steps {
                bat 'mvn.cmd compile'
            }
        }
         stage('test') {
            steps {
                bat 'mvn.cmd test'
            }
        }
        stage('package') {
            steps {
                bat 'mvn.cmd package'
            }
        }
    }
    post {
        success {
            echo 'The build was successful!'
        }
        failure {
            echo 'The build failed.'
        }
    }
}