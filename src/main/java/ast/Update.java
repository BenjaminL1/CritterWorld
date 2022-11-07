package ast;

import parse.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Update extends AbstractNode
{

    Mem memType;
    Expr updateValue;

    public Update(Mem memType, Expr updateValue)
    {
        this.memType = memType;
        this.updateValue = updateValue;
    }

    @Override
    public Node clone(){
        Mem clonedMem = (Mem)this.memType.clone();
        Expr clonedExpr = (Expr)this.updateValue.clone();
        Update cloned = new Update(clonedMem, clonedExpr);

        return cloned;
    }

    public Mem getMemType()
    {
        return memType;
    }

    public Expr getExpr()
    {
        return updateValue;
    }

    public void changeMemType(Mem newMem)
    {
        memType = newMem;
    }

    public void changeExpr(Expr e)
    {
        updateValue = e;
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

    public List<Node> getChildren(){
        List<Node> list = new ArrayList<Node>();
        list.add(memType);
        list.add(updateValue);
        return list;
    }

    public void accept(Visitor v)
    {
        v.visit(this);
    }
}
