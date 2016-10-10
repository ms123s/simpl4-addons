#########################################################
# usage
#########################################################
usage() {
    echo "usage: $0"
}

usage() {
  echo >&2

  echo >&2 "usage: put-into-repo -n name"

  echo >&2
  echo >&2 'options:'
  echo >&2 "  -a : artifactId"
  echo >&2 "  -g : groupId"
  echo >&2 "  -f : file"
  echo >&2 "  -v : version"
  echo >&2
}

#########################################################
# global
#########################################################
FILE=
ARTIFACTID=
GROUPID=
VERSION=
#########################################################
# main
#########################################################
shortoptions='a:g:f:v:'
longoptions='artifactId:,groupId:,file:,version:'
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
      -f|--file)
				shift
				FILE=$1
				shift
			;;
      -a|--artifactId)
				shift
				ARTIFACTID=$1
				shift
			;;
      -g|--groupId)
				shift
				GROUPID=$1
				shift
			;;
      -v|--version)
				shift
				VERSION=$1
				shift
			;;
      *)
				break
      ;;
   esac
done

if [ -z "$FILE" -o -z "$ARTIFACTID" -o -z "$GROUPID" -o -z "$VERSION"] ; then
	usage;
	exit 1
fi

mvn install:install-file -Dfile=$FILE -DgroupId=$GROUPID  -DartifactId=$ARTIFACTID -Dversion=$VERSION -Dpackaging=jar -DlocalRepositoryPath=./localrepo
