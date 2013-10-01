package net.sf.javagimmicks.jpa.testing.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "foo")
public class FooEntity
{
   private int _id;
   private String _value;

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
}
