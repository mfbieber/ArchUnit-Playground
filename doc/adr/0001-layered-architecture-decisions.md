# 1. Layered Architecture Decisions

Date: 2018-10-26

## Status

Accepted

## Context

Controllers should not be accessed by any layers.
Persistence should only be accessed by repositories.
Repositories should only be accessed by controllers.

## Decision

The change that we're proposing or have agreed to implement.

## Consequences

What becomes easier or more difficult to do and any risks introduced by the change that will need to be mitigated.
