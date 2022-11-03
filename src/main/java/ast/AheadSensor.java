package ast;

public class AheadSensor extends Sensor
{
    Expr e;
    public AheadSensor(Expr e)
    {
        this.e = e;
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
