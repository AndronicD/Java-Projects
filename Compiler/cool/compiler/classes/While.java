package cool.compiler.classes;

import cool.compiler.ASTVisitor;
import cool.parser.CoolParser;
import org.antlr.v4.runtime.Token;

public class While extends ExpressionNode {
    ExpressionNode condition;
    ExpressionNode body;
    Token from;

    public While(CoolParser.WhileContext context, Token from, ExpressionNode condition, ExpressionNode body, Token token){
        super(token, context);
        this.condition = condition;
        this.body = body;
        this.from = from;
    }

    public ExpressionNode getCondition() {
        return condition;
    }

    public ExpressionNode getBody() {
        return body;
    }

    public Token getFrom() {return from;}

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
