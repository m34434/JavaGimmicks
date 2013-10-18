package net.sf.javagimmicks.swing.model;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;

public class ListTreeModelBuilderTest
{
   public static void main(final String[] args)
   {
      final ListTreeModel<String> model = buildSampleModel();

      final JFrame window = new JFrame("Tree test");
      window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      window.getContentPane().add(new JScrollPane(new JTree(model)));
      window.pack();

      window.setVisible(true);
   }

   private static ListTreeModel<String> buildSampleModel()
   {
      // @formatter:off
      return new ListTreeModelBuilder<String>(true)
         .add("Root").children()
            .add("A").children()
               .add("1")
               .add("2")
               .add("3").parent()
            .add("B").children()
               .add("4")
               .add("5")
               .add("6").parent()
            .add("C").children()
               .add("7")
               .add("8")
               .add("9").parent()
            .add("D")
         .getModel();
      // @formatter:on
   }
}
