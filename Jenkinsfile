pipeline {

    agent any

  //  options {
    //    disableConcurrentBuilds()
  //  }

  //  triggers {
    //    cron('H/5 * * * *')
   // }

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

        stage('Run Tests') {

            matrix {

                axes {

                    axis {
                        name 'BROWSER'
                        values 'chrome', 'firefox'
                    }
                }

                stages {

                    stage('Checkout') {

                        steps {

                            ws("${env.WORKSPACE}@${BROWSER}") {

                                checkout scm

                            }
                        }
                    }

                    stage('Execute Tests') {

                        steps {

                            ws("${env.WORKSPACE}@${BROWSER}") {

                                bat """
                                    echo ==============================
                                    echo Browser=%BROWSER%
                                    echo Environment=${params.ENV}
                                    echo Workspace=%CD%
                                    echo ==============================

                                    mvn clean test ^
                                    -Dbrowser=%BROWSER% ^
                                    -Denv=${params.ENV} ^
                                    -Dsurefire.suiteXmlFiles=src/test/resources/${params.SUITE}.xml
                                """
                            }
                        }
                    }

                    stage('Collect Results') {

                        steps {

                            ws("${env.WORKSPACE}@${BROWSER}") {

                                stash(
                                        name: "results-${BROWSER}",
                                        includes: """
                                        target/surefire-reports/**,
                                        Reports/**,
                                        allure-results/**
                                    """,
                                        allowEmpty: true
                                )
                            }
                        }
                    }
                }
            }
        }

        stage('Collect All Browser Results') {

            steps {

                script {

                    ['chrome', 'firefox'].each { browser ->

                        dir("results/${browser}") {

                            unstash "results-${browser}"
                        }
                    }
                }
            }
        }

        stage('Publish Reports') {

            steps {

                script {

                    echo "Publishing test results..."

                    junit(
                            testResults: 'results/**/target/surefire-reports/*.xml',
                            allowEmptyResults: true
                    )

                    archiveArtifacts(
                            artifacts: 'results/**/*',
                            fingerprint: true,
                            allowEmptyArchive: true
                    )
                }
            }
        }

        stage('Generate Allure Report') {

            steps {

                script {

                    bat """
                        if exist allure-combined rmdir /S /Q allure-combined
                        mkdir allure-combined
                    """

                    ['chrome', 'firefox'].each { browser ->

                        bat """
                            if exist results\\${browser}\\allure-results (
                                xcopy /E /I /Y results\\${browser}\\allure-results allure-combined
                            )
                        """
                    }

                    allure([
                            includeProperties: false,
                            jdk: '',
                            results: [[path: 'allure-combined']]
                    ])
                }
            }
        }
    }

    post {

        success {

            echo 'Build Successful'
        }

        failure {

            echo 'Build Failed'
        }

        unstable {

            echo 'Build Unstable - Check test results or reports'
        }
    }
}