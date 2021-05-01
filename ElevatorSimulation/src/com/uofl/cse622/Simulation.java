package com.uofl.cse622;

import java.util.*;

public class Simulation {
    private double timer = 0;

    private int numberOfWorkersAt830;
    private int numberOfWorkersAt845;
    private int numberOfWorkersAt9;

    private boolean timePunchAt830;
    private boolean timePunchAt845;
    private boolean timePunchAt9;

    //Declaring constants
    private static final Map<Floor, Integer> FLOOR_INDEX = new HashMap<>() {{
        put(Floor.GROUND, 0);
        put(Floor.SECOND, 1);
        put(Floor.THIRD, 2);
        put(Floor.FOURTH, 3);
    }};
    private static final Double[][] FLOOR_TRAVEL = {
            {null, 1.00, 1.50, 1.75},
            {1.00, null, 0.50, 0.75},
            {1.50, 0.50, null, 0.50},
            {1.75, 0.50, 0.25, null}
    };
    private Queue<Worker> workerQueue;
    private ArrayList<Worker> result;
    private Elevator elevator;

    //Constructor to initialize some parameters
    public Simulation(){
        this.workerQueue = new LinkedList<>();
        this.result = new ArrayList<>();
        this.elevator = new Elevator();
    }

    //start the simulation
    public void startSimulation(){
        double timeTookElevatorToReachDestination = 0;
        double leftOverTime = 0;
        boolean isElevatorGoingUp = true;
        Floor nextDestinationForElevator;
        addNewWorkersInQueue();
        addWorkersInElevator();

        while(!workerQueue.isEmpty() || timer <= 60.00){

            nextDestinationForElevator = elevator.getNextDestination();
            if(nextDestinationForElevator == null){
                //elevator is empty then going to ground floor
                isElevatorGoingUp = false;
            }

            if(isElevatorGoingUp){
                int source = FLOOR_INDEX.get(elevator.getCurrentFloor());
                int destination = FLOOR_INDEX.get(nextDestinationForElevator);
                double t = FLOOR_TRAVEL[source][destination];
                elevator.moveToFloor(nextDestinationForElevator);
                timer += t;
                removeWorkersFromElevator();
                timer += 0.50;
                timeTookElevatorToReachDestination = t+0.50;
            } else{
                //elevator is going to ground floor
                int source = FLOOR_INDEX.get(elevator.getCurrentFloor());
                int destination = FLOOR_INDEX.get(Floor.GROUND);
                double t = FLOOR_TRAVEL[source][destination];
                timer += t;
                //move elevator to the ground
                elevator.moveToGround();
                addWorkersInElevator();
                timer += 0.50;
                timeTookElevatorToReachDestination = t+0.50;
                isElevatorGoingUp = true;
            }


            int numberOfTimesWorkerBatchArrives = (int) (leftOverTime + timeTookElevatorToReachDestination);
            //check the time and add new workers
            if(numberOfTimesWorkerBatchArrives >= 1 &&
                    (timer - numberOfTimesWorkerBatchArrives ) <= 60.00){
                for(int i = 0; i < numberOfTimesWorkerBatchArrives; i++){
                    addNewWorkersInQueue();
                }
            }

            leftOverTime = (leftOverTime + timeTookElevatorToReachDestination) - numberOfTimesWorkerBatchArrives;

            if(timer >= 29 && !timePunchAt830){
                numberOfWorkersAt830 = workerQueue.size();
                timePunchAt830 = true;
            }
            if(timer >= 44 && !timePunchAt845){
                numberOfWorkersAt845 = workerQueue.size();
                timePunchAt845 = true;
            }
            if(timer >= 59 && !timePunchAt9){
                numberOfWorkersAt9 = workerQueue.size();
                timePunchAt9 = true;
            }

        }

        printOverallResult();
    }

    private void printOverallResult() {
        double waitTimeTotal = 0;
        double workerTookElevator = 0;
        double workerWalkingToSecondFloor = 0;
        double workerWalkingToThirdFloor = 0;
        double workerWalkingToFourthFloor = 0;
        for (Worker worker :
                result) {
            System.out.println(worker);
            if(!worker.isWalking()){
                waitTimeTotal += worker.getWaitTime();
                workerTookElevator += 1;
            } else {
                if(worker.getDestination() == Floor.SECOND){
                    workerWalkingToSecondFloor += 1;
                }
                if(worker.getDestination() == Floor.THIRD){
                    workerWalkingToThirdFloor += 1;
                }
                if(worker.getDestination() == Floor.FOURTH){
                    workerWalkingToFourthFloor += 1;
                }
            }
        }
        System.out.println("\n\nAverage Wait Time: "+waitTimeTotal/workerTookElevator);
        System.out.println("\n\nWorkers walking to second floor: "+workerWalkingToSecondFloor);
        System.out.println("Workers walking to third floor: "+workerWalkingToThirdFloor);
        System.out.println("Workers walking to fourth floor: "+workerWalkingToFourthFloor);

        boolean found = false;
        while(!found){
            int count = 0;
            Worker lastWorker = result.get(result.size() - ( 1 + count));
            if(!lastWorker.isWalking()){
                found = true;
                System.out.println("\n\nTime that last worker boards elevator: "+
                        getTimeForTheLastWorker( lastWorker.getArrivalTime(), lastWorker.getWaitTime()));
            }
            count = count + 1;
        }

        System.out.println("\n\nAverage Number of workers in queue at 8:30 = "+numberOfWorkersAt830);
        System.out.println("Average Number of workers in queue at 8:45 = "+numberOfWorkersAt845);
        System.out.println("Average Number of workers in queue at 9:00 = "+numberOfWorkersAt9);
    }

    private String getTimeForTheLastWorker(double arrivalTime, double waitTime) {
        int originalHour = 8;
        int originalMinutes = 0;

        int hour = (int) ((arrivalTime+1)/60);
        double leftOverTime = ((arrivalTime+1)/60) - hour;
        int minutes = (int) (leftOverTime * 60);

        originalHour += hour;
        originalMinutes += minutes;

        hour = (int) (waitTime/60);
        leftOverTime = (waitTime/60) - hour;
        minutes = (int) (leftOverTime * 60);

        originalHour += hour;
        originalMinutes += minutes;

        return ""+originalHour+":"+originalMinutes;
    }

    private void addWorkersInElevator() {
        //workers get into the elevator
        if(elevator.getCurrentFloor() == Floor.GROUND) {
            int count = 0;
            int workersAlreadyPresentInElevator = elevator.getNumberOfWorkers();
            while (count + workersAlreadyPresentInElevator < 12 && !workerQueue.isEmpty()) {
                Worker boardingWorker = workerQueue.poll();
                boardingWorker.setBoardingTime(timer);
                elevator.addNewWorkerInElevator(boardingWorker);
                count++;
            }
            elevator.sortWorkersByDestination();
        }
    }

    private void removeWorkersFromElevator() {
        Floor currentFloor = elevator.getCurrentFloor();
        Iterator<Worker> itr = elevator.getWorkers().iterator();
        while(itr.hasNext()){
            Worker departingWorker = itr.next();
            if( departingWorker.getDestination() == currentFloor ){
                departingWorker.setWaitTime(departingWorker.getBoardingTime() - departingWorker.getArrivalTime());
                result.add(departingWorker);
                itr.remove();
            }
        }
    }

    private void addNewWorkersInQueue() {
        //Worker arrives and get in the queue!
        int numberOfWorker = numberOfWorkerArrives();
        for(int i = 0; i < numberOfWorker; i++){
            Worker newWorker = new Worker(workerQueue.size() > 12);
            newWorker.setArrivalTime(timer);
            if(!newWorker.isWalking()) {
                workerQueue.add(newWorker);
            } else {
                result.add(newWorker);
            }
        }
    }

    //Get Workers based on exponential inter-arrival rate of 6.
    private int numberOfWorkerArrives(){
        return (int)Math.ceil((Math.log(1 - Math.random()) * (-6)));
    }

}
