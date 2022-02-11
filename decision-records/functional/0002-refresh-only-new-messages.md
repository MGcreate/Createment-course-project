# 0002. Refresh only new messages
Date: 2022-01-24

## Status
Accepted

## Context
When a message is posted, ideally it would be visible instantaneously to all users. This means that open channel windows need to automatically refresh.

## Decision
The channel message log will refresh automatically every second, which we estimate is sufficiently close to "instantaneous". The OID of the most recently printed message is used to post a request to the endpoint, which returns all messages sent after (or simultaneously with) this reference messages. The returned messages are then checked against those already shown in the message log, and only new messages are added.

## Consequences
- The message log doesn't have to be rebuilt entirely on every refresh action, saving resources.
- Not the entire channel history needs to be requested on every refresh action, saving bandwidth.
- The procedure for requesting data has become more complex, both from JS to the endpoint and from the endpoint to the SQL database.
- There is a very small chance of messages not being displayed if there is latency in posting messages to the SQL database. If so, this should be resolved upon refreshing the channel window.
- Refresh is not truly instantaneous
- Open chat windows will continuously post requests to server even if no new messages are added, which may be a waste of bandwidth/server capacity.