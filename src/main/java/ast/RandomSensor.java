package ast;

public class RandomSensor extends Sensor
{
    Expr e;
    public RandomSensor(Expr e)
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
