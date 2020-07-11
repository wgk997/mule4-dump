# SRS Base API Template

## Starting a new Project with the Base API Template

1. Import the Base API Template from Anypoint Exchange.
2. At the project level, select Refactor -> Rename the project to the API Name (i.e. ticket-system-api)
3. Select the __apiName__.xml and Refactor->Rename the file to the RAML name, as defined in Design center (i.e. ticket-system-api) for the given API
4. Open the newly renamed file:
	- replace all instances of __apiName__ with the RAML name (i.e. ticket-system-api). 
	- replace the config-ref on the http listener to the listener defined within global.xml that meets the deployment scenario (Cloudhub, onprem, etc.). For example: Cloudhub_HTTP_Listener_Configuration
5. Open global.xml:
	- replace all instances of __apiName__ with the RAML name (i.e. ticket-system-api). 

6. In the pom.xml:
  - Change the artifactId element to be the name-of-the-application-separated-by-dashes
  - Change the version element of the project to 1.0.0.
  - Delete the `<type>template</type>` line.
  - Delete the plugin that is not used (i.e. cloudhub, onprem)
7. Import the application from Design Center by selecting Anypoint Platform -> Import from Design Center
8. Right click on newly imported RAML in src/main/api, select Mule->Generate Flows.
9. If deploying API on-prem, open mule-deploy.properties, uncomment the domain=srs-domain and add comment indicator (#) to beginning of line: domain=default
10. Open src/main/resources/log4j2.xml. Replace 'srs-base-api-template' with name-of-the-application-separated-by-dashes.
11. In src/main/resources, refactor the .properties files: change api.name to RAML name (i.e. ticket-system-api)

### Development

Before deploying this project to the Exchange, the .git directory will need to be moved out of the project. Publishing the .git directory to the Exchange will cause problems for developers trying to take advantage of the project as a fresh
application.


###Deployment
To deploy to Cloudhub:
mvn package mule:deploy -Denv.deploy.username= -Denv.deploy.password -Denv.ch.env.name= -DappExt= -Denv= -Danypoint.platform.client_secret -Danypoint.platform.client_id=

To deploy to ARM (on-premise):
mvn clean package mule:deploy -Denv.deploy.username= -Denv.deploy.password= -Darm.server= -Darm.env=  -Darm.target.type= -DappExt=
