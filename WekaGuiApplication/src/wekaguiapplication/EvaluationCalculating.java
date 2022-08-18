package wekaguiapplication;

import java.awt.BorderLayout;
import weka.filters.AllFilter;
import weka.filters.MultiFilter;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;
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
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomTree;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.LMT;
import weka.core.Utils;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.gui.visualize.PlotData2D;
import weka.gui.visualize.ThresholdVisualizePanel;

public class EvaluationCalculating {

    WekaGui gui = new WekaGui();
    public String nameString;
    public String summString;
    public String classString;
    public String matrixString;
    String FileLocation = gui.FileLocation;
    public static final JFrame jf = new JFrame();

    private int default_fold = 10;

    public static int attribute_size;
//    public static String attribute_names[] ;
    public static ArrayList<String> attribute_names = new ArrayList<>();

    public void getAttributes() throws Exception {
        DataSource source = new DataSource(FileLocation);
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

    public void RFTree() throws Exception {

        if (gui.filterState == true) {

            switch (gui.filterName) {
                case "Attribute Selection":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        RandomForest RF = new RandomForest();

                        AttributeSelection atrSelect = new AttributeSelection();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(atrSelect);

                        FC.setClassifier(RF);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        RandomForest RF = new RandomForest();
                        FilteredClassifier FC = new FilteredClassifier();
                        AttributeSelection atrSelect = new AttributeSelection();

                        FC.setFilter(atrSelect);
                        FC.setClassifier(RF);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;
                case "Multi Filter":

                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("MULTI PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        RandomForest RF = new RandomForest();

                        MultiFilter multi = new MultiFilter();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(multi);

                        FC.setClassifier(RF);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("MULTİ CROSS");
                        RandomForest RF = new RandomForest();
                        FilteredClassifier FC = new FilteredClassifier();
                        MultiFilter multi = new MultiFilter();

                        FC.setFilter(multi);
                        FC.setClassifier(RF);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }

                    break;
                case "Remove":

                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("REMOVE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        RandomForest RF = new RandomForest();

                        Remove remove = new Remove();

                        String[] options = new String[]{"-R", Integer.toString(gui.list_Attributes.getSelectedIndex())};
                        remove.setOptions(options);
                        
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(remove);

                        FC.setClassifier(RF);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("REMOVE CROSS");
                        RandomForest RF = new RandomForest();
                        FilteredClassifier FC = new FilteredClassifier();
                        Remove remove = new Remove();

//                        String[] options = new String[]{"-R", Integer.toString(gui.list_Attributes.getSelectedIndex())};
//                        remove.setOptions(options);
                        
                        FC.setFilter(remove);
                        FC.setClassifier(RF);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }

                    break;
                case "Normalize":

                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("NORMALIZE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        RandomForest RF = new RandomForest();

                        Normalize normalize = new Normalize();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(normalize);

                        FC.setClassifier(RF);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("NORMALIZE CROSS");
                        RandomForest RF = new RandomForest();
                        FilteredClassifier FC = new FilteredClassifier();
                        Normalize normalize = new Normalize();

                        FC.setFilter(normalize);
                        FC.setClassifier(RF);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }

                    break;
                case "All Filter":

                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ALL FILTER PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        RandomForest RF = new RandomForest();

                        AllFilter allfilt = new AllFilter();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(allfilt);

                        FC.setClassifier(RF);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ALL FILTER CROSS");
                        RandomForest RF = new RandomForest();
                        FilteredClassifier FC = new FilteredClassifier();
                        AllFilter allfilt = new AllFilter();

                        FC.setFilter(allfilt);
                        FC.setClassifier(RF);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }

                    break;
                case "Replace Missing Values":

                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("REPLACE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        RandomForest RF = new RandomForest();

                        ReplaceMissingValues RMV = new ReplaceMissingValues();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(RMV);

                        FC.setClassifier(RF);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("REPLACE CROSS");
                        RandomForest RF = new RandomForest();
                        FilteredClassifier FC = new FilteredClassifier();
                        ReplaceMissingValues RMV = new ReplaceMissingValues();

                        FC.setFilter(RMV);
                        FC.setClassifier(RF);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }

                    break;

                default:

            }

        } else {
            if (gui.bttn_State == 1) {

                //UNFILTERED CROSS
                System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                System.out.println("bttnstate= " + gui.bttn_State);
                DataSource source = new DataSource(FileLocation);
                RandomForest RF = new RandomForest();

                Instances dataSet = source.getDataSet();

                System.out.println("CROSS VALIDATON");
                //Instances dataSet = source.getDataSet();
                dataSet.setClassIndex(dataSet.numAttributes() - 1);

                RF.buildClassifier(dataSet);
                System.out.println(RF);
                System.out.println("----------------------------------------------");

                Evaluation eval = new Evaluation(dataSet);

                Random rand = new Random(1);

                int folds = default_fold;

                folds = gui.fold_count;

                DataSource source2 = new DataSource(FileLocation);

                Instances testDataSet = source2.getDataSet();

                testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                eval.crossValidateModel(RF, testDataSet, folds, rand);

                nameString = RF.toString();
                summString = eval.toSummaryString("\n=== Summary ===\n", false);
                classString = eval.toClassDetailsString();
                matrixString = eval.toMatrixString();

                System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                System.out.println(eval.toClassDetailsString());
                System.out.println("----------------------------------------------");
                System.out.println(eval.toMatrixString());
                System.out.println("----------------------------------------------");

                ThresholdCurve tc = new ThresholdCurve();
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

            } else if (gui.bttn_State == 0) {

                //UNFILTERED PERCENTAGE
                System.out.println("bttnstate= " + gui.bttn_State);
                DataSource source = new DataSource(FileLocation);
                RandomForest RF = new RandomForest();

                Instances dataSet = source.getDataSet();

                System.out.println("SPLIT PERCENTAGE ");
                dataSet.randomize(new java.util.Random(2));

                //Random rand = new Random(1);
                int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                int testSize = dataSet.numInstances() - trainSize;

                Instances train = new Instances(dataSet, 0, trainSize);

                Instances test = new Instances(dataSet, trainSize, testSize);

                test.setClassIndex(test.numAttributes() - 1);
                train.setClassIndex(test.numAttributes() - 1);

                RF.buildClassifier(train);

                Evaluation eval = new Evaluation(train);

                eval.evaluateModel(RF, test);

                System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                System.out.println(eval.toClassDetailsString());
                System.out.println("----------------------------------------------");
                System.out.println(eval.toMatrixString());
                System.out.println("----------------------------------------------");

                nameString = RF.toString();
                summString = eval.toSummaryString("\n=== Summary ===\n", false);
                classString = eval.toClassDetailsString();
                matrixString = eval.toMatrixString();
                ThresholdCurve tc = new ThresholdCurve();
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

            }
        }
    }

    public void RTTree() throws Exception {

        if (gui.filterState == true) {

            switch (gui.filterName) {
                case "Attribute Selection":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        RandomTree RT = new RandomTree();

                        AttributeSelection atrSelect = new AttributeSelection();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(atrSelect);

                        FC.setClassifier(RT);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        RandomTree RT = new RandomTree();
                        FilteredClassifier FC = new FilteredClassifier();
                        AttributeSelection atrSelect = new AttributeSelection();

                        FC.setFilter(atrSelect);
                        FC.setClassifier(RT);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;

                case "Multi Filter":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        RandomTree RT = new RandomTree();

                        MultiFilter multi = new MultiFilter();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(multi);

                        FC.setClassifier(RT);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        RandomTree RT = new RandomTree();
                        FilteredClassifier FC = new FilteredClassifier();
                        MultiFilter multi = new MultiFilter();

                        FC.setFilter(multi);
                        FC.setClassifier(RT);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;
                case "Remove":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        RandomTree RT = new RandomTree();

                        Remove remove = new Remove();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(remove);

                        FC.setClassifier(RT);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        RandomTree RT = new RandomTree();
                        FilteredClassifier FC = new FilteredClassifier();
                        Remove remove = new Remove();

                        FC.setFilter(remove);
                        FC.setClassifier(RT);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;
                case "Normalize":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        RandomTree RT = new RandomTree();

                        Normalize normalize = new Normalize();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(normalize);

                        FC.setClassifier(RT);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        RandomTree RT = new RandomTree();
                        FilteredClassifier FC = new FilteredClassifier();
                        Normalize normalize = new Normalize();

                        FC.setFilter(normalize);
                        FC.setClassifier(RT);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;
                case "All Filter":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        RandomTree RT = new RandomTree();

                        AllFilter allfilt = new AllFilter();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(allfilt);

                        FC.setClassifier(RT);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        RandomTree RT = new RandomTree();
                        FilteredClassifier FC = new FilteredClassifier();
                        AllFilter allfilt = new AllFilter();

                        FC.setFilter(allfilt);
                        FC.setClassifier(RT);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;
                case "Replace Missing Values":

                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        RandomTree RT = new RandomTree();

                        ReplaceMissingValues RMV = new ReplaceMissingValues();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(RMV);

                        FC.setClassifier(RT);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        RandomTree RT = new RandomTree();
                        FilteredClassifier FC = new FilteredClassifier();
                        ReplaceMissingValues RMV = new ReplaceMissingValues();

                        FC.setFilter(RMV);
                        FC.setClassifier(RT);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;

            }
        } else {
            if (gui.bttn_State == 1) {
                System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                System.out.println("bttnstate= " + gui.bttn_State);

                DataSource source = new DataSource(FileLocation);
                Instances dataSet = source.getDataSet();

                dataSet.setClassIndex(dataSet.numAttributes() - 1);

                RandomTree RT = new RandomTree();
                RT.buildClassifier(dataSet);
                System.out.println(RT);
                System.out.println("----------------------------------------------");
                Evaluation eval = new Evaluation(dataSet);
                Random rand = new Random(1);

                int folds = default_fold;
                folds = gui.fold_count;

                DataSource source2 = new DataSource(FileLocation);

                Instances testDataSet = source2.getDataSet();

                testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                eval.crossValidateModel(RT, testDataSet, folds, rand);

                nameString = RT.toString();
                summString = eval.toSummaryString("\n=== Summary ===\n", false);
                classString = eval.toClassDetailsString();
                matrixString = eval.toMatrixString();

                System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                System.out.println(eval.toClassDetailsString());
                System.out.println("----------------------------------------------");
                System.out.println(eval.toMatrixString());
                System.out.println("----------------------------------------------");

                ThresholdCurve tc = new ThresholdCurve();
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
            } else if (gui.bttn_State == 0) {

                System.out.println("bttnstate= " + gui.bttn_State);
                DataSource source = new DataSource(FileLocation);
                RandomTree RT = new RandomTree();

                Instances dataSet = source.getDataSet();

                System.out.println("SPLIT PERCENTAGE ");
                dataSet.randomize(new java.util.Random(0));

                //Random rand = new Random(1);
                int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                int testSize = dataSet.numInstances() - trainSize;

                Instances train = new Instances(dataSet, 0, trainSize);

                Instances test = new Instances(dataSet, trainSize, testSize);

                test.setClassIndex(test.numAttributes() - 1);
                train.setClassIndex(test.numAttributes() - 1);

                RT.buildClassifier(train);

                Evaluation eval = new Evaluation(train);

                eval.evaluateModel(RT, test);

                System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                System.out.println(eval.toClassDetailsString());
                System.out.println("----------------------------------------------");
                System.out.println(eval.toMatrixString());
                System.out.println("----------------------------------------------");

                nameString = RT.toString();
                summString = eval.toSummaryString("\n=== Summary ===\n", false);
                classString = eval.toClassDetailsString();
                matrixString = eval.toMatrixString();
                ThresholdCurve tc = new ThresholdCurve();
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

            }

        }
    }

    public void J48Tree() throws Exception {
        if (gui.filterState == true) {

            switch (gui.filterName) {
                case "Attribute Selection":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        J48 j48tree = new J48();

                        AttributeSelection atrSelect = new AttributeSelection();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(atrSelect);

                        FC.setClassifier(j48tree);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        J48 j48tree = new J48();
                        FilteredClassifier FC = new FilteredClassifier();
                        AttributeSelection atrSelect = new AttributeSelection();

                        FC.setFilter(atrSelect);
                        FC.setClassifier(j48tree);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;

                case "Multi Filter":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        J48 j48tree = new J48();

                        MultiFilter multi = new MultiFilter();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(multi);

                        FC.setClassifier(j48tree);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        J48 j48tree = new J48();
                        FilteredClassifier FC = new FilteredClassifier();
                        MultiFilter multi = new MultiFilter();

                        FC.setFilter(multi);
                        FC.setClassifier(j48tree);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;
                case "Remove":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        J48 j48tree = new J48();

                        Remove remove = new Remove();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(remove);

                        FC.setClassifier(j48tree);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        J48 j48tree = new J48();
                        FilteredClassifier FC = new FilteredClassifier();
                        Remove remove = new Remove();

                        FC.setFilter(remove);
                        FC.setClassifier(j48tree);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;
                case "Normalize":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        J48 j48tree = new J48();

                        Normalize normalize = new Normalize();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(normalize);

                        FC.setClassifier(j48tree);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        J48 j48tree = new J48();
                        FilteredClassifier FC = new FilteredClassifier();
                        Normalize normalize = new Normalize();

                        FC.setFilter(normalize);
                        FC.setClassifier(j48tree);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;
                case "All Filter":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        J48 j48tree = new J48();

                        AllFilter allfilt = new AllFilter();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(allfilt);

                        FC.setClassifier(j48tree);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        J48 j48tree = new J48();
                        FilteredClassifier FC = new FilteredClassifier();
                        AllFilter allfilt = new AllFilter();

                        FC.setFilter(allfilt);
                        FC.setClassifier(j48tree);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;
                case "Replace Missing Values":

                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        J48 j48tree = new J48();

                        ReplaceMissingValues RMV = new ReplaceMissingValues();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(RMV);

                        FC.setClassifier(j48tree);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        J48 j48tree = new J48();
                        FilteredClassifier FC = new FilteredClassifier();
                        ReplaceMissingValues RMV = new ReplaceMissingValues();

                        FC.setFilter(RMV);
                        FC.setClassifier(j48tree);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;

            }
        } else {
            if (gui.bttn_State == 1) {
                System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                System.out.println("bttnstate= " + gui.bttn_State);

                DataSource source = new DataSource(FileLocation);
                Instances dataSet = source.getDataSet();

                dataSet.setClassIndex(dataSet.numAttributes() - 1);

                J48 j48tree = new J48();
                j48tree.buildClassifier(dataSet);
                System.out.println(j48tree);
                System.out.println("----------------------------------------------");

                Evaluation eval = new Evaluation(dataSet);
                Random rand = new Random(1);

                int folds = default_fold;
                folds = gui.fold_count;

                DataSource source2 = new DataSource(FileLocation);

                Instances testDataSet = source2.getDataSet();

                testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                eval.crossValidateModel(j48tree, testDataSet, folds, rand);

                nameString = j48tree.toString();
                summString = eval.toSummaryString("\n=== Summary ===\n", false);
                classString = eval.toClassDetailsString();
                matrixString = eval.toMatrixString();

                System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                System.out.println(eval.toClassDetailsString());
                System.out.println("----------------------------------------------");
                System.out.println(eval.toMatrixString());
                System.out.println("----------------------------------------------");

                ThresholdCurve tc = new ThresholdCurve();
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
            } else if (gui.bttn_State == 0) {

                System.out.println("bttnstate= " + gui.bttn_State);
                DataSource source = new DataSource(FileLocation);
                J48 j48tree = new J48();

                Instances dataSet = source.getDataSet();

                System.out.println("SPLIT PERCENTAGE ");
                dataSet.randomize(new java.util.Random(0));

                //Random rand = new Random(1);
                int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                int testSize = dataSet.numInstances() - trainSize;

                Instances train = new Instances(dataSet, 0, trainSize);

                Instances test = new Instances(dataSet, trainSize, testSize);

                test.setClassIndex(test.numAttributes() - 1);
                train.setClassIndex(test.numAttributes() - 1);

                j48tree.buildClassifier(train);

                Evaluation eval = new Evaluation(train);

                eval.evaluateModel(j48tree, test);

                System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                System.out.println(eval.toClassDetailsString());
                System.out.println("----------------------------------------------");
                System.out.println(eval.toMatrixString());
                System.out.println("----------------------------------------------");

                nameString = j48tree.toString();
                summString = eval.toSummaryString("\n=== Summary ===\n", false);
                classString = eval.toClassDetailsString();
                matrixString = eval.toMatrixString();
                ThresholdCurve tc = new ThresholdCurve();
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

            }

        }
    }

    public void BNBayes() throws Exception {
        if (gui.filterState == true) {

            switch (gui.filterName) {
                case "Attribute Selection":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        BayesNet BN = new BayesNet();

                        AttributeSelection atrSelect = new AttributeSelection();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(atrSelect);

                        FC.setClassifier(BN);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        BayesNet BN = new BayesNet();
                        FilteredClassifier FC = new FilteredClassifier();
                        AttributeSelection atrSelect = new AttributeSelection();

                        FC.setFilter(atrSelect);
                        FC.setClassifier(BN);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;

                case "Multi Filter":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        BayesNet BN = new BayesNet();

                        MultiFilter multi = new MultiFilter();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(multi);

                        FC.setClassifier(BN);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        BayesNet BN = new BayesNet();
                        FilteredClassifier FC = new FilteredClassifier();
                        MultiFilter multi = new MultiFilter();

                        FC.setFilter(multi);
                        FC.setClassifier(BN);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;
                case "Remove":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        BayesNet BN = new BayesNet();

                        Remove remove = new Remove();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(remove);

                        FC.setClassifier(BN);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        BayesNet BN = new BayesNet();
                        FilteredClassifier FC = new FilteredClassifier();
                        Remove remove = new Remove();

                        FC.setFilter(remove);
                        FC.setClassifier(BN);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;
                case "Normalize":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        BayesNet BN = new BayesNet();

                        Normalize normalize = new Normalize();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(normalize);

                        FC.setClassifier(BN);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        BayesNet BN = new BayesNet();
                        FilteredClassifier FC = new FilteredClassifier();
                        Normalize normalize = new Normalize();

                        FC.setFilter(normalize);
                        FC.setClassifier(BN);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;
                case "All Filter":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        BayesNet BN = new BayesNet();

                        AllFilter allfilt = new AllFilter();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(allfilt);

                        FC.setClassifier(BN);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        BayesNet BN = new BayesNet();
                        FilteredClassifier FC = new FilteredClassifier();
                        AllFilter allfilt = new AllFilter();

                        FC.setFilter(allfilt);
                        FC.setClassifier(BN);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;
                case "Replace Missing Values":

                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        BayesNet BN = new BayesNet();

                        ReplaceMissingValues RMV = new ReplaceMissingValues();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(RMV);

                        FC.setClassifier(BN);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        BayesNet BN = new BayesNet();
                        FilteredClassifier FC = new FilteredClassifier();
                        ReplaceMissingValues RMV = new ReplaceMissingValues();

                        FC.setFilter(RMV);
                        FC.setClassifier(BN);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;

            }
        } else {
            if (gui.bttn_State == 1) {
                System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                System.out.println("bttnstate= " + gui.bttn_State);

                DataSource source = new DataSource(FileLocation);
                Instances dataSet = source.getDataSet();

                dataSet.setClassIndex(dataSet.numAttributes() - 1);

                BayesNet BN = new BayesNet();
                BN.buildClassifier(dataSet);
                System.out.println(BN);
                System.out.println("----------------------------------------------");

                Evaluation eval = new Evaluation(dataSet);
                Random rand = new Random(1);

                int folds = default_fold;
                folds = gui.fold_count;

                DataSource source2 = new DataSource(FileLocation);

                Instances testDataSet = source2.getDataSet();

                testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                eval.crossValidateModel(BN, testDataSet, folds, rand);

                nameString = BN.toString();
                summString = eval.toSummaryString("\n=== Summary ===\n", false);
                classString = eval.toClassDetailsString();
                matrixString = eval.toMatrixString();

                System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                System.out.println(eval.toClassDetailsString());
                System.out.println("----------------------------------------------");
                System.out.println(eval.toMatrixString());
                System.out.println("----------------------------------------------");

                ThresholdCurve tc = new ThresholdCurve();
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
            } else if (gui.bttn_State == 0) {

                System.out.println("bttnstate= " + gui.bttn_State);
                DataSource source = new DataSource(FileLocation);
                BayesNet BN = new BayesNet();

                Instances dataSet = source.getDataSet();

                System.out.println("SPLIT PERCENTAGE ");
                dataSet.randomize(new java.util.Random(0));

                //Random rand = new Random(1);
                int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                int testSize = dataSet.numInstances() - trainSize;

                Instances train = new Instances(dataSet, 0, trainSize);

                Instances test = new Instances(dataSet, trainSize, testSize);

                test.setClassIndex(test.numAttributes() - 1);
                train.setClassIndex(test.numAttributes() - 1);

                BN.buildClassifier(train);

                Evaluation eval = new Evaluation(train);

                eval.evaluateModel(BN, test);

                System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                System.out.println(eval.toClassDetailsString());
                System.out.println("----------------------------------------------");
                System.out.println(eval.toMatrixString());
                System.out.println("----------------------------------------------");

                nameString = BN.toString();
                summString = eval.toSummaryString("\n=== Summary ===\n", false);
                classString = eval.toClassDetailsString();
                matrixString = eval.toMatrixString();
                ThresholdCurve tc = new ThresholdCurve();
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

            }
        }
    }

    public void NBBayes() throws Exception {
        if (gui.filterState == true) {

            switch (gui.filterName) {
                case "Attribute Selection":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        NaiveBayes NB = new NaiveBayes();

                        AttributeSelection atrSelect = new AttributeSelection();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(atrSelect);

                        FC.setClassifier(NB);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        NaiveBayes NB = new NaiveBayes();
                        FilteredClassifier FC = new FilteredClassifier();
                        AttributeSelection atrSelect = new AttributeSelection();

                        FC.setFilter(atrSelect);
                        FC.setClassifier(NB);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;

                case "Multi Filter":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        NaiveBayes NB = new NaiveBayes();

                        MultiFilter multi = new MultiFilter();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(multi);

                        FC.setClassifier(NB);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        NaiveBayes NB = new NaiveBayes();
                        FilteredClassifier FC = new FilteredClassifier();
                        MultiFilter multi = new MultiFilter();

                        FC.setFilter(multi);
                        FC.setClassifier(NB);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;
                case "Remove":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        NaiveBayes NB = new NaiveBayes();

                        Remove remove = new Remove();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(remove);

                        FC.setClassifier(NB);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        NaiveBayes NB = new NaiveBayes();
                        FilteredClassifier FC = new FilteredClassifier();
                        Remove remove = new Remove();

                        FC.setFilter(remove);
                        FC.setClassifier(NB);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;
                case "Normalize":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        NaiveBayes NB = new NaiveBayes();

                        Normalize normalize = new Normalize();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(normalize);

                        FC.setClassifier(NB);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        NaiveBayes NB = new NaiveBayes();
                        FilteredClassifier FC = new FilteredClassifier();
                        Normalize normalize = new Normalize();

                        FC.setFilter(normalize);
                        FC.setClassifier(NB);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;
                case "All Filter":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        NaiveBayes NB = new NaiveBayes();

                        AllFilter allfilt = new AllFilter();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(allfilt);

                        FC.setClassifier(NB);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        NaiveBayes NB = new NaiveBayes();
                        FilteredClassifier FC = new FilteredClassifier();
                        AllFilter allfilt = new AllFilter();

                        FC.setFilter(allfilt);
                        FC.setClassifier(NB);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;
                case "Replace Missing Values":

                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        NaiveBayes NB = new NaiveBayes();

                        ReplaceMissingValues RMV = new ReplaceMissingValues();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(RMV);

                        FC.setClassifier(NB);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        NaiveBayes NB = new NaiveBayes();
                        FilteredClassifier FC = new FilteredClassifier();
                        ReplaceMissingValues RMV = new ReplaceMissingValues();

                        FC.setFilter(RMV);
                        FC.setClassifier(NB);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;

            }
        } else {
            if (gui.bttn_State == 1) {
                System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                System.out.println("bttnstate= " + gui.bttn_State);

                DataSource source = new DataSource(FileLocation);
                Instances dataSet = source.getDataSet();

                dataSet.setClassIndex(dataSet.numAttributes() - 1);

                NaiveBayes NB = new NaiveBayes();
                NB.buildClassifier(dataSet);
                System.out.println(NB);
                System.out.println("----------------------------------------------");

                Evaluation eval = new Evaluation(dataSet);
                Random rand = new Random(1);

                int folds = default_fold;
                folds = gui.fold_count;

                DataSource source2 = new DataSource(FileLocation);

                Instances testDataSet = source2.getDataSet();

                testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                eval.crossValidateModel(NB, testDataSet, folds, rand);

                nameString = NB.toString();
                summString = eval.toSummaryString("\n=== Summary ===\n", false);
                classString = eval.toClassDetailsString();
                matrixString = eval.toMatrixString();

                System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                System.out.println(eval.toClassDetailsString());
                System.out.println("----------------------------------------------");
                System.out.println(eval.toMatrixString());
                System.out.println("----------------------------------------------");

                ThresholdCurve tc = new ThresholdCurve();
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
            } else if (gui.bttn_State == 0) {

                System.out.println("bttnstate= " + gui.bttn_State);
                DataSource source = new DataSource(FileLocation);
                NaiveBayes NB = new NaiveBayes();

                Instances dataSet = source.getDataSet();

                System.out.println("SPLIT PERCENTAGE ");
                dataSet.randomize(new java.util.Random(0));
//
                //Random rand = new Random(1);
                int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                int testSize = dataSet.numInstances() - trainSize;

                Instances train = new Instances(dataSet, 0, trainSize);

                Instances test = new Instances(dataSet, trainSize, testSize);

                test.setClassIndex(test.numAttributes() - 1);
                train.setClassIndex(test.numAttributes() - 1);

                NB.buildClassifier(train);

                Evaluation eval = new Evaluation(train);

                eval.evaluateModel(NB, test);

                System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                System.out.println(eval.toClassDetailsString());
                System.out.println("----------------------------------------------");
                System.out.println(eval.toMatrixString());
                System.out.println("----------------------------------------------");

                nameString = NB.toString();
                summString = eval.toSummaryString("\n=== Summary ===\n", false);
                classString = eval.toClassDetailsString();
                matrixString = eval.toMatrixString();
                ThresholdCurve tc = new ThresholdCurve();
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

            }
        }
    }

    public void LMTTree() throws Exception {
        if (gui.filterState == true) {

            switch (gui.filterName) {
                case "Attribute Selection":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        LMT LMTTree = new LMT();

                        AttributeSelection atrSelect = new AttributeSelection();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(atrSelect);

                        FC.setClassifier(LMTTree);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        LMT LMTTree = new LMT();
                        FilteredClassifier FC = new FilteredClassifier();
                        AttributeSelection atrSelect = new AttributeSelection();

                        FC.setFilter(atrSelect);
                        FC.setClassifier(LMTTree);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;

                case "Multi Filter":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        LMT LMTTree = new LMT();

                        MultiFilter multi = new MultiFilter();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(multi);

                        FC.setClassifier(LMTTree);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        LMT LMTTree = new LMT();
                        FilteredClassifier FC = new FilteredClassifier();
                        MultiFilter multi = new MultiFilter();

                        FC.setFilter(multi);
                        FC.setClassifier(LMTTree);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;
                case "Remove":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        LMT LMTTree = new LMT();

                        Remove remove = new Remove();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(remove);

                        FC.setClassifier(LMTTree);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        LMT LMTTree = new LMT();
                        FilteredClassifier FC = new FilteredClassifier();
                        Remove remove = new Remove();

                        FC.setFilter(remove);
                        FC.setClassifier(LMTTree);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;
                case "Normalize":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        LMT LMTTree = new LMT();

                        Normalize normalize = new Normalize();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(normalize);

                        FC.setClassifier(LMTTree);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        LMT LMTTree = new LMT();
                        FilteredClassifier FC = new FilteredClassifier();
                        Normalize normalize = new Normalize();

                        FC.setFilter(normalize);
                        FC.setClassifier(LMTTree);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;
                case "All Filter":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        LMT LMTTree = new LMT();

                        AllFilter allfilt = new AllFilter();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(allfilt);

                        FC.setClassifier(LMTTree);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        LMT LMTTree = new LMT();
                        FilteredClassifier FC = new FilteredClassifier();
                        AllFilter allfilt = new AllFilter();

                        FC.setFilter(allfilt);
                        FC.setClassifier(LMTTree);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;
                case "Replace Missing Values":

                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        LMT LMTTree = new LMT();

                        ReplaceMissingValues RMV = new ReplaceMissingValues();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(RMV);

                        FC.setClassifier(LMTTree);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        LMT LMTTree = new LMT();
                        FilteredClassifier FC = new FilteredClassifier();
                        ReplaceMissingValues RMV = new ReplaceMissingValues();

                        FC.setFilter(RMV);
                        FC.setClassifier(LMTTree);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;

            }
        } else {
            if (gui.bttn_State == 1) {
                System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                System.out.println("bttnstate= " + gui.bttn_State);

                DataSource source = new DataSource(FileLocation);
                Instances dataSet = source.getDataSet();

                dataSet.setClassIndex(dataSet.numAttributes() - 1);

                LMT LMTTree = new LMT();
                LMTTree.buildClassifier(dataSet);
                System.out.println(LMTTree);
                System.out.println("----------------------------------------------");

                Evaluation eval = new Evaluation(dataSet);
                Random rand = new Random(1);

                int folds = default_fold;
                folds = gui.fold_count;
                System.out.println("fooooooooooooooooooooooooooooolds" + folds);

                DataSource source2 = new DataSource(FileLocation);

                Instances testDataSet = source2.getDataSet();

                testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                eval.crossValidateModel(LMTTree, testDataSet, folds, rand);

                nameString = LMTTree.toString();
                summString = eval.toSummaryString("\n=== Summary ===\n", false);
                classString = eval.toClassDetailsString();
                matrixString = eval.toMatrixString();

                System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                System.out.println(eval.toClassDetailsString());
                System.out.println("----------------------------------------------");
                System.out.println(eval.toMatrixString());
                System.out.println("----------------------------------------------");

                ThresholdCurve tc = new ThresholdCurve();
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
            } else if (gui.bttn_State == 0) {

                System.out.println("bttnstate= " + gui.bttn_State);
                DataSource source = new DataSource(FileLocation);
                LMT LMTTree = new LMT();

                Instances dataSet = source.getDataSet();

                System.out.println("SPLIT PERCENTAGE ");
                dataSet.randomize(new java.util.Random(0));

                //Random rand = new Random(1);
                int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                int testSize = dataSet.numInstances() - trainSize;

                Instances train = new Instances(dataSet, 0, trainSize);

                Instances test = new Instances(dataSet, trainSize, testSize);

                test.setClassIndex(test.numAttributes() - 1);
                train.setClassIndex(test.numAttributes() - 1);

                LMTTree.buildClassifier(train);

                Evaluation eval = new Evaluation(train);

                eval.evaluateModel(LMTTree, test);
                System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                System.out.println(eval.toClassDetailsString());
                System.out.println("----------------------------------------------");
                System.out.println(eval.toMatrixString());
                System.out.println("----------------------------------------------");

                nameString = LMTTree.toString();
                summString = eval.toSummaryString("\n=== Summary ===\n", false);
                classString = eval.toClassDetailsString();
                matrixString = eval.toMatrixString();
                ThresholdCurve tc = new ThresholdCurve();
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

            }
        }
    }

    public void REP_Tree() throws Exception {
        if (gui.filterState == true) {

            switch (gui.filterName) {
                case "Attribute Selection":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        REPTree REP = new REPTree();

                        AttributeSelection atrSelect = new AttributeSelection();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(atrSelect);

                        FC.setClassifier(REP);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        REPTree REP = new REPTree();
                        FilteredClassifier FC = new FilteredClassifier();
                        AttributeSelection atrSelect = new AttributeSelection();

                        FC.setFilter(atrSelect);
                        FC.setClassifier(REP);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;

                case "Multi Filter":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        REPTree REP = new REPTree();

                        MultiFilter multi = new MultiFilter();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(multi);

                        FC.setClassifier(REP);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        REPTree REP = new REPTree();
                        FilteredClassifier FC = new FilteredClassifier();
                        MultiFilter multi = new MultiFilter();

                        FC.setFilter(multi);
                        FC.setClassifier(REP);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;
                case "Remove":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        REPTree REP = new REPTree();

                        Remove remove = new Remove();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(remove);

                        FC.setClassifier(REP);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        REPTree REP = new REPTree();
                        FilteredClassifier FC = new FilteredClassifier();
                        Remove remove = new Remove();

                        FC.setFilter(remove);
                        FC.setClassifier(REP);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;
                case "Normalize":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        REPTree REP = new REPTree();

                        Normalize normalize = new Normalize();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(normalize);

                        FC.setClassifier(REP);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        REPTree REP = new REPTree();
                        FilteredClassifier FC = new FilteredClassifier();
                        Normalize normalize = new Normalize();

                        FC.setFilter(normalize);
                        FC.setClassifier(REP);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;
                case "All Filter":
                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        REPTree REP = new REPTree();

                        AllFilter allfilt = new AllFilter();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(allfilt);

                        FC.setClassifier(REP);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        REPTree REP = new REPTree();
                        FilteredClassifier FC = new FilteredClassifier();
                        AllFilter allfilt = new AllFilter();

                        FC.setFilter(allfilt);
                        FC.setClassifier(REP);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;
                case "Replace Missing Values":

                    if (gui.bttn_State == 0) {

                        //ATTRIBUTE PERCENTAGE
//                getAttributes();
                        System.out.println("ATTRIBUTE PERCENTAGE");
                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        dataSet.randomize(new java.util.Random(0));

                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                        int testSize = dataSet.numInstances() - trainSize;

                        Instances train = new Instances(dataSet, 0, trainSize);

                        Instances test = new Instances(dataSet, trainSize, testSize);

                        test.setClassIndex(test.numAttributes() - 1);
                        train.setClassIndex(test.numAttributes() - 1);

                        REPTree REP = new REPTree();

                        ReplaceMissingValues RMV = new ReplaceMissingValues();

//                String[] options = new String[]{"-R", "1"};
//                atrSelect.setOptions(options);
                        FilteredClassifier FC = new FilteredClassifier();

                        FC.setFilter(RMV);

                        FC.setClassifier(REP);

                        FC.buildClassifier(train);

                        System.out.println(FC);
                        Evaluation eval = new Evaluation(train);

                        eval.evaluateModel(FC, test);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    } else if (gui.bttn_State == 1) {

                        //ATTRIBUTE CROSS
//                getAttributes();       
                        System.out.println("ATTRIBUTE CROSS");
                        REPTree REP = new REPTree();
                        FilteredClassifier FC = new FilteredClassifier();
                        ReplaceMissingValues RMV = new ReplaceMissingValues();

                        FC.setFilter(RMV);
                        FC.setClassifier(REP);

                        System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                        System.out.println("bttnstate= " + gui.bttn_State);

                        DataSource source = new DataSource(FileLocation);

                        Instances dataSet = source.getDataSet();

                        System.out.println("CROSS VALIDATON");
                        //Instances dataSet = source.getDataSet();
                        dataSet.setClassIndex(dataSet.numAttributes() - 1);

                        FC.buildClassifier(dataSet);
                        System.out.println(FC);
                        System.out.println("----------------------------------------------");

                        Evaluation eval = new Evaluation(dataSet);

                        Random rand = new Random(1);

                        int folds = default_fold;

                        folds = gui.fold_count;

                        DataSource source2 = new DataSource(FileLocation);

                        Instances testDataSet = source2.getDataSet();

                        testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                        eval.crossValidateModel(FC, testDataSet, folds, rand);

                        nameString = FC.toString();
                        summString = eval.toSummaryString("\n=== Summary ===\n", false);
                        classString = eval.toClassDetailsString();
                        matrixString = eval.toMatrixString();

                        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                        System.out.println(eval.toClassDetailsString());
                        System.out.println("----------------------------------------------");
                        System.out.println(eval.toMatrixString());
                        System.out.println("----------------------------------------------");

                        ThresholdCurve tc = new ThresholdCurve();
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

                    }
                    /*
            
                     */
                    break;

            }
        } else {
            if (gui.bttn_State == 1) {
                System.out.println("persplit " + gui.Rad_Bttn_PercentageSplit.isSelected());
                System.out.println("bttnstate= " + gui.bttn_State);

                DataSource source = new DataSource(FileLocation);
                Instances dataSet = source.getDataSet();

                dataSet.setClassIndex(dataSet.numAttributes() - 1);

                REPTree REP = new REPTree();
                REP.buildClassifier(dataSet);

                System.out.println(REP);
                System.out.println("----------------------------------------------");

                Evaluation eval = new Evaluation(dataSet);
                Random rand = new Random(1);

                int folds = default_fold;
                folds = gui.fold_count;

                DataSource source2 = new DataSource(FileLocation);

                Instances testDataSet = source2.getDataSet();

                testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                eval.crossValidateModel(REP, testDataSet, folds, rand);

                nameString = REP.toString();
                summString = eval.toSummaryString("\n=== Summary ===\n", false);
                classString = eval.toClassDetailsString();
                matrixString = eval.toMatrixString();

                System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                System.out.println(eval.toClassDetailsString());
                System.out.println("----------------------------------------------");
                System.out.println(eval.toMatrixString());
                System.out.println("----------------------------------------------");

                ThresholdCurve tc = new ThresholdCurve();
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
            } else if (gui.bttn_State == 0) {

                System.out.println("bttnstate= " + gui.bttn_State);
                DataSource source = new DataSource(FileLocation);
                REPTree REP = new REPTree();

                Instances dataSet = source.getDataSet();

                System.out.println("SPLIT PERCENTAGE ");
                dataSet.randomize(new java.util.Random(0));

                //Random rand = new Random(1);
                int trainSize = (int) Math.round(dataSet.numInstances() * gui.percentage);

                int testSize = dataSet.numInstances() - trainSize;

                Instances train = new Instances(dataSet, 0, trainSize);

                Instances test = new Instances(dataSet, trainSize, testSize);

                test.setClassIndex(test.numAttributes() - 1);
                train.setClassIndex(test.numAttributes() - 1);

                REP.buildClassifier(train);

                Evaluation eval = new Evaluation(train);

                eval.evaluateModel(REP, test);

                System.out.println(eval.toSummaryString("=== Summary ===\n", false));
                System.out.println(eval.toClassDetailsString());
                System.out.println("----------------------------------------------");
                System.out.println(eval.toMatrixString());
                System.out.println("----------------------------------------------");

                nameString = REP.toString();
                summString = eval.toSummaryString("\n=== Summary ===\n", false);
                classString = eval.toClassDetailsString();
                matrixString = eval.toMatrixString();
                ThresholdCurve tc = new ThresholdCurve();
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

            }

        }
    }
}
