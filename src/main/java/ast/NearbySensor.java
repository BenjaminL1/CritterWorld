package ast;

import java.util.List;

public class NearbySensor extends Sensor
{
    Expr e;
    public NearbySensor(Expr e)
    {
        this.e = e;
    }

    public Expr getExpr()
    {
        return e;
    }

    public void changeExpr(Expr e)
    {
        this.e = e;
    }

    @Override
    public Node clone(){
        Expr clonedExpr = (Expr) this.e.clone();
        NearbySensor cloned = new NearbySensor(clonedExpr);
        clonedExpr.setParent(cloned);
        return cloned;
    }

    @Override
    public List<Node> getChildren() {
        return null;
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb) {
        sb.append("nearby[");
        e.prettyPrint(sb);
        sb.append("]");
        return sb;
    }

    @Override
    public void accept(Visitor v)
    {
        v.visit(this);
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public boolean classInv() {
        return false;
    }
}
