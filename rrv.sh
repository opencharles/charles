#!/bin/bash
# Rultor release versioning script for Maven projects.
#
# It looks for the project’s version, which MUST respect the pattern 
# [0-9]*\.[0-9]*\.[0-9]*-SNAPSHOT and BE THE FIRST MATCH in pom.xml
#
# What it does: updates the pom.xml version of the project according to
# the variable ${tag} provided to rultor. Specifically, it increments the 
# 3rd digit and adds '-SNAPSHOT' to it.
#
# IMPORTANT:
#     the given tag has to contain 3 numbers separated by dots!
#     
#     e.g. tag = 1.0.1 or tag = 3.2.53 will result in new versions of 1.0.2-SNAPSHOT
#     or 3.2.54-SNAPSHOT

CURRENT_VERSION=$(grep -o '[0-9]*\.[0-9]*\.[0-9]*-SNAPSHOT' -m 1 pom.xml)

NUMBERS=($(echo $tag | grep -o -E '[0-9]+'))

echo "CURRENT VERSION IS"
echo $CURRENT_VERSION

NEXT_VERSION=${NUMBERS[0]}'.'${NUMBERS[1]}'.'$((${NUMBERS[2]}+1))'-SNAPSHOT'

echo "RELEASE VERSION IS"
echo $tag

echo "NEXT VERSION IS"
echo $NEXT_VERSION

sed -i "s/${CURRENT_VERSION}/${NEXT_VERSION}/" pom.xml

git commit -am "${tag}"
git checkout master
git merge __rultor
git checkout __rultor
