#!/bin/sh
#########################################################
# usage
#########################################################
usage() {
    echo "usage: $0"
}

usage() {
  echo >&2

  echo >&2 "usage: genaddon -n name"

  echo >&2
  echo >&2 'options:'
  echo >&2 "  -n : name"
  echo >&2
}

#########################################################
# global
#########################################################
NAME=
#########################################################
# main
#########################################################
shortoptions='n:'
longoptions='name:'
getopt=$(getopt -o $shortoptions --longoptions  $longoptions -- "$@")
if [ $? != 0 ]; then
   usage
   exit 1;
fi

eval set -- "$getopt"
while true; do
   case "$1" in
      -h|--help)
         usage
         exit 1
      ;;
      -n|--name)
				shift
				NAME=$1
				shift
			;;
      *)
				break
      ;;
   esac
done

if [ -z "$NAME" ] ; then
	usage;
	exit 1
fi
mvn archetype:generate \
    -DarchetypeGroupId=org.apache.karaf.archetypes \
    -DarchetypeArtifactId=karaf-feature-archetype \
    -DarchetypeVersion=4.0.0 \
    -DgroupId=org.simpl4.addons \
    -DartifactId=$NAME \
    -Dversion=1.0 \
    -Dpackage=org.simpl4.addons
