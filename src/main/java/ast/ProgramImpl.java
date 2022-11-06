package ast;

import cms.util.maybe.Maybe;

import java.util.ArrayList;

/** A data structure representing a critter program. */
public class ProgramImpl extends AbstractNode implements Program
{
    private ArrayList<Rule> rules;

    public ProgramImpl()
    {
        rules = new ArrayList<Rule>();
    }

    @Override
    public Node clone() {
        ProgramImpl cloned = new ProgramImpl();
        for(Node rule : rules){
            cloned.getChildren().add(rule.clone());
        }
        return cloned;
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb){
        
    }

    public ArrayList<Rule> getRules()
    {
        return rules;
    }

    public void addRule(Rule rule)
    {
        rules.add(rule);
    }

    @Override
    public void accept(Visitor v)
    {
        v.visit(this);
    }

    public int size()
    {
        return rules.size();
    }

    public void remove(Rule rule)
    {
        rules.remove(rule);
    }

    @Override
    public Program mutate()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Maybe<Program> mutate(int index, Mutation m) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Maybe<Node> findNodeOfType(NodeCategory type) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NodeCategory getCategory() {
        return NodeCategory.PROGRAM;
    }

    @Override
    public String toString(){
        // TODO Auto-generated method stub
        return null;
    }

    public boolean classInv() {
        // TODO
        return false;
    }
}
