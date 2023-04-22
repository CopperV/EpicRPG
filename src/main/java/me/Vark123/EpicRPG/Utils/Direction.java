package me.Vark123.EpicRPG.Utils;

public enum Direction {

	N,
    NE,
    E,
    SE,
    S,
    SW,
    W,
    NW;
	
    public static final Direction[] Directions = {N, E, S, W};
    public static final Direction[] doubleDirection = {S, SW, W, NW, N, NE, E, SE, S};
    
}
