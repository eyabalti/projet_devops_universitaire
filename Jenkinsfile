pipeline {
  agent any
  environment { IMAGE_NAME = 'gestion-universitaire' }
  stages {
    stage('Checkout') { steps { checkout scm } }
    stage('Tests') {
      steps {
        sh 'mvn -B clean test'
      }
    }
    stage('Build image') {
      steps {
        sh 'docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} .'
      }
    }
    stage('Deploy (lab)') {
      when { branch 'main' }
      steps {
        sh 'docker compose up -d --build'
      }
    }
  }
  post {
    success { echo 'Pipeline terminé : build/tests/deploiement OK' }
    failure { echo 'Echec pipeline : consulter les logs de la stage en erreur' }
  }
}
