pipeline {

    agent any

    options {
        timestamps()
        skipDefaultCheckout(true)
        disableConcurrentBuilds()
    }

    environment {
        GRID_URL = 'http://localhost:4444/wd/hub'
        EXECUTION = 'grid'
        MAVEN_OPTS = '-Xmx2048m'
    }

    stages {

        // ============================================================
        // 1. CHECK ENVIRONMENT
        // ============================================================
        stage('Environment Check') {
            steps {
                script {

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

                        echo Checking Selenium Grid:
                        curl -s http://localhost:4444/status
                        echo.
                    '''
                }
            }
        }


        // ============================================================
        // 2. CHECKOUT CODE
        // ============================================================
        stage('Checkout') {
            steps {
                script {

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
        }


        // ============================================================
        // 3. RUN TESTS IN PARALLEL
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
                                        echo Browser      : chrome
                                        echo Execution    : %EXECUTION%
                                        echo Grid URL     : %GRID_URL%
                                        echo Workspace    : %CD%
                                        echo.

                                        mvn clean test ^
                                            -Dbrowser=chrome ^
                                            -Dexecution=grid ^
                                            -DgridUrl=%GRID_URL%
                                    '''
                                }
                            }

                            // ----------------------------------------
                            // STASH CHROME RESULTS
                            // Always attempt this even if Maven fails
                            // ----------------------------------------
                            dir('chrome-workspace') {

                                echo "=============================================="
                                echo "        COLLECTING CHROME RESULTS"
                                echo "=============================================="

                                stash(
                                        name: 'results-chrome',
                                        includes: '''
                                        target/surefire-reports/**
                                        allure-results/**
                                    ''',
                                        allowEmpty: true
                                )

                                echo "Chrome results stashed successfully."
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
                                        echo Browser      : firefox
                                        echo Execution    : %EXECUTION%
                                        echo Grid URL     : %GRID_URL%
                                        echo Workspace    : %CD%
                                        echo.

                                        mvn clean test ^
                                            -Dbrowser=firefox ^
                                            -Dexecution=grid ^
                                            -DgridUrl=%GRID_URL%
                                    '''
                                }
                            }

                            // ----------------------------------------
                            // STASH FIREFOX RESULTS
                            // ----------------------------------------
                            dir('firefox-workspace') {

                                echo "=============================================="
                                echo "        COLLECTING FIREFOX RESULTS"
                                echo "=============================================="

                                stash(
                                        name: 'results-firefox',
                                        includes: '''
                                        target/surefire-reports/**
                                        allure-results/**
                                    ''',
                                        allowEmpty: true
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
                                        echo Browser      : edge
                                        echo Execution    : %EXECUTION%
                                        echo Grid URL     : %GRID_URL%
                                        echo Workspace    : %CD%
                                        echo.

                                        mvn clean test ^
                                            -Dbrowser=edge ^
                                            -Dexecution=grid ^
                                            -DgridUrl=%GRID_URL%
                                    '''
                                }
                            }

                            // ----------------------------------------
                            // STASH EDGE RESULTS
                            // ----------------------------------------
                            dir('edge-workspace') {

                                echo "=============================================="
                                echo "        COLLECTING EDGE RESULTS"
                                echo "=============================================="

                                stash(
                                        name: 'results-edge',
                                        includes: '''
                                        target/surefire-reports/**
                                        allure-results/**
                                    ''',
                                        allowEmpty: true
                                )

                                echo "Edge results stashed successfully."
                            }
                        }
                    }
                }
            }
        }


        // ============================================================
        // 4. COLLECT ALL BROWSER RESULTS
        // ============================================================
        stage('Collect All Browser Results') {

            steps {

                script {

                    echo "=============================================="
                    echo "        COLLECTING ALL BROWSER RESULTS"
                    echo "=============================================="

                    // --------------------------------------------
                    // CHROME
                    // --------------------------------------------
                    dir('results/chrome') {

                        deleteDir()

                        echo "Collecting Chrome results..."

                        unstash 'results-chrome'
                    }


                    // --------------------------------------------
                    // FIREFOX
                    // --------------------------------------------
                    dir('results/firefox') {

                        deleteDir()

                        echo "Collecting Firefox results..."

                        unstash 'results-firefox'
                    }


                    // --------------------------------------------
                    // EDGE
                    // --------------------------------------------
                    dir('results/edge') {

                        deleteDir()

                        echo "Collecting Edge results..."

                        unstash 'results-edge'
                    }


                    echo "=============================================="
                    echo "        ALL RESULTS COLLECTED"
                    echo "=============================================="
                }
            }
        }


        // ============================================================
        // 5. VERIFY RESULTS
        // ============================================================
        stage('Verify Collected Results') {

            steps {

                script {

                    echo "=============================================="
                    echo "        VERIFYING COLLECTED RESULTS"
                    echo "=============================================="

                    bat '''
                        echo.
                        echo ================= CHROME =================
                        if exist results\\chrome\\target\\surefire-reports (
                            dir results\\chrome\\target\\surefire-reports
                        ) else (
                            echo Chrome JUnit reports not found
                        )

                        echo.
                        echo ================= FIREFOX =================
                        if exist results\\firefox\\target\\surefire-reports (
                            dir results\\firefox\\target\\surefire-reports
                        ) else (
                            echo Firefox JUnit reports not found
                        )

                        echo.
                        echo ================= EDGE =================
                        if exist results\\edge\\target\\surefire-reports (
                            dir results\\edge\\target\\surefire-reports
                        ) else (
                            echo Edge JUnit reports not found
                        )

                        echo.
                        echo ================= ALLURE =================

                        if exist results\\chrome\\allure-results (
                            echo Chrome Allure results found
                        ) else (
                            echo Chrome Allure results NOT found
                        )

                        if exist results\\firefox\\allure-results (
                            echo Firefox Allure results found
                        ) else (
                            echo Firefox Allure results NOT found
                        )

                        if exist results\\edge\\allure-results (
                            echo Edge Allure results found
                        ) else (
                            echo Edge Allure results NOT found
                        )
                    '''
                }
            }
        }


        // ============================================================
        // 6. PUBLISH JUNIT / TESTNG RESULTS
        // ============================================================
        stage('Publish Reports') {

            steps {

                script {

                    echo "=============================================="
                    echo "        PUBLISHING TEST REPORTS"
                    echo "=============================================="

                    junit(
                            testResults: 'results/**/target/surefire-reports/junitreports/*.xml',
                            allowEmptyResults: true,
                            skipPublishingChecks: true
                    )

                    echo "JUnit reports published."
                }
            }
        }


        // ============================================================
        // 7. CREATE COMBINED ALLURE RESULTS
        // ============================================================
        stage('Prepare Allure Results') {

            steps {

                script {

                    echo "=============================================="
                    echo "        PREPARING ALLURE RESULTS"
                    echo "=============================================="

                    bat '''
                        if exist combined-allure-results (
                            rmdir /S /Q combined-allure-results
                        )

                        mkdir combined-allure-results

                        echo.
                        echo Copying Chrome Allure results...

                        if exist results\\chrome\\allure-results (
                            xcopy /E /I /Y ^
                                results\\chrome\\allure-results\\* ^
                                combined-allure-results\\
                        )

                        echo.
                        echo Copying Firefox Allure results...

                        if exist results\\firefox\\allure-results (
                            xcopy /E /I /Y ^
                                results\\firefox\\allure-results\\* ^
                                combined-allure-results\\
                        )

                        echo.
                        echo Copying Edge Allure results...

                        if exist results\\edge\\allure-results (
                            xcopy /E /I /Y ^
                                results\\edge\\allure-results\\* ^
                                combined-allure-results\\
                        )

                        echo.
                        echo Combined Allure result files:

                        dir combined-allure-results
                    '''
                }
            }
        }


        // ============================================================
        // 8. GENERATE ALLURE REPORT
        // ============================================================
        stage('Generate Allure Report') {

            steps {

                script {

                    echo "=============================================="
                    echo "        GENERATING ALLURE REPORT"
                    echo "=============================================="

                    bat '''
                        if exist combined-allure-results\\*.json (
                            echo Allure result files found.
                        ) else (
                            echo No Allure result JSON files found.
                        )
                    '''

                    // Jenkins Allure plugin
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
                echo Jenkins workspace:
                echo %WORKSPACE%
                echo.

                echo Final result directories:
                if exist results (
                    dir results
                ) else (
                    echo No results directory found
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
            echo "Check the JUnit and Allure reports."
        }


        unstable {

            echo "=============================================="
            echo "        BUILD UNSTABLE"
            echo "=============================================="

            echo "One or more tests may have warnings or failures."
        }
    }
}