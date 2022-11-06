package ast;

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
        return new NearbySensor((Expr) this.e.clone());
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
