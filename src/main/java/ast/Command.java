package ast;

import java.util.LinkedList;
import java.util.List;

public class Command extends AbstractNode
{

    private LinkedList<Node> children = new LinkedList<>();

    public Command(){
    }
    public List<Node> getChildren(){
        return children;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public NodeCategory getCategory() {
        return null;
    }

    @Override
    public boolean classInv() {
        return false;
    }
}
