### service-dell-server-configuration-profile

Exports and imports/applies part or all of a server configuration profile for a Dell server.

### Purpose

Provides a REST API retrieval and application of configuration data on Dell 11th Generation and newer servers.

http://en.community.dell.com/techcenter/extras/m/white_papers/20439335


### How to use

A docker container for this service is avalable at:  https://hub.docker.com/r/rackhd/dell-server-configuration-profile/

#### Startup
~~~
sudo docker run -p 0.0.0.0:46018:46018 --privileged --name dell-server-configuration-profile -d rackhd/dell-server-configuration-profile:latest
~~~

#### API Definitions
A Swagger UI is provided by the microservice at http://<<ip>>:46018/swagger-ui.html

#### Definitions
**shareType**
Type of network share to mount 
Possible Values: 0=nfs, 2=cifs

**shutdownType** 
Type of the host shut down before perform the import operation. 
Possible Values: Graceful=0, Forced =1, NoReboot=2

---

#### Get Components
This operation gives the server system configuration based upon the component names or ALL the configured components if no component names specified in the request. 

##### Example Post
~~~
http://<<ip>>:46018/api/1.0/server/configuration/getComponents
{
  "componentNames": ["LifecycleController.Embedded.1", "RAID.Integrated.1-1"],
  "fileName": "FileName.xml",
  "serverIP": "<<Target Server IP Address>>",
  "serverPassword": "<<Target Server Password>>",
  "serverUsername": "<<Target Server Username>>",
  "shareAddress": "<<Network Share IP address>>",
  "shareName": "<<Network Share Name e.g. /nfs>>",
  "shareType": 0
}
~~~
___
#### Export Configuration From a Dell Server
This operation allow user to export the system configuration from the server to file on a remote share. 
##### Example Post
~~~
http://<<service ip>>:46018/api/1.0/server/configuration/export
{
  "componentNames": [ " " ],
  "fileName": "Filename.xml",
  "serverIP": "<<Target Server IP Address>>",
  "serverPassword": "<<Target Server Password>>",
  "serverUsername": "<<Target Server Username>>",
  "shareAddress": "<<Network Share IP address>>",
  "shareName": "<<Network Share Name e.g. /nfs>>",
  "shareType": 0,
  "shutdownType": 0
}
~~~
___

#### Import Configuration to a Dell Server
Imports the configuration from a json file (same format as the file produced in the export call) on the share specified.
##### Example Post
~~~
http://<<service ip>>:46018/api/1.0/server/configuration/import
{
  "componentNames": [ " "  ],
  "fileName": "Filename.xml",
  "serverIP":  "<<Target Server IP Address>>",
  "serverPassword": "<<Target Server Password>>",
  "serverUsername": "<<Target Server Username>>",
  "shareAddress": "<<Network Share IP address>>",
  "shareName": "<<Network Share Name e.g. /nfs>>",
  "shareType": 0,
  "shutdownType": 0
}
~~~
___
#### Update a Subset of Components for a Dell Server
This operation allow user to update the server system components. 
##### Example Post
~~~
http://<<service ip>>:46018/api/1.0/server/configuration/updatecomponents
{
  "componentNames": ["LifecycleController.Embedded.1", "RAID.Integrated.1-1"],
  "fileName": "FileName.xml",
  "serverIP": "<<Target Server IP Address>>",
  "serverPassword": "<<Target Server Password>>",
  "serverUsername": "<<Target Server Username>>",
  "shareAddress": "<<Network Share IP address>>",
  "shareName": "<<Network Share Name e.g. /nfs>>",
  "shareType": 0
}
~~~

### Licensing
This docker microservice is available under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.txt). 

Source code for this microservice is available in repositories at https://github.com/RackHD.  

The microservice makes use of dependent Jar libraries that may be covered by other licenses. In order to comply with the requirements of applicable licenses, the source for dependent libraries used by this microservice is available for download at:  https://bintray.com/rackhd/binary/download_file?file_path=smi-service-dell-server-configuration-profile-dependency-sources-devel.zip    

Additionally the binary and source jars for all dependent libraries are available for download on Maven Central.

---
### Support
Slack Channel: codecommunity.slack.com