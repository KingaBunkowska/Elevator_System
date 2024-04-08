package model;

public class WrongFloorException extends Exception{

    public WrongFloorException(int floor){
        super("Floor " + floor + " is invalid");
    }

}
