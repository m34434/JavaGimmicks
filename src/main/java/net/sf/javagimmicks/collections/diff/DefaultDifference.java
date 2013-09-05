package net.sf.javagimmicks.collections.diff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultDifference<T> implements Difference<T>
{
	protected int _deleteStartIndex;
	protected int _deleteEndIndex;
	protected List<T> _fromList;

	protected int _addStartIndex;
	protected int _addEndIndex;
	protected List<T> _toList;
	
	public DefaultDifference(
		int deleteStartIndex, int deleteEndIndex,
		int addStartIndex, int addEndIndex,
		List<T> deleteList, List<T> addlist)
	{
		_deleteStartIndex = deleteStartIndex;
		_deleteEndIndex = deleteEndIndex;

		_addStartIndex = addStartIndex;
		_addEndIndex = addEndIndex;

		_fromList = deleteList;
		_toList = addlist;
	}
	
	public DefaultDifference()
	{
		this(0, NONE, 0, NONE, new ArrayList<T>(), new ArrayList<T>());
	}

	public int getAddStartIndex()
	{
		return _addStartIndex;
	}

	public int getAddEndIndex()
	{
		return _addEndIndex;
	}

	@SuppressWarnings("unchecked")
	public List<T> getAddList()
	{
		return isAdd() ?
			Collections.unmodifiableList(_toList.subList(_addStartIndex, _addEndIndex + 1)) :
			Collections.EMPTY_LIST;
	}
	
	public boolean isAdd()
	{
		return getAddEndIndex() != NONE;
	}

	public int getDeleteStartIndex()
	{
		return _deleteStartIndex;
	}

	public int getDeleteEndIndex()
	{
		return _deleteEndIndex;
	}

	@SuppressWarnings("unchecked")
	public List<T> getDeleteList()
	{
		return isDelete() ?
			Collections.unmodifiableList(_fromList.subList(_deleteStartIndex, _deleteEndIndex + 1)) :
			Collections.EMPTY_LIST;
	}
	
	public boolean isDelete()
	{
		return getDeleteEndIndex() != NONE;
	}

	public Difference<T> invert()
	{
		return DifferenceUtils.getInvertedDifference(this);
	}

	public String toString()
	{
		return DifferenceUtils.toString(this);
	}
	
	public void setDeleteStartIndex(int startIndex)
	{
		_deleteStartIndex = startIndex;
	}

	public void setDeleteEndIndex(int endIndex)
	{
		_deleteEndIndex = endIndex;
	}
	
	public void setAddStartIndex(int startIndex)
	{
		_addStartIndex = startIndex;
	}

	public void setAddEndIndex(int endIndex)
	{
		_addEndIndex = endIndex;
	}

	public void setFromList(List<T> list)
	{
		_fromList = list;
	}

	public void setToList(List<T> list)
	{
		_toList = list;
	}
}
