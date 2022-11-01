package ast;

import parse.TokenType;

public class Action extends AbstractNode{

    TokenType name;
    Expr value;

    public Action(TokenType name){
        this.name = name;
    }

    public Action(TokenType name, Expr value){
        this.name = name;
        this.value = value;
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
