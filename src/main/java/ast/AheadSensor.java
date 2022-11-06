package ast;

public class AheadSensor extends Sensor
{
    Expr e;
    public AheadSensor(Expr e)
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
