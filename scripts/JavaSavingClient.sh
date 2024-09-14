#! /usr/bin/bash
dir=$(cygpath -w $(dirname "$0"))
java -jar $dir/SaveClient.jar $@
