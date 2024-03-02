package cool.structures.scope;

import cool.structures.Scope;
import cool.structures.Symbol;
import cool.structures.symbol.IdSymbol;

public class CaseScope implements Scope {
    public IdSymbol idSymbol;
    public Scope scope;

    public CaseScope(IdSymbol idSymbol, Scope scope){
        this.idSymbol = idSymbol;
        this.scope = scope;
    }

    @Override
    public boolean add(Symbol sym) {
        return false;
    }

    @Override
    public Symbol lookup(String str) {
        return str.equals(idSymbol.getName()) ? idSymbol : scope.lookup(str);
    }

    @Override
    public Scope getParent() {
        return scope;
    }
}
