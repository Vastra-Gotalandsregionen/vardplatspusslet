# Vårdplatspusslet

## Quick start development
* Start webapp in servlet container on port 8080 with context path "/vardplatspusslet".
* Cd to core-bc/modules/client.
* Type "npm install".
* Type "npm start".
* Browse to localhost:4200.

## Build and start with Docker container
If the above steps don't work for any reason (e.g. using an M1 Mac, or a version of Node which isn't compatible with the dependencies), the above may be replaced by:

````
docker run --rm -p 4200:4200 --platform linux/amd64 -it -v ${PWD}:/client node:12 bash -c 'cd client && npm install && npm run start-from-docker'
````

Or build for production:

````
docker run --rm --platform linux/amd64 -it -v ${PWD}:/client node:12 bash -c 'cd client && npm install && npm run build-prod'
````
