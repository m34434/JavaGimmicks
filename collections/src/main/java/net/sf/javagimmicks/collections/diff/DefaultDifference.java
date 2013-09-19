package net.sf.javagimmicks.collections.diff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.javagimmicks.collections.decorators.AbstractUnmodifiableListDecorator;

/**
 * A default implementation of {@link Difference}.
 */
public class DefaultDifference<T> implements Difference<T>
{
   protected int _deleteStartIndex;
   protected int _deleteEndIndex;
   protected List<T> _fromList;

   protected int _addStartIndex;
   protected int _addEndIndex;
   protected List<T> _toList;

   /**
    * Creates a new instance with all necessary information.
    * 
    * @param deleteStartIndex
    *           the first index of the <i>delete {@link Range}</i>
    * @param deleteEndIndex
    *           the last index of the <i>delete {@link Range}</i>
    * @param addStartIndex
    *           the first index of the <i>add {@link Range}</i>
    * @param addEndIndex
    *           the last index of the <i>add {@link Range}</i>
    * @param fromList
    *           the <i>from</i> {@link List} ("left side") of the comparison
    * @param toList
    *           the <i>to</i> {@link List} ("right side") of the comparison
    */
   public DefaultDifference(
         final int deleteStartIndex, final int deleteEndIndex,
         final int addStartIndex, final int addEndIndex,
         final List<T> fromList, final List<T> toList)
   {
      _deleteStartIndex = deleteStartIndex;
      _deleteEndIndex = deleteEndIndex;

      _addStartIndex = addStartIndex;
      _addEndIndex = addEndIndex;

      _fromList = fromList;
      _toList = toList;
   }

   public DefaultDifference()
   {
      this(0, NONE, 0, NONE, new ArrayList<T>(), new ArrayList<T>());
   }

   @Override
   public Range<T> deleteRange()
   {
      return new DeleteRange();
   }

   @Override
   public Range<T> addRange()
   {
      return new AddRange();
   }

   @Override
   public int getAddStartIndex()
   {
      return _addStartIndex;
   }

   @Override
   public int getAddEndIndex()
   {
      return _addEndIndex;
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<T> getAddList()
   {
      return isAdd() ?
            Collections.unmodifiableList(_toList.subList(_addStartIndex, _addEndIndex + 1)) :
            Collections.EMPTY_LIST;
   }

   @Override
   public boolean isAdd()
   {
      return getAddEndIndex() != NONE;
   }

   @Override
   public int getDeleteStartIndex()
   {
      return _deleteStartIndex;
   }

   @Override
   public int getDeleteEndIndex()
   {
      return _deleteEndIndex;
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<T> getDeleteList()
   {
      return isDelete() ?
            Collections.unmodifiableList(_fromList.subList(_deleteStartIndex, _deleteEndIndex + 1)) :
            Collections.EMPTY_LIST;
   }

   @Override
   public boolean isDelete()
   {
      return getDeleteEndIndex() != NONE;
   }

   @Override
   public Difference<T> invert()
   {
      return DifferenceUtils.getInvertedDifference(this);
   }

   @Override
   public String toString()
   {
      return DifferenceUtils.toString(this);
   }

   public void setDeleteStartIndex(final int startIndex)
   {
      _deleteStartIndex = startIndex;
   }

   public void setDeleteEndIndex(final int endIndex)
   {
      _deleteEndIndex = endIndex;
   }

   public void setAddStartIndex(final int startIndex)
   {
      _addStartIndex = startIndex;
   }

   public void setAddEndIndex(final int endIndex)
   {
      _addEndIndex = endIndex;
   }

   public void setFromList(final List<T> list)
   {
      _fromList = list;
   }

   public void setToList(final List<T> list)
   {
      _toList = list;
   }

   private class DeleteRange extends AbstractUnmodifiableListDecorator<T> implements Range<T>
   {
      public DeleteRange()
      {
         super(_deleteEndIndex != NONE ? _fromList.subList(_deleteStartIndex, _deleteEndIndex + 1) : Collections
               .<T> emptyList());
      }

      @Override
      public int getStartIndex()
      {
         return _deleteStartIndex;
      }

      @Override
      public int getEndIndex()
      {
         return _deleteEndIndex;
      }

      @Override
      public boolean exists()
      {
         return _deleteEndIndex != NONE;
      }
   }

   private class AddRange extends AbstractUnmodifiableListDecorator<T> implements Range<T>
   {
      public AddRange()
      {
         super(_addEndIndex != NONE ? _toList.subList(_addStartIndex, _addEndIndex + 1) : Collections.<T> emptyList());
      }

      @Override
      public int getStartIndex()
      {
         return _addStartIndex;
      }

      @Override
      public int getEndIndex()
      {
         return _addEndIndex;
      }

      @Override
      public boolean exists()
      {
         return _addEndIndex != NONE;
      }
   }
}
