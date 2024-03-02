package cool.compiler.classes;

import cool.compiler.ASTNode;
import cool.compiler.ASTVisitor;
import cool.parser.CoolParser;
import cool.structures.symbol.IdSymbol;
import org.antlr.v4.runtime.Token;

public class LocalNode extends ASTNode {
    public Token identifier;
    public Token type;

    public ExpressionNode expression;
    public IdSymbol idSymbol;

    public LocalNode(CoolParser.LocalContext context, Token identifier, Token type, ExpressionNode expression, Token token) {
        super(token, context);
        this.identifier = identifier;
        this.type = type;
        this.expression = expression;
    }

    public IdSymbol getIdSymbol() {
        return idSymbol;
    }

    public void setIdSymbol(IdSymbol idSymbol) {
        this.idSymbol = idSymbol;
    }


    public Token getIdentifier() {
        return identifier;
    }

    public Token getType() {
        return type;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public ExpressionNode getExpression() {
        return expression;
    }
}
