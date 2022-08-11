package wekaguiapplication;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        txt_formatted_folds = new javax.swing.JFormattedTextField();
        bttn_Visualiser = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton2.setText("Open File");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("jButton3");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3)
                    .addComponent(jButton2))
                .addContainerGap(885, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jButton2)
                .addGap(55, 55, 55)
                .addComponent(jButton3)
                .addContainerGap(374, Short.MAX_VALUE))
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bttn_Visualiser, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_formatted_folds)
                        .addComponent(jComboBox1, 0, 164, Short.MAX_VALUE))
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(113, 113, 113)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 679, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addGap(3, 3, 3)
                        .addComponent(txt_formatted_folds, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(bttn_Visualiser))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 466, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Classify", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jTabbedPane1)
                .addGap(3, 3, 3))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 541, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("TabbedPane");

        pack();
    }// </editor-fold>//GEN-END:initComponents
 
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
EvaluationCalculating calculate = new EvaluationCalculating(); 
        int number= Integer.parseInt(txt_formatted_folds.getText());

        fold_count=number;

        jTextArea1.setText("");

        try {

            if (jComboBox1.getSelectedItem().equals("RandomForest")) {
                System.out.println("RandomForest");
                calculate.RFTree();
            }

            else if (jComboBox1.getSelectedItem().equals("BayesNet")) {
                System.out.println("BayesNet");
                calculate.BNBayes();
            }

            else if (jComboBox1.getSelectedItem().equals("NaiveBayes")) {
                System.out.println("NaiveBayes");
                calculate.NBBayes();
            }

            else if (jComboBox1.getSelectedItem().equals("J48")) {
                System.out.println("J48");
                calculate.J48Tree();
            }

            else if (jComboBox1.getSelectedItem().equals("LMT")) {
                System.out.println("LMT");
                calculate.LMTTree();
            }

            else if (jComboBox1.getSelectedItem().equals("REPTree")) {
                System.out.println("REPTree");
                calculate.REP_Tree();
            }

            else if (jComboBox1.getSelectedItem().equals("RandomTree")) {
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

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        JFileChooser fileChoose = new JFileChooser();
        fileChoose.setCurrentDirectory(new File("user.dir"));

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Arff File", "arff");
        fileChoose.addChoosableFileFilter(filter);

        int fileSelectControl = fileChoose.showSaveDialog(null);

        if (fileSelectControl == JFileChooser.APPROVE_OPTION) {
            FileLocation = fileChoose.getSelectedFile().getAbsolutePath();
            System.out.println(FileLocation);

            FileLocation=("milknew.arff");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void bttn_VisualiserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttn_VisualiserActionPerformed
       
        EvaluationCalculating calc = new EvaluationCalculating();
                
        calc.jf.setVisible(true);
        calc.jf.setSize(800, 700);
    }//GEN-LAST:event_bttn_VisualiserActionPerformed

          
    public static String FileLocation;    
    public static int fold_count;    

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new WekaGui().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bttn_Visualiser;
    public javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    public javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    public javax.swing.JPanel jPanel2;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTabbedPane jTabbedPane1;
    public javax.swing.JTextArea jTextArea1;
    private javax.swing.JFormattedTextField txt_formatted_folds;
    // End of variables declaration//GEN-END:variables
}
