#!/bin/bash

#AFTER importing project into eclipse with the buildship plugin run this script

./gradlew eclipseJdtApt
./gradlew eclipseFactorypath
./gradlew eclipseJdt
