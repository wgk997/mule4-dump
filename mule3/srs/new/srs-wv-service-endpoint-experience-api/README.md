# SRS Base API Template

## Starting a new Project with the Base API Template

1. Clone this repo in your local machine
2. Open terminal or command prompt and navigate to root of cloned project
3. Run mvn clean install, the build should be successful
4. Now navigate to your workspace (It can be any empty directory).
5. Run following command, make sure you replace "your-app-name" with your app name and deployTo with either arm(process and system apis which go to aws) or cloudhub (experience apis which go to cloudhub).
  => mvn -B archetype:generate -DarchetypeGroupId=com.srs.distribution -DarchetypeArtifactId=srs-api-template-archetype -DgroupId=com.srs.mulesoft.com -DartifactId=<your-app-name> -DdeployTo=<either arm or cloudhub>
6. Above command will generate a skeleton project.  
7. Go to anypoint studio. Click File -> Import -> Anypoint Studio -> Maven Based Mule Project from pom.xml -> browse the pom.xml from skeleton project generated from above command.
8. DO NOT CHECK "copy project into workspace".
9. If you have selected "arm" above, go to mule-project.xml and select srs-domain in domain dropdown.
10. Create a raml file in designed in design center with this naming pattern: <your-app-name.raml>. 
11. Import the RAML from Design Center by selecting Anypoint Platform -> Import from Design Center


###Deployment
Jenkins is used to deploy the projects from Bitbucket to Dev, QA, Prod, using Maven commands within pipeline. See Confluence (https://srsdigital.atlassian.net/wiki/spaces/SRSINNOV/pages/174325777/Continuous+Integration) for configuring new deployment job for your new API.

Refer to this confluence link (https://avioconsulting.atlassian.net/wiki/spaces/SD/pages/481460845/Steps+to+deploy+existing+on-prem+APIs+to+new+AWS+environment) to deploy existing on-prem APIs to new AWS environment or cloudhub environment.
