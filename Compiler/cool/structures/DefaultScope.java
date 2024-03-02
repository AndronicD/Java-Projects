package cool.structures;

import cool.compiler.classes.ClassNode;

import java.util.*;

public class DefaultScope implements Scope {
    
    private Map<String, Symbol> symbols = new LinkedHashMap<>();
    private Map<String, ClassNode> globalClassScope = new LinkedHashMap<>();
    
    private Scope parent;
    
    public DefaultScope(Scope parent) {
        this.parent = parent;
    }

    @Override
    public boolean add(Symbol sym) {
        // Reject duplicates in the same scope.
        if (symbols.containsKey(sym.getName()))
            return false;
        
        symbols.put(sym.getName(), sym);
        
        return true;
    }

    public boolean addClass(ClassNode classNode){
        if (globalClassScope.containsKey(classNode.getClassName().getText())){
            return false;
        }

        globalClassScope.put(classNode.getClassName().getText(), classNode);
        return true;
    }

    public ClassNode getClassByName(String name){
        return globalClassScope.get(name);
    }

    @Override
    public Symbol lookup(String name) {
        var sym = symbols.get(name);
        
        if (sym != null)
            return sym;
        
        if (parent != null)
            return parent.lookup(name);
        
        return null;
    }

    @Override
    public Scope getParent() {
        return parent;
    }
    
    @Override
    public String toString() {
        return symbols.values().toString();
    }

}
