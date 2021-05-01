package com.uofl.cse622;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Elevator {

    private static final Map<Floor, Integer> FLOOR_INDEX = new HashMap<>() {{
        put(Floor.GROUND, 0);
        put(Floor.SECOND, 1);
        put(Floor.THIRD, 2);
        put(Floor.FOURTH, 3);
    }};


    private Floor currentFloor;
    private ArrayList<Worker> workers;

    public Elevator(){
        this.currentFloor = Floor.GROUND;
        this.workers = new ArrayList<>();
    }

    public int getNumberOfWorkers() {
        return this.workers.size();
    }

    public ArrayList<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(ArrayList<Worker> workers) {
        this.workers = workers;
    }

    public void addNewWorkerInElevator(Worker worker){
        this.workers.add(worker);
    }

    public Floor getCurrentFloor() {
        return currentFloor;
    }

    public void moveUp(){
        switch(currentFloor){
            case GROUND:
                this.currentFloor = Floor.SECOND;
                break;
            case SECOND:
                this.currentFloor = Floor.THIRD;
                break;
            case THIRD:
                this.currentFloor = Floor.FOURTH;
                break;
            case FOURTH:
                System.out.println("Elevator cannot go up! MAX FLOOR HIT!");
                this.currentFloor = Floor.FOURTH;
                break;
        }
    }

    public void moveDown(){
        switch(currentFloor){
            case GROUND:
                System.out.println("Elevator cannot go down! MIN FLOOR");
                this.currentFloor = Floor.GROUND;
                break;
            case SECOND:
                this.currentFloor = Floor.GROUND;
                break;
            case THIRD:
                this.currentFloor = Floor.SECOND;
                break;
            case FOURTH:
                this.currentFloor = Floor.THIRD;
                break;
        }
    }

    public void moveToGround(){
        while(this.currentFloor != Floor.GROUND){
            this.moveDown();
        }
    }

    public void moveToFloor(Floor floor){
        this.currentFloor = floor;
    }

    public Floor getNextDestination(){
        sortWorkersByDestination();
        if(this.workers.isEmpty()){
            return null;
        }
        return this.workers.get(0).getDestination();
    }

    public void sortWorkersByDestination(){
        Collections.sort(this.workers);
    }
}
