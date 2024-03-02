package cool.compiler.classes;

import cool.compiler.ASTVisitor;
import cool.parser.CoolParser;
import org.antlr.v4.runtime.Token;

public class Assignment extends ExpressionNode {
    Token id;
    ExpressionNode expressionNode;
    Token from;

    public Token getFrom() {
        return from;
    }

    public Assignment(CoolParser.AssignmentContext context, ExpressionNode expressionNode, Token id, Token token){
        super(token, context);
        this.id = id;
        this.expressionNode = expressionNode;
        this.from = context.expr().start;
    }

    public Token getId() {
        return id;
    }

    public void setId(Token id) {
        this.id = id;
    }

    public ExpressionNode getExpressionNode() {
        return expressionNode;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
