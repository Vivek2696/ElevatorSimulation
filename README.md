![](elevator.gif)

# ElevatorSimulation
>   CSE 622 Project

## Overview of Program:
* An office building has 4 floors ( G, 2, 3, 4 ) and 1 elevator.
* The elevator has a capacity of 12 people.
* On average, six workers arrive per minute between 8:00 am and 9:00 am. (Inter-arrival rate is exponential with mean = 0.1667).
* Workers are equally likely to be going to 2nd, 3rd, or 4th floor. p(2) = p(3) = p(4) = 1/3.
* Travel time of the elevator from floor to floor (in minutes) is as follows
* ![elevator_times](elevator_times.PNG = 250x250)
* The elevator only stops at a given floor if someone is getting off at that floor.
* When the elevator stops at a floor, the door remains open for .5 minutes, regardless of how many passengers are getting on or off.
* If there are more than 12 people waiting on the elevator, some people will use the stairs. A person going to the second floor will have a .50 chance of walking. A person going to the third floor will have a .33 chance of walking. A person going to the fourth floor will have a .10 chance of walking.
