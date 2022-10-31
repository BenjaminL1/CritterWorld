package ast;

import java.util.LinkedList;

/** A representation of a critter rule. */
public class Rule extends AbstractNode {

    LinkedList<Node> children = new LinkedList<Node>();
    // ??? Do we need to store the "-->" or can we just store the conditions and assume the "-->" lies between the last condition
    //     and the command?

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
