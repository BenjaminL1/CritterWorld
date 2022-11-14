package model;

import ast.Program;
import cms.util.maybe.Maybe;

import java.io.PrintStream;
import java.util.ArrayList;

public class World extends ControlOnlyWorld implements ReadOnlyWorld
{

    private int numRows;
    private int numColumns;
    private Tile[][] tiles;
    private int numSteps;
    private ArrayList<Critter> critters;

    public World(){
        this.numRows = (int)(Math.random()*100);
        this.numColumns = (int)(Math.random()*100);
        this.tiles = new Tile[numRows][numColumns];
    }

    public World(int width, int height){
        this.numRows = height + 1;
        this.numColumns = width + 1;
        this.tiles = new Tile[height + 1][width + 1];
    }


    @Override
    public boolean addCritter(String species, int[] mem, Program ast) {
        boolean flag = false;
        int count = 0;
        while( !flag && count < numRows*numColumns){
            flag = addCritter( species, mem, ast, (int)(Math.random()*numRows), (int)(Math.random()*numColumns), (int)(Math.random()*6));
        }
        return flag;
    }

    @Override
    public boolean addCritter(String species, int[] mem, Program ast, int row, int column, int dir)
    {
        if(tiles[column][row] != null)
        {
            Tile curr = tiles[column][row];
            if(curr.getIsCritter() || curr.getIsFood() || curr.getIsRock()) return false;
        }

        tiles[column][row] = new Tile(new Critter(species, ast, mem, row, column, dir));
        return true;

    }


    @Override
    public boolean addRock(int row, int column)
    {
        if(tiles[column][row] != null)
        {
            Tile curr = tiles[column][row];
            if(curr.getIsCritter() || curr.getIsFood() || curr.getIsRock()) return false;
        }
        tiles[column][row] = new Tile();
        return true;
    }

    @Override
    public boolean addFood(int row, int column, int amount)
    {
        if(tiles[column][row] != null)
        {
            Tile curr = tiles[column][row];
            if(curr.getIsCritter() || curr.getIsFood() || curr.getIsRock()) return false;
        }
        tiles[column][row] = new Tile(amount);
        return true;
    }

    @Override
    void advanceTimeStep()
    {
        for (Critter critter: critters)
        {

        }
    }

    @Override
    void printWorld(PrintStream out)
    {

    }

    @Override
    public int getSteps()
    {
        return numSteps;
    }

    @Override
    public int getNumberOfAliveCritters()
    {
        return critters.size();
    }

    @Override
    public Maybe<ReadOnlyCritter> getReadOnlyCritter(int c, int r) {
        if(c > numColumns || r > numRows || tiles[c][r] == null || !tiles[c][r].getIsCritter()) return Maybe.none();

        return Maybe.some(tiles[c][r].getCritter());
    }

    @Override
    public int getTerrainInfo(int c, int r) {
        if(c > numColumns || r > numRows) throw new IndexOutOfBoundsException();
        if( tiles[c][r] == null) return 0;
        if(tiles[c][r].getIsRock()) return -1;
        else if(tiles[c][r].getIsFood()) return (tiles[c][r].getNumFood() + 1) * -1;
        else if(tiles[c][r].getIsCritter()) return tiles[c][r].getCritter().getDirection() + 1;
        return 0;
    }
}
