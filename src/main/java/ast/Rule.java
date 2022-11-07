package ast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** A representation of a critter rule. */
public class Rule extends AbstractNode {

    private Condition condition;
    private Command command;

    public Rule(Condition condition, Command command)
    {
        this.condition = condition;
        this.command = command;
    }
    @Override
    public Node clone(){
        Condition clonedCondition = (Condition)this.condition.clone();
        Command clonedCommand = (Command) this.command.clone();
        Rule cloned = new Rule(clonedCondition, clonedCommand);
        clonedCondition.setParent(cloned);
        clonedCommand.setParent(cloned);
        return cloned;
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb) {
        sb.append(condition.prettyPrint(sb));
        sb.append((" --> "));
        sb.append(command.prettyPrint(sb));

        return sb;
    }

    public Condition getCondition()
    {
        return condition;
    }

    public Command getCommand()
    {
        return command;
    }

    public List<Node> getChildren(){
        List<Node> list = new ArrayList<Node>();
        list.add(condition);
        list.add(command);
        return list;
    }

    public void changeCondition(Condition c)
    {
        condition = c;
    }

    public void changeCommand(Command c)
    {
        command = c;
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

    public void accept(Visitor v)
    {
        v.visit(this);
    }
}
