/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package wekaguiapplication;

import java.awt.BorderLayout;
import java.awt.List;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.ThresholdCurve;
import weka.classifiers.meta.FilteredClassifier;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomTree;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.LMT;
import weka.core.Utils;
import weka.gui.visualize.PlotData2D;
import weka.gui.visualize.ThresholdVisualizePanel;

/**
 *
 * @author Onur Eren Akgül
 */
public class testClass {

    public static final JFrame jf = new JFrame();

    public static int attribute_size;
//    public static String attribute_names[] ;
    public static ArrayList<String> attribute_names = new ArrayList<>();

    public void getAttributes() throws Exception {
        DataSource source = new DataSource("milknew.arff");
        Instances dataSet = source.getDataSet();

        attribute_size = dataSet.numAttributes();
//        System.out.println(dataSet.numAttributes());
//        System.out.println(source.getDataSet());
        for (int i = 0; i < dataSet.numAttributes(); i++) {

            attribute_names.add(dataSet.attribute(i).name());
            System.out.println("Attribute name: " + attribute_names.get(i));
        }
        System.out.println("Attribute name: " + attribute_names);
    }

    public void RFFtesting() throws Exception {

        getAttributes();

        DataSource source = new DataSource("milknew.arff");

        Instances dataSet = source.getDataSet();

        dataSet.randomize(new java.util.Random(0));
        
        int trainSize = (int) Math.round(dataSet.numInstances() * 0.66);

        int testSize = dataSet.numInstances() - trainSize;

        Instances train = new Instances(dataSet, 0, trainSize);

        Instances test = new Instances(dataSet, trainSize, testSize);

        dataSet.setClassIndex(dataSet.numAttributes() - 1);
        test.setClassIndex(test.numAttributes() - 1);
        train.setClassIndex(test.numAttributes() - 1);
        
        RandomForest RF = new RandomForest();

        AttributeSelection atrSelect = new AttributeSelection();

        // String[] options = new String[]{"-R","1"};
//        atrSelect.setOptions(options);
        FilteredClassifier FC = new FilteredClassifier();

        FC.setFilter(atrSelect);

        FC.setClassifier(RF);

        FC.buildClassifier(train);

        System.out.println(FC);
        //RF.buildClassifier(train);
        Evaluation eval = new Evaluation(train);

        eval.evaluateModel(FC, test);

        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
        System.out.println(eval.toClassDetailsString());
        System.out.println("----------------------------------------------");
        System.out.println(eval.toMatrixString());
        System.out.println("----------------------------------------------");

        /*DataSource source = new DataSource("milknew.arff");
        RandomForest RF = new RandomForest();

        Instances dataSet = source.getDataSet();

        dataSet.randomize(new java.util.Random(0));

        //Random rand = new Random(1);
        int trainSize = (int) Math.round(dataSet.numInstances() * 0.2);

        int testSize = dataSet.numInstances() - trainSize;

        Instances train = new Instances(dataSet, 0, trainSize);

        Instances test = new Instances(dataSet, trainSize, testSize);

        test.setClassIndex(test.numAttributes() - 1);

        RF.buildClassifier(test);

        Evaluation eval = new Evaluation(test);

        eval.evaluateModel(RF, test);

        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
        System.out.println(eval.toClassDetailsString());
        System.out.println("----------------------------------------------");
        System.out.println(eval.toMatrixString());
        System.out.println("----------------------------------------------");
         */
 /* nameString = RF.toString();
        summString = eval.toSummaryString("\n=== Summary ===\n", false);
        classString = eval.toClassDetailsString();
        matrixString = eval.toMatrixString();*/
 /*  ThresholdCurve tc = new ThresholdCurve();
        int classIndex = 0; // ROC for the 1st class label
        Instances curve = tc.getCurve(eval.predictions(), classIndex);

        PlotData2D plotdata = new PlotData2D(curve);
        plotdata.setPlotName(curve.relationName());
        plotdata.addInstanceNumberAttribute();

        ThresholdVisualizePanel tvp = new ThresholdVisualizePanel();
        tvp.setROCString("(Area under ROC = " + Utils.doubleToString(ThresholdCurve.getROCArea(curve), 4) + ")");
        tvp.setName(curve.relationName());
        tvp.addPlot(plotdata);

        
        jf.setName("WEKA ROC: " + tvp.getName());
        jf.setSize(500, 400);
        jf.getContentPane().setLayout(new BorderLayout());
        jf.getContentPane().add(tvp, BorderLayout.CENTER);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         */
    }
    public void RFtesting() throws Exception {

        
        DataSource source = new DataSource("milknew.arff");
        RandomForest RF = new RandomForest();

        Instances dataSet = source.getDataSet();

//        dataSet.randomize(new java.util.Random(0));

        //Random rand = new Random(1);
        int trainSize = (int) Math.round(dataSet.numInstances() * 0.2);

        int testSize = dataSet.numInstances() - trainSize;

        Instances train = new Instances(dataSet, 0, trainSize);

        Instances test = new Instances(dataSet, trainSize, testSize);

        test.setClassIndex(test.numAttributes() - 1);

        RF.buildClassifier(test);

        Evaluation eval = new Evaluation(test);

        eval.evaluateModel(RF, test);

        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
        System.out.println(eval.toClassDetailsString());
        System.out.println("----------------------------------------------");
        System.out.println(eval.toMatrixString());
        System.out.println("----------------------------------------------");
         
 /* nameString = RF.toString();
        summString = eval.toSummaryString("\n=== Summary ===\n", false);
        classString = eval.toClassDetailsString();
        matrixString = eval.toMatrixString();*/
 /*  ThresholdCurve tc = new ThresholdCurve();
        int classIndex = 0; // ROC for the 1st class label
        Instances curve = tc.getCurve(eval.predictions(), classIndex);

        PlotData2D plotdata = new PlotData2D(curve);
        plotdata.setPlotName(curve.relationName());
        plotdata.addInstanceNumberAttribute();

        ThresholdVisualizePanel tvp = new ThresholdVisualizePanel();
        tvp.setROCString("(Area under ROC = " + Utils.doubleToString(ThresholdCurve.getROCArea(curve), 4) + ")");
        tvp.setName(curve.relationName());
        tvp.addPlot(plotdata);

        
        jf.setName("WEKA ROC: " + tvp.getName());
        jf.setSize(500, 400);
        jf.getContentPane().setLayout(new BorderLayout());
        jf.getContentPane().add(tvp, BorderLayout.CENTER);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         */
    }

    public void RTtesting() throws Exception {

        DataSource source = new DataSource("milknew.arff");
        RandomTree RT = new RandomTree();

        Instances dataSet = source.getDataSet();

//        dataSet.randomize(new java.util.Random(0));
        //Random rand = new Random(1);
        int trainSize = (int) Math.round(dataSet.numInstances() * 0.2);

        int testSize = dataSet.numInstances() - trainSize;

        Instances train = new Instances(dataSet, 0, trainSize);

        Instances test = new Instances(dataSet, trainSize, testSize);

        test.setClassIndex(test.numAttributes() - 1);

        RT.buildClassifier(test);

        Evaluation eval = new Evaluation(test);

        eval.evaluateModel(RT, test);

        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
        System.out.println(eval.toClassDetailsString());
        System.out.println("----------------------------------------------");
        System.out.println(eval.toMatrixString());
        System.out.println("----------------------------------------------");

        /* nameString = RT.toString();
        summString = eval.toSummaryString("\n=== Summary ===\n", false);
        classString = eval.toClassDetailsString();
        matrixString = eval.toMatrixString();*/
 /*  ThresholdCurve tc = new ThresholdCurve();
        int classIndex = 0; // ROC for the 1st class label
        Instances curve = tc.getCurve(eval.predictions(), classIndex);

        PlotData2D plotdata = new PlotData2D(curve);
        plotdata.setPlotName(curve.relationName());
        plotdata.addInstanceNumberAttribute();

        ThresholdVisualizePanel tvp = new ThresholdVisualizePanel();
        tvp.setROCString("(Area under ROC = " + Utils.doubleToString(ThresholdCurve.getROCArea(curve), 4) + ")");
        tvp.setName(curve.relationName());
        tvp.addPlot(plotdata);

        
        jf.setName("WEKA ROC: " + tvp.getName());
        jf.setSize(500, 400);
        jf.getContentPane().setLayout(new BorderLayout());
        jf.getContentPane().add(tvp, BorderLayout.CENTER);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         */
    }

    public void J48testing() throws Exception {

        DataSource source = new DataSource("milknew.arff");
        J48 j48tree = new J48();

        Instances dataSet = source.getDataSet();

//        dataSet.randomize(new java.util.Random(0));
        //Random rand = new Random(1);
        int trainSize = (int) Math.round(dataSet.numInstances() * 0.2);

        int testSize = dataSet.numInstances() - trainSize;

        Instances train = new Instances(dataSet, 0, trainSize);

        Instances test = new Instances(dataSet, trainSize, testSize);

        test.setClassIndex(test.numAttributes() - 1);

        j48tree.buildClassifier(test);

        Evaluation eval = new Evaluation(test);

        eval.evaluateModel(j48tree, test);

        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
        System.out.println(eval.toClassDetailsString());
        System.out.println("----------------------------------------------");
        System.out.println(eval.toMatrixString());
        System.out.println("----------------------------------------------");

        /* nameString = j48tree.toString();
        summString = eval.toSummaryString("\n=== Summary ===\n", false);
        classString = eval.toClassDetailsString();
        matrixString = eval.toMatrixString();*/
 /*  ThresholdCurve tc = new ThresholdCurve();
        int classIndex = 0; // ROC for the 1st class label
        Instances curve = tc.getCurve(eval.predictions(), classIndex);

        PlotData2D plotdata = new PlotData2D(curve);
        plotdata.setPlotName(curve.relationName());
        plotdata.addInstanceNumberAttribute();

        ThresholdVisualizePanel tvp = new ThresholdVisualizePanel();
        tvp.setROCString("(Area under ROC = " + Utils.doubleToString(ThresholdCurve.getROCArea(curve), 4) + ")");
        tvp.setName(curve.relationName());
        tvp.addPlot(plotdata);

        
        jf.setName("WEKA ROC: " + tvp.getName());
        jf.setSize(500, 400);
        jf.getContentPane().setLayout(new BorderLayout());
        jf.getContentPane().add(tvp, BorderLayout.CENTER);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         */
    }

    public void NBtesting() throws Exception {

        DataSource source = new DataSource("milknew.arff");
        NaiveBayes NB = new NaiveBayes();

        Instances dataSet = source.getDataSet();

//        dataSet.randomize(new java.util.Random(0));
        //Random rand = new Random(1);
        int trainSize = (int) Math.round(dataSet.numInstances() * 0.2);

        int testSize = dataSet.numInstances() - trainSize;

        Instances train = new Instances(dataSet, 0, trainSize);

        Instances test = new Instances(dataSet, trainSize, testSize);

        test.setClassIndex(test.numAttributes() - 1);

        NB.buildClassifier(test);

        Evaluation eval = new Evaluation(test);

        eval.evaluateModel(NB, test);

        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
        System.out.println(eval.toClassDetailsString());
        System.out.println("----------------------------------------------");
        System.out.println(eval.toMatrixString());
        System.out.println("----------------------------------------------");

        /* nameString = NB.toString();
        summString = eval.toSummaryString("\n=== Summary ===\n", false);
        classString = eval.toClassDetailsString();
        matrixString = eval.toMatrixString();*/
 /*  ThresholdCurve tc = new ThresholdCurve();
        int classIndex = 0; // ROC for the 1st class label
        Instances curve = tc.getCurve(eval.predictions(), classIndex);

        PlotData2D plotdata = new PlotData2D(curve);
        plotdata.setPlotName(curve.relationName());
        plotdata.addInstanceNumberAttribute();

        ThresholdVisualizePanel tvp = new ThresholdVisualizePanel();
        tvp.setROCString("(Area under ROC = " + Utils.doubleToString(ThresholdCurve.getROCArea(curve), 4) + ")");
        tvp.setName(curve.relationName());
        tvp.addPlot(plotdata);

        
        jf.setName("WEKA ROC: " + tvp.getName());
        jf.setSize(500, 400);
        jf.getContentPane().setLayout(new BorderLayout());
        jf.getContentPane().add(tvp, BorderLayout.CENTER);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         */
    }

    public void BNtesting() throws Exception {

        DataSource source = new DataSource("milknew.arff");
        BayesNet BN = new BayesNet();

        Instances dataSet = source.getDataSet();

        dataSet.randomize(new java.util.Random(0));
        dataSet.randomize(new java.util.Random(0));
        dataSet.randomize(new java.util.Random(0));
        dataSet.randomize(new java.util.Random(0));
        dataSet.randomize(new java.util.Random(0));
        dataSet.randomize(new java.util.Random(0));
        dataSet.randomize(new java.util.Random(0));
        dataSet.randomize(new java.util.Random(0));
        dataSet.randomize(new java.util.Random(0));
        dataSet.randomize(new java.util.Random(0));
        dataSet.randomize(new java.util.Random(0));

        //  Random rand = new Random(1);
        int trainSize = (int) Math.round(dataSet.numInstances() * 0.2);

        int testSize = dataSet.numInstances() - trainSize;

        Instances train = new Instances(dataSet, 0, trainSize);

        Instances test = new Instances(dataSet, trainSize, testSize);

        test.setClassIndex(test.numAttributes() - 1);

        BN.buildClassifier(test);

        Evaluation eval = new Evaluation(test);

        eval.evaluateModel(BN, test);

        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
        System.out.println(eval.toClassDetailsString());
        System.out.println("----------------------------------------------");
        System.out.println(eval.toMatrixString());
        System.out.println("----------------------------------------------");

        /* nameString = BN.toString();
        summString = eval.toSummaryString("\n=== Summary ===\n", false);
        classString = eval.toClassDetailsString();
        matrixString = eval.toMatrixString();*/
 /*  ThresholdCurve tc = new ThresholdCurve();
        int classIndex = 0; // ROC for the 1st class label
        Instances curve = tc.getCurve(eval.predictions(), classIndex);

        PlotData2D plotdata = new PlotData2D(curve);
        plotdata.setPlotName(curve.relationName());
        plotdata.addInstanceNumberAttribute();

        ThresholdVisualizePanel tvp = new ThresholdVisualizePanel();
        tvp.setROCString("(Area under ROC = " + Utils.doubleToString(ThresholdCurve.getROCArea(curve), 4) + ")");
        tvp.setName(curve.relationName());
        tvp.addPlot(plotdata);

        
        jf.setName("WEKA ROC: " + tvp.getName());
        jf.setSize(500, 400);
        jf.getContentPane().setLayout(new BorderLayout());
        jf.getContentPane().add(tvp, BorderLayout.CENTER);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         */
    }

    public void REPtesting() throws Exception {

        DataSource source = new DataSource("milknew.arff");
        REPTree REP = new REPTree();

        Instances dataSet = source.getDataSet();

        dataSet.randomize(new java.util.Random(0));

        //Random rand = new Random(1);
        int trainSize = (int) Math.round(dataSet.numInstances() * 0.2);

        int testSize = dataSet.numInstances() - trainSize;

        Instances train = new Instances(dataSet, 0, trainSize);

        Instances test = new Instances(dataSet, trainSize, testSize);

        test.setClassIndex(test.numAttributes() - 1);

        REP.buildClassifier(test);

        Evaluation eval = new Evaluation(test);

        eval.evaluateModel(REP, test);

        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
        System.out.println(eval.toClassDetailsString());
        System.out.println("----------------------------------------------");
        System.out.println(eval.toMatrixString());
        System.out.println("----------------------------------------------");

        /* nameString = REP.toString();
        summString = eval.toSummaryString("\n=== Summary ===\n", false);
        classString = eval.toClassDetailsString();
        matrixString = eval.toMatrixString();*/
 /*  ThresholdCurve tc = new ThresholdCurve();
        int classIndex = 0; // ROC for the 1st class label
        Instances curve = tc.getCurve(eval.predictions(), classIndex);

        PlotData2D plotdata = new PlotData2D(curve);
        plotdata.setPlotName(curve.relationName());
        plotdata.addInstanceNumberAttribute();

        ThresholdVisualizePanel tvp = new ThresholdVisualizePanel();
        tvp.setROCString("(Area under ROC = " + Utils.doubleToString(ThresholdCurve.getROCArea(curve), 4) + ")");
        tvp.setName(curve.relationName());
        tvp.addPlot(plotdata);

        
        jf.setName("WEKA ROC: " + tvp.getName());
        jf.setSize(500, 400);
        jf.getContentPane().setLayout(new BorderLayout());
        jf.getContentPane().add(tvp, BorderLayout.CENTER);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         */
    }

    public void LMTtesting() throws Exception {

        DataSource source = new DataSource("milknew.arff");
        LMT LMTree = new LMT();

        Instances dataSet = source.getDataSet();

        dataSet.randomize(new java.util.Random(0));

        //Random rand = new Random(1);
        int trainSize = (int) Math.round(dataSet.numInstances() * 0.2);

        int testSize = dataSet.numInstances() - trainSize;

        Instances train = new Instances(dataSet, 0, trainSize);

        Instances test = new Instances(dataSet, trainSize, testSize);

        test.setClassIndex(test.numAttributes() - 1);

        LMTree.buildClassifier(test);

        Evaluation eval = new Evaluation(test);

        eval.evaluateModel(LMTree, test);

        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
        System.out.println(eval.toClassDetailsString());
        System.out.println("----------------------------------------------");
        System.out.println(eval.toMatrixString());
        System.out.println("----------------------------------------------");

        /* nameString = LMTree.toString();
        summString = eval.toSummaryString("\n=== Summary ===\n", false);
        classString = eval.toClassDetailsString();
        matrixString = eval.toMatrixString();*/
 /*  ThresholdCurve tc = new ThresholdCurve();
        int classIndex = 0; // ROC for the 1st class label
        Instances curve = tc.getCurve(eval.predictions(), classIndex);

        PlotData2D plotdata = new PlotData2D(curve);
        plotdata.setPlotName(curve.relationName());
        plotdata.addInstanceNumberAttribute();

        ThresholdVisualizePanel tvp = new ThresholdVisualizePanel();
        tvp.setROCString("(Area under ROC = " + Utils.doubleToString(ThresholdCurve.getROCArea(curve), 4) + ")");
        tvp.setName(curve.relationName());
        tvp.addPlot(plotdata);

        
        jf.setName("WEKA ROC: " + tvp.getName());
        jf.setSize(500, 400);
        jf.getContentPane().setLayout(new BorderLayout());
        jf.getContentPane().add(tvp, BorderLayout.CENTER);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         */
    }
}
