package cool.compiler;

import cool.compiler.classes.*;
import cool.structures.Scope;
import cool.structures.Symbol;
import cool.structures.SymbolTable;
import cool.structures.scope.ClassScope;
import cool.structures.scope.MethodScope;
import cool.structures.symbol.IdSymbol;
import org.antlr.v4.runtime.Token;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResolutionPassVisitor implements ASTVisitor<Symbol>{
    Scope currentScope = null;

    @Override
    public Symbol visit(Arithmetic arithmetic) {
        Symbol leftOperandSymbol = arithmetic.getLeft().accept(this);
        Symbol rightOperandSymbol = arithmetic.getRight().accept(this);

        if (leftOperandSymbol == null || rightOperandSymbol == null)
            return null;

        if(leftOperandSymbol != SymbolTable.intClassScope){
            SymbolTable.error(arithmetic.getContext(), arithmetic.getContext().start, "Operand of " + arithmetic.getOp().getText() + " has type " + leftOperandSymbol.getName() + " instead of Int");
        }

        if(rightOperandSymbol != SymbolTable.intClassScope){
            SymbolTable.error(arithmetic.getContext(), arithmetic.getContext().stop, "Operand of " + arithmetic.getOp().getText() + " has type " + rightOperandSymbol.getName() + " instead of Int");
        }
        return SymbolTable.intClassScope;
    }

    @Override
    public Symbol visit(Assignment assignment) {
        if(SymbolTable.self.equals(assignment.getId().getText())){
            return SymbolTable.boolClassScope;
        }

        Symbol assignmentSymbol = assignment.getExpressionNode().accept(this);

        if(assignmentSymbol == null){
            return null;
        }

        Symbol assignmentIdSymbol = currentScope.lookup(assignment.getId().getText());

        if(assignmentIdSymbol == null){
            return null;
        }
        ClassScope assignmentType = ((IdSymbol) assignmentIdSymbol).getClassScope();

        String inherit = ((ClassScope) assignmentSymbol).getInheritName();
        while (inherit != null) {
            if (inherit.equals(assignmentType.getName()))
                return assignmentSymbol;

            Symbol inherited = SymbolTable.globals.lookup(inherit);
            inherit = ((ClassScope) inherited).getInheritName();
        }

        if(assignmentSymbol != assignmentType){
            SymbolTable.error(assignment.getContext(), assignment.getFrom(), "Type " + assignmentSymbol.getName() + " of assigned expression is incompatible with declared type " + assignmentType.getName() + " of identifier " + assignmentIdSymbol.getName());
        }
        return assignmentSymbol;
    }

    @Override
    public Symbol visit(Block block) {
        List<Symbol> blockSymbols = new LinkedList<>();

        for (ExpressionNode expressionNode : block.getExpressionNodes()) {
            Symbol result = expressionNode.accept(this);

            if (result != null) {
                blockSymbols.add(result);
            }
        }

        if(blockSymbols.isEmpty()){
            return null;
        }

        return blockSymbols.get(blockSymbols.size() - 1);
    }

    @Override
    public Symbol visit(CaseBlock caseBlock) {
        Symbol commonInherit = SymbolTable.objectClassScope;
        List<Symbol> caseExpressionsSymbols = new LinkedList<>();
        for (ExpressionNode rfCaseBranch : caseBlock.getCaseExpressions()) {
            Symbol result = rfCaseBranch.accept(this);

            if (result != null) {
                caseExpressionsSymbols.add(result);
            }
        }

        if(caseExpressionsSymbols.isEmpty()){
            return null;
        }

        if(caseExpressionsSymbols.size() == 1){
            return caseExpressionsSymbols.get(0);
        }

        Symbol firstSymbol = caseExpressionsSymbols.get(0);
        for(int i = 1; i < caseExpressionsSymbols.size(); i++){
            Symbol toFindCommonInherit = caseExpressionsSymbols.get(i);
            commonInherit = findCommonInherit((ClassScope) firstSymbol, (ClassScope) toFindCommonInherit);
            if(commonInherit == SymbolTable.objectClassScope){
                return SymbolTable.objectClassScope;
            }
        }

        return commonInherit;
    }

    private static Symbol findCommonInherit(ClassScope firstSymbol, ClassScope secondSymbol) {
        Set<ClassScope> types = Stream.iterate(firstSymbol, Objects::nonNull, s -> (ClassScope) s.getParent())
                .collect(Collectors.toSet());

        return Stream.iterate(secondSymbol, Objects::nonNull, s -> (ClassScope) s.getParent())
                .filter(types::contains)
                .findFirst()
                .orElse(SymbolTable.objectClassScope);
    }

    @Override
    public Symbol visit(CaseExpr caseExpr) {
        Symbol caseSymbolType = SymbolTable.globals.lookup(caseExpr.getType().getText());
        if(caseSymbolType == null){
            SymbolTable.error(caseExpr.getContext(), caseExpr.getType(), "Case variable " + caseExpr.getId().getText() + " has undefined type " + caseExpr.getType().getText());
            return null;
        }

        IdSymbol caseBranchIdSymbol = new IdSymbol(caseExpr.getId().getText(), caseExpr.getType().getText());
        caseBranchIdSymbol.setClassScope((ClassScope) caseSymbolType);

        Symbol expressionSymbol = caseExpr.getExpressionNode().accept(this);

        return expressionSymbol;
    }

    @Override
    public Symbol visit(ClassNode classNode) {
        ClassScope classScope = classNode.getClassScope();
        if (classScope == null)
            return null;

        ClassNode currentClass = classNode;
        ClassScope currentClassScope = currentClass.getClassScope();
        Symbol classParent;

        while(currentClassScope != SymbolTable.objectClassScope){
            if(currentClass.getInheritClassName() == null) {
                break;
            }
            classParent = SymbolTable.globals.lookup(currentClass.getInheritClassName().getText());

            if (classParent == null) {
                SymbolTable.error(currentClass.getContext(), currentClass.getInheritClassName(), "Class " + currentClass.getClassName().getText() + " has undefined parent " + currentClass.getInheritClassName().getText());
                return null;
            }

            currentClass.getClassScope().scope = ((ClassScope) classParent);
            currentClassScope = (ClassScope) classParent;
            currentClass = SymbolTable.globals.getClassByName(currentClassScope.getName());

            if (currentClass == null){
                return null;
            }

            if (classScope == currentClass.getClassScope()) {
                SymbolTable.error(classNode.getContext(), classNode.getClassName(), "Inheritance cycle for class " + classNode.getClassName().getText());
                return null;
            }
        }

        currentScope = classScope;
        classNode.getFeatures().forEach(featureNode -> featureNode.accept(this));
        return null;
    }

    @Override
    public Symbol visit(Dispatch dispatch) {
        return null;
    }

    @Override
    public Symbol visit(ExpressionNode expressionNode) {
        expressionNode.accept(this);
        return null;
    }

    @Override
    public Symbol visit(False falseToken) {
        return SymbolTable.boolClassScope;
    }

    @Override
    public Symbol visit(FeatureNode featureNode) {
        if(featureNode.getAttributeContext() == 1){
            if(featureNode.getIdSymbol() == null){
                return null;
            }

            Token attributeName = featureNode.getFeatureId();
            Token attributeType = featureNode.getFeatureType();

            Scope inherit = currentScope.getParent();
            if(inherit == null){
                return null;
            }
            Symbol inheritSymbol = inherit.lookup(attributeName.getText());

            if(inheritSymbol != null){
                SymbolTable.error(featureNode.getContext(), attributeName, "Class " + ((ClassScope) currentScope).getName() + " redefines inherited attribute " + attributeName.getText());
                return null;
            }

            Symbol type = SymbolTable.globals.lookup(attributeType.getText());

            if(type == null){
                SymbolTable.error(featureNode.getContext(), attributeType, "Class " + ((ClassScope) currentScope).getName() + " has attribute " + attributeName.getText() + " with undefined type " + attributeType.getText());
                return null;
            }

            featureNode.setIdSymbolGeneral(type);

            if(!(featureNode.getExpressions().size() == 0)){
                featureNode.getExpressions().get(0).accept(this);
            }
        }

        if(featureNode.getMethodContext() == 1){
            Token methodName = featureNode.getFeatureId();
            Token methodType = featureNode.getFeatureType();

            MethodScope methodScope = featureNode.getMethodScope();
            if (methodScope == null)
                return null;

            Symbol returnType = SymbolTable.globals.lookup(methodType.getText());
            methodScope.setReturnType(returnType);

            Scope initialScope = currentScope;
            currentScope = methodScope;

            featureNode.getFormalList().forEach(formalNode -> formalNode.accept(this));
            Scope parentInitialScope = initialScope.getParent();
            MethodScope overrideMethod = ((ClassScope) parentInitialScope).lookupMethod(methodScope.getName());

            if(overrideMethod != null){
                Map<String, Symbol> parameters = methodScope.getParameters();
                Map<String, Symbol> overrideParameters = overrideMethod.getParameters();

                if(parameters.size() != overrideParameters.size()){
                    SymbolTable.error(featureNode.getContext(), methodName, "Class " + ((ClassScope) initialScope).getName() + " overrides method " + methodName.getText() + " with different number of formal parameters");
                    return null;
                }

                List<Map.Entry<String, Symbol>> paramsList = new LinkedList<>(parameters.entrySet());
                List<Map.Entry<String, Symbol>> overrideParamsList = new LinkedList<>(overrideParameters.entrySet());

                for(int i = 0; i < overrideParameters.size(); i++){
                    Map.Entry<String, Symbol> parameter = paramsList.get(i);
                    Map.Entry<String, Symbol> overriddenParameter = overrideParamsList.get(i);

                    Symbol parameterSymbol = parameter.getValue();
                    Symbol overriddenParameterSymbol = overriddenParameter.getValue();

                    ClassScope parameterScope = ((IdSymbol) parameterSymbol).getClassScope();
                    ClassScope overriddenParameterScope = ((IdSymbol) overriddenParameterSymbol).getClassScope();

                    if(!parameterScope.getName().equals(overriddenParameterScope.getName())){
                        SymbolTable.error(featureNode.getContext(), featureNode.getFormalList().get(i).getType(),
                                "Class " + ((ClassScope) initialScope).getName() + " overrides method "
                                        + featureNode.getFeatureId().getText()
                                        + " but changes type of formal parameter "
                                        + parameter.getKey()
                                        + " from "
                                        + overriddenParameterScope.getName()
                                        + " to "
                                        + parameterScope.getName()
                                        );
                        return null;
                    }
                }

                Symbol parameterReturnSymbol = methodScope.getReturnType();
                Symbol overrideReturnSymbol = overrideMethod.getReturnType();

                if(!parameterReturnSymbol.getName().equals(overrideReturnSymbol.getName())){
                    SymbolTable.error(featureNode.getContext(), methodType, "Class " + ((ClassScope) initialScope).getName() + " overrides method " + methodName.getText() + " but changes return type from " + overrideReturnSymbol.getName() + " to " + parameterReturnSymbol.getName());
                    return null;
                }
            }
            if(featureNode.getExpressions().get(0) != null){
                Symbol bodySymbol = featureNode.getExpressions().get(0).accept(this);

                if(bodySymbol == null){
                    currentScope = initialScope;
                    return null;
                }

                if (returnType == bodySymbol) {
                    currentScope = initialScope;
                    return null;
                }

                String inherit = ((ClassScope)bodySymbol).getInheritName();
                while (inherit != null) {
                    if (inherit.equals(returnType.getName())) {
                        currentScope = initialScope;
                        return null;
                    }

                    Symbol inherited = SymbolTable.globals.lookup(inherit);
                    if (!(inherited instanceof ClassScope)) {
                        SymbolTable.error(featureNode.getContext(), featureNode.getContext().start, "Type " + bodySymbol.getName() + " of the body of method " + methodName.getText() + " is incompatible with declared return type " + returnType);
                        currentScope = initialScope;
                        return null;
                    }
                    inherit = ((ClassScope) inherited).getInheritName();
                }

                currentScope = initialScope;
                return null;
            }
       }
        return null;
    }

    @Override
    public Symbol visit(FormalNode formalNode) {
        if(formalNode.getIdSymbol() == null){
            return null;
        }

        Token formalName = formalNode.getId();
        Token formalType = formalNode.getType();

        Symbol formalSymbolType = SymbolTable.globals.lookup(formalType.getText());
        if(formalSymbolType == null){
            Scope inherit = currentScope.getParent();
            SymbolTable.error(formalNode.getContext(), formalType, "Method " + ((MethodScope) currentScope).getName() + " of class " + ((ClassScope) inherit).getName() + " has formal parameter " + formalName.getText() + " with undefined type " + formalType.getText());
            return null;
        }

        formalNode.setIdSymbolGeneral(formalSymbolType);
        return null;
    }

    @Override
    public Symbol visit(Id id) {
        Symbol idSymbol = currentScope.lookup(id.getToken().getText());

        if(id.getToken().getText().equals(SymbolTable.self)){
            return null;
        }

        if(idSymbol == null){
            return null;
        }
        return ((IdSymbol) idSymbol).getClassScope();
    }

    @Override
    public Symbol visit(If ifToken) {
        Symbol ifConditionSymbol = ifToken.getCondition().accept(this);
        Symbol ifThenBranchSymbol = ifToken.getThenBranch().accept(this);
        Symbol ifElseBranchSymbol = ifToken.getElseBranch().accept(this);

        if(SymbolTable.boolClassScope != ifConditionSymbol){
            SymbolTable.error(ifToken.getContext(), ifToken.getFrom(), "If condition has type " + ifConditionSymbol.getName() + " instead of Bool");
        }

        List<ClassScope> thenInheritChain = new LinkedList<>();
        while(ifThenBranchSymbol != null){
            thenInheritChain.add((ClassScope) ifThenBranchSymbol);
            ifThenBranchSymbol = (ClassScope) ((ClassScope) ifThenBranchSymbol).getParent();
        }

        List<ClassScope> elseInheritChain = new LinkedList<>();
        while(ifElseBranchSymbol != null){
            elseInheritChain.add((ClassScope) ifElseBranchSymbol);
            ifElseBranchSymbol = (ClassScope) ((ClassScope) ifElseBranchSymbol).getParent();
        }

        for (ClassScope classScope : thenInheritChain) {
            if (elseInheritChain.contains(classScope)) {
                return classScope;
            }
        }

        return SymbolTable.objectClassScope;
    }

    @Override
    public Symbol visit(ImplicitDispatch implicitDispatch) {
        return null;
    }

    @Override
    public Symbol visit(Int intToken) {
        return SymbolTable.intClassScope;
    }

    @Override
    public Symbol visit(Let let) {
        let.getLetList().forEach(localNode -> localNode.accept(this));
        return let.getExpressionNode().accept(this);
    }

    @Override
    public Symbol visit(LocalNode localNode) {
        IdSymbol localIdSymbol = localNode.getIdSymbol();
        if(localIdSymbol == null){
            return null;
        }

        Token localNodeName = localNode.getIdentifier();
        Token localNodeType = localNode.getType();

        Symbol localNodeTypeSymbol = SymbolTable.globals.lookup(localNodeType.getText());
        if(localNodeTypeSymbol == null){
            SymbolTable.error(localNode.getContext(), localNodeType, "Let variable " + localNodeName.getText() + " has undefined type " + localNodeType.getText());
            return null;
        }

        localIdSymbol.setClassScope((ClassScope) localNodeTypeSymbol);
        if(localNode.getExpression() != null){
            Symbol expressionSymbol = localNode.getExpression().accept(this);

            if(expressionSymbol == null){
                return null;
            }

            currentScope.add(localIdSymbol);

            if (localNodeTypeSymbol == expressionSymbol)
                return null;

            String inherit = ((ClassScope) expressionSymbol).getInheritName();
            while (inherit != null) {
                if (inherit.equals(localNodeTypeSymbol.getName()))
                    return null;

                Symbol inherited = SymbolTable.globals.lookup(inherit);
                if (!(inherited instanceof ClassScope)) {
                    SymbolTable.error(localNode.getContext(), localNode.getExpression().getContext().start, "Type " + localNodeTypeSymbol.getName() + " of initialization expression of identifier " + localIdSymbol.getName() + " is incompatible with declared type " + localIdSymbol.getClassScope().getName());
                    return null;
                }
                inherit = ((ClassScope) inherited).getInheritName();
            }

            SymbolTable.error(localNode.getContext(), localNode.getExpression().getContext().start, "Type " + localNodeTypeSymbol.getName() + " of initialization expression of identifier " + localIdSymbol.getName() + " is incompatible with declared type " + localIdSymbol.getClassScope().getName());
        }
        return null;
    }

    @Override
    public Symbol visit(New newToken) {
        Symbol newSymbol = SymbolTable.globals.lookup(newToken.getId().getText());
        if(newSymbol == null){
            SymbolTable.error(newToken.getContext(), newToken.getId(), "new is used with undefined type " + newToken.getId().getText());
            return null;
        }
        return newSymbol;
    }

    @Override
    public Symbol visit(ProgramNode programNode) {
        programNode.getClasses().forEach(classNode -> classNode.accept(this));
        return null;
    }

    @Override
    public Symbol visit(Relational relational) {
        Token relationalSymbol = relational.getOp();

        Symbol leftOperandSymbol = relational.getLeft().accept(this);
        Symbol rightOperandSymbol = relational.getRight().accept(this);

        boolean leftOperandSymbolType = leftOperandSymbol == SymbolTable.intClassScope || leftOperandSymbol == SymbolTable.boolClassScope || leftOperandSymbol == SymbolTable.stringClassScope;
        boolean rightOperandSymbolType = rightOperandSymbol == SymbolTable.intClassScope || rightOperandSymbol == SymbolTable.boolClassScope || rightOperandSymbol == SymbolTable.stringClassScope;

        if(relationalSymbol.getText().equals("=")){
            if((leftOperandSymbolType || rightOperandSymbolType) && leftOperandSymbol != rightOperandSymbol){
                SymbolTable.error(relational.getContext(), relational.getToken(), "Cannot compare " + leftOperandSymbol.getName() + " with " + rightOperandSymbol.getName());
            }
        }
        else {
            if (leftOperandSymbol != SymbolTable.intClassScope) {
                if (rightOperandSymbol == SymbolTable.intClassScope) {
                    SymbolTable.error(relational.getContext(), relational.getContext().start, "Operand of " + relational.getOp().getText() + " has type " + leftOperandSymbol.getName() + " instead of Int");
                }
            }

            if (rightOperandSymbol != SymbolTable.intClassScope) {
                if (leftOperandSymbol == SymbolTable.intClassScope) {
                    SymbolTable.error(relational.getContext(), relational.getContext().stop, "Operand of " + relational.getOp().getText() + " has type " + rightOperandSymbol.getName() + " instead of Int");
                }
            }
        }

        return SymbolTable.boolClassScope;
    }

    @Override
    public Symbol visit(StringToken stringToken) {
        return SymbolTable.stringClassScope;
    }

    @Override
    public Symbol visit(True trueToken) {
        return SymbolTable.boolClassScope;
    }

    @Override
    public Symbol visit(Unary unary) {
        Symbol symbol = unary.getExpr().accept(this);

        if(symbol != SymbolTable.boolClassScope && unary.getOp().getText().equals("~")){
            SymbolTable.error(unary.getContext(), unary.getContext().stop, "Operand of " + unary.getToken().getText() + " has type " + symbol.getName() + " instead of Int");
            return SymbolTable.intClassScope;
        }

        if(symbol != SymbolTable.boolClassScope && unary.getOp().getText().equals("not")){
            SymbolTable.error(unary.getContext(), unary.getContext().stop, "Operand of " + unary.getToken().getText() + " has type " + symbol.getName() + " instead of Bool");
            return SymbolTable.boolClassScope;
        }

        if(unary.getOp().getText().equals("isvoid")){
            return SymbolTable.boolClassScope;
        }

        return symbol;
    }

    @Override
    public Symbol visit(While whileToken) {
        Symbol whileConditionSymbol = whileToken.getCondition().accept(this);

        if(whileConditionSymbol != SymbolTable.boolClassScope){
            SymbolTable.error(whileToken.getContext(), whileToken.getFrom(), "While condition has type " + whileConditionSymbol.getName() + " instead of Bool");
            return SymbolTable.objectClassScope;
        }

        return whileConditionSymbol;
    }
}
