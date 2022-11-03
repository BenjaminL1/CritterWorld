package ast;

public class NearbySensor extends Sensor
{
    Expr e;
    public NearbySensor(Expr e)
    {
        this.e = e;
    }

    public Expr getExpression(Expr e)
    {
        return e;
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
