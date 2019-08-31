# Overview

The communication between server and client is based on Websockets. Every request by a client has a corresponding response by the server which will also get broadcasted to all lobby members. For the time being the lobby selector isn't fully live updating.

The protocol uses Json as a format.

# Basic example

Let's imagine a scenario where a user wants to update his username. To successfully do so the client will dispatch the following message:

```json
{
   "callId": "0a23df",
   "type": "EVENT",
   "event": "changeUsername",
   "data": [
       "coolguy23"
   ]
}
```

If the syntax is correct and the request is successfully processed by the backend the client will receive the following message:

```json
{
  "callId": "0a23df",
  "data": "\"coolguy23\"",
  "user": {
    "displayName": "coolguy23",
    "userId": "userfc693e33-9681-4723-bb55-61e146a3879c"
  }
}
```

# Syntax

Every request needs to have the following 4 fields:

* __callId__: Used to link request and response together. Can be seen as some sort of token.
* __type__: Top-level categories to group events. 3 types exist currently:
  * __EVENT__: Used for "things that happen". E.g.: Players joins lobby, Player leaves lobby, etc.
  * __EXECUTE__: Requests relating to the media player. E.g.: A user starts or stops the media player.
  * __GET__: Not interacting with any server-side data. E.g.: Getting the list of all lobbies.
* __event__: method name of the subcategory of the top-level type.
* __data__: A list of method parameters. Amount needs to be identical to required.

Every response will have the following 3 top-level fields:

* __callId__: Used to link request and response together. Can be seen as some sort of token.
* __data__: String encoded json data
* __user__: the user object of the player who submitted the request
  * __displayName__: username displayed in lobbies or on profile. Doesn't need to be unique
  * __userId__: unique user id used for identifying the user in the system.

# Requests

Every request grouped by type

## Event

#### joinLobby

```
adds user to a lobby:
expected Params:
[0]lobbyId: String
[1]userId: String

@return if successful: lobby object, else false
```  

#### createLobby

```
creates a new lobby with a given name
expected Params:
  [0]displayName: String

example json:
{
    "callId": "asdf",
    "type": "EVENT",
    "event": "createLobby",
    "data": [
      "cool lobby name"
    ]
}

@return if successful: lobby object of created lobby
```

#### leaveLobby

```
Leave the current lobby. Will not do anything if user is not in a lobby.
@return A message indicating the leaveLobby method has been called. 
Nothing else is provided as user information is sufficient.
```

## Execute

#### stopPlayer

Currently not implemented

#### startPlayer

Currently not implemented

#### moveTimestamp

Currently not implemented

## Get

#### getLobbies

```
paginated return of lobbies
expected data:
  start: integer representing first element to return, starts at 1
  size: integer representing page size
```

# Running this project

1. cd into the top level directory of this project/
2. depending on the operating system, either type:
    1. on Linux/bash: `./gradlew run`
    1. on Windows/cmd: `gradlew.bat run`