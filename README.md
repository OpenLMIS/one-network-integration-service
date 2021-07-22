# one-network-integration-service

## Quick Start
1. Fork/clone this repository from GitHub.

 ```shell
 git clone https://github.com/OpenLMIS/one-network-integration-service.git
 ```
2. Add an environment file called `.env` to the root folder of the project, with the required 
project settings and credentials. For a starter environment file, you can use [this 
one](https://raw.githubusercontent.com/OpenLMIS/openlmis-ref-distro/master/settings-sample.env). e.g.

 ```shell
 cd one-network-integration-service
 curl -o .env -L https://raw.githubusercontent.com/OpenLMIS/openlmis-ref-distro/master/settings-sample.env
 ```
3. Develop w/ Docker by running `docker-compose run --service-ports one-network-integration-service`.
See [Developing w/ Docker](#devdocker). You should now be in an interactive shell inside
the newly created development environment.
4. Run `gradle build` to build. After the build steps finish, you should see 'Build Successful'.
5. Start the service with `gradle bootRun`. Once it is running, you should see
'Started Application in NN seconds'. Your console will not return to a prompt as long as
the service is running. The service may write errors and other output to your console.
6. You must authenticate to get a valid `access_token` before you can use the service.
Follow the [Security](https://github.com/OpenLMIS/openlmis-example/blob/master/README.md#security)
instructions to generate a POST request to the authorization server at `http://localhost:8081/`.
You can use a tool like [Postman](https://www.getpostman.com/) to generate the POST.
The authorization server will return an `access_token` which you must save for use on requests to
this OpenLMIS service. The token will expire with age, so be ready to do this step often.
7. Go to `http://localhost:8080/?access_token=<yourAccessToken>` to see the service name and version.
Note: If localhost does not work, the docker container with the service running might not be
bridged to your host workstation. In that case, you can determine your Docker IP address by
running `docker-machine ip` and then visit `http://<yourDockerIPAddress>:8080/`.

### Build Deployment Image
The specialized docker-compose.builder.yml is geared toward CI and build 
servers for automated building, testing and docker image generation of 
the service.

Before building the deployment image, make sure you have a `.env` file as outlined in the Quick
Start instructions.

```shell
> docker-compose -f docker-compose.builder.yml run builder
> docker-compose -f docker-compose.builder.yml build image
```

## Downloading SFTP Host Key
SFTP Host key is needed for application to run.
It's possible to download it via terminal using command:
> ssh-keyscan -t rsa host-address.com > file

Copy this key from the file (without `host-address.com ssh-rsa`) and save as `sftpHostKey.txt`
