#!/bin/bash

set -e
set -o pipefail
trap 'echo ERROR' ERR

WAR="VerifiedNsfwBrowser"

cd "$(dirname "$0")"
z="$(pwd)"

#automatic version bump
version="$(date +%Y%m%d)"
GWTXML="./src/main/resources/muksihs/steem/postbrowser/VerifiedNsfwBrowser.gwt.xml"
sed -i "s# name=\"version\" value=\"........\"/># name=\"version\" value=\"$version\"/>#" "$GWTXML"
git add "$GWTXML" || true
git commit -m "autocommit version on build" "$GWTXML" || true

#build
gradle clean
gradle build
cd build/libs
#unpack the war
rm -rf "$WAR"
unzip "$WAR".war -d "$WAR"
#remove servlet stuff
rm -rf "$WAR"/META-INF
rm -rf "$WAR"/WEB-INF

#publish
rsync -arzv --human-readable --progress --delete-after "$WAR/" "muksihs@muksihs.com:/var/www/html/$WAR/"

xdg-open "http://muksihs.com/$WAR/"

echo "DONE."

