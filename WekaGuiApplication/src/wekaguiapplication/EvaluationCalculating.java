package wekaguiapplication;

import java.util.Random;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomTree;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.LMT;

public class EvaluationCalculating {

    WekaGui gui = new WekaGui();
    public String nameString;
    public String summString;
    public String classString;
    public String matrixString;
    String FileLocation= gui.FileLocation;
    
    private int default_fold = 10;
    public void RFTree() throws Exception {

        System.out.println(FileLocation);
        
        DataSource source = new DataSource(FileLocation);
        Instances dataSet = source.getDataSet();

        dataSet.setClassIndex(dataSet.numAttributes() - 1);

        RandomForest RF = new RandomForest();
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
                
        
        //gui.jTextArea1.append(eval.toSummaryString("=== Summary ===\n", false));
        System.out.println(eval.toSummaryString("=== Summary ===\n", false));
        System.out.println(eval.toClassDetailsString());
        System.out.println("----------------------------------------------");
        System.out.println(eval.toMatrixString());
        System.out.println("----------------------------------------------");
    }

    public void RTTree() throws Exception {

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

    }

    public void J48Tree() throws Exception {

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
    }

    public void BNBayes() throws Exception {

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
    }

    public void NBBayes() throws Exception {

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

    }

    public void LMTTree() throws Exception {

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
        System.out.println("fooooooooooooooooooooooooooooolds"+folds);
        

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

    }

    public void REP_Tree() throws Exception {

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

    }
}
