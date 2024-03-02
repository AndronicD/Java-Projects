package cool.compiler;

import cool.compiler.classes.*;
import cool.structures.Scope;
import cool.structures.Symbol;
import cool.structures.SymbolTable;
import cool.structures.scope.ClassScope;
import cool.structures.scope.MethodScope;
import cool.structures.symbol.IdSymbol;
import org.antlr.v4.runtime.Token;

public class DefinitionPassVisitor implements ASTVisitor<Void>{
    Scope currentScope = null;

    @Override
    public Void visit(Arithmetic arithmetic) {
        return null;
    }

    @Override
    public Void visit(Assignment assignment) {
        if(SymbolTable.self.equals(assignment.getId().getText())){
            SymbolTable.error(assignment.getContext(), assignment.getId(), "Cannot assign to self");
            return null;
        }
        assignment.getExpressionNode().accept(this);
        return null;
    }

    @Override
    public Void visit(Block block) {
        if(block.getExpressionNodes() != null){
            block.getExpressionNodes().forEach(expressionNode -> expressionNode.accept(this));
        }
        return null;
    }

    @Override
    public Void visit(CaseBlock caseBlock) {
        caseBlock.getCondition().accept(this);
        caseBlock.getCaseExpressions().forEach(expressionNode -> expressionNode.accept(this));
        return null;
    }

    @Override
    public Void visit(CaseExpr caseExpr) {
        if(SymbolTable.self.equals(caseExpr.getId().getText())){
            SymbolTable.error(caseExpr.getContext(), caseExpr.getId(), "Case variable has illegal name self");
            return null;
        }

        if(SymbolTable.selfType.equals(caseExpr.getType().getText())){
            SymbolTable.error(caseExpr.getContext(), caseExpr.getType(), "Case variable " + caseExpr.getId().getText() + " has illegal type SELF_TYPE");
        }

        caseExpr.getExpressionNode().accept(this);
        return null;
    }

    @Override
    public Void visit(ClassNode classNode) {
        Token className = classNode.getClassName();
        Token inheritClassName = classNode.getInheritClassName();

        Symbol inheritClassSymbol;

        if(className.getText().equals(SymbolTable.selfType)){
            SymbolTable.error(classNode.getContext(), classNode.getToken(), "Class has illegal name SELF_TYPE");
            return null;
        }

        if(inheritClassName != null){
            inheritClassSymbol = SymbolTable.globals.lookup(inheritClassName.getText());
        }
        else{
            inheritClassSymbol = SymbolTable.objectClassScope;
        }

        Symbol classScope = new ClassScope(className.getText(), (Scope) inheritClassSymbol);

        if(!SymbolTable.globals.add(classScope)){
            SymbolTable.error(classNode.getContext(), classNode.getToken(), "Class " + className.getText() + " is redefined");
            return null;
        }

        SymbolTable.globals.addClass(classNode);

        if(inheritClassName != null && SymbolTable.notAllowedToBeInherited.contains(inheritClassSymbol)){
            SymbolTable.error(classNode.getContext(), classNode.getInheritClassName(), "Class " + className.getText() + " has illegal parent " + inheritClassName.getText());
            return null;
        }

        currentScope = (ClassScope) classScope;

        classNode.setClassScope((ClassScope) classScope);

        classNode.getFeatures().forEach(featureNode -> featureNode.accept(this));

        return null;
    }

    @Override
    public Void visit(Dispatch dispatch) {
        return null;
    }

    @Override
    public Void visit(ExpressionNode expressionNode) {
        return null;
    }

    @Override
    public Void visit(False falseToken) {
        return null;
    }

    @Override
    public Void visit(FeatureNode featureNode) {
        if(featureNode.getAttributeContext() == 1){
            Token attributeName = featureNode.getFeatureId();
            Token attributeType = featureNode.getFeatureType();

            if(attributeName.getText().equals(SymbolTable.self)){
                SymbolTable.error(featureNode.getContext(), attributeName, "Class " + (ClassScope) currentScope + " has attribute with illegal name self");
                return null;
            }

            IdSymbol attributeSymbol = new IdSymbol(attributeName.getText(), attributeType.getText());
            if(!currentScope.add(attributeSymbol)){
                SymbolTable.error(featureNode.getContext(), attributeName, "Class " + (ClassScope) currentScope + " redefines attribute " + attributeName.getText());
                return null;
            }

            featureNode.setIdSymbol(attributeSymbol);

            if(!(featureNode.getExpressions().size() == 0)){
                featureNode.getExpressions().get(0).accept(this);
            }
        }


        if(featureNode.getMethodContext() == 1){
            Token methodName = featureNode.getFeatureId();
            Token methodType = featureNode.getFeatureType();
            MethodScope methodScope = new MethodScope(methodName.getText(), currentScope, methodType.getText());

            if(!currentScope.add(methodScope)){
                SymbolTable.error(featureNode.getContext(), methodName, "Class " + ((ClassScope) currentScope).getName() + " redefines method " + methodName.getText());
                return null;
            }

            featureNode.setMethodScope(methodScope);

            Scope initialScope = currentScope;
            currentScope = methodScope;

            featureNode.getFormalList().forEach(formalNode -> formalNode.accept(this));
            featureNode.getExpressions().get(0).accept(this);
            currentScope = initialScope;
        }
        return null;
    }

    @Override
    public Void visit(FormalNode formalNode) {
        Token formalName = formalNode.getId();
        Token formalType = formalNode.getType();

        if(SymbolTable.self.equals(formalName.getText())){
            Scope inherit = currentScope.getParent();
            SymbolTable.error(formalNode.getContext(), formalName, "Method " + ((MethodScope) currentScope).getName() + " of class " + ((ClassScope) inherit).getName() + " has formal parameter with illegal name self");
            return null;
        }

        if(SymbolTable.selfType.equals(formalType.getText())){
            Scope inherit = currentScope.getParent();
            SymbolTable.error(formalNode.getContext(), formalType, "Method " + ((MethodScope) currentScope).getName() + " of class " + ((ClassScope) inherit).getName() + " has formal parameter " + formalName.getText() + " with illegal type SELF_TYPE");
            return null;
        }

        IdSymbol formalSymbol = new IdSymbol(formalName.getText(), formalType.getText());
        if(!currentScope.add(formalSymbol)){
            Scope inherit = currentScope.getParent();
            SymbolTable.error(formalNode.getContext(), formalName, "Method " + ((MethodScope) currentScope).getName() + " of class " + ((ClassScope) inherit).getName() + " redefines formal parameter " + formalName.getText());
            return null;
        }

        formalNode.setIdSymbol(formalSymbol);
        return null;
    }

    @Override
    public Void visit(Id id) {
        return null;
    }

    @Override
    public Void visit(If ifToken) {
        ifToken.getCondition().accept(this);
        ifToken.getThenBranch().accept(this);
        ifToken.getElseBranch().accept(this);
        return null;
    }

    @Override
    public Void visit(ImplicitDispatch implicitDispatch) {
        return null;
    }

    @Override
    public Void visit(Int intToken) {
        return null;
    }

    @Override
    public Void visit(Let let) {
        let.getLetList().forEach(localNode -> localNode.accept(this));
        let.getExpressionNode().accept(this);
        return null;
    }

    @Override
    public Void visit(LocalNode localNode) {
        if(SymbolTable.self.equals(localNode.getIdentifier().getText())){
            SymbolTable.error(localNode.getContext(), localNode.getIdentifier(), "Let variable has illegal name self");
            return null;
        }

        IdSymbol localIdSymbol = new IdSymbol(localNode.getIdentifier().getText(), localNode.getType().getText());
        localNode.setIdSymbol(localIdSymbol);

        if(localNode.getExpression() != null){
            localNode.getExpression().accept(this);
        }
        return null;
    }

    @Override
    public Void visit(New newToken) {
        return null;
    }

    @Override
    public Void visit(ProgramNode programNode) {
        programNode.getClasses().forEach(classNode -> classNode.accept(this));
        return null;
    }

    @Override
    public Void visit(Relational relational) {
        relational.getLeft().accept(this);
        relational.getRight().accept(this);
        return null;
    }

    @Override
    public Void visit(StringToken stringToken) {
        return null;
    }

    @Override
    public Void visit(True trueToken) {
        return null;
    }

    @Override
    public Void visit(Unary unary) {
        unary.getExpr().accept(this);
        return null;
    }

    @Override
    public Void visit(While whileToken) {
        whileToken.getCondition().accept(this);
        whileToken.getBody().accept(this);
        return null;
    }
}
