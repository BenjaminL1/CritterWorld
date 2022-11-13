package model;

import ast.Program;
import cms.util.maybe.Maybe;

public class Critter implements ReadOnlyCritter
{
    private String species;
    private Program ast;
    private int[] mem;
    private int row;
    private int column;
    private int direction;

    public Critter(String species, Program ast, int[] mem, int row, int column, int direction)
    {
        this.species = species;
        this.ast = ast;
        this.mem = mem;
        this.row = row;
        this.column = column;
        this.direction = direction;
    }

    public Program getProgram()
    {
        return ast;
    }

//    public int[] getMem()
//    {
//        return mem;
//    }

    public int getRow()
    {
        return row;
    }

    public int getColumn()
    {
        return column;
    }

    public int getDirection()
    {
        return direction;
    }

    public int getMemValue(int index)
    {
        return index >= 0 && index < mem.length ? mem[index] : 0;
    }

    public void setMem(int index, int newNum)
    {
        if ((index >= 3 && index <= 5 && newNum >= 1) || (index == 6 && newNum >= 0 && newNum <= 99))
        {
            mem[index] = newNum;
        }
    }

    @Override
    public String getSpecies()
    {
        return species;
    }

    @Override
    public int[] getMemory()
    {
        return mem.clone();
    }

    @Override
    public String getProgramString()
    {
        return ast.toString();
    }

    @Override
    public Maybe<String> getLastRuleString()
    {
        // TODO implement
        return Maybe.none();
    }
}
