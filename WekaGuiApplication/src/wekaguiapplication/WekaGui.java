package wekaguiapplication;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.filechooser.FileNameExtensionFilter;
import weka.classifiers.evaluation.ThresholdCurve;
import weka.classifiers.trees.RandomForest;
import weka.core.Utils;
import weka.gui.visualize.ThresholdVisualizePanel;

public class WekaGui extends javax.swing.JFrame {

    public WekaGui() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        list_Attributes = new javax.swing.JList<>();
        combo_Filters = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        txt_formatted_folds = new javax.swing.JFormattedTextField();
        bttn_Visualiser = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        Rad_Bttn_CrossValidation = new javax.swing.JRadioButton();
        Rad_Bttn_PercentageSplit = new javax.swing.JRadioButton();
        txt_formatted_perSplit = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.darkGray, java.awt.Color.black));

        jPanel1.setBorder(new javax.swing.border.MatteBorder(null));
        jPanel1.setForeground(new java.awt.Color(153, 153, 153));

        jButton2.setText("Open File");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Apply Filter");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(list_Attributes);

        combo_Filters.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Filter", "Multi Filter", "Attribute Selection", "Normalize", "Remove", "Replace Missing Values" }));

        jLabel6.setText("Select Filter");

        jLabel7.setText("Attributes");

        jButton4.setText("Undo");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(combo_Filters, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(31, 31, 31)
                        .addComponent(jButton3)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(304, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(combo_Filters, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton2))
                .addGap(30, 30, 30)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(196, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Preprocessing", jPanel1);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "NaiveBayes", "BayesNet", "J48", "LMT", "RandomForest", "RandomTree", "REPTree" }));

        jButton1.setText("Run Classifier");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setToolTipText("");
        jTextArea1.setAlignmentX(1.0F);
        jTextArea1.setAlignmentY(1.0F);
        jScrollPane1.setViewportView(jTextArea1);

        txt_formatted_folds.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txt_formatted_folds.setText("10");

        bttn_Visualiser.setText("Show Visualiser");
        bttn_Visualiser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttn_VisualiserActionPerformed(evt);
            }
        });

        jLabel1.setText("Enter Folds");

        jLabel2.setText("Select Classifier");

        buttonGroup1.add(Rad_Bttn_CrossValidation);
        Rad_Bttn_CrossValidation.setSelected(true);
        Rad_Bttn_CrossValidation.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                Rad_Bttn_CrossValidationStateChanged(evt);
            }
        });

        buttonGroup1.add(Rad_Bttn_PercentageSplit);
        Rad_Bttn_PercentageSplit.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                Rad_Bttn_PercentageSplitStateChanged(evt);
            }
        });

        txt_formatted_perSplit.setEditable(false);
        txt_formatted_perSplit.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txt_formatted_perSplit.setText("66");

        jLabel3.setText("Enter Percentage Split %");

        jLabel4.setText("Cross-Validation");

        jLabel5.setText("Percentage Split");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(8, 8, 8)
                                .addComponent(Rad_Bttn_PercentageSplit)
                                .addGap(8, 8, 8)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(txt_formatted_perSplit, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Rad_Bttn_CrossValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(txt_formatted_folds, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bttn_Visualiser, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(58, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Rad_Bttn_CrossValidation)
                                    .addComponent(jLabel4))
                                .addGap(9, 9, 9)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txt_formatted_perSplit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(Rad_Bttn_PercentageSplit, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                            .addComponent(jLabel5)
                                            .addGap(3, 3, 3)))))
                            .addComponent(txt_formatted_folds, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addGap(18, 18, 18)
                                .addComponent(bttn_Visualiser)))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Classify", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("TabbedPane");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static int bttn_State = -1;

    public static boolean filterState = false;
    public static String filterName;
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        System.out.println("filterstate= " + filterState);

        filterState = true;
        filterName = combo_Filters.getSelectedItem().toString();

        System.out.println("filtername= "+filterName);
        System.out.println("filterstate= " + filterState);
        
//        System.out.println("selected list: "+list_Attributes.getSelectedValue());

    }//GEN-LAST:event_jButton3ActionPerformed

    DefaultListModel listmodel = new DefaultListModel();
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        JFileChooser fileChoose = new JFileChooser();
        fileChoose.setCurrentDirectory(new File("user.dir"));

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Arff File", "arff");
        fileChoose.addChoosableFileFilter(filter);

        int fileSelectControl = fileChoose.showSaveDialog(null);

        if (fileSelectControl == JFileChooser.APPROVE_OPTION) {
            FileLocation = fileChoose.getSelectedFile().getAbsolutePath();
            System.out.println(FileLocation);

            FileLocation = ("milknew.arff");

        }

        EvaluationCalculating calc = new EvaluationCalculating();

        try {
            calc.getAttributes();
        } catch (Exception e) {

        }
        listmodel.clear();
        list_Attributes.setModel(listmodel);
        for (int i = 0; i < calc.attribute_size; i++) {
            listmodel.addElement(calc.attribute_names.get(i));
            list_Attributes.setModel(listmodel);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void Rad_Bttn_PercentageSplitStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_Rad_Bttn_PercentageSplitStateChanged

        if (Rad_Bttn_PercentageSplit.isSelected() == true) {
            txt_formatted_perSplit.setEditable(true);
            txt_formatted_folds.setEditable(false);
        } else if (Rad_Bttn_PercentageSplit.isSelected() == false) {

            txt_formatted_perSplit.setEditable(false);
            txt_formatted_folds.setEditable(true);

        }
    }//GEN-LAST:event_Rad_Bttn_PercentageSplitStateChanged

    private void Rad_Bttn_CrossValidationStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_Rad_Bttn_CrossValidationStateChanged

        if (Rad_Bttn_CrossValidation.isSelected() == true) {
            txt_formatted_perSplit.setEditable(false);
            txt_formatted_folds.setEditable(true);
        } else if (Rad_Bttn_PercentageSplit.isSelected() == false) {

            txt_formatted_perSplit.setEditable(true);
            txt_formatted_folds.setEditable(false);

        }
    }//GEN-LAST:event_Rad_Bttn_CrossValidationStateChanged

    private void bttn_VisualiserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttn_VisualiserActionPerformed

        EvaluationCalculating calc = new EvaluationCalculating();

        calc.jf.setVisible(true);
        calc.jf.setSize(800, 700);
    }//GEN-LAST:event_bttn_VisualiserActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        EvaluationCalculating calculate = new EvaluationCalculating();

        if (Rad_Bttn_CrossValidation.isSelected() == true) {
            bttn_State = 1;
        } else if (Rad_Bttn_PercentageSplit.isSelected() == true) {
            bttn_State = 0;
        }
        System.out.println("bttn_state: " + bttn_State);
        int number = Integer.parseInt(txt_formatted_folds.getText());
        float per = Float.parseFloat(txt_formatted_perSplit.getText());

        fold_count = number;
        percentage = per;

        percentage = per / 100;

        System.out.println("perc= " + percentage);
        jTextArea1.setText("");

        try {

            if (jComboBox1.getSelectedItem().equals("RandomForest")) {
                System.out.println("RandomForest");
                calculate.RFTree();
            } else if (jComboBox1.getSelectedItem().equals("BayesNet")) {
                System.out.println("BayesNet");
                calculate.BNBayes();
            } else if (jComboBox1.getSelectedItem().equals("NaiveBayes")) {
                System.out.println("NaiveBayes");
                calculate.NBBayes();
            } else if (jComboBox1.getSelectedItem().equals("J48")) {
                System.out.println("J48");
                calculate.J48Tree();
            } else if (jComboBox1.getSelectedItem().equals("LMT")) {
                System.out.println("LMT");
                calculate.LMTTree();
            } else if (jComboBox1.getSelectedItem().equals("REPTree")) {
                System.out.println("REPTree");
                calculate.REP_Tree();
            } else if (jComboBox1.getSelectedItem().equals("RandomTree")) {
                System.out.println("RandomTree");
                calculate.RTTree();
            }

        } catch (Exception e) {

        }
        jTextArea1.append(calculate.nameString);
        jTextArea1.append("\n");
        jTextArea1.append(calculate.summString);
        jTextArea1.append("\n");
        jTextArea1.append(calculate.classString);
        jTextArea1.append("\n");
        jTextArea1.append(calculate.matrixString);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        System.out.println("filterstate= " + filterState);
        filterState = false;
        System.out.println("filterstate= " + filterState);
    }//GEN-LAST:event_jButton4ActionPerformed

    public static String FileLocation;
    public static int fold_count;
    public static float percentage;

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new WekaGui().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JRadioButton Rad_Bttn_CrossValidation;
    public javax.swing.JRadioButton Rad_Bttn_PercentageSplit;
    private javax.swing.JButton bttn_Visualiser;
    public javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox<String> combo_Filters;
    public javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    public javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    public javax.swing.JPanel jPanel2;
    public javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JTabbedPane jTabbedPane1;
    public javax.swing.JTextArea jTextArea1;
    public javax.swing.JList<String> list_Attributes;
    private javax.swing.JFormattedTextField txt_formatted_folds;
    public javax.swing.JFormattedTextField txt_formatted_perSplit;
    // End of variables declaration//GEN-END:variables
}
