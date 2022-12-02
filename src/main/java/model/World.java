package model;

import ast.Mutation;
import ast.MutationFactory;
import ast.Node;
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
    private boolean enableManna;
    private boolean enableForcedMutation;

    public World()
    {
        this.numRows = Constants.HEIGHT;
        this.numColumns = Constants.WIDTH;

        // TODO: delete commented out section below
        this.numRows = 9;
        this.numColumns = 7;

        this.tiles = new Tile[numRows][numColumns];
        this.critters = new ArrayList<>();
    }

    public World(int width, int height, boolean enableManna, boolean enableForcedMutation)
    {
        this.numRows = height;
        this.numColumns = width;
        this.tiles = new Tile[numRows][numColumns];
        this.critters = new ArrayList<>();
        this.enableManna = enableManna;
        this.enableForcedMutation = enableForcedMutation;
    }

    public Tile[][] getTiles()
    {
        return tiles;
    }

    @Override
    public Tile[][] tileView()
    {
        return tiles.clone();
    }

    @Override
    public int getNumRows()
    {
        return numRows;
    }

    @Override
    public int getNumColumns()
    {
        return numColumns;
    }


//    public int getNumRows()
//    {
//        return numRows;
//    }
//
//    public int getNumColumns()
//    {
//        return numColumns;
//    }

    @Override
    public boolean addCritter(String species, int[] mem, Program ast)
    {
//        System.out.println(numRows + " " + numColumns);
        boolean flag = false;
        int count = 0;
        while (!flag && count < numRows * numColumns)
        {
            int row = (int)(Math.random() * numRows);
            int column = (tiles.length - 1 - row) % 2 == 0 ? (int) (Math.random() * ((numColumns + 1) / 2)) * 2:
                    (int) (Math.random() * (numColumns / 2)) * 2 + 1;
            flag = addCritter(species, mem, ast, row, column, (int) (Math.random() * 6));

            Critter critter = tiles[row][column].getCritter();
            critter.setJustCreated(false);
            if(critter.getMemValue(4) > critter.getMemValue(3) * Constants.ENERGY_PER_SIZE)
                critter.setMem(4, critter.getMemValue(3) * Constants.ENERGY_PER_SIZE);

            count++;
        }
        return flag;
    }

    @Override
    public boolean addCritter(String species, int[] mem, Program ast, int row, int column, int dir)
    {
        row = tiles.length - 1 - row;
        if(tiles[row][column] != null)
        {
            Tile curr = tiles[row][column];
            if(curr.getIsCritter() || curr.getIsFood() || curr.getIsRock()) return false;
        }
        tiles[row][column] = new Tile(new Critter(species, ast, mem, row, column, dir));
        critters.add(tiles[row][column].getCritter());
        return true;
    }

    /*
        add a loaded critter, justCreated should be false
     */
    @Override
    public boolean addCritter(String species, int[] mem, Program ast, int row, int column, int dir, boolean loaded)
    {
        row = tiles.length - 1 - row;
        if(tiles[row][column] != null)
        {
            Tile curr = tiles[row][column];
            if(curr.getIsCritter() || curr.getIsFood() || curr.getIsRock()) return false;
        }
        tiles[row][column] = new Tile(new Critter(species, ast, mem, row, column, dir));
        critters.add(tiles[row][column].getCritter());
        Critter critter = tiles[row][column].getCritter();
        critter.setJustCreated( !loaded );

        if(critter.getMemValue(4) > critter.getMemValue(3) * Constants.ENERGY_PER_SIZE)
            critter.setMem(4, critter.getMemValue(3) * Constants.ENERGY_PER_SIZE);
        return true;
    }


    @Override
    public boolean addRock(int row, int column)
    {
        row = tiles.length - 1 - row;
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
        row = tiles.length - 1 - row;
        if(row > numRows - 1|| row < 0 || column > numColumns - 1|| column < 0) return false;
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
        if(critters.size() == 0) System.out.println("all critters dead at timestep: " + numSteps);

        for (int i = 0; i < critters.size(); i++)
        {
            Critter critter = critters.get(i);
            if(critter.isJustCreated())
            {
                critter.setJustCreated(false);
                continue;
            }

            Interpreter interpreter = new Interpreter(this, critter);
            interpreter.interpretProgram(critter.getProgram());
            if (!critters.contains(critter))
            {
                i--;
            }
        }
        for(Critter critter: critters)
        {
            if(critter.isMating()){
                critter.setMem(4, critter.getMemValue(3));
                System.out.println("failed mate: " + critter.getMemValue(4));
            }
            critter.setMating(false);
        }

//        for(Critter critter : critters){
//            System.out.println(critter.toString());
//
//        }
        numSteps++;
    }

    @Override
    public void printWorld(PrintStream out)
    {
        for (int i = 0; i < tiles.length; i++)
        {
            for (int j = (tiles.length - 1 - i) % 2; j < tiles[i].length; j += 2)
            {
                if (j == 1)
                {
                    System.out.print(" ");
                }

                if (tiles[i][j] == null)
                {
                    System.out.print("-");
                }
                else if (tiles[i][j].getIsRock())
                {
                    System.out.print("#");
                }
                else if (tiles[i][j].getIsCritter())
                {
                    System.out.print(tiles[i][j].getCritter().getDirection());
                }
                else if (tiles[i][j].getIsFood())
                {
                    System.out.print("F");
                }

                if (j < tiles[i].length - 1)
                {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
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
    public Maybe<ReadOnlyCritter> getReadOnlyCritter(int c, int r)
    {
        r = tiles.length - 1 - r;
        if(c >= numColumns || c < 0 || r >= numRows || r < 0 || tiles[r][c] == null || !tiles[r][c].getIsCritter()) return Maybe.none();
        return Maybe.some(tiles[r][c].getCritter());
    }

    @Override
    public int getTerrainInfo(int c, int r)
    {
        r = tiles.length - 1 - r;
        if(c >= numColumns || c < 0 || r >= numRows || r < 0) return Constants.ROCK_VALUE; // out of bounds indices should be treated as a rock
        if(tiles[r][c] == null) return 0;
        if(tiles[r][c].getIsRock()) return Constants.ROCK_VALUE; // -1
        else if(tiles[r][c].getIsFood()) return (tiles[r][c].getNumFood() + 1) * -1;
        else if(tiles[r][c].getIsCritter()) return tiles[r][c].getCritter().getDirection() + 1;
        return 0;
    }

    public boolean deadCritter(Critter critter)
    {
        int row = critter.getRow();
        int column = critter.getColumn();
        if( !tiles[row][column].getCritter().equals(critter) ) return false;

        System.out.println(critter.getSpecies() + " died at time step: " + numSteps);
        tiles[row][column] = new Tile(critter.getMemValue(3) * Constants.FOOD_PER_SIZE);
        critters.remove(critter);

        return true;
    }

    public void setCritterPosition(Critter critter, int r, int c)
    {
        tiles[critter.getRow()][critter.getColumn()] = null;
        tiles[r][c] = new Tile(critter);
        critter.setPosition(r, c);
    }

    @Override
    public void addManna()
    {
        if(critters.size() == 0){
            return;
        }

        if(Math.random() > ((double) (1) / (double) (getNumberOfAliveCritters()))) return;

        int numTiles = numRows * numColumns;

        for(int i=0; i < ( Constants.MANNA_COUNT * numTiles / 1000); i++){

            int r = (int)(Math.random() * numRows);
            int c = (tiles.length - 1 - r) % 2 == 0 ? (int) (Math.random() * ((numColumns + 1) / 2)) * 2:
                    (int) (Math.random() * (numColumns / 2)) * 2 + 1;

            if (tiles[r][c] == null)
            {
                tiles[r][c] = new Tile(Constants.MANNA_AMOUNT);
            }

            else
            {
                while (tiles[r][c].getIsRock() || tiles[r][c].getIsCritter())
                {
                    r = (int) (Math.random() * numRows);
                    c = (tiles.length - 1 - r) % 2 == 0 ? (int) (Math.random() * ((numColumns + 1) / 2)) * 2 :
                            (int) (Math.random() * (numColumns / 2)) * 2 + 1;
                    if (tiles[r][c] == null)
                    {
                        tiles[r][c] = new Tile(Constants.MANNA_AMOUNT);
                        break;
                    }
                }
                if (tiles[r][c] != null)
                {
                    tiles[r][c] = new Tile(tiles[r][c].getNumFood() + Constants.MANNA_AMOUNT);
                }
            }
        }

    }

    @Override
    public void forcedMutate()
    {
        for(Critter critter : critters)
        {
            Program ast = critter.getProgram();
            int selector = (int) Math.random() * ast.size();
            Node mutatedNode = ast.nodeAt(selector);

            int mutation = (int) (Math.random() * 6);

            switch(mutation)
            {
                case 0:
                    Mutation remove = MutationFactory.getRemove();
                    remove.apply(ast, mutatedNode);
                    break;
                case 1:
                    Mutation swap = MutationFactory.getSwap();
                    swap.apply(ast, mutatedNode);
                    break;
                case 2:
                    Mutation replace = MutationFactory.getReplace();
                    replace.apply(ast, mutatedNode);
                    break;
                case 3:
                    Mutation transform = MutationFactory.getTransform();
                    transform.apply(ast, mutatedNode);
                    break;
                case 4:
                    Mutation insert = MutationFactory.getInsert();
                    insert.apply(ast, mutatedNode);
                    break;
                case 5:
                    Mutation duplicate = MutationFactory.getDuplicate();
                    duplicate.apply(ast, mutatedNode);
                    break;
            }
        }
    }
}
