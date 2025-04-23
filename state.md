## Context Menus
Context menus are for now the primary means of moving tasks between tabs. Below are the valid menu entries for each tab.
```mermaid
---
title: Inbox Context Menu Transitions
---
stateDiagram-v2
    [*] --> Inbox
    
    state "Move to..." as move {
        Next
        Waiting
        Someday
    }
    Inbox --> move
    Inbox --> Projects : Make Project...
    Inbox --> [*] : Delete
```
```mermaid
---
title: Next Context Menu Transitions
---
stateDiagram-v2
    state "Move to..." as move {
        Waiting
        Someday
    }
    Next --> move
    Next --> Projects : Make Project...
    Next --> Complete : Complete
    Next --> [*] : Delete
```
```mermaid
---
title: Waiting Context Menu Transitions
---
stateDiagram-v2
    state "Move to..." as move {
        Next
        Someday
    }
    Waiting --> move
    Waiting --> Projects : Make Project...
    Waiting --> Complete : Complete
    Waiting --> [*] : Delete
```
```mermaid
---
title: Someday Context Menu Transitions
---
stateDiagram-v2
    state "Move to..." as move {
        Next
        Waiting
    }
    Someday --> move
    Someday --> Projects : Make Project...
    Someday --> [*] : Delete
```
```mermaid
---
title: Complete Context Menu Transitions
---
stateDiagram-v2
    Complete --> Next : Uncomplete
    Complete --> [*] : Delete
```
## Transitions
These are the inputs the user can enter when moving a task from one status to another as in the above diagrams.
### To Inbox
Inputs: 
> description

### To Next
Inputs:
> Scheduled Date

> Due Date

### To Waiting
Inputs:
> Scheduled Date

> Due Date

> Notes

### To Projects
Inputs: _incomplete, finish me!_

### To Someday
Inputs: _none_