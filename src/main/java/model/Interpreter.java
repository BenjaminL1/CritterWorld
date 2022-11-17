package model;

import ast.*;
import ast.Number;
import org.eclipse.jetty.http.HttpTokens;
import parse.TokenType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Interpreter
{
    private World world;
    private Critter critter;
    private int numRulesRun;

    public Interpreter(World world, Critter critter)
    {
        this.world = world;
        this.critter = critter;
        this.numRulesRun = 0;
    }

    public boolean interpret()
    {
        while(numRulesRun < Constants.MAX_RULES_PER_TURN)
        {
            if (interpretProgram(critter.getProgram()) == 0){
                System.out.println(critter.getLastRuleString());
                return true;
            }
        }

        CritterAction critterAction = new CritterAction(world, critter);
        critterAction.perform(TokenType.WAIT);
        return false;
    }
    public int interpretProgram(Program program)
    {
        Rule lastRule = null;
        for(Node rule : program.getChildren())
        {
            int i = interpretRule(rule);
            if(i == 0)
            {
                lastRule = (Rule) rule;
                critter.setLastRuleString(lastRule.toString());
                return 0;
            }
            else if(i == 1)
            {
                lastRule = (Rule) rule;
                critter.setLastRuleString(lastRule.toString());
            }
            numRulesRun ++;
        }

        if(lastRule == null)
        {
            return -1;
        }
        else return 1;
    }
    public int interpretRule(Node node)
    {
        Rule rule = (Rule) node;
        boolean updated = false;
        if(interpretCondition(rule.getCondition()))
        {
            critter.setLastRuleString(node.toString());
            for(Node child : rule.getCommand().getChildren())
            {
                if(child.getClass() == Update.class)
                {
                    interpretUpdate(child);
                    System.out.println(critter.getSpecies() + ": " + child.toString());
                    updated = true;
                }
                else if(child.getClass() == Action.class)
                {
                    System.out.println(critter.getSpecies() + ":" + child.toString());
                    if(interpretAction(child)) return 0;
                    else System.out.println("failed action bozo");
                }
            }
        }
        if(updated) return 1;
        else return -1;
    }
    public boolean interpretUpdate(Node update)
    {
        Update curr = (Update) update;
        int memType = interpretExpression(curr.getMemType().getExpr());
        int updateValue = interpretExpression(curr.getExpr());

        if(memType >= critter.getMemValue(0)){
            System.out.println("not valid mem value bozo");
            return false;
        }
        critter.setMem(memType, updateValue);


        return true;
    }
    public boolean interpretAction(Node node)
    {
        Action action = (Action) node;
        TokenType tt = action.getName();
        CritterAction critterAction = new CritterAction(world, critter);

        if(tt == TokenType.SERVE){
            return critterAction.serve(interpretExpression(action.getExpr()));
        }
        else{
            return critterAction.perform(tt);
        }
    }
    public boolean interpretCondition(Node condition){

        if(condition.getClass() == Relation.class){
            return interpretRelation(condition);
        }

        BinaryCondition bc = (BinaryCondition) condition;
        boolean left = false;
        boolean right = false;
        BinaryCondition.Operator op = bc.getOp();
        if(bc.getLeft().getClass() == BinaryCondition.class){
            left = interpretCondition(bc.getLeft());
        }
        else if(bc.getLeft().getClass() == Relation.class){
            left = interpretRelation(bc.getLeft());
        }
        if(bc.getRight().getClass() == BinaryCondition.class){
            right = interpretCondition(bc.getRight());
        }
        else if(bc.getRight().getClass() == Relation.class){
            right = interpretRelation(bc.getRight());
        }

        if(op == BinaryCondition.Operator.AND){
            return left && right;
        }
        else return left || right;


    }

    public boolean interpretRelation(Node node){
        Relation relation = (Relation) node;
        int left = interpretExpression(relation.getLeft());
        int right = interpretExpression(relation.getRight());
        String rel = relation.getOperator().toString();

        switch(rel){
            case("<"):
                return (left < right);
            case("<="):
                return (left <= right);
            case("="):
                return (left == right);
            case(">="):
                return (left >= right);
            case(">"):
                return (left > right);
            case("!="):
                return (left != right);
            default:
                System.out.println(rel + " is not a valid relation operator");
                return false;
        }
    }

    public int interpretExpression(Node node){
        Expr expr = (Expr) node;
        if(expr.getClass() == BinaryOp.class){
            BinaryOp binOp = (BinaryOp) expr;
            int left = interpretExpression((binOp.getLeft()));
            int right = interpretExpression(binOp.getRight());
            String op = binOp.getOp().toString();

            switch(op){
                case("+"):
                    return left + right;
                case("-"):
                    return left - right;
                case("*"):
                    return left * right;
                case("/"):
                    return (left / right);
                case("mod"):
                    return (left % right);
                default:
                    System.out.println(op + " is not a valid binary operator stoopid");
                    return -1;

            }
        }

        else if(expr.getClass() == NegativeExpr.class){
            NegativeExpr negged = (NegativeExpr) expr;
            return -1 * interpretExpression(negged.getRight());
        }

        else if(expr.getClass() == Number.class){
            Number num = (Number) expr;
            return num.getNum();
        }

        else if(expr.getClass() == Mem.class){
            Mem mem = (Mem) expr;
            return critter.getMemValue(interpretExpression(mem.getExpr()));
        }

        else if(expr.getClass() == AheadSensor.class){
            AheadSensor as = (AheadSensor) expr;
            return interpretAheadSensor(interpretExpression(as.getExpr()));
        }
        else if(expr.getClass() == RandomSensor.class){
            RandomSensor rs = (RandomSensor) expr;
            return (int) (Math.random() * interpretExpression(rs.getExpr()));
        }

        return 0;
    }

    // TODO account for out of bounds indices
    public int interpretNearbySensor(int dir)
    {
        dir = Math.abs((dir + critter.getDirection())) % 6;
        if (dir == 0)
        {
            int info = world.getTerrainInfo(critter.getColumn(), critter.getRow() - 2);
            if(info == 0) return 0;
            else if(info < -1) return info;
            else if(info == -1) return -1;
            else
            {
                Tile[][] tiles = world.getTiles();
                Critter crit = tiles[critter.getColumn()][critter.getRow() + 2].getCritter();
                return crit.getMemValue(3)*1000 + crit.getMemValue(6)*10 +  critter.getDirection();
            }
        }
        else if (dir == 1)
        {
            int info = world.getTerrainInfo(critter.getColumn() + 1, critter.getRow() - 1);
            if(info == 0) return 0;
            else if(info < -1) return info;
            else if(info == -1) return -1;
            else
            {
                Tile[][] tiles = world.getTiles();
                Critter crit = tiles[critter.getColumn()][critter.getRow() + 2].getCritter();
                return crit.getMemValue(3)*1000 + crit.getMemValue(6)*10 +  crit.getDirection();

            }
        }
        else if (dir == 2)
        {
            int info = world.getTerrainInfo(critter.getColumn() + 1, critter.getRow() + 1);
            if(info == 0) return 0;
            else if(info < -1) return info;
            else if(info == -1) return -1;
            else{
                Tile[][] tiles = world.getTiles();
                Critter crit = tiles[critter.getColumn()][critter.getRow() + 2].getCritter();
                return crit.getMemValue(3)*1000 + crit.getMemValue(6)*10 +  crit.getDirection();

            }
        }
        else if (dir == 3)
        {
            int info = world.getTerrainInfo(critter.getColumn(), critter.getRow() + 2);
            if(info == 0) return 0;
            else if(info < -1) return info;
            else if(info == -1) return -1;
            else{
                Tile[][] tiles = world.getTiles();
                Critter crit = tiles[critter.getColumn()][critter.getRow() + 2].getCritter();
                return crit.getMemValue(3)*1000 + crit.getMemValue(6)*10 +  crit.getDirection();

            }
        }
        else if (dir == 4)
        {
            int info = world.getTerrainInfo(critter.getColumn() - 1, critter.getRow() + 1);
            if(info == 0) return 0;
            else if(info < -1) return info;
            else if(info == -1) return -1;
            else{
                Tile[][] tiles = world.getTiles();
                Critter crit = tiles[critter.getColumn()][critter.getRow() + 2].getCritter();
                return crit.getMemValue(3)*1000 + crit.getMemValue(6)*10 +  crit.getDirection();

            }
        }
        else if (dir == 5)
        {
            int info = world.getTerrainInfo(critter.getColumn() - 1, critter.getRow() - 1);
            if(info == 0) return 0;
            else if(info < -1) return info;
            else if(info == -1) return -1;
            else
            {
                Tile[][] tiles = world.getTiles();
                Critter crit = tiles[critter.getColumn()][critter.getRow() + 2].getCritter();
                return crit.getMemValue(3)*1000 + crit.getMemValue(6)*10 +  crit.getDirection();

            }
        }
        else
        {
            return 0;
        }
    }

    // TODO account for out of bounds indices
    public int interpretAheadSensor(int dist)
    {
        dist = Math.max(dist, 0);
        int dir = critter.getDirection();
        int row = critter.getRow();
        int column = critter.getColumn();
        int info;
        if (dir == 0)
        {
            row -= dist * 2;
            info = world.getTerrainInfo(column, row);
        }
        else if (dir == 1)
        {
            row -= dist;
            column += dist;
            info = world.getTerrainInfo(column, row);
        }
        else if (dir == 2)
        {
            row += dist;
            column += dist;
            info = world.getTerrainInfo(column, row);
        }
        else if (dir == 3)
        {
            row += dist * 2;
            info = world.getTerrainInfo(column, row);
        }
        else if (dir == 4)
        {
            row += dist;
            column -= dist;
            info = world.getTerrainInfo(column, row);
        }
        else
        {
            row -= dist;
            column -= dist;
            info = world.getTerrainInfo(column, row);
        }

        if (info <= 0)
        {
            return info;
        }
        else
        {
            Tile[][] tiles = world.getTiles();
            Critter target = tiles[row][column].getCritter();
            return target.getMemValue(3) * 1000 + target.getMemValue(6) * 10 + target.getDirection();
        }
    }

    public int interpretSmellSensor()
    {
        Tile[][] tiles = world.getTiles();
        int row = critter.getRow();
        int column = critter.getColumn();
        int dir = critter.getDirection();
        LinkedList<int[]> tileSearch = new LinkedList<int[]>();
        tileSearch.add(new int[]{row - 2, column});
        tileSearch.add(new int[]{row - 1, column + 1});
        tileSearch.add(new int[]{row + 1, column + 1});
        tileSearch.add(new int[]{row + 2, column});
        tileSearch.add(new int[]{row + 1, column - 1});
        tileSearch.add(new int[]{row - 1, column - 1});
        int[] foodCoordinates = findNearestFood(tiles, tileSearch);
        int cx = column;
        int cy = tiles[0].length - 1 - row;
        int fx = foodCoordinates[1];
        int fy = foodCoordinates[0];
        int dist = Math.max(Math.abs(fx - cx), Math.abs(fx - cx + fy - cy) / 2);
        dist = Math.max(dist, Math.abs(fx - cx - fy + cy));
        if (foodCoordinates[0] == -1 || dist > Constants.MAX_SMELL_DISTANCE)
        {
            return 1000000;
        }

        // TODO fix int division
        // based off assumption row is first
        int tanned = (int) Math.atan((double) (fx - cx) / (double) (fy - cy));
        if( (foodCoordinates[1] - row) < 0)
        {
            tanned += 180;
        }

        int dirOfFood = critter.getDirection();
        if(tanned >= 0 && tanned < 60)
        {
            dirOfFood = 1 - dirOfFood + 6;
        }
        else if(tanned >= 60 && tanned < 120)
        {
            dirOfFood = 0 - dirOfFood + 6;
        }
        else if(tanned >= 120 && tanned < 180)
        {
            dirOfFood = 5 - dirOfFood + 6;
        }
        else if(tanned >= 180 && tanned < 240)
        {
            dirOfFood = 4 - dirOfFood + 6;
        }
        else if(tanned >= 240 && tanned < 300)
        {
            dirOfFood = 3 - dirOfFood + 6;
        }
        else if(tanned >= 300 && tanned < 360)
        {
            dirOfFood = 2 - dirOfFood + 6;
        }
        dirOfFood %= 6;
        return 1000 * dist + dirOfFood;
    }

    public int[] findNearestFood(Tile[][] tiles, LinkedList<int[]> tileSearch)
    {
        while (tileSearch.peek() != null)
        {
            int[] coordinates = tileSearch.poll();
            if (tiles[coordinates[0]][coordinates[1]].getIsFood())
            {
                return coordinates;
            }
            int rowSize = tiles.length;
            int columnSize = tiles[0].length;
            int row = coordinates[0];
            int column = coordinates[1];
            if (row - 2 >= 0)
            {
                tileSearch.add(new int[]{row - 2, column});
            }
            if (row - 1 >= 0 && column + 1 < columnSize)
            {
                tileSearch.add(new int[]{row - 1, column + 1});
            }
            if (row + 1 < rowSize && column + 1 < columnSize)
            {
                tileSearch.add(new int[]{row + 1, column + 1});
            }
            if (row + 1 < rowSize)
            {
                tileSearch.add(new int[]{row + 2, column});
            }
            if (row + 1 < rowSize && column - 1 >= 0)
            {
                tileSearch.add(new int[]{row + 1, column - 1});
            }
            if (row - 1 >= 0 && column - 1 >= 0)
            {
                tileSearch.add(new int[]{row - 1, column - 1});
            }
        }
        return new int[]{-1, -1};
    }

    public int interpretRandomSensor(RandomSensor rs, int n)
    {
        return n < 2 ? 0 : (int) (Math.random() * n);
    }


    public void setMem(int memIndex, int changeNumber)
    {
        critter.setMem(memIndex, changeNumber);
    }
}
