package wekaguiapplication;

import java.awt.BorderLayout;
import java.util.Random;
import javax.swing.JFrame;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.ThresholdCurve;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomTree;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.LMT;
import weka.core.Utils;
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

    public void RFTree() throws Exception {

        if (gui.bttn_State == 1) {
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

    public void RTTree() throws Exception {

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

    public void J48Tree() throws Exception {

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

    public void BNBayes() throws Exception {

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

    public void NBBayes() throws Exception {

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

    public void LMTTree() throws Exception {
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

    public void REP_Tree() throws Exception {
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
