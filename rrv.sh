#!/bin/bash
# Rultor release versioning shell script for Maven projects.
# This script is supposed to be run at the end rultor’s release process.
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
#
#
#

CURRENT_VERSION=$(grep -o '[0-9]*\.[0-9]*\.[0-9]*-SNAPSHOT' -m 1 pom.xml)
NUMBERS=($(echo $CURRENT_VERSION | grep -o -E '[0-9]+'))
tag="bug"

echo "CURRENT VERSION IS"
echo $CURRENT_VERSION

#minor release concerns the second digit
if [ "$tag" == "minor" ]; then
    tag=${NUMBERS[0]}'.'$((${NUMBERS[1]}+1))'.0'
    NEXT_VERSION=${NUMBERS[0]}'.'$((${NUMBERS[1]}+1))'.1-SNAPSHOT'
elif [ "$tag" == "major" ]; then
    tag=$((${NUMBERS[0]}+1))'.0.0'
    NEXT_VERSION=$((${NUMBERS[0]}+1))'.0.1-SNAPSHOT'
else #By default it's a release with bug-fixes (third digit)
    tag=${NUMBERS[0]}'.'${NUMBERS[1]}'.'${NUMBERS[2]}
    NEXT_VERSION=${NUMBERS[0]}'.'${NUMBERS[1]}'.'$((${NUMBERS[2]}+1))'-SNAPSHOT'
fi

echo "RELEASE VERSION IS"
echo $tag


echo "NEXT VERSION IS"
echo $NEXT_VERSION

sed -i "s/${CURRENT_VERSION}/${NEXT_VERSION}/" pom.xml
git commit -am "${tag}"