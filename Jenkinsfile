pipeline {
  agent {
    kubernetes {
      cloud "GCP"
      yamlFile "build-agent.yaml"
    }
  }

  stages {
    stage('Build and push to gcr') {
      steps {
        container("gcloud-builder") {
          sh 'git config --global --add safe.directory /home/jenkins/agent/workspace/petclininc-app'
          script {
            // Generate a unique tag based on the commit hash
            def commitHash = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
            env.dockerTag = "dev-commit-${commitHash}-${BUILD_NUMBER}"
            env.appName = 'petclininc-2'
            env.gkeClusterName = 'gcp-devops-project'
            env.Zone = 'us-central1-a'
            env.gcpProject = 'rndlab'
            env.registryRepo = 'rnd'

            sh "docker build -t ${env.appName}:${env.dockerTag} ."

            withCredentials([file(credentialsId: 'gcr-id', variable: 'SERVICE_ACCOUNT_KEY')]) {
              sh 'gcloud auth activate-service-account --key-file=$SERVICE_ACCOUNT_KEY'
              sh "gcloud container clusters get-credentials ${env.gkeClusterName} --zone ${env.Zone} --project ${env.gcpProject}"
              sh "gcloud auth configure-docker"
            }

            env.gcrImage = "us-central1-docker.pkg.dev/${env.gcpProject}/${env.registryRepo}/${env.appName}:${env.dockerTag}"
            sh "docker tag ${env.appName}:${env.dockerTag} ${env.gcrImage}"
            sh "docker push ${env.gcrImage}"
          }
        }
      }
    }

    stage('Deploy to GKE') {
      steps {
        container("gcloud-builder") {
          script {
            sh "kubectl set image deployment/${env.appName} ${env.appName}=${env.gcrImage} -n development"
          }
        }
      }
    }
  }

  post {
    always {
      // Clean up the agent pod after the job completes
      cleanWs()
      deleteDir()
    }
  }
}