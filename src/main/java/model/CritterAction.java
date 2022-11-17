package model;

import ast.*;
import parse.TokenType;

public class CritterAction
{
    private World world;
    private Critter critter;

    public CritterAction (World world, Critter critter)
    {
        this.world = world;
        this.critter = critter;
    }

    public boolean perform(TokenType action)
    {
        String name = action.toString();
        switch(name)
        {
            case ("wait"):
                return waitTurn();
            case ("forward"):
                return move(true);
            case ("back"):
                return move(false);
            case ("left"):
                return turn(true);
            case ("right"):
                return turn(false);
            case ("eat"):
                return eat();
            case ("attack"):
                return attack();
            case ("grow"):
                return grow();
            case ("bud"):
                return bud();
            case ("mate"):
                return mate();
            default:
                return false;
        }

    }

    public boolean waitTurn()
    {
        critter.setMem(4, critter.getMemValue(4 ) + critter.getMemValue(3) * Constants.SOLAR_FLUX);
        return true;
    }
    public boolean move (boolean direction){ //direction here refers to forwards or backwards

        int r = critter.getRow();
        int c = critter.getColumn();

        if(critter.getMemValue(4) < critter.getMemValue(3) * 3) {
            world.deadCritter(critter);
            return false;
        }

        if(critter.getMemValue(4) == critter.getMemValue(3) * 3)
        {
            Tile[][] tiles = world.getTiles();
            int row = -1;
            int column = -1;
            if(direction){
                switch(critter.getDirection())
                {
                    case 0:
                        column = c;
                        row = r - 2;
                    case 1:
                        column = c + 1;
                        row = r - 1;
                    case 2:
                        column = c + 1;
                        row = r + 1;
                    case 3:
                        column = c;
                        row = r + 2;
                    case 4:
                        column = c - 1;
                        row = r + 1;
                    case 5:
                        column = c - 1;
                        row = r - 1;
                }
            }

            else
            {
                switch(critter.getDirection())
                {
                    case 0:
                        column = c;
                        row = r + 2;
                    case 1:
                        column = c - 1;
                        row = r + 1;
                    case 2:
                        column = c - 1;
                        row = r - 1;
                    case 3:
                        column = c;
                        row = r - 2;
                    case 4:
                        column = c + 1;
                        row = r - 1;
                    case 5:
                        column = c + 1;
                        row = r + 1;
                }
            }
            if(column < 0 || row < 0 || column >= tiles[0].length || row >= tiles.length
                    || tiles[row][column].getIsRock() || tiles[row][column].getIsFood() || tiles[row][column].getIsCritter()){
                world.deadCritter(critter);
                return false;
            }
            else{
                world.setCritterPosition(critter, column, row);
                world.deadCritter(critter);
                return true;
            }
        }

        if(critter.getMemValue(4) > critter.getMemValue(3) * 3) {

            Tile[][] tiles = world.getTiles();
            int row = -1;
            int column = -1;
            if(direction){
                switch(critter.getDirection()){
                    case 0:
                        column = c;
                        row = r - 2;
                    case 1:
                        column = c + 1;
                        row = r - 1;
                    case 2:
                        column = c + 1;
                        row = r + 1;
                    case 3:
                        column = c;
                        row = r + 2;
                    case 4:
                        column = c - 1;
                        row = r + 1;
                    case 5:
                        column = c - 1;
                        row = r - 1;
                }
                if(column < 0 || row < 0 || column >= tiles[0].length || row >= tiles.length
                        || tiles[row][column].getIsRock() || tiles[row][column].getIsFood() || tiles[row][column].getIsCritter()){
                    critter.setMem(4, critter.getMemValue(4) - critter.getMemValue(3) * Constants.MOVE_COST);
                    return false;
                }
                else{
                    critter.setMem(4, critter.getMemValue(4) - critter.getMemValue(3) * Constants.MOVE_COST);
                    world.setCritterPosition(critter, column, row);
                    return true;
                }
            }

            else{
                switch(critter.getDirection()){
                    case 0:
                        column = c;
                        row = r + 2;
                    case 1:
                        column = c - 1;
                        row = r + 1;
                    case 2:
                        column = c - 1;
                        row = r - 1;
                    case 3:
                        column = c;
                        row = r - 2;
                    case 4:
                        column = c + 1;
                        row = r - 1;
                    case 5:
                        column = c + 1;
                        row = r + 1;
                }
                if(column < 0 || row < 0 || column >= tiles[0].length || row >= tiles.length
                        || tiles[row][column].getIsRock() || tiles[row][column].getIsFood() || tiles[row][column].getIsCritter()){
                    critter.setMem(4, critter.getMemValue(4) - critter.getMemValue(3) * Constants.MOVE_COST);
                    return false;
                }
                else{
                    world.setCritterPosition(critter, column, row);
                    critter.setMem(4, critter.getMemValue(4) - critter.getMemValue(3) * Constants.MOVE_COST);
                    return true;
                }
            }
        }
        return false;
    }
    public boolean turn (boolean orientation){ //true is left, false is right

        int newDir = critter.getDirection();

        if(critter.getMemValue(4) < critter.getMemValue(3)){

            world.deadCritter(critter);
            return false;
        }

        else if(critter.getMemValue(4) == critter.getMemValue(3)){

            if(orientation){
                newDir += 5;
                newDir %= 6;
                critter.setDirection(newDir);
            }
            else{
                newDir += 1;
                newDir %= 6;
                critter.setDirection(newDir);
            }
            world.deadCritter(critter);
            return true;
        }

        else if(critter.getMemValue(4) > critter.getMemValue(3)){

            if(orientation){
                newDir += 5;
                newDir %= 6;
                critter.setDirection(newDir);
            }
            else{
                newDir += 1;
                newDir %= 6;
                critter.setDirection(newDir);
            }

            return true;
        }

        return false;
    }
    public boolean eat (){

        Tile[][] tiles = world.getTiles();
        int r = critter.getRow();
        int c = critter.getColumn();
        switch(critter.getDirection()){
            case 0:
                c = c;
                r = r + 2;
            case 1:
                c = c - 1;
                r = r + 1;
            case 2:
                c = c - 1;
                r = r - 1;
            case 3:
                c = c;
                r = r - 2;
            case 4:
                c = c + 1;
                r = r - 1;
            case 5:
                c = c + 1;
                r = r + 1;
        }

        if(c < 0 || r < 0 || c >= tiles[0].length || r >= tiles.length && (tiles[r][c] != null && !tiles[r][c].getIsFood())) return false;


        int food;
        if(tiles[r][c] == null) food = 0;
        else food = tiles[r][c].getNumFood();

        int energyBefore = critter.getMemValue(4);

        if(energyBefore + food <= critter.energyCapacity()){
            critter.setMem(4, energyBefore + food);
            tiles[r][c] = null;
            return true;
        }

        else if(energyBefore + food > critter.energyCapacity()){

            critter.setMem(4, critter.energyCapacity());
            tiles[r][c] = new Tile(food - (critter.energyCapacity()) - energyBefore);
            return true;
        }

        return false;
    }
    public boolean attack (){

        int currEnergy = critter.getMemValue(4);
        int energyUsed = critter.getMemValue(3) * Constants.ATTACK_COST;

        critter.setMem(4, currEnergy - energyUsed);

        if(critter.getMemValue(4) < 0){
            world.deadCritter(critter);
        }

        Tile[][] tiles = world.getTiles();
        int r = critter.getRow();
        int c = critter.getColumn();
        switch(critter.getDirection()){
            case 0:
                c = c;
                r = r + 2;
            case 1:
                c = c - 1;
                r = r + 1;
            case 2:
                c = c - 1;
                r = r - 1;
            case 3:
                c = c;
                r = r - 2;
            case 4:
                c = c + 1;
                r = r - 1;
            case 5:
                c = c + 1;
                r = r + 1;
        }

        if(c < 0 || r < 0 || c >= tiles[0].length || r >= tiles.length || !tiles[r][c].getIsCritter()){
            if(currEnergy == 0) world.deadCritter(critter);
            return false;
        }

        Critter target = tiles[r][c].getCritter();
        int size1 = critter.getMemValue(3);
        int size2 = target.getMemValue(3);

        int offensive = critter.getMemValue(2);
        int defensive = critter.getMemValue(1);

        double x = Constants.DAMAGE_INC * (size1 * offensive - size2 * defensive);
        double Px = 1 / (1 + Math.exp(x));
        int damageDone = (int) (Math.round( Constants.BASE_DAMAGE * size1 * Px));

        int targetNewEnergy = target.getMemValue(4) - damageDone;
        if(targetNewEnergy < 0) world.deadCritter(target);
        else target.setMem(4, targetNewEnergy);

        if(currEnergy == energyUsed){
            world.deadCritter(critter);
        }

        return true;

    }
    public boolean grow (){
        int cost = critter.getMemValue(3) * critter.complexity() * Constants.GROW_COST;
        int currEnergy = critter.getMemValue(4);
        if(currEnergy < cost){
            world.deadCritter(critter);
            return false;
        }

        critter.setMem(4, currEnergy - cost);
        critter.setMem(3, critter.getMemValue(3) + 1);
        return true;
    }
    public boolean bud ()
    {
        int newEnergy = critter.getMemValue(4) - critter.complexity() * Constants.BUD_COST;
        if(newEnergy < 0){
            world.deadCritter(critter);
            return false;
        }

        critter.setMem(4, newEnergy);

        Tile[][] tiles = world.getTiles();
        int r = critter.getRow();
        int c = critter.getColumn();
        switch(critter.getDirection()){
            case 0:
                c = c;
                r = r + 2;
            case 1:
                c = c - 1;
                r = r + 1;
            case 2:
                c = c - 1;
                r = r - 1;
            case 3:
                c = c;
                r = r - 2;
            case 4:
                c = c + 1;
                r = r - 1;
            case 5:
                c = c + 1;
                r = r + 1;
        }

        if( c < 0 || r < 0 || c >= tiles[0].length || r >= tiles.length && ( tiles[r][c] != null
                && tiles[r][c].getIsRock() || tiles[r][c].getIsFood() || tiles[r][c].getIsCritter())){
            if(newEnergy == 0) world.deadCritter(critter);
            return false;
        }

        Program clonedAST = (Program) critter.getProgram().clone();

        while(Math.random() < 1/4){
            int selecter = (int) Math.random() * clonedAST.size();
            Node mutatedNode = clonedAST.nodeAt(selecter);

            int mutation = (int) (Math.random() * 6);

            switch(mutation){
                case 0:
                    Mutation remove = MutationFactory.getRemove();
                    remove.apply(clonedAST, mutatedNode);
                case 1:
                    Mutation swap = MutationFactory.getSwap();
                    swap.apply(clonedAST, mutatedNode);
                case 2:
                    Mutation replace = MutationFactory.getReplace();
                    replace.apply(clonedAST, mutatedNode);
                case 3:
                    Mutation transform = MutationFactory.getTransform();
                    transform.apply(clonedAST, mutatedNode);
                case 4:
                    Mutation insert = MutationFactory.getInsert();
                    insert.apply(clonedAST, mutatedNode);
                case 5:
                    Mutation duplicate = MutationFactory.getDuplicate();
                    duplicate.apply(clonedAST, mutatedNode);
            }
        }

        int[] mem = new int[critter.getMemory().length];
        for(int i=0; i< mem.length; i++){
            if(i==3) mem[3] = 1;
            else if( i == 4) mem[4] = Constants.INITIAL_ENERGY;
            else if(i == 6) mem[6] = 0;
            else if(i >= 7) mem[i] = 0;
            else {
                mem[i] = critter.getMemValue(i);
            }
        }

        world.addCritter(critter.getSpecies(), mem, clonedAST, r, c, critter.getDirection() );

        if(newEnergy == 0) world.deadCritter(critter);
        return true;

    }
    public boolean mate ()
    {
        int cost = critter.complexity() * Constants.MATE_COST;
        int newEnergy = critter.getMemValue(4) - cost;
        if(newEnergy < 0){
            world.deadCritter(critter);
            return false;
        }
        critter.setMem(4, newEnergy);

        Tile[][] tiles = world.getTiles();
        int r = critter.getRow();
        int c = critter.getColumn();
        switch(critter.getDirection()){
            case 0:
                c = c;
                r = r + 2;
            case 1:
                c = c - 1;
                r = r + 1;
            case 2:
                c = c - 1;
                r = r - 1;
            case 3:
                c = c;
                r = r - 2;
            case 4:
                c = c + 1;
                r = r - 1;
            case 5:
                c = c + 1;
                r = r + 1;
        }

        if(c < 0 || r < 0 || c >= tiles[0].length || r >= tiles.length
                || tiles[r][c].getIsRock() || tiles[r][c].getIsFood() || !tiles[r][c].getIsCritter()){
            if(newEnergy == 0) world.deadCritter(critter);
            return false;
        }

        Critter mate = tiles[r][c].getCritter();

        boolean behindMate = true;

        int r2 = r;
        int c2 = c;

        switch(critter.getDirection()){
            case 0:
                c2 = c2;
                r2 = r2 + 2;
            case 1:
                c2 = c2 - 1;
                r2 = r2 + 1;
            case 2:
                c2 = c2 - 1;
                r2 = r2 - 1;
            case 3:
                c2 = c2;
                r2 = r2 - 2;
            case 4:
                c2 = c2 + 1;
                r2 = r2 - 1;
            case 5:
                c2 = c2 + 1;
                r2 = r2 + 1;
        }

        if(c < 0 || r < 0 || c >= tiles[0].length || r >= tiles.length
                || tiles[r][c].getIsRock() || tiles[r][c].getIsFood() || tiles[r][c].getIsCritter()){
            behindMate = false;
        }

        boolean behindSelf = true;

        r = critter.getRow();
        c = critter.getColumn();
        switch( (critter.getDirection() + 3) % 6){
            case 0:
                c = c;
                r = r + 2;
            case 1:
                c = c - 1;
                r = r + 1;
            case 2:
                c = c - 1;
                r = r - 1;
            case 3:
                c = c;
                r = r - 2;
            case 4:
                c = c + 1;
                r = r - 1;
            case 5:
                c = c + 1;
                r = r + 1;
        }

        if(c < 0 || r < 0 || c >= tiles[0].length || r >= tiles.length
                || tiles[r][c].getIsRock() || tiles[r][c].getIsFood() || tiles[r][c].getIsCritter()){
            behindSelf = false;
        }

        if( !behindMate && !behindSelf) return false;

        if(mate.isMating() == false) {
            critter.setMating(true);
            return false;
        }
        else{
            int matedRow;
            int matedColumn;
            int matedDirection;
            if(behindMate && behindSelf){
                if(Math.random() > 0.5) behindMate = false;
                else behindSelf = false;
            }
            if(!behindMate && behindSelf){
                matedRow = r;
                matedColumn = c;
                matedDirection = critter.getDirection();
            }
            else{
                matedRow = r2;
                matedColumn = c2;
                matedDirection = mate.getDirection();
            }

            String newSpeciesName;
            if(Math.random() > 0.5) newSpeciesName = critter.getSpecies();
            else newSpeciesName = mate.getSpecies();

            int size;
            if(Math.random() < 0.5){
                size = critter.getMemValue(0);
            }
            else{
                size = mate.getMemValue(0);
            }
            int[] mem = new int[size];
            for(int i=0; i< mem.length; i++){
                if(i==3) mem[3] = 1;
                else if( i == 4) mem[4] = Constants.INITIAL_ENERGY;
                else if(i == 6) mem[6] = 0;
                else if(i >= 7) mem[i] = 0;
                else {
                    if(Math.random() > 0.5) mem[i] = critter.getMemValue(i);
                    else mem[i] = mate.getMemValue(i);
                }
            }

            world.addCritter(newSpeciesName, mem, matedCritter(critter, mate), matedRow, matedColumn, matedDirection);

            critter.setMating(false);
            mate.setMating(false);
        }

        if(critter.getMemValue(4) == 0) world.deadCritter(critter);

        return true;

    }

    public Program matedCritter(Critter mate1, Critter mate2){

        double pickGenomeSize = Math.random();

        Program mate1AST = mate1.getProgram();
        Program mate2AST = mate2.getProgram();

        Program newProgram = new ProgramImpl();
        int size;
        if(pickGenomeSize < 0.5){
            size = mate1AST.size();
        }
        else{
            size = mate2AST.size();
        }

        for(int i=0; i<size; i++){
            if(i > mate1AST.size()) newProgram.getChildren().add(mate2AST.getChildren().get(i).clone());
            else if(i > mate2AST.size()) newProgram.getChildren().add(mate1AST.getChildren().get(i).clone());
            else {
                if(Math.random() > 0.5) newProgram.getChildren().add(mate1AST.getChildren().get(i).clone());
                else newProgram.getChildren().add(mate2AST.getChildren().get(i).clone());
            }
        }

        return newProgram;
    }


    public boolean serve (int value)
    {
        int energySpent = critter.getMemValue(3) + value;

        if(energySpent > critter.getMemValue(4)) {
            world.deadCritter(critter);
            return false;
        }

        critter.setMem(4, critter.getMemValue(4) - energySpent);

        Tile[][] tiles = world.getTiles();
        int r = critter.getRow();
        int c = critter.getColumn();
        switch(critter.getDirection()){
            case 0:
                c = c;
                r = r + 2;
            case 1:
                c = c - 1;
                r = r + 1;
            case 2:
                c = c - 1;
                r = r - 1;
            case 3:
                c = c;
                r = r - 2;
            case 4:
                c = c + 1;
                r = r - 1;
            case 5:
                c = c + 1;
                r = r + 1;
        }

        if(c < 0 || r < 0 || c >= tiles[0].length || r >= tiles.length
                || tiles[r][c].getIsRock() || tiles[r][c].getIsCritter()){
            if(critter.getMemValue(4) == 0) world.deadCritter(critter);
            return false;
        }

        tiles[r][c] = new Tile(tiles[r][c].getNumFood() + value);

        if(critter.getMemValue(4) == 0){
            world.deadCritter(critter);
        }

        return true;
    }


}
