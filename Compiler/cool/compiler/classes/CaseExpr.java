package cool.compiler.classes;

import cool.compiler.ASTVisitor;
import cool.parser.CoolParser;
import org.antlr.v4.runtime.Token;

public class CaseExpr extends ExpressionNode {
    Token id;
    Token type;
    ExpressionNode expressionNode;

    public CaseExpr(CoolParser.Case_exprContext context, ExpressionNode expressionNode, Token id, Token type, Token token){
        super(token, context);
        this.id = id;
        this.type = type;
        this.expressionNode = expressionNode;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Token getId() {
        return id;
    }

    public Token getType() {
        return type;
    }

    public ExpressionNode getExpressionNode() {
        return expressionNode;
    }
}
