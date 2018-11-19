pipeline {
    agent any
    triggers {
        pollSCM('* * * * *')
    }
    stages {
        stage("Compile") {
            steps {
                sh "./mvnw compile"
            }
        }
        stage("Unit test") {
            steps {
                sh "./mvnw test"
            }
        }
        stage("Code coverage") {
            steps {
                sh "./mvnw jacoco:report"
                publishHTML (target: [
                    reportDir: 'target/site/jacoco',
                    reportFiles: 'index.html',
                    reportName: 'JaCoCo Report'
                ])
                sh "./mvnw jacoco:check"
            }
        }
        stage("Static code analysis") {
            steps {
                sh "./mvnw checkstyle:checkstyle"
                publishHTML (target: [
                    reportDir: 'target/site',
                    reportFiles: 'checkstyle.html',
                    reportName: 'Checkstyle Report'
                ])
                sh "./mvnw checkstyle:check"
            }
        }
        stage("Package") {
            steps {
                sh "./mvnw package"
            }
        }
        stage("Acceptance test") {
            steps {
                sh "docker-compose -f docker-compose.yml -f acceptance/docker-compose-acceptance.yml build test"
                sh "docker-compose -f docker-compose.yml -f acceptance/docker-compose-acceptance.yml -p acceptance up -d"
                sh 'test $(docker wait acceptance_test_1) -eq 0'
            }
        }
    }
    post {
        always {
            sh "docker-compose -f docker-compose.yml -f acceptance/docker-compose-acceptance.yml -p acceptance down"
        }
    }
}