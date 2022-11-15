package model;

import parse.TokenType;

public class CritterAction
{
    private World world;
    private Critter critter;

    public CritterAction (World world, Critter critter){
        this.world = world;
        this.critter = critter;
    }

    public boolean action(TokenType actionName)
    {
        String name = actionName.toString();
        switch(name){
            case ("wait"):
                return waitTurn();
            case ("forward"):
                return move(true);
            case ("back"):
                return move(false);
            case ("left"):
                return waitTurn();
            case ("right"):
                return waitTurn();
            case ("eat"):
                return waitTurn();
            case ("attack"):
                return waitTurn();
            case ("grow"):
                return waitTurn();
            case ("bud"):
                return waitTurn();
            case ("mate"):
                return waitTurn();
            case ("serve"):
                return waitTurn();

        }
    }


    public boolean waitTurn(){
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

        if(critter.getMemValue(4) == critter.getMemValue(3) * 3) {

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
            }
            if(column < 0 || row < 0 || column > tiles[0].length || row > tiles.length
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
                if(column < 0 || row < 0 || column > tiles[0].length || row > tiles.length
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
                if(column < 0 || row < 0 || column > tiles[0].length || row > tiles.length
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

        if(c < 0 || r < 0 || c > tiles[0].length || r > tiles.length || !tiles[r][c].getIsFood()) return false;

        int food = tiles[r][c].getNumFood();
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

        if(c < 0 || r < 0 || c > tiles[0].length || r > tiles.length || !tiles[r][c].getIsCritter()) return false;

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
    public boolean bud (){

    }
    public boolean mate (){

    }
    public boolean serve (){

    }


}
