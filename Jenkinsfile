pipeline {

    agent any

    options {
        timestamps()
        skipDefaultCheckout(true)
        disableConcurrentBuilds()
    }

    environment {

        // Selenium Grid 4 URL
        GRID_URL = 'http://localhost:4444'

        EXECUTION = 'grid'

        MAVEN_OPTS = '-Xmx2048m'
    }

    stages {

        // ============================================================
        // 1. ENVIRONMENT CHECK
        // ============================================================

        stage('Environment Check') {

            steps {

                echo "=============================================="
                echo "        ENVIRONMENT CHECK"
                echo "=============================================="

                bat '''
                    echo Jenkins Workspace:
                    echo %WORKSPACE%
                    echo.

                    echo Java Version:
                    java -version
                    echo.

                    echo Maven Version:
                    mvn -version
                    echo.

                    echo Docker Version:
                    docker --version
                    echo.

                    echo Selenium Grid URL:
                    echo %GRID_URL%
                    echo.

                    echo Selenium Grid Status:
                    curl -s http://localhost:4444/status
                    echo.
                '''
            }
        }


        // ============================================================
        // 2. CHECKOUT
        // ============================================================

        stage('Checkout') {

            steps {

                deleteDir()

                checkout scm

                echo "=============================================="
                echo "        SOURCE CODE CHECKOUT COMPLETE"
                echo "=============================================="

                bat '''
                    echo Current directory:
                    cd

                    echo.

                    echo Project files:
                    dir
                '''
            }
        }


        // ============================================================
        // 3. RUN ALL BROWSERS IN PARALLEL
        // ============================================================

        stage('Run Tests - All Browsers') {

            parallel {

                // ====================================================
                // CHROME
                // ====================================================

                stage('Chrome Tests') {

                    steps {

                        script {

                            catchError(
                                    buildResult: 'FAILURE',
                                    stageResult: 'FAILURE'
                            ) {

                                dir('chrome-workspace') {

                                    deleteDir()

                                    checkout scm

                                    echo "=============================================="
                                    echo "        CHROME TEST EXECUTION"
                                    echo "=============================================="

                                    bat '''
                                        echo Browser   : chrome
                                        echo Grid URL  : %GRID_URL%
                                        echo Workspace : %CD%
                                        echo.

                                        if exist allure-results (
                                            rmdir /S /Q allure-results
                                        )

                                        mkdir allure-results

                                        echo.
                                        echo Running Chrome tests...
                                        echo.

                                        mvn clean test ^
                                                        -Dbrowser=chrome ^
                                                        -Dexecution=grid ^
                                                        -DgridUrl=%GRID_URL% ^
                                                        -Dallure.results.directory=allure-results
                                                        '''
                                      }
                               }


                            // =================================================
                            // STASH CHROME RESULTS
                            // =================================================

                            dir('chrome-workspace') {

                                echo "=============================================="
                                echo "        CHROME RESULT FILES"
                                echo "=============================================="

                                bat '''
                                    echo.
                                    echo Surefire reports:

                                    if exist target\\surefire-reports (
                                        dir target\\surefire-reports
                                    ) else (
                                        echo No Surefire reports found
                                    )

                                    echo.
                                    echo Allure results:

                                    if exist allure-results (
                                        dir allure-results
                                    ) else (
                                        echo No Allure results found
                                    )
                                '''

                                stash(
                                        name: 'results-chrome',
                                        includes: 'target/surefire-reports/**,allure-results/**',
                                        useDefaultExcludes: false,
                                        allowEmpty: false
                                )

                                echo "Chrome results stashed."
                            }
                        }
                    }
                }


                // ====================================================
                // FIREFOX
                // ====================================================

                stage('Firefox Tests') {

                    steps {

                        script {

                            catchError(
                                    buildResult: 'FAILURE',
                                    stageResult: 'FAILURE'
                            ) {

                                dir('firefox-workspace') {

                                    deleteDir()

                                    checkout scm

                                    echo "=============================================="
                                    echo "        FIREFOX TEST EXECUTION"
                                    echo "=============================================="

                                    bat '''
                                        echo Browser   : firefox
                                        echo Grid URL  : %GRID_URL%
                                        echo Workspace : %CD%
                                        echo.

                                        if exist allure-results (
                                            rmdir /S /Q allure-results
                                        )

                                        mkdir allure-results

                                        echo.
                                        echo Running Firefox tests...
                                        echo.

                                        mvn clean test ^
                                                     -Dbrowser=firefox ^
                                                     -Dexecution=grid ^
                                                     -DgridUrl=%GRID_URL% ^
                                                     -Dallure.results.directory=allure-results
                                            '''
                                        }
                                }


                            // =================================================
                            // STASH FIREFOX RESULTS
                            // =================================================

                            dir('firefox-workspace') {

                                echo "=============================================="
                                echo "        FIREFOX RESULT FILES"
                                echo "=============================================="

                                bat '''
                                    echo.
                                    echo Surefire reports:

                                    if exist target\\surefire-reports (
                                        dir target\\surefire-reports
                                    ) else (
                                        echo No Surefire reports found
                                    )

                                    echo.
                                    echo Allure results:

                                    if exist allure-results (
                                        dir allure-results
                                    ) else (
                                        echo No Allure results found
                                    )
                                '''

                                stash(
                                        name: 'results-firefox',
                                        includes: 'target/surefire-reports/**,allure-results/**',
                                        useDefaultExcludes: false,
                                        allowEmpty: false
                                )

                                echo "Firefox results stashed successfully."
                            }
                        }
                    }
                }


                // ====================================================
                // EDGE
                // ====================================================

                stage('Edge Tests') {

                    steps {

                        script {

                            catchError(
                                    buildResult: 'FAILURE',
                                    stageResult: 'FAILURE'
                            ) {

                                dir('edge-workspace') {

                                    deleteDir()

                                    checkout scm

                                    echo "=============================================="
                                    echo "        EDGE TEST EXECUTION"
                                    echo "=============================================="

                                    bat '''
                                        echo Browser   : edge
                                        echo Grid URL  : %GRID_URL%
                                        echo Workspace : %CD%
                                        echo.

                                        if exist allure-results (
                                            rmdir /S /Q allure-results
                                        )

                                        mkdir allure-results

                                        echo.
                                        echo Running Edge tests...
                                        echo.

                                        mvn clean test ^
                                                    -Dbrowser=edge ^
                                                    -Dexecution=grid ^
                                                    -DgridUrl=%GRID_URL% ^
                                                    -Dallure.results.directory=allure-results
                                              '''
                                        }
                                }


                            // =================================================
                            // STASH EDGE RESULTS
                            // =================================================

                            dir('edge-workspace') {

                                echo "=============================================="
                                echo "        EDGE RESULT FILES"
                                echo "=============================================="

                                bat '''
                                    echo.
                                    echo Surefire reports:

                                    if exist target\\surefire-reports (
                                        dir target\\surefire-reports
                                    ) else (
                                        echo No Surefire reports found
                                    )

                                    echo.
                                    echo Allure results:

                                    if exist allure-results (
                                        dir allure-results
                                    ) else (
                                        echo No Allure results found
                                    )
                                '''

                                stash(
                                        name: 'results-edge',
                                        includes: 'target/surefire-reports/**,allure-results/**',
                                        useDefaultExcludes: false,
                                        allowEmpty: false
                                )

                                echo "Edge results stashed successfully."
                            }
                        }
                    }
                }
            }
        }


        // ============================================================
        // 4. COLLECT RESULTS
        // ============================================================

        stage('Collect All Browser Results') {

            steps {

                script {

                    echo "=============================================="
                    echo "        COLLECTING ALL RESULTS"
                    echo "=============================================="


                    // Chrome
                    dir('results/chrome') {

                        deleteDir()

                        echo "Collecting Chrome results..."

                        unstash 'results-chrome'
                    }


                    // Firefox
                    dir('results/firefox') {

                        deleteDir()

                        echo "Collecting Firefox results..."

                        unstash 'results-firefox'
                    }


                    // Edge
                    dir('results/edge') {

                        deleteDir()

                        echo "Collecting Edge results..."

                        unstash 'results-edge'
                    }


                    echo "=============================================="
                    echo "        RESULTS COLLECTION COMPLETE"
                    echo "=============================================="
                }
            }
        }


        // ============================================================
        // 5. VERIFY RESULTS
        // ============================================================

        stage('Verify Collected Results') {

            steps {

                echo "=============================================="
                echo "        VERIFYING RESULTS"
                echo "=============================================="

                bat '''
                    echo.
                    echo ================= CHROME =================

                    if exist results\\chrome\\allure-results (
                        echo Chrome Allure results FOUND
                        dir results\\chrome\\allure-results
                    ) else (
                        echo Chrome Allure results NOT FOUND
                    )


                    echo.
                    echo ================= FIREFOX =================

                    if exist results\\firefox\\allure-results (
                        echo Firefox Allure results FOUND
                        dir results\\firefox\\allure-results
                    ) else (
                        echo Firefox Allure results NOT FOUND
                    )


                    echo.
                    echo ================= EDGE =================

                    if exist results\\edge\\allure-results (
                        echo Edge Allure results FOUND
                        dir results\\edge\\allure-results
                    ) else (
                        echo Edge Allure results NOT FOUND
                    )


                    echo.
                    echo ================= SUREFIRE =================

                    if exist results\\chrome\\target\\surefire-reports (
                        echo Chrome Surefire reports FOUND
                    )

                    if exist results\\firefox\\target\\surefire-reports (
                        echo Firefox Surefire reports FOUND
                    )

                    if exist results\\edge\\target\\surefire-reports (
                        echo Edge Surefire reports FOUND
                    )
                '''
            }
        }


        // ============================================================
        // 6. PUBLISH JUNIT REPORTS
        // ============================================================

        stage('Publish JUnit Reports') {

            steps {

                echo "=============================================="
                echo "        PUBLISHING JUNIT REPORTS"
                echo "=============================================="

                junit(
                        testResults: 'results/**/target/surefire-reports/*.xml',
                        allowEmptyResults: true,
                        skipPublishingChecks: true
                )
            }
        }


        // ============================================================
        // 7. PREPARE COMBINED ALLURE RESULTS
        // ============================================================

        stage('Prepare Allure Results') {

            steps {

                echo "=============================================="
                echo "        PREPARING ALLURE RESULTS"
                echo "=============================================="

                bat '''
                    if exist combined-allure-results (
                        rmdir /S /Q combined-allure-results
                    )

                    mkdir combined-allure-results


                    echo.
                    echo ================= CHROME =================

                    if exist results\\chrome\\allure-results (
                        xcopy /E /I /Y ^
                            results\\chrome\\allure-results\\* ^
                            combined-allure-results\\
                    ) else (
                        echo Chrome Allure results NOT FOUND
                    )


                    echo.
                    echo ================= FIREFOX =================

                    if exist results\\firefox\\allure-results (
                        xcopy /E /I /Y ^
                            results\\firefox\\allure-results\\* ^
                            combined-allure-results\\
                    ) else (
                        echo Firefox Allure results NOT FOUND
                    )


                    echo.
                    echo ================= EDGE =================

                    if exist results\\edge\\allure-results (
                        xcopy /E /I /Y ^
                            results\\edge\\allure-results\\* ^
                            combined-allure-results\\
                    ) else (
                        echo Edge Allure results NOT FOUND
                    )


                    echo.
                    echo ================= COMBINED RESULTS =================

                    dir combined-allure-results
                '''
            }
        }


        // ============================================================
        // 8. GENERATE ALLURE REPORT
        // ============================================================

        stage('Generate Allure Report') {

            steps {

                echo "=============================================="
                echo "        GENERATING ALLURE REPORT"
                echo "=============================================="

                bat '''
                    echo.
                    echo Checking Allure result files...

                    if exist combined-allure-results\\*-result.json (
                        echo Allure result JSON files FOUND.
                    ) else (
                        echo ERROR: Allure result JSON files NOT FOUND.
                    )

                    echo.

                    dir combined-allure-results
                '''


                allure(
                        includeProperties: false,
                        jdk: '',
                        results: [
                                [
                                        path: 'combined-allure-results'
                                ]
                        ]
                )

                echo "Allure report generated."
            }
        }
    }


    // ================================================================
    // POST ACTIONS
    // ================================================================

    post {

        always {

            echo "=============================================="
            echo "        PIPELINE COMPLETED"
            echo "=============================================="

            bat '''
                echo.
                echo Jenkins Workspace:
                echo %WORKSPACE%

                echo.

                echo Result directories:

                if exist results (
                    dir results
                ) else (
                    echo No results directory found
                )

                echo.

                echo Combined Allure Results:

                if exist combined-allure-results (
                    dir combined-allure-results
                ) else (
                    echo No combined Allure directory found
                )
            '''
        }


        success {

            echo "=============================================="
            echo "        BUILD SUCCESS"
            echo "=============================================="

            echo "All browser tests completed successfully."
        }


        failure {

            echo "=============================================="
            echo "        BUILD FAILED"
            echo "=============================================="

            echo "One or more browser test executions failed."
            echo "Check JUnit and Allure reports."
        }


        unstable {

            echo "=============================================="
            echo "        BUILD UNSTABLE"
            echo "=============================================="

            echo "One or more tests may have warnings or failures."
        }
    }
}

