package ast;

import parse.TokenType;

public class Update extends AbstractNode{

    Expr memType;
    Expr updateValue;

    public Update(Expr memType, Expr updateValue){
        this.memType = memType;
        this.updateValue = updateValue;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public NodeCategory getCategory() {
        return null;
    }

    @Override
    public boolean classInv() {
        return false;
    }
}
