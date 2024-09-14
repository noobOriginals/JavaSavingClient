#! /usr/bin/bash
jar_path="$(cygpath -a -d "$(dirname "$0")/SaveClient.jar")"
java -jar $jar_path $@
