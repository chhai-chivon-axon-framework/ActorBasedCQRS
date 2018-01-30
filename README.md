# ActorBasedCQRS

API Used:

Create new bank account
POST /api/bankaccounts
BODY {"account_name":"Kishoj Bajracharya", "account_number":"ACC1234567890"}

Deposit amount
PUT /api/bankaccounts/deposit
BODY {"account_name":"Kishoj Bajracharya", "account_number":"ACC1234567890", "amount":50}

Withdraw amount
PUT /api/bankaccounts/withdraw
BODY {"account_name":"Kishoj Bajracharya", "account_number":"ACC1234567890", "amount":50}

Get read model for account with account number: {account_number}
GET /api/bankaccounts/{account_number}

To get all the events from EventStore (This is just for test)
GET /api/eventstores

To get all the decoded-events from EventStore
GET /api/eventstores/decode

To get all the events from EventStore for specific aggregateId
GET /api/eventstores/{aggregateId}

To get all the decoded-events from EventStore for specific aggregateId
GET /api/eventstores/{aggregateId}/decode

To get all the payloads from EventStore
GET /api/eventstores/objects

To get all the payloads from EventStore for specific aggregateId
GET /api/eventstores/objects/{aggregateId}

To get all the snapshots
/api/snapshoteventstores

To get all the decoded-snapshots
/api/snapshoteventstores/decode

To get payloads from all the decoded-snapshots
/api/snapshoteventstores/objects

To get payload of decoded-snapshot by aggregate_id
/api/snapshoteventstores/objects/{aggregate_id}

To replay all events related with account
GET /api/replay/account

To replay events only related to account number {account_number}
GET /api/replay/account/{account_number}
