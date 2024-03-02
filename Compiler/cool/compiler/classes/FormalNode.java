package cool.compiler.classes;

import cool.compiler.ASTNode;
import cool.compiler.ASTVisitor;
import cool.parser.CoolParser;
import cool.structures.Symbol;
import cool.structures.scope.ClassScope;
import cool.structures.symbol.IdSymbol;
import org.antlr.v4.runtime.Token;

public abstract class FormalNode extends ASTNode {
    private Token id;
    private Token type;
    private IdSymbol idSymbol;

    public FormalNode(CoolParser.FormalContext context, Token id, Token type, Token token) {
        super(token, context);
        this.id = id;
        this.type = type;
    }

    public Token getId() {
        return id;
    }

    public Token getType() {
        return type;
    }

    public IdSymbol getIdSymbol() {return idSymbol;}

    public void setIdSymbol(IdSymbol idSymbol) {this.idSymbol = idSymbol;}

    public void setIdSymbolGeneral(Symbol symbolGeneral) {this.idSymbol.setClassScope((ClassScope) symbolGeneral);}

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}