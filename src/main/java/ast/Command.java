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

    public Node clone()
    {
        Command cloned = new Command();
        for(Node child : this.children)
        {
            cloned.getChildren().add(child.clone());
        }
        return cloned;
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

    public void replace(Update update, Update newUpdate)
    {
        for (int i = 0; i < children.size(); i++)
        {
            if (children.get(i) == update)
            {
                children.set(i, newUpdate);
                break;
            }
        }
    }
    public void replace(Action action, Action newAction)
    {
        int size = children.size();
        if (children.get(size - 1) == action)
        {
            children.set(size - 1, newAction);
        }
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
