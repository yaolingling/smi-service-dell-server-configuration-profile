#!/bin/bash
#############################################################
##	nfs_tool.sh
##
##-----------------------------------------------------------
##  Utility to configure the nfs service
##-----------------------------------------------------------
##
##	Copyright (c) 2015 Dell Computer Corporation
##
#############################################################


# This function called from firstBoot.sh
# Creates controller NFS share and directory for remote shares

function createDefaultShare
{
	local -r shareLocal=${LOCAL_SHARE_PATH}/nfs
	
	local defaultShare=${shareLocal}" *(rw,sync,no_subtree_check,no_root_squash,no_all_squash)"

	if [ -e ${shareLocal} ]; then
		echo "Controller file share directory ( ${shareLocal} ) already exists."
	else
		echo "Creating directory ${shareLocal} for controller file share..."
		mkdir -p ${shareLocal}	
		#change the permissions on shares directory and sub directories
		chmod -R 777 /shares
		cp -ra /opt/dell/icee/appliance/templates/server/. ${shareLocal}
	fi

	local tmp=`exportfs | grep -io ${shareLocal}`

	if [[ -n ${tmp} ]]; then
		echo "Controller file share has already been exported."
	else
		echo "Exporting controller file share..."
		echo ${defaultShare} >> /etc/exports
		exportfs -a		
	fi	
}


function mountRemoteShare
{
	local url=""
	local credentials=""
	local -r mountPoint=${REMOTE_SHARE_PATH}/${shareName}
	local -u type=${shareType}
	
	case "${type}" in
		NFS)
			url=${shareAddress}:${sharePath}
		;;
		CIFS)
			url="//${shareAddress}/${sharePath}"
			credentials="-o username=${shareUsername},password=${sharePassword}"
		;;
	esac	

	# check if this share is already mounted
	local tmp=`mount | grep ${url}`
	if [[ -n ${tmp} ]]; then
		if [ "$(echo ${tmp} | cut -d ' ' -f 3)" == ${mountPoint} ]; then
			echo "Already mounted: `echo ${tmp} | cut -d ' ' -f 1-5`"
			echo " Nothing to do!"
			exit 1
		fi
	fi
	
	# check if mount point is available
	if [ -e ${mountPoint} ]; then
		if [ -d ${mountPoint} ]; then
			if [ "$(ls -A ${mountPoint})" ]; then
				echo "${mountPoint} directory already exists and is not empty."
				exit 1
			fi
		else
			echo "${mountPoint} exists and is not a directory."
			exit 1
		fi
	fi
	
	echo "Creating directory ${mountPoint} for remote file share..."
	mkdir -p ${mountPoint}
	chmod 777 ${mountPoint}
	
	echo "Mounting ${shareType} share ( ${url} ) at ${mountPoint}"
	mount -t ${shareType} ${credentials} ${url} ${mountPoint}
	exit $?
}


function unmountRemoteShare
{
	if [[ -z ${shareName} ]]; then
		echo "Must provide share name"
		exit 1
	fi 

	local mountPoint=""

	if [[ "${shareName}" =~ ^/ ]]; then
		mountPoint=${REMOTE_SHARE_PATH}${shareName}
	else
		mountPoint=${REMOTE_SHARE_PATH}/${shareName}
	fi

	echo "Un-mounting remote share ${mountPoint} ..."
	

	# check if this share is already mounted
	local tmp=`mount | grep "${mountPoint} "`
	if [[ -n ${tmp} ]]; then
		echo "Un-mounting share: `echo ${tmp} | cut -d ' ' -f 1-5`"
		umount ${mountPoint}
		rm -rf ${mountPoint}
	else
		echo "No share currently mounted at: ${mountPoint}"
	fi
	
	exit 0
}


function usage
{
echo " --configure|-c (Configure the controllers file share at /shares/local/nfs. No other args required.)"
echo
echo "          ******  Mounting/Un-mounting Remote Shares ******"
echo " --name|-n      <shareName> (Share name will be used as mount location folder name)"
echo " --address|-a   <shareAddress> (IP address of remote share)"
echo " --path|-s      <sharePath> (Path of remote share)"
echo " --type|-t      <shareType> (Type of remote share NFS|CIFS)"
echo " --username|-u  <shareUsername> (User name for remote share access - CIFS only)"
echo " --password|-p  <sharePassword> (Password for remote share access - CIFS only)"
echo
echo " --unmount|-m   <shareName> (Un-mount a remote share. shareName = mount folder)"
echo
}


######## MAIN ########

declare -r LOCAL_SHARE_PATH="/shares/local"
declare -r REMOTE_SHARE_PATH="/shares/remote"

# Set to defaults for controller share creation
declare shareName="nfs"
declare shareAddress=""
declare sharePath=${LOCAL_SHARE_PATH}
declare -l shareType="nfs"
declare sharePassword=""
declare shareUsername=""

declare proceed=1


if [[ -z "${1}" ]]; then
	echo "No arguments specified..."
	usage
	exit 0
fi

while true; do
	if [ $1 ]; then
		case "$1" in
			--configure|-c)
					createDefaultShare
					# Necessary to force exported folder permissions
					exec chmod -R 777 ${LOCAL_SHARE_PATH}
					exit 0
				;;
			--unmount|-m)
					shift
					shareName=${1}
					unmountRemoteShare
					exit 0
				;;
			--name|-n)
					shift
					shareName=${1}
				;;
			--address|-a)
					shift
					shareAddress=${1}
				;;
			--path|-s)
					shift
					sharePath=${1}
				;;
			--type|-t)
					shift
					shareType=${1}
				;;
			--username|-u)
					shift
					shareUsername=${1}
				;;
			--password|-p)
					shift
					sharePassword=${1}
				;;
			# invalid option	
			*)
				echo "Invalid Option"
				;;
		esac
		shift
	else
		break
	fi
done

if [[ -z "${shareName}" ]]; then
	echo Share name is required
	proceed=0
fi

if [[ -z "${shareAddress}" ]]; then
	echo Share address is required
	proceed=0
fi

if [[ -z "${sharePath}" ]]; then
	echo Share path is required
	proceed=0
fi

if [[ -z "${shareType}" ]]; then
	echo Share type is required
	proceed=0
else 
	case "${shareType}" in
		NFS|nfs)		
		;;
		CIFS|cifs)
			if [[ -z "${shareUsername}" || -z "${sharePassword}" ]]; then
				echo "Credentials are required with CIFS share."
				proceed=0
			fi
		;;
		*)
			echo "Invalid share type: ${shareType}"
			proceed=0
		;;
	esac	
fi

if [[ $proceed -eq 0 ]]; then
	exit 1
fi

mountRemoteShare
exit 0
