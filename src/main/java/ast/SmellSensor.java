package ast;

public class SmellSensor extends Sensor
{
    @Override
    public Node clone(){
        return new SmellSensor();
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
