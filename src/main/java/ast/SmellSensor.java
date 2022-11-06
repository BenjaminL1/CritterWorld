package ast;

public class SmellSensor extends Sensor
{
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
