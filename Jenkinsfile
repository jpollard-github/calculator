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
        stage("Docker build") {
            steps {
                sh "docker build -t jpollard91/calculator ."
            }
        }
        stage("Docker push") {
            steps {
                sh "docker login -u jpollard91 -p m0nk3y91"
                sh "docker push jpollard91/calculator"
            }
        }
        stage("Deploy to staging") {
            steps {
              sh "docker run -d -p 8765:8080 --name calculator jpollard91/calculator"
            }
        }
        stage("Acceptance test") {
            steps {
                sleep 60
                sh "bash acceptance_test.sh"
            }
        }
    }
    post {
        always {
            sh "docker stop calculator"
        }
    }
}