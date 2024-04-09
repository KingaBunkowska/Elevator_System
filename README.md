## Installation Instructions

1. Ensure that you have Java installed on your system.
2. Clone the repository using the following command:
```bash
git clone github.com/KingaBunkowska/Elevator_System
```
3. Nawigate to project directory
```bash 
cd Elevator_System
```
4. Run project
```bash 
./gradlew.bat run
```

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

## Handling Requests

The aim behind the creation of the algorithm for selecting and handling elevator requests was to ensure maximum user comfort while saving energy. Therefore, requests are handled as follows:

- If an elevator is idle and there are external requests in the system, it will select the nearest request and start moving in that direction.

- If, during its journey, the elevator reaches a floor where there is a request in the direction it is already travelling, system will stop it and make it accept request (only one elevator per floor) and open its doors.

- Elevators will continue in one direction for as long as possible, changing direction only when there are no planned visits in the current direction.

- Elevators handle internal requests autonomously since only they can execute them.

Additionally, if an elevator is under maintenance, it will not accept any external requests.
