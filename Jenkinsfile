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
      stage('Environment Check') {

        steps {

            bat """
            echo ==========================================
            echo JENKINS ENVIRONMENT CHECK
            echo ==========================================

            echo USERNAME:
            whoami

            echo COMPUTER:
            hostname

            echo JAVA:
            java -version

            echo MAVEN:
            mvn -version

            echo DOCKER:
            docker version

            echo DOCKER CONTAINERS:
            docker ps

            echo TEMP:
            echo %TEMP%

            echo TMP:
            echo %TMP%

            echo WORKSPACE:
            echo %WORKSPACE%

            echo ==========================================
        """
        }
    }
        stage('Run Tests') {

            matrix {

                axes {

                    axis {
                        name 'BROWSER'
                        values 'firefox'
                    }
                }

                stages {

                    stage('Checkout') {

                        steps {

                            ws("${env.WORKSPACE}@${BROWSER}"){

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

                                bat """
                                    echo ==========================================
                                    echo COLLECTING RESULTS FOR %BROWSER%
                                    echo ==========================================

                                    echo Current workspace:
                                    echo %CD%

                                    echo.
                                    echo Checking JUnit reports...

                                    if exist target\\surefire-reports (
                                        dir target\\surefire-reports
                                    ) else (
                                        echo No target\\surefire-reports directory found
                                    )

                                    echo.
                                    echo Checking Allure results...

                                    if exist allure-results (
                                        dir allure-results
                                    ) else (
                                        echo No allure-results directory found
                                    )
                                """

                                stash(
                                        name: "results-${BROWSER}",
                                        includes: "target/surefire-reports/**/*,allure-results/**/*,Reports/**/*",
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

                    ['chrome', 'firefox', 'edge'].each { browser ->

                        echo "===================================="
                        echo "Collecting ${browser} results"
                        echo "===================================="

                        dir("results/${browser}") {

                            deleteDir()

                            unstash "results-${browser}"

                            bat """
                                echo.
                                echo ===== ${browser} RESULTS =====
                                dir /s
                            """
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
                    
                    echo.
                    echo EDGE:
                    if exist results\\\\edge (
                         dir results\\\\edge /s
                    ) else (
                       echo Edge results NOT FOUND
                    )
                """
            }
        }

        stage('Publish Reports') {

            steps {

                script {

                    echo "Publishing test results..."

                    bat """
                echo ==========================================
                echo JUNIT XML FILES
                echo ==========================================

                dir results\\chrome\\target\\surefire-reports
                dir results\\firefox\\target\\surefire-reports
                dir results\\edge\\target\\surefire-reports

                echo.
                echo ==========================================
                echo SEARCHING FOR FAILURES / ERRORS / SKIPS
                echo ==========================================

                findstr /S /I /C:"failures=" /C:"errors=" /C:"skipped=" results\\*\\target\\surefire-reports\\TEST-*.xml
            """

                    junit(
                            testResults: 'results/**/target/surefire-reports/TEST-*.xml',
                            allowEmptyResults: false
                    )

                    archiveArtifacts(
                            artifacts: 'results/**/*',
                            fingerprint: true,
                            allowEmptyArchive: false
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

                    ['chrome', 'firefox', 'edge'].each { browser ->

                        bat """
                            echo ==========================================
                            echo Copying Allure results from ${browser}
                            echo ==========================================

                            if exist results\\${browser}\\allure-results (
                                xcopy /E /I /Y results\\${browser}\\allure-results allure-combined
                            ) else (
                                echo No Allure results found for ${browser}
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