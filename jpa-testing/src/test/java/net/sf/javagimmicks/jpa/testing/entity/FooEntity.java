package net.sf.javagimmicks.jpa.testing.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "foo")
public class FooEntity implements Comparable<FooEntity>
{
   private int _id;
   private String _value;

   public FooEntity()
   {}

   public FooEntity(final int id, final String value)
   {
      setId(id);
      setValue(value);
   }

   @Id
   @Column
   public int getId()
   {
      return _id;
   }

   public void setId(final int id)
   {
      _id = id;
   }

   @Column
   public String getValue()
   {
      return _value;
   }

   public void setValue(final String value)
   {
      _value = value;
   }

   @Override
   public int compareTo(final FooEntity o)
   {
      if (o == null)
      {
         return 1;
      }

      if (this._id != o._id)
      {
         return this._id - o._id;
      }
      else
      {
         return this._value.compareTo(o._value);
      }
   }
}
