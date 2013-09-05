package net.sf.javagimmicks.collections;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

public abstract class AbstractRing<E> extends AbstractCollection<E> implements Ring<E>
{
    protected int _modCount = Integer.MIN_VALUE;
    
    // Method implementations overriding AbstractCollection
    public boolean add(E value)
    {
        // Get the initial RingCursor add the element before the current position
        // (this means at the 'end' of the ring)
        cursor().insertBefore(value);
        
        return true;
    }

    public boolean addAll(Collection<? extends E> collection)
    {
        // Get the initial RingCursor add the elements before the current position
        // (this means at the 'end' of the ring)
        cursor().insertBefore(collection);
        
        return true;
    }
    
    public Iterator<E> iterator()
    {
        return cursor().iterator();
    }

    public String toString()
    {
       return cursor().toString();
    }
    
    protected static abstract class BasicRingCursor<E, R extends AbstractRing<E>> extends AbstractRingCursor<E>
    {
        protected final R _ring;
        protected int _expectedModCount;
        
        protected BasicRingCursor(R ring)
        {
            _ring = ring;
            _expectedModCount = _ring._modCount;
        }

        public int size()
        {
           return _ring.size();
        }
        
        protected void checkForModification()
        {
            if(_ring._modCount != _expectedModCount)
            {
                throw new ConcurrentModificationException();
            }
        }
    }
    
}
