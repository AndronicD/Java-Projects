package cool.compiler.classes;

import cool.compiler.ASTVisitor;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class Arithmetic extends ExpressionNode {  // plus, minus, devide, multiply
    ExpressionNode left;
    ExpressionNode right;
    Token op;

    public Arithmetic(ParserRuleContext context, ExpressionNode left, ExpressionNode right, Token op, Token token) {
        super(token, context);
        this.op = op;
        this.left = left;
        this.right = right;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public ExpressionNode getLeft() {
        return left;
    }

    public ExpressionNode getRight() {
        return right;
    }

    public Token getOp() {
        return op;
    }
}
