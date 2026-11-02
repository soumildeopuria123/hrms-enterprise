#!/bin/sh
#
# Maven2 Start Script for UN*X
#

# resolve links: $0 may be a link
PRG="$0"
# need this for daisy chained symlinks to work
while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
    else
    PRG=`dirname "$PRG"`"/$link"
    fi
done

# Get standard environment variables
PRG_DIR=`dirname "$PRG"`

# Use the maximum available, or set MAX_FORK_DAEMON if desired
MAX_FORK_DAEMON="${MAX_FORK_DAEMON:-512}"

# Build the classpath for the Maven wrapper
WRAPPER_JAR="$PRG_DIR/.mvn/wrapper/maven-wrapper.jar"

# Execute Maven
if [ -r "$WRAPPER_JAR" ] ; then
    exec "$JAVACMD" "$@" -classpath "$WRAPPER_JAR" org.apache.maven.wrapper.MavenWrapperMain "$@"
else
    echo "Cannot find maven-wrapper.jar" >&2
    exit 1
fi
