# Tracer

**Tracer** is a program built to trace user actions on a SaaS platform.

Its main purposes are:
- Keep track of user actions in order to determine bad behavior
- Maintain an immutable database of the data accessed by the user (without actually keeping the data, or any information that could allow the inference of the aforementioned data information)
- Allow generating statistics such as what data was mostly used by the users in a certain interval of time

## User actions

We plan to trace the following user actions:
- create a new dataset
- create a new version of an existing dataset
- 

## Rest API

## List supported user actions

**GET /api/v1/actions**

Returns a list of string containing the user actions that can be traced.
You must use one of these strings when putting together JSON POST used for traces add.
The system does not take into account if the string is in capital letters or not.

## List supported hash types

**GET /api/v1/hash_types**

The list of hashes supported by this application.
These can be used to hash various fields in a trace, such as the content of a file part of a dataset.  

### Add new trace

**POST /api/v1/traces**

Various traces can be registered through this call.
In the following sub-paragraphs, we analyze the fields needed in the POSTed data.

We split them in two groups:
- one containing those common to all POST calls
- a second one with those specific to a certain user action (user action ID specified in each paragraph's title) 

##### Common fields 

These fields must be present in all calls.
For various user actions, please check the other paragraphs.

```
{
	"userId": <String, values not limited>,
	"datasetId": <String, values not limited>,
	"userAction": <String, values from a predefined list>
}
```


#### Create a new dataset ( _CREATE_DATASET_ )

This action traces the creation of a new dataset.
The POST data has to include a list of resources traced by the system.
This list can be empty.

```
{
	<common fields>,
	"resources": <List, not null>
}
```

#### Create new version of an existing dataset ( _CREATE_VERSION_DATASET_ )
This action cannot be used when there's no previous version of the dataset

```
{
	<common fields>,
	<CREATE_DATASET fields>,
	"previousId": <String, not null, not blank>
}
```


