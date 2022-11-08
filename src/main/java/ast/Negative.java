package ast;

import java.util.List;

public class Negative extends AbstractNode
{
    @Override
    public List<Node> getChildren() {
        return null;
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb) {
        sb.append("-");
        return sb;
    }

    @Override
    public Node clone() {
        return new Negative();
    }

    @Override
    public NodeCategory getCategory() {
        return null;
    }

    @Override
    public void accept(Visitor v)
    {
        v.visit(this);
    }


    @Override
    public boolean classInv()
    {
        return true;
    }
}
