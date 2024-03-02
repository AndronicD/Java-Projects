package cool.compiler;

public interface ASTVisitor<T> {
    T visit(Arithmetic arithmetic);
    T visit(Assignment assignment);
    T visit(Block block);
    T visit(CaseBlock caseBlock);
    T visit(CaseExpr caseExpr);
    T visit(ClassNode classNode);
    T visit(Dispatch dispatch);
    T visit(ExpressionNode expressionNode);
    T visit(False falseToken);
    T visit(FeatureNode featureNode);
    T visit(FormalNode formalNode);
    T visit(Id id);
    T visit(If ifToken);
    T visit(ImplicitDispatch implicitDispatch);
    T visit(Int intToken);
    T visit(Let let);
    T visit(LocalNode localNode);
    T visit(New newToken);
    T visit(ProgramNode programNode);
    T visit(Relational relational);
    T visit(Self self);
    T visit(StringToken stringToken);
    T visit(True trueToken);
    T visit(Unary unary);
    T visit(While whileToken);
}

