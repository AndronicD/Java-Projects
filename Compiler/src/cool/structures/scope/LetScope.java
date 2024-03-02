package cool.structures.scope;

import cool.structures.Scope;
import cool.structures.Symbol;

import java.util.LinkedHashMap;

public class LetScope extends Symbol implements Scope {
    public Scope scope;
    public LinkedHashMap<String, Symbol> localSymbols = new LinkedHashMap<>();

    public LetScope(String name, Scope scope) {
        super(name);
        this.scope = scope;
    }

    @Override
    public boolean add(Symbol sym) {
        return localSymbols.putIfAbsent(sym.getName(), sym) == null;
    }

    @Override
    public Symbol lookup(String str) {
        Symbol sym = localSymbols.get(str);
        return (sym != null) ? sym : (scope != null) ? scope.lookup(str) : null;

    }

    @Override
    public Scope getParent() {
        return scope;
    }
}
