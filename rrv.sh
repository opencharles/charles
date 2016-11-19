#!/bin/bash
# Rultor release versioning script for Maven projects.
#
# It looks for the project’s version, which MUST respect the pattern 
# [0-9]*\.[0-9]*\.[0-9]*-SNAPSHOT and BE THE FIRST MATCH in pom.xml
#
# What it does: depending on ${tag} variable provided by rultor, it
# increments one of the digits and commits the change, as follows:
#   Suppose the current version in pom.xml is 1.2.3-SNAPSHOT
#
#     1) if tag == ‘bugfixes’ (or any string besides 'minor' and 'major'), then:
#         released version will be 1.2.3
#         next version will be 1.2.4-SNAPSHOT
#
#     2) tag == ‘minor’, then:
#         released version will be 1.3.0
#         next version will be 1.3.1-SNAPSHOT
#
#     3) tag == ‘major’, then:
#         released version will be 2.0.0
#         next version will be 2.0.1-SNAPSHOT

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
