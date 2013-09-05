package net.sf.javagimmicks.collections;

import java.util.Collection;
import java.util.List;

public interface Traverser<E> extends Traversable<E>
{
    public E next();
    public E next(int count);
    
    public E previous();
    public E previous(int count);
    
    public E get();
    public E set(E value);
    
    public void insertAfter(E value);
    public void insertBefore(E value);
    
    public void insertAfter(Collection<? extends E> collection);
    public void insertBefore(Collection<? extends E> collection);
    
    public E remove();
    
    public Traverser<E> traverser();
    public List<E> toList(); 
    public Ring<E> ring();
}
