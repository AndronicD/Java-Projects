package cool.compiler.classes;

import cool.compiler.ASTVisitor;
import cool.parser.CoolParser;
import org.antlr.v4.runtime.Token;

public class New extends ExpressionNode {
    Token id;
    public New(CoolParser.NewContext context, Token id, Token token) {
        super(token, context);
        this.id = id;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Token getId() {
        return id;
    }
}
