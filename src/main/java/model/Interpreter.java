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
            case("")
        }
    }
    public boolean interpretAction(Node action){

    }
    public boolean interpretCondition(Node condition){

    }

    public boolean interpretBinaryCondition(Node condition){

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
