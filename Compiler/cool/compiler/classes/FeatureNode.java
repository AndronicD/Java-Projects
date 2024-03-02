package cool.compiler.classes;

import cool.compiler.ASTNode;
import cool.compiler.ASTVisitor;
import cool.compiler.classes.ExpressionNode;
import cool.compiler.classes.FormalNode;
import cool.parser.CoolParser;
import cool.structures.Symbol;
import cool.structures.scope.ClassScope;
import cool.structures.scope.MethodScope;
import cool.structures.symbol.IdSymbol;
import org.antlr.v4.runtime.Token;

import java.util.List;

public abstract class FeatureNode extends ASTNode {
    private Token featureId;
    private Token featureType;
    private List<FormalNode> formalList;
    private List<ExpressionNode> expressions;
    private int attributeContext;
    private int methodContext;
    private IdSymbol idSymbol;
    private MethodScope methodScope;

    public int getAttributeContext() {
        return attributeContext;
    }

    public int getMethodContext() {
        return methodContext;
    }

    // Constructor for method feature
    public FeatureNode(CoolParser.FeatureContext context,
                       Token featureId,
                       Token featureType,
                       List<FormalNode> formalListNode,
                       List<ExpressionNode> expressions,
                       int attributeContext,
                       int methodContext,
                       Token token) {
        super(token, context);
        this.featureId = featureId;
        this.featureType = featureType;
        this.formalList = formalListNode;
        this.expressions = expressions;
        this.attributeContext = attributeContext;
        this.methodContext = methodContext;
    }

    public Token getFeatureId() {
        return featureId;
    }

    public Token getFeatureType() {
        return featureType;
    }

    public List<ExpressionNode> getExpressions() {
        return expressions;
    }

    public List<FormalNode> getFormalList() {
        return formalList;
    }

    public IdSymbol getIdSymbol() {
        return idSymbol;
    }

    public void setIdSymbol(IdSymbol idSymbol) {
        this.idSymbol = idSymbol;
    }

    public void setIdSymbolGeneral(Symbol symbolGeneral) {this.idSymbol.setClassScope((ClassScope) symbolGeneral);}

    public MethodScope getMethodScope() {
        return methodScope;
    }

    public void setMethodScope(MethodScope methodScope) {
        this.methodScope = methodScope;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
