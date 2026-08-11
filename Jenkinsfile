pipeline {

    agent any

    options {
        disableConcurrentBuilds()
    }

    triggers {
        cron('H/5 * * * *')
    }

    parameters {

        choice(
                name: 'ENV',
                choices: ['qa', 'uat', 'prod'],
                description: 'Select Environment'
        )

        choice(
                name: 'SUITE',
                choices: ['smoke', 'regression'],
                description: 'Select Test Suite'
        )
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Run Tests') {

            matrix {

                axes {

                    axis {
                        name 'BROWSER'
                        values 'chrome', 'firefox', 'edge'
                    }
                }

                stages {

                    stage('Execute Tests') {

                        steps {

                            bat """
                            echo Browser=%BROWSER%
                            echo Environment=${params.ENV}

                            mvn clean test ^
                            -Dbrowser=%BROWSER% ^
                            -Denv=${params.ENV} ^
                            -Dsurefire.suiteXmlFiles=src/test/resources/${params.SUITE}.xml
                            """
                        }
                    }
                }
            }
        }
    }

    post {

        always {

            publishHTML(target: [
                    reportDir: 'Reports',
                    reportFiles: 'AutomationReport.html',
                    reportName: 'Extent Report',
                    keepAll: true,
                    alwaysLinkToLastBuild: true,
                    allowMissing: true
            ])

            archiveArtifacts(
                    artifacts: 'Reports/**/*',
                    fingerprint: true,
                    allowEmptyArchive: true
            )

            junit(
                    testResults: 'target/surefire-reports/*.xml',
                    allowEmptyResults: true
            )

            allure([
                    includeProperties: false,
                    jdk: '',
                    results: [[path: 'allure-results']]
            ])
        }

        success {
            echo 'Build Successful'
        }

        failure {
            echo 'Build Failed'
        }

        unstable {
            echo 'Build Unstable'
        }
    }
}