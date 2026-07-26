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
        choice(
            name: 'ENVIRONMENT',
            choices: [
                'certification',
                'development',
                'production'
            ],
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
            choices: [
                'chrome',
                'safari'
            ],
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
        cron('34 11 * * *')

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
                    if (params.BROWSER == 'safari' && params.HEADLESS) {
                        error(
                            'Safari no dispone de un modo headless compatible. ' +
                            'Desmarca el parámetro HEADLESS para ejecutar en Safari.'
                        )
                    }

                    withEnv([
                        "TEST_ENVIRONMENT=${params.ENVIRONMENT}",
                        "TEST_TAGS=${params.TAGS}"
                    ]) {
                        if (params.BROWSER == 'safari') {

                            echo 'Ejecutando pruebas en Safari visible'

                            sh '''
                                mvn -B -ntp clean verify \
                                  -Dcucumber.filter.tags="$TEST_TAGS" \
                                  -Denvironment="$TEST_ENVIRONMENT" \
                                  -Dwebdriver.driver=safari
                            '''

                        } else {

                            def chromeSwitches = params.HEADLESS
                                ? '--headless=new;--window-size=1920,1080'
                                : '--window-size=1920,1080'

                            withEnv([
                                "CHROME_SWITCHES=${chromeSwitches}"
                            ]) {
                                echo "Ejecutando pruebas en Chrome. Headless: ${params.HEADLESS}"

                                sh '''
                                    mvn -B -ntp clean verify \
                                      -Dcucumber.filter.tags="$TEST_TAGS" \
                                      -Denvironment="$TEST_ENVIRONMENT" \
                                      -Dwebdriver.driver=chrome \
                                      -Dchrome.switches="$CHROME_SWITCHES"
                                '''
                            }
                        }
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

        success {
                emailext(
                    to: "${env.NOTIFICATION_EMAIL}",
                    subject: "✅ Ejecución exitosa: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    mimeType: 'text/html',
                    body: """
                        <h2>Las pruebas terminaron correctamente</h2>

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

        failure {
            script {

                def motivoError = sh(
                    script: '''
                        set +e

                        REPORTES="target/failsafe-reports/*.txt"

                        # 1. Buscar primero el mensaje contextual creado por la automatización.
                        MOTIVO=$(grep -h -m 1 \
                          '^java\\.lang\\.AssertionError:' \
                          $REPORTES 2>/dev/null \
                          | head -n 1 \
                          | sed -E \
                            's/^java\\.lang\\.AssertionError:[[:space:]]*//')

                        # 2. Si no existe AssertionError, buscar la causa técnica más profunda.
                        if [ -z "$MOTIVO" ]; then
                            MOTIVO=$(grep -h \
                              '^[[:space:]]*Caused by:' \
                              $REPORTES 2>/dev/null \
                              | tail -n 1 \
                              | sed -E \
                                's/^[[:space:]]*Caused by:[[:space:]]*[^:]+:[[:space:]]*//')
                        fi

                        # 3. Si tampoco existe, buscar cualquier excepción Java.
                        if [ -z "$MOTIVO" ]; then
                            MOTIVO=$(grep -h -m 1 \
                              '^java\\..*(Exception|Error):' \
                              $REPORTES 2>/dev/null \
                              | head -n 1 \
                              | sed -E \
                                's/^java\\.[^:]+:[[:space:]]*//')
                        fi

                        printf '%s' "$MOTIVO"
                    ''',
                    returnStdout: true,
                    encoding: 'UTF-8'
                ).trim()

                if (!motivoError) {
                    motivoError =
                        'No se encontró un motivo específico. Revisa el Console Output.'
                }

                def motivoErrorHtml = motivoError
                        .replace('&', '&amp;')
                        .replace('<', '&lt;')
                        .replace('>', '&gt;')

                emailext(
                    to: "${env.NOTIFICATION_EMAIL}",
                    subject: "❌ Falló ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    mimeType: 'text/html',
                    body: """
                        <h2 style="color: #c62828;">
                            La ejecución de pruebas falló
                        </h2>

                        <p>
                            <strong>Trabajo:</strong>
                            ${env.JOB_NAME}
                        </p>

                        <p>
                            <strong>Ejecución:</strong>
                            #${env.BUILD_NUMBER}
                        </p>

                        <p>
                            <strong>Resultado:</strong>
                            ${currentBuild.currentResult}
                        </p>

                        <h3>Motivo del error</h3>

                        <div style="
                            background-color: #fff3f3;
                            border-left: 5px solid #c62828;
                            border-radius: 4px;
                            padding: 15px;
                            font-family: monospace;
                        ">
                            ${motivoErrorHtml}
                        </div>

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