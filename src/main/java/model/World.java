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

    public World()
    {
        this.numRows = (int)(Math.random()*100);
        this.numColumns = (int)(Math.random()*100);
        this.tiles = new Tile[numRows][numColumns];
    }

    public World(int width, int height){
        this.numRows = height + 1;
        this.numColumns = width + 1;
        this.tiles = new Tile[height + 1][width + 1];
    }

    public Tile[][] getTiles(){
        return tiles;
    }

    @Override
    public boolean addCritter(String species, int[] mem, Program ast)
    {
        boolean flag = false;
        int count = 0;
        while( !flag && count < numRows*numColumns)
        {
            flag = addCritter( species, mem, ast, (int)(Math.random()*numRows), (int)(Math.random()*numColumns), (int)(Math.random()*6));
        }
        return flag;
    }

    @Override
    public boolean addCritter(String species, int[] mem, Program ast, int row, int column, int dir)
    {
        if(tiles[row][column] != null)
        {
            Tile curr = tiles[row][column];
            if(curr.getIsCritter() || curr.getIsFood() || curr.getIsRock()) return false;
        }

        tiles[row][column] = new Tile(new Critter(species, ast, mem, tiles.length - row, column, dir));
        critters.add(tiles[row][column].getCritter());
        return true;

    }


    @Override
    public boolean addRock(int row, int column)
    {
        if(tiles[row][column] != null)
        {
            Tile curr = tiles[row][column];
            if(curr.getIsCritter() || curr.getIsFood() || curr.getIsRock()) return false;
        }
        tiles[row][column] = new Tile();
        return true;
    }

    @Override
    public boolean addFood(int row, int column, int amount)
    {
        if(tiles[row][column] != null)
        {
            Tile curr = tiles[row][column];
            if(curr.getIsCritter() || curr.getIsFood() || curr.getIsRock()) return false;
        }
        tiles[row][column] = new Tile(amount);
        return true;
    }

    @Override
    public void advanceTimeStep()
    {
        for (Critter critter: critters)
        {

        }
    }

    @Override
    public void printWorld(PrintStream out)
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
        if(c > numColumns || r > numRows || tiles[r][c] == null || !tiles[r][c].getIsCritter()) return Maybe.none();

        return Maybe.some(tiles[r][c].getCritter());
    }

    @Override
    public int getTerrainInfo(int c, int r) {
        if(c > numColumns || r > numRows) throw new IndexOutOfBoundsException();
        if( tiles[r][c] == null) return 0;
        if(tiles[r][c].getIsRock()) return -1;
        else if(tiles[r][c].getIsFood()) return (tiles[r][c].getNumFood() + 1) * -1;
        else if(tiles[r][c].getIsCritter()) return tiles[r][c].getCritter().getDirection() + 1;
        return 0;
    }

    public boolean deadCritter(Critter critter){

        int row = critter.getRow();
        int column = critter.getColumn();
        if( !tiles[row][column].getCritter().equals(critter) ) return false;

        tiles[row][column] = new Tile(critter.getMemValue(3) * Constants.FOOD_PER_SIZE);
        critters.remove(critter);

        return true;
    }

    public void setCritterPosition(Critter critter, int r, int c){
        tiles[critter.getColumn()][critter.getRow()] = null;
        tiles[r][c] = new Tile(critter);
        critter.setPosition(r, c);
    }
}
