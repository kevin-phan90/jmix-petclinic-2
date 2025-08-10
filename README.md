![](src/main/resources/META-INF/resources/images/petclinic_logo_with_slogan.svg)

Jmix Petclinic is an example application built with Jmix framework. It is based on the commonly known [Spring Petclinic](https://github.com/spring-projects/spring-petclinic) example.

## Online Demo

The Jmix Petclinic application is available online at https://demo.jmix.io/petclinic

## Application Overview

Jmix Petclinic provides the following functionality:

- Managing Pet Visits through a Calendar
- Tracking Visit Treatments for Nurses
- Creating Pets and Owners
- Managing Nurses and Veterinarians of the Petclinic

## Domain Model

![](etc/domain-model.png)

## Build Docker
- Build application with Gradle
```shell
./gradlew -Pvaadin.productionMode=true --no-build-cache --no-daemon  bootJar
```
- Build docker image
```shell
docker build -t petclinic-2 .
```
- Add Docker registry
```shell
gcloud auth configure-docker us-central1-docker.pkg.dev
```
- Tag image
```shell
docker tag petclinic-2:latest us-central1-docker.pkg.dev/rndlab/rnd/petclinic-2:latest
```
- Push image
```shell
docker push us-central1-docker.pkg.dev/rndlab/rnd/petclinic-2:latest
```