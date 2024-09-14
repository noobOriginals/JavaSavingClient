#! /usr/bin/bash
dir=$(dirname "$0")
cd $dir
javac -d bin/build source/*.java
cd bin/build
jar cvfm ../SaveClient.jar ../../templates/Manifest.txt *
cd ..
mkdir release-bundle
cp SaveClient.jar release-bundle
cp ../scripts/JavaSavingClient.sh release-bundle
cp ../templates/SavePaths.txt release-bundle
cd $dir
