package ast;

import java.util.LinkedList;

/** A representation of a critter rule. */
public class Rule extends AbstractNode {

    private Condition condition;
    private Command command;

    public Rule(Condition condition, Command command)
    {
        this.condition = condition;
        this.command = command;
    }

    public Condition getCondition()
    {
        return condition;
    }

    public Command getCommand()
    {
        return command;
    }

    @Override
    public NodeCategory getCategory() {
        return NodeCategory.RULE;
    }

    @Override
    public String toString(){
        // TODO Auto-generated method stub
        return null;
    }

    public boolean classInv() {
        // TODO implement/override
        return false;
    }
}
