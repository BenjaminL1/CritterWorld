package ast;

import java.util.List;
import cms.util.maybe.Maybe;

public abstract class AbstractNode implements Node
{
    private Node parent;

    public void addParentPointer(Node parent)
    {
        this.parent = parent;
    }

    public Node getParent()
    {
        return parent;
    }


    @Override
    public int size()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Node nodeAt(int index)
    {
        if(index > this.size()) throw new IndexOutOfBoundsException();

        if(index == 0) return this;

        if(this.getChildren() == null) return null;

        for(Node node : this.getChildren()){
            index--;
            Node curr = node.nodeAt(index);
            if(index == 0) return curr;
        }
        return null;
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Node clone()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Node> getChildren(){
        return null;
    }

    /**
     * You can remove this method if you don't like it.
     *
     * Sets the parent of this {@code Node}.
     *
     * @param p the node to set as this {@code Node}'s parent.
     */
    public void setParent(Node p)
    {
        // TODO Auto-generated method stub
    }

    /**
     * @return the String representation of the tree rooted at this {@code
     * Node}.
     */
    public abstract String toString();

}
