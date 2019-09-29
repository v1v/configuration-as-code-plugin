pipeline {
    agent  any
    stages {
        stage('error') {
            steps {
                log1 level: "INFO", text: "foo"
            }
        }
    }
}