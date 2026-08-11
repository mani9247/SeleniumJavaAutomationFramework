pipeline {

    agent any

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

                                cleanWs()

                                checkout scm

                                echo "===================================="
                                echo "Browser Workspace"
                                echo "Browser : ${BROWSER}"
                                echo "Workspace : ${env.WORKSPACE}@${BROWSER}"
                                echo "===================================="
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

                                echo "Collecting results for ${BROWSER}"

                                bat """
                                    echo ==============================
                                    echo Surefire Reports
                                    echo ==============================

                                    if exist target\\surefire-reports (
                                        dir target\\surefire-reports
                                    ) else (
                                        echo No Surefire report directory found
                                    )

                                    echo ==============================
                                    echo Extent Reports
                                    echo ==============================

                                    if exist Reports (
                                        dir Reports
                                    ) else (
                                        echo No Reports directory found
                                    )

                                    echo ==============================
                                    echo Allure Results
                                    echo ==============================

                                    if exist allure-results (
                                        dir allure-results
                                    ) else (
                                        echo No allure-results directory found
                                    )
                                """

                                stash(
                                        name: "results-${BROWSER}",
                                        includes: '''
                                        target/surefire-reports/**
                                        Reports/**
                                        allure-results/**
                                    ''',
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

                        echo "===================================="
                        echo "Collecting ${browser} results"
                        echo "===================================="

                        dir("results/${browser}") {

                            deleteDir()

                            unstash "results-${browser}"
                        }
                    }
                }
            }
        }

        stage('Verify Collected Results') {

            steps {

                bat """
                    echo ==========================================
                    echo COLLECTED RESULTS
                    echo ==========================================

                    echo.
                    echo CHROME:
                    if exist results\\chrome (
                        dir results\\chrome /s
                    ) else (
                        echo Chrome results NOT FOUND
                    )

                    echo.
                    echo FIREFOX:
                    if exist results\\firefox (
                        dir results\\firefox /s
                    ) else (
                        echo Firefox results NOT FOUND
                    )
                """
            }
        }

        stage('Publish Reports') {

            steps {

                script {

                    echo "Publishing test results..."

                    echo "=========================================="
                    echo "JUnit XML FILES"
                    echo "=========================================="

                    bat """
                        echo Chrome JUnit files:
                        dir results\\chrome\\target\\surefire-reports\\TEST-*.xml 2>nul

                        echo.
                        echo Firefox JUnit files:
                        dir results\\firefox\\target\\surefire-reports\\TEST-*.xml 2>nul
                    """

                    junit(
                            testResults: 'results/**/target/surefire-reports/TEST-*.xml',
                            allowEmptyResults: false
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
                                echo Copying ${browser} Allure results...

                                xcopy /E /I /Y ^
                                results\\${browser}\\allure-results ^
                                allure-combined
                            ) else (
                                echo No Allure results for ${browser}
                            )
                        """
                    }

                    echo "=========================================="
                    echo "Combined Allure Results"
                    echo "=========================================="

                    bat """
                        dir allure-combined
                    """

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