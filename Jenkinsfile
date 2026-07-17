pipeline {

    agent any

    parameters {

        choice(
            name: 'BROWSER',
            choices: ['chrome', 'firefox', 'edge'],
            description: 'Select Browser'
        )
         choice(
                name: 'ENV',
                choices: ['qa', 'uat', 'prod'],
                description: 'Select Environment'
            )
        choice(
                name: 'SUITE',
                choices: ['smoke','regression'],
                description: 'Select Test Suite'
        )

    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean compile'
            }
        }

        stage('Run Tests') {
            steps {
                bat """
                         mvn test ^
                        -Dbrowser=${params.BROWSER} ^
                        -Denv=${params.ENV} ^
                        -DsuiteXmlFile=src/test/resources/${params.SUITE}.xml
                 """
            }
        }

        stage('Publish Report') {
            steps {
                publishHTML(target: [
                    reportDir: 'Reports',
                    reportFiles: 'AutomationReport.html',
                    reportName: 'Extent Report',
                    keepAll: true,
                    alwaysLinkToLastBuild: true,
                    allowMissing: false
                ])
            }
        }
    }

    post {

        always {

            archiveArtifacts artifacts: 'Reports/**/*',
                    fingerprint: true

            junit 'target/surefire-reports/*.xml'
        }

        success {
            echo 'Build Successful'
        }

        failure {
            echo 'Build Failed'
        }
    }

}