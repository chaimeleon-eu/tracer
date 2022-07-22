# Tracer

**Tracer** is a program meant to trace user actions in a microservices deployment.

Its main purposes are:
- Keep track of user actions in order to determine bad behavior
- Maintain an immutable database of the data accessed by the user (without actually keeping the data, or any information that could allow the inference of the aforementioned data information)
- Allow generating statistics such as what data was mostly used by the users in a certain interval of time

## Functionality

**Tracer** uses a blockchain system (BigchainDB) to store the data it records.
Blockchain represents the perfect technology, because of its resilience to data modification and its distributed nature.
Once a trace gets inserted into the blockchain, it cannot be modified without compromising the whole chain.
Furthermore, since the nodes containing the full copy of the data are distributed in various locations, the automatic redundancy adds another layer of protection to the saved data.
An instance of **Tracer** can connect to its own stand-alone instance of BigchainDB, or it can use a shared deployment.
We strongly recommend the former approach, since the idea of blockchain is that of each node having a copy of the whole chain.

### Caching

Blockchains can reject blocks for various reasons e.g. invalid format.
Due to the distributed nature of the system, the fate of a block stay unknown for quite a while.
On the other hand, our tracing applications **must** respond immediately when it is called.
As a result, we added a trace caching feature that stores all traces before sending them to the blockchain.
This way the application can respond immediately, and preserve all traces regardless of their status in the blockchain.
Once a trace has been successfully added, it is removed from the cache.

## User actions

The application traces the following user actions:
- Create a new dataset
- Create new version of an existing dataset
- Visualize a certain version of a dataset
- Launch a Kubernetes pod with mounted datasets
- Create a new model in a Kubernetes Application
- Use one or more models in a Kubernetes Application

## Tracing resources

Each dataset (including its various versions) contains a number of resources.
These resources are normally files such as imaging data or patient information.
**Tracer** allows the inclusion of various information about resources such as the resource type, the hash of the name, the hash of each reasource's path, and the hash of the content.
This information allows the user to check the integrity of the resources of a dataset.

The dataset resources can appear in the following form:
- as a remote file on a HTTP(S) URL; **Tracer** will try to download the content and hash it.
- as Base64 encoded content; **Tracer** will convert to binary and hash the data.
- as a hash; **Tracer** will set the content hash as the the one provided as input

## Hashing

There are multiple hashing algorithms supported in our application, all of them being cryptographic hashes.
This means that the security of the hashed data is builtin, and an unauthorized access cannot deal much of a damage.
The blockchain itself uses _SHA-256_ for its needs (such as a block's full hash), but **Tracer** can use different types of hashes for its traces.
Since traces are stand-alone, separate objects in a block, their content is not forced in any way or form by the blockchain technology.
The allowance of different hash types gives us the following advantages:
- increase or decrease the strength of the hash using the bit requirements
- allow trusted third parties to provide their own hashed data with their hash of choice
- switch to a different version of a hash without a problem, if there are issues with the a certain version (SHA latest version is 3)
- use a different hashing algorithm altogether

## Authentication

**Tracer** supports two types of authentication (selected via the _Authorization_ HTTP header):
- Basic HTTP ( _Authorization_  header value starts with ***Basic***, followed by a space and by the Base64 encoded  _username:password_ )
- OIDC via Keycloak ( _Authorization_  header value starts with ***Bearer***, followed by a space and by the OIDC token )

## Blockchain

Currently we support only one blockchain provider. 
We plan to add support for multiple ones in the near future.

### BigchainDB

The official all in one Docker images might be available only for older versions of BlockchainDB.
In order to use the latest stable version of the blockchain provider, you could built the container yourself.
You can build and run an all in one Docker container with all BigchainDB's services following this steps:

- Clone the repo

```
github clone https://github.com/bigchaindb/bigchaindb
```

- Build the all in one image

```
docker build --tag bigchaindb-all-in-one:1 -f bigchaindb/Dockerfile-all-in-one bigchaindb
```

- Run the all in one image of BlockchainDB

```
docker run -d --name bigchaindb -p 9984:9984 -p 9985:9985 -p 27017:27017 -p 26657:26657 -v $HOME/bigchaindb_docker/mongodb/data/db:/data/db -v $HOME/bigchaindb_docker/mongodb/data/configdb:/data/configdb -v $HOME/bigchaindb_docker/tendermint:/tendermint bigchaindb-all-in-one:latest
```

### Docker container

We provide a Dockerfile base on Alpine that contains the web service. 
You can use something like the following command to build the image (considering the you cloned the repo in _tracer_ ):
`docker  build --label tracer-web -t tracer-web -f tracer/Dockerfile tracer`

## Rest API

**Tracer** exposes a Rest API that allows the interaction with it.
The following sub-paragraphs describe each of the implemented calls.

### List supported user actions

**GET /api/v1/traces/actions**

Returns a list of string containing the user actions that can be traced.
You must use one of these strings when putting together JSON POST used for traces add.
The system does not take into account if the string is in capital letters or not.

### List supported hash types

**GET /api/v1/traces/hashes**

The list of hashes supported by this application.
These can be used to hash various fields in a trace, such as the content of a file part of a dataset.  

### List supported dataset resources types

**GET /api/v1/traces/dataset_resources**

This call returns the list of supported resources types for a dataset.
These values are used when you want to create a new dataset with its resources.
Since a dataset has various types of resources, you have to set the type.

### List supported request resource contents types

**GET /api/v1/traces/request_resource_contents**

This call returns the list of available request resources' content types.
Since the content of a dataset's resource can be made available via different ways, you have to set the type of the path of the resource.
The application can either obtain the content (downloading the data or simply accessing it if the resource is mounted), or it can add

### Add new trace

**POST /api/v1/traces**

Various traces can be registered through this call.
In the following sub-paragraphs, we analyze the fields needed in the POSTed data.

We split them in two groups:
- one containing those common to all POST calls
- a second one with those specific to a certain user action (user action ID specified in each paragraph's title)

#### Dataset Resources

When you want to trace the resources assigned to a dataset, you have to post a list of them.
More details about them can be found in the _Tracing resources_ section.

The JSON representation's common fields of a resource that should be added to the traces database is the following:

```
{
    "id": <String, not null, not empty, values not limited to a specific set>,
    "contentType": <String, values from a predefined set>,
    "name": <String, not null, not empty, values not limited to a specific set>
    "resourceType": <String, values from a predefined set>
}
```

Aside from these fields, by _contentType_ , each resource has its own set of fields:
- remote HTTP(s) file:

```
{
    "url": <String, not null, not blank, valid URL format pointing to a file>
}
```
- Base64 encoded content:

```
{
    "data": <String, not null, not blank, Base64 of the content>
}
```

- hashed content (the allowed values of the _hashType_ field can be obtained calling the helper hash types endpoint, check the REST API section detailing it):

```
{
    "hash": <String, not null, not blank, Base64 of the hash itself>,
    "hashType": <String, values from a predefined set>
}
```

#### Common fields

These fields must be present in all calls.
For the available user actions, please check the API call to list the supported user actions.

```
{
    "userId": <String, values not limited>,
    "callerId": <String, values not limited>,
    "userAction": <String, values from a predefined set>
}
```


#### Create a new dataset ( _CREATE_NEW_DATASET_ )

This action traces the creation of a new dataset.
The POST data has to include a list of resources traced by the system.
This list can be empty.

```
{
    <common fields>,
    "datasetId": <String, values not limited>,
    "resources": <List, not null>
}
```

#### Create new version of an existing dataset ( _CREATE_VERSION_DATASET_ )
This action cannot be used when there's no previous version of the dataset.

```
{
    <common fields>,
    "datasetsId": <String, not null, not blank, values not limited>,
    "previousId": <String, not null, not blank, values not limited>
    "resources": <List, not null>
}
```

#### Visualize a certain version of a dataset ( _VISUALIZE_VERSION_DATASET_ )

```
{
    <common fields>,
    "datasetId": <String, not null, not blank, values not limited>
}
```

#### Launch a Kubernetes pod with mounted datasets  ( _USE_DATASETS_POD_ )
The Kubernetes pod should mount at least one dataset.
The JSON call includes the list of the datasets' IDs as strings.
The IDs are for either the original version of a dataset, or any ulterior versions of it.

```
{
    <common fields>,
    "datasetsIds": <List, not null, not empty>
}
```

#### Create a new model in a Kubernetes Application ( _CREATE_MODEL_POD_ )

```
{
    <common fields>,
    "datasetsIds": <List, not null, not blank>,
    "applicationId": <String, not null, not blank>,
    "modelId": <String, not null, not blank, values not limited>
}
```

#### Use one or more models in a Kubernetes Application ( _USE_MODEL_POD_ )

```
{
    <common fields>,
    "datasetId": <String, not null, not blank, values not limited>,
    "applicationId": <String, not null, not blank>,
    "modelsIds": <List, not null, not empty>
}
```

### Get all traces by user ID

**GET /api/v1/traces/{userId}**

It returns a list of all traces containing the user ID specified in the API path.
The list can be empty if not actions by the requested user have been found.

### Get all traces in cache

**GET /api/v1/traces/cache**

It returns a list (could be empty) containing all traces submitted to the application that:
- are waiting for their inclusion in the blockchain
- could not be inserted into the blockchain

# Examples

These are dev examples, suited for a controlled environment. 
Please take care when configuring tracer for a production environment (such as the self signed certificates and ignore it with curl).

First, let's export some variables to facilitate our work.

Tracer's base domain `export TRACER_BASE='https://localhost:8090'`

Keycloak's base domain (if you need it) `export KEYCLOAK_BASE='http://localhost:8080'`

The authorization header as basic authorization

```
export AUTH_HEADER='Authorization: Basic <base64 username:password>'
```

or using a token for Keycloak (and `curl -L -X POST -H 'Content-Type: application/x-www-form-urlencoded' --data-urlencode 'client_id=tracer' --data-urlencode 'grant_type=password' --data-urlencode 'scope=openid' --data-urlencode 'username=<your user>' --data-urlencode 'password=<your password>' ${KEYCLOAK_BASE}/auth/realms/CHAIMELEON/protocol/openid-connect/token` to get a token)

```
export AUTH_HEADER='Authorization: Bearer <your token>'
```


### Add new trace

```
curl -k -v -X POST -H "Content-Type: application/json" -H "${AUTH_HEADER}" -d '{"userAction":"CREATE_NEW_DATASET","userId":"userId", "datasetId": "2", "resources": [{"id": "2", "contentType": "FILE_DATA", "name": "resource 12121", "resourceType": "IMAGING_DATA", "data": "VGhpcyBpcyBhIGZpbGU="}]}' ${TRACER_BASE}/api/v1/traces
```

### List traces per user

```
curl -k -v -H "${AUTH_HEADER}" -X GET ${TRACER_BASE}/api/v1/traces?actionUserId=userId
```

### List supported user actions

```
curl -k -v -H "${AUTH_HEADER}" -X GET ${TRACER_BASE}/api/v1/traces/actions
```

