package model;

import ast.Program;

import java.io.PrintStream;

public abstract class ControlOnlyWorld
{
    public abstract boolean addCritter(String species, int[] mem, Program ast);

    public abstract boolean addCritter(String species, int[] mem, Program ast, int row, int column, int dir);

    public abstract boolean addRock(int row, int column);

    public abstract boolean addFood(int row, int column, int amount);

    abstract void advanceTimeStep();

    abstract void printWorld(PrintStream out);

}
