package net.sf.javagimmicks.swing.model;

import java.awt.GridLayout;
import java.util.Arrays;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DependencyComboBoxModelTest
{
    public static void main(String[] args)
    {
        List<ComboBoxModel> models = withBuilder();
//        List<ComboBoxModel> models = withoutBuilder();
        
        JPanel boxPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        boxPanel.add(new JComboBox(models.get(0)));
        boxPanel.add(new JComboBox(models.get(1)));
        boxPanel.add(new JComboBox(models.get(2)));
        
        JFrame window = new JFrame("Test");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.getContentPane().add(boxPanel);
        window.pack();
        
        window.setVisible(true);
        
    }

    private static List<ComboBoxModel> withBuilder()
    {
        return new DependencyComboBoxModelBuilder()
            
            .add("A").children()
                .add("A1").children()
                    .add("A1x")
                    .add("A1y")
                    .add("A1z").parent()
                .add("A2").children()
                    .add("A2x")
                    .add("A2y")
                    .add("A2z").parent()
                .add("A3").children()
                    .add("A3x")
                    .add("A3y")
                    .add("A3z").parent()
                .add("A4").children()
                    .add("A4x")
                    .add("A4y")
                    .add("A4z").parent().parent()
            
            .add("B").children()
                .add("B1").children()
                    .add("B1x")
                    .add("B1y")
                    .add("B1z").parent()
                .add("B2").children()
                    .add("B2x")
                    .add("B2y")
                    .add("B2z").parent().parent()
            
            .add("C").children()
                .add("C1").children()
                    .add("C1x")
                    .add("C1y")
                    .add("C1z").parent()
                .add("C2").children()
                    .add("C2x")
                    .add("C2y")
                    .add("C2z").parent()
                .add("C3").children()
                    .add("C3x")
                    .add("C3y")
                    .add("C3z").parent().parent()
            
            .buildModels();
    }
    
    @SuppressWarnings("unused")
    private static List<ComboBoxModel> withoutBuilder()
    {
        ComboBoxModel model1 = new DefaultComboBoxModel(new String[]{"A", "B", "C"});
        
        DependencyComboBoxModel model2 = new DependencyComboBoxModel(model1);
        model2.registerModel(new String[]{"A1", "A2", "A3", "A4"}, "A");
        model2.registerModel(new String[]{"B1", "B2"}, "B");
        model2.registerModel(new String[]{"C1", "C2", "C3"}, "C");
        
        DependencyComboBoxModel model3 = new DependencyComboBoxModel(model1, model2);
        model3.registerModel(new String[]{"A1x", "A1y", "A1z"}, "A", "A1");
        model3.registerModel(new String[]{"A2x", "A2y", "A2z"}, "A", "A2");
        model3.registerModel(new String[]{"A3x", "A3y", "A3z"}, "A", "A3");
        model3.registerModel(new String[]{"A4x", "A4y", "A4z"}, "A", "A4");

        model3.registerModel(new String[]{"B1x", "B1y", "B1z"}, "B", "B1");
        model3.registerModel(new String[]{"B2x", "B2y", "B2z"}, "B", "B2");
        
        model3.registerModel(new String[]{"C1x", "C1y", "C1z"}, "C", "C1");
        model3.registerModel(new String[]{"C2x", "C2y", "C2z"}, "C", "C2");
        model3.registerModel(new String[]{"C3x", "C3y", "C3z"}, "C", "C3");

        return Arrays.asList(new ComboBoxModel[]{model1, model2, model3});
    }
}
