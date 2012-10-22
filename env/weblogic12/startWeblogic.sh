#!/bin/sh

# WARNING: This file is created by the Configuration Wizard.
# Any changes to this script may be lost when adding extensions to this configuration.

DOMAIN_HOME="/app/oracle/user_projects/domains/archiveeDomain"

EXT_PRE_CLASSPATH="/app/archivee/log4j.properties"

export EXT_PRE_CLASSPATH

export debugFlag="true"

export CLASS_CACHE="true"

export USER_MEM_ARGS="-Xms512m -Xmx1024m -XX:CompileThreshold=8000 -XX:PermSize=256m -XX:MaxPermSize=512m"

${DOMAIN_HOME}/bin/startWebLogic.sh $*