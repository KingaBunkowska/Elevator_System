## Solution Architecture

My solution to the given problem consists of 3 main classes.

- The `Elevator` class stores information about:
    - The floor the elevator is currently on
    - Its state (moving (up or down), loading (open doors), idle (in place, close doors) or is in service)
    - The floors the people inside the elevator want to reach


- The `ElevatorSystem` class manages the elevators. It:
    - Processes external elevator calls and assigns these calls to elevators that are not currently operating or stops operating elevators at the moment
    - Has a simplified mechanism to check if the elevators are functional (i.e., if they are on the correct floor) and can either send them for maintenance or bring them back into operation (in theory, simulating elevator breakdown and its subsequent repair)


- The `Simulation` class is responsible for running the simulation. It:
    - Has an `ElevatorSystem` field
    - Allows interaction with it by
        - Making external elevator calls (`makeRequest` method)
        - Making internal calls from within the elevator (`makeRequestFromInside` method)
    - Allows for advancing one step in the simulation and returning the state of all elevators
