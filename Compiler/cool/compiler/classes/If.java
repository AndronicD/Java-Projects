package cool.compiler.classes;

import cool.compiler.ASTVisitor;
import cool.parser.CoolParser;
import org.antlr.v4.runtime.Token;

public class If extends ExpressionNode {
    ExpressionNode condition;
    ExpressionNode thenBranch;
    ExpressionNode elseBranch;
    Token from;

    public If(CoolParser.IfContext context, Token from, ExpressionNode condition, ExpressionNode thenBranch, ExpressionNode elseBranch, Token token){
        super(token, context);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
        this.from = from;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public ExpressionNode getCondition() {
        return condition;
    }

    public ExpressionNode getThenBranch() {
        return thenBranch;
    }

    public ExpressionNode getElseBranch() {
        return elseBranch;
    }

    public Token getFrom() {return from;}
}
