pipeline {
    agent  any
    stages {
        stage('error') {
            steps {
                myerror([timeout: 1])
            }
        }
    }
}