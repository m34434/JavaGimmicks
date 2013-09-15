/**
 * 
 */
package net.sf.javagimmicks.util;

import java.io.Serializable;
import java.util.Comparator;

@SuppressWarnings({ "unchecked", "rawtypes" })
public final class ComparableComparator<T extends Comparable<? super T>> implements Comparator<T>, Serializable
{
    private static final long serialVersionUID = 1790088535783496697L;

    static
    {
       INSTANCE = new ComparableComparator();
    }
    
    /**
     * @deprecated Use static method {@link #getInstance()} instead. 
     */
    @Deprecated
    public ComparableComparator()
    {
        
    }
    
    public static ComparableComparator<? extends Comparable<?>> INSTANCE;
    
    public static final <T extends Comparable<? super T>> ComparableComparator<T> getInstance()
    {
        return (ComparableComparator<T>)INSTANCE;
    }
    
    public int compare(T oObject1, T oObject2)
    {
        return oObject1.compareTo(oObject2);
    }
}