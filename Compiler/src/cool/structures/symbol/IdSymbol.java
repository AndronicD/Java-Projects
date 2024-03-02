package cool.structures.symbol;

import cool.structures.Symbol;
import cool.structures.SymbolTable;
import cool.structures.scope.ClassScope;

public class IdSymbol extends Symbol {
    public boolean isResolved;
    public String idType;
    public ClassScope classScope;

    public IdSymbol(String name, String idType) {
        super(name);
        this.idType = idType;
    }

    public boolean isResolved() {
        return isResolved;
    }

    public void setResolved(boolean resolved) {
        isResolved = resolved;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public ClassScope getClassScope() {
        return classScope;
    }

    public void setClassScope(ClassScope classScope) {
        this.classScope = classScope;
    }

    public void resolve() {
        isResolved = true;
        classScope = (ClassScope) SymbolTable.globals.lookup(idType);
    }
}
