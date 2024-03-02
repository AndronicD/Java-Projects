package cool.structures;

import cool.compiler.classes.ClassNode;

public interface Scope {
    public boolean add(Symbol sym);
    
    public Symbol lookup(String str);
    
    public Scope getParent();

    public boolean addClass(ClassNode classNode);

    public ClassNode getClassByName(String name);
}
