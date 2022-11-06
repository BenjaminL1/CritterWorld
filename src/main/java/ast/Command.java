package ast;

import java.util.LinkedList;
import java.util.List;

public class Command extends AbstractNode
{

    private LinkedList<Node> children;

    public Command()
    {
        children = new LinkedList<>();
    }
    public List<Node> getChildren()
    {
        return children;
    }

    public void add(Node node)
    {
        children.add(node);
    }

    public void remove(Node node)
    {
        children.remove(node);
    }

    @Override
    public String toString()
    {
        return null;
    }

    @Override
    public NodeCategory getCategory()
    {
        return null;
    }

    @Override
    public boolean classInv()
    {
        return false;
    }

    public void accept(Visitor v)
    {
        v.visit(this);
    }
}
