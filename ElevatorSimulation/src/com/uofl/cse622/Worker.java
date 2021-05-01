package com.uofl.cse622;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Worker implements Comparable<Worker> {

    private static final Map<Floor, Integer> FLOOR_INDEX = new HashMap<>() {{
        put(Floor.GROUND, 0);
        put(Floor.SECOND, 1);
        put(Floor.THIRD, 2);
        put(Floor.FOURTH, 3);
    }};

    private static final Random random = new Random();
    private static int idGenerator = 1;

    private final int workerID;
    private final boolean isWalking;
    private double arrivalTime;
    private double boardingTime;
    private double waitTime;
    private Floor destination;

    public Worker(boolean isQueueLongerThan12Workers){
        this.workerID = generateID();
        this.destination = findDestination();
        if(isQueueLongerThan12Workers){
            isWalking = decideIfWalking();
        } else {
            isWalking = false;
        }
    }

    public int getWorkerID() {
        return workerID;
    }

    public boolean isWalking() {
        return isWalking;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public double getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(double waitTime) {
        this.waitTime = waitTime;
    }

    public Floor getDestination() {
        return destination;
    }

    public void setBoardingTime(double boardingTime) {
        this.boardingTime = boardingTime;
    }

    public double getBoardingTime() {
        return boardingTime;
    }

    private int generateID(){
        return idGenerator++;
    }

    private Floor findDestination(){
        int floor = Worker.random.nextInt(3)+2;
        Floor destination;
        switch (floor){
            case 2:
                destination = Floor.SECOND;
                break;
            case 3:
                destination = Floor.THIRD;
                break;
            case 4:
                destination = Floor.FOURTH;
                break;
            default:
                destination = Floor.THIRD;
                System.out.println("Error occur in deciding the destination for Worker");
                break;
        }
        return destination;
    }

    private boolean decideIfWalking(){
        boolean isWalking = false;
        if(this.destination == null){
            this.destination = findDestination();
        } else{
            switch (destination){
                case SECOND:
                    if(Math.random() < 0.5)
                        isWalking = true;
                    break;
                case THIRD:
                    if(Math.random() < 0.33)
                        isWalking = true;
                    break;
                case FOURTH:
                    if(Math.random() < 0.10)
                        isWalking = true;
                    break;
                default:
                    isWalking = false;
            }
        }
        return isWalking;
    }

    @Override
    public String toString(){
        return "Worker ID: "+getWorkerID()+" isWalking="+isWalking+", Destination="+destination
                +", waitTime="+waitTime+", arrivalTime="+arrivalTime+", boardTime="+boardingTime;
    }

    @Override
    public int compareTo(Worker worker) {
        int firstWorkerDestination = FLOOR_INDEX.get(this.destination);
        int secondWorkerDestination = FLOOR_INDEX.get(worker.getDestination());
        return firstWorkerDestination - secondWorkerDestination;
    }
}
