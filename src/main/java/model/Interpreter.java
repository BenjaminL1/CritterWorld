package model;

import ast.*;

public class Interpreter
{
    private World world;
    private Critter critter;

    public Interpreter(World world, Critter critter){
        this.world = world;
        this.critter = critter;
    }

    public boolean interpret(){
        for(int i=0; i<1000; i++){
            if(interpretProgram(critter.getProgram()) == 0) break;
        }
    }
    public int interpretProgram(Program program){
        for(Node rule : program.getChildren()){
            public interpretRule();
        }
    }
    public boolean interpretRule(Node node){
        Rule rule = (Rule) node;
        if(interpretCondition(rule.getCondition())){
            for(Node child : rule.getCommand().getChildren()){
                if(child.getClass() == Update.class) interpretUpdate(child);
                else if(child.getClass() == Action.class) {
                    interpretAction(child);
                    return 0;
                }
            }
        }
        else {
            return false;
        }
    }
    public boolean interpretUpdate(Node update){

    }
    public boolean interpretAction(Node action){

    }
    public boolean interpretCondition(Node condition){

    }
    public boolean interpretRelation(Node relation){

    }
    public int interpretExpression(Node Expr){

    }
    public int interpretMem(Node mem){

    }


    public void setMem(int memIndex, int changeNumber){

    }
}
