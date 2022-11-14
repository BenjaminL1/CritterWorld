package model;

import ast.*;
import ast.Number;
import parse.TokenType;

public class Interpreter
{
    private World world;
    private Critter critter;

    public Interpreter(World world, Critter critter)
    {
        this.world = world;
        this.critter = critter;
    }

    public boolean interpret()
    {
        for(int i=0; i<1000; i++)
        {
            if(interpretProgram(critter.getProgram()) == 0) break;
        }
        // TODO
        return false;
    }
    public int interpretProgram(Program program)
    {
        // TODO uncomment
//        for(Node rule : program.getChildren()){
//            public interpretRule();
//        }
        // TODO
        return 0;
    }
    public int interpretRule(Node node){
        Rule rule = (Rule) node;
        boolean updated = false;
        if(interpretCondition(rule.getCondition())){
            for(Node child : rule.getCommand().getChildren()){
                if(child.getClass() == Update.class) {
                    interpretUpdate(child);
                    updated = true;
                }
                else if(child.getClass() == Action.class) {
                    interpretAction(child);
                    return 0;
                }
            }
        }
        if(updated) return 1;
        else return -1;
    }
    public boolean interpretUpdate(Node update){
        Update curr = (Update) update;
        int memType = interpretExpression(curr.getMemType().getExpr());
        int updateValue = interpretExpression(curr.getExpr());

        switch(memType){
            case 0:
                critter.setMem(0, updateValue);
                break;
            case 1:
                critter.setMem(1, updateValue);
                break;
            case 2:
                critter.setMem(2, updateValue);
                break;
            case 3:
                critter.setMem(3, updateValue);
                break;
            case 4:
                critter.setMem(4, updateValue);
                break;
            case 5:
                critter.setMem(5, updateValue);
                break;
            default:
                System.out.println( memType + " is not a valid mem index");
                return false;
        }

        return true;
    }
    public boolean interpretAction(Node action){
        //create a critter action
        // TODO
        return false;
    }
    public boolean interpretCondition(Node condition){
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
            return interpretAheadSensor(as, interpretExpression(as.getExpr()));
        }
        // TODO
        return 0;
    }

    public int interpretAheadSensor(AheadSensor as, int dist)
    {
        // TODO
        return 0;
    }

    public void setMem(int memIndex, int changeNumber)
    {

    }
}
