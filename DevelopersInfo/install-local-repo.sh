#!/bin/sh
# written by tomas.hubalek@inloop.eu
# more info at http://community.jaspersoft.com/wiki/contributing-jaspersoft-studio-and-building-sources#ContributingtoJaspersoftStudioandbuildingfromsources-PackagingJaspersoftStudiocreatethedistributions

# configure script here ----------------------------------------------------------------------
LOCAL_REPO_PATH="/Users/tom/Documents/workspace-4.4/eclipse_local_repo"
ECLIPSE_PATH="/Applications/Eclipse-4.4"
EXPANDED_ZIP_PATH="/Users/tom/Documents/workspace-4.4/jss382/"

if [ ! -d ${LOCAL_REPO_PATH} ] ; then
	mkdir -p ${LOCAL_REPO_PATH}
fi

# simple check whether it was configured properly --------------------------------------------

if [ ! -e ${EXPANDED_ZIP_PATH}/artifacts.jar ] ; then
	echo "Expanded path ${EXPANDED_ZIP_PATH} should contain file artifacts.jar. Check your config."
	exit 1
fi

# command itself -----------------------------------------------------------------------------
${ECLIPSE_PATH}/eclipse  -debug -consolelog -nosplash -verbose -application org.eclipse.equinox.p2.publisher.FeaturesAndBundlesPublisher -metadataRepository file:${LOCAL_REPO_PATH} -artifactRepository file:${LOCAL_REPO_PATH} -source ${EXPANDED_ZIP_PATH} -compress -append -publishArtifacts

# inform user --------------------------------------------------------------------------------
echo Artifacts installed.
echo Don\'t forget to add <targetplatform.repo>${LOCAL_REPO_PATH}</targetplatform.repo> into your settings.xml
