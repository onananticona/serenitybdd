pipeline {
    agent any

    options {
        // No permite dos ejecuciones simultáneas del mismo trabajo.
        disableConcurrentBuilds()

        // Conserva las últimas 20 ejecuciones y los artefactos de las últimas 10.
        buildDiscarder(
            logRotator(
                numToKeepStr: '20',
                artifactNumToKeepStr: '10'
            )
        )

        // Detiene la ejecución si supera una hora.
        timeout(time: 60, unit: 'MINUTES')

        // Haremos el checkout explícitamente.
        skipDefaultCheckout(true)
    }

    parameters {
        string(
            name: 'ENVIRONMENT',
            defaultValue: 'certification',
            trim: true,
            description: 'Ambiente definido en serenity.conf'
        )

        string(
            name: 'TAGS',
            defaultValue: '@login',
            trim: true,
            description: 'Etiqueta o expresión Cucumber. Ejemplo: @login'
        )

        choice(
            name: 'BROWSER',
            choices: ['chrome'],
            description: 'Navegador de ejecución'
        )

        booleanParam(
            name: 'HEADLESS',
            defaultValue: true,
            description: 'Ejecutar Chrome sin mostrar la ventana'
        )
    }

    triggers {
        // Regresión automática de lunes a viernes durante la hora de las 2 a. m.
        cron('H 2 * * 1-5')

        // Revisa GitHub cada 15 minutos y ejecuta si encuentra cambios.
        pollSCM('H/15 * * * *')
    }

    environment {
        JAVA_HOME = '/Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home'

        // Permite encontrar Java, Maven, Git y las herramientas de Homebrew.
        PATH = "${JAVA_HOME}/bin:/opt/homebrew/bin:${env.PATH}"
    }

    stages {
        stage('Descargar código') {
            steps {
                // Limpia archivos de una ejecución anterior.
                deleteDir()

                // Descarga la rama configurada en el trabajo.
                checkout scm
            }
        }

        stage('Verificar entorno') {
            steps {
                sh '''
                    echo "========================================"
                    echo "Java utilizado"
                    echo "========================================"
                    java -version

                    echo "========================================"
                    echo "Maven utilizado"
                    echo "========================================"
                    mvn -version

                    echo "========================================"
                    echo "Git utilizado"
                    echo "========================================"
                    git --version
                '''
            }
        }

        stage('Ejecutar pruebas') {
            steps {
                script {
                    def chromeSwitches = params.HEADLESS
                        ? '--headless=new;--window-size=1920,1080'
                        : '--window-size=1920,1080'

                    withEnv([
                        "TEST_ENVIRONMENT=${params.ENVIRONMENT}",
                        "TEST_TAGS=${params.TAGS}",
                        "TEST_BROWSER=${params.BROWSER}",
                        "CHROME_SWITCHES=${chromeSwitches}"
                    ]) {
                        sh '''
                            mvn -B -ntp clean verify \
                              -Dcucumber.filter.tags="$TEST_TAGS" \
                              -Denvironment="$TEST_ENVIRONMENT" \
                              -Dwebdriver.driver="$TEST_BROWSER" \
                              -Dchrome.switches="$CHROME_SWITCHES"
                        '''
                    }
                }
            }
        }
    }

    post {
        always {
            junit(
                allowEmptyResults: true,
                testResults: '**/target/surefire-reports/*.xml,**/target/failsafe-reports/*.xml'
            )

            publishHTML(
                target: [
                    allowMissing: true,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'target/site/serenity',
                    reportFiles: 'index.html',
                    reportName: 'E2E Report',
                    reportTitles: 'Serenity BDD'
                ]
            )

            archiveArtifacts(
                allowEmptyArchive: true,
                artifacts: 'target/site/serenity/**',
                fingerprint: true
            )
        }

        failure {
            emailext(
                to: "${env.NOTIFICATION_EMAIL}",
                subject: "❌ Falló ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                mimeType: 'text/html',
                body: """
                    <h2>La ejecución de pruebas falló</h2>

                    <p><strong>Trabajo:</strong> ${env.JOB_NAME}</p>
                    <p><strong>Ejecución:</strong> #${env.BUILD_NUMBER}</p>
                    <p><strong>Resultado:</strong> ${currentBuild.currentResult}</p>

                    <p>
                        <a href="${env.BUILD_URL}">
                            Abrir ejecución en Jenkins
                        </a>
                    </p>

                    <p>
                        <a href="${env.BUILD_URL}console">
                            Revisar Console Output
                        </a>
                    </p>
                """
            )
        }

        unstable {
            emailext(
                to: "${env.NOTIFICATION_EMAIL}",
                subject: "⚠️ Ejecución inestable: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                mimeType: 'text/html',
                body: """
                    <h2>La ejecución terminó con pruebas inestables</h2>

                    <p><strong>Trabajo:</strong> ${env.JOB_NAME}</p>
                    <p><strong>Ejecución:</strong> #${env.BUILD_NUMBER}</p>
                    <p><strong>Resultado:</strong> ${currentBuild.currentResult}</p>

                    <p>
                        <a href="${env.BUILD_URL}">
                            Revisar resultados y reporte
                        </a>
                    </p>
                """
            )
        }

        fixed {
            emailext(
                to: "${env.NOTIFICATION_EMAIL}",
                subject: "✅ Se recuperó ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                mimeType: 'text/html',
                body: """
                    <h2>El Pipeline volvió a funcionar correctamente</h2>

                    <p><strong>Trabajo:</strong> ${env.JOB_NAME}</p>
                    <p><strong>Ejecución:</strong> #${env.BUILD_NUMBER}</p>
                    <p><strong>Resultado:</strong> ${currentBuild.currentResult}</p>

                    <p>
                        <a href="${env.BUILD_URL}">
                            Abrir ejecución en Jenkins
                        </a>
                    </p>
                """
            )
        }

        cleanup {
            deleteDir()
        }
    }
}