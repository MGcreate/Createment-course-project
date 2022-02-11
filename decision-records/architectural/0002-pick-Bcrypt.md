# 0002. Pick Bcrypt

Date: 2022-01-24

## Status

Accepted

## Context

We do not wish to save unhashed passwords in the database.
Therefore we need a way to hash the passwords.
There are a lot of ways to do this, including writing our own hashfunction.

Bcrypt does this for us, is easy to use, and we are able to strengthen the security by adding 'salt' and setting the amount of times the hashfunction runs.
By default this hashfunction is run 10 times, increasing this number means increasing its 'strength'.

## Decision

We will use Bcrypt to encrypt passwords before saving them to the database

## Consequences

Passwords will be encrypted as soon as they are received. And will be stored in a secure way.
Security of the website will be ensured this way.
