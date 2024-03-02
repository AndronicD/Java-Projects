package cool.compiler.classes;

import cool.compiler.ASTVisitor;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class Unary extends ExpressionNode { //tilde, not, isvoid, paranteze, assignment_in_block
    ExpressionNode expr;
    Token op;

    public Token getOp() {
        return op;
    }

    public Unary(ParserRuleContext context, ExpressionNode expr, Token op, Token token) {
        super(token, context);
        this.op = op;
        this.expr = expr;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public ExpressionNode getExpr() {
        return expr;
    }
}
