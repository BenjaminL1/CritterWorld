package ast;

import parse.TokenType;

public class Data extends AbstractNode{

    private TokenType data;

    public Data(TokenType data){
        this.data = data;
    }

    public TokenType getData() {
        return data;
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
