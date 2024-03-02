package cool.compiler.classes;

import cool.compiler.ASTVisitor;
import cool.parser.CoolParser;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class Block extends ExpressionNode {
    List<ExpressionNode> expressionNodes;

    public Block(CoolParser.BlockContext context, List<ExpressionNode> exprListNode, Token token){
        super(token, context);
        this.expressionNodes = exprListNode;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public List<ExpressionNode> getExpressionNodes() {
        return expressionNodes;
    }
}
