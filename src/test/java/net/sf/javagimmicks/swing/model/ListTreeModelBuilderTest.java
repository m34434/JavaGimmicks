package net.sf.javagimmicks.swing.model;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;

public class ListTreeModelBuilderTest
{  
   public static void main(String[] args)
   {
      ListTreeModel<String> model = new ListTreeModelBuilder<String>(true)
         .child("Root").children()
            .child("A").children()
               .child("1")
               .child("2")
               .child("3").parent()
            .child("B").children()
               .child("4")
               .child("5")
               .child("6").parent()
            .child("C").children()
               .child("7")
               .child("8")
               .child("9").parent()
            .child("D")
         .buildModel();
      
      JFrame window = new JFrame("Tree test");
      window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      window.getContentPane().add(new JScrollPane(new JTree(model)));
      window.pack();
      
      window.setVisible(true);
   }
}
