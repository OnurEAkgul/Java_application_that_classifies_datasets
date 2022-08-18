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

public class NewClass {

    public static void main(String args[]) throws Exception {

        testClass test = new testClass();
        EvaluationCalculating EC = new EvaluationCalculating();

        test.RFFtesting();
//        test.getAttributes();
//        test.RFtesting();
//        test.BNtesting();
//        test.J48testing();
//        test.NBtesting();
//        test.REPtesting();
//        test.RTtesting();
//        test.LMTtesting();

//        EC.RFTree();
//        EC.RTTree();
        //EC.BNBayes();
        //EC.LMTTree();
        // EC.NBBayes();
//        EC.REP_Tree();
//        EC.J48Tree();
    }

}
