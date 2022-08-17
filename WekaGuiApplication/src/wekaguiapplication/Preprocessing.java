package wekaguiapplication;

import weka.classifiers.bayes.BayesNet;
import weka.classifiers.meta.FilteredClassifier;
import weka.filters.AllFilter;
import weka.filters.MultiFilter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;
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

public class Preprocessing {

    EvaluationCalculating calc = new EvaluationCalculating();

    AllFilter allFilter = new AllFilter();
    MultiFilter multiFilter = new MultiFilter();

    AttributeSelection attributeSelect = new AttributeSelection();
    Normalize normal = new Normalize();

    ReplaceMissingValues replaceMissing = new ReplaceMissingValues();

    Remove remove = new Remove();

    String x;

    FilteredClassifier filter = new FilteredClassifier();
    
    public void allFilt() throws Exception{
        
        
        filter.setFilter(allFilter);
        switch (x) {
            case ("RandomForest"):
                filter.setClassifier(new RandomForest());
                break;
            case ("BayesNet"):
                filter.setClassifier(new BayesNet());
                break;
            case ("NaiveBayes"):
                filter.setClassifier(new NaiveBayes());
                break;
            case ("J48"):
                filter.setClassifier(new J48());
                break;
            case ("LMT"):
                filter.setClassifier(new LMT());
                break;
            case ("REPTree"):
                filter.setClassifier(new REPTree());
                break;
            case ("RandomTree"):
                filter.setClassifier(new RandomTree());
                break;

            default:
                break;
        }
    }

    public void multiFilt()throws Exception {
        
        switch (x) {
            case ("RandomForest"):
                filter.setClassifier(new RandomForest());
                break;
            case ("BayesNet"):
                filter.setClassifier(new BayesNet());
                break;
            case ("NaiveBayes"):
                filter.setClassifier(new NaiveBayes());
                break;
            case ("J48"):
                filter.setClassifier(new J48());
                break;
            case ("LMT"):
                filter.setClassifier(new LMT());
                break;
            case ("REPTree"):
                filter.setClassifier(new REPTree());
                break;
            case ("RandomTree"):
                filter.setClassifier(new RandomTree());
                break;

            default:
                break;
        }
        

    }

    public void attrSel() throws Exception{
        
        switch (x) {
            case ("RandomForest"):
                filter.setClassifier(new RandomForest());
                break;
            case ("BayesNet"):
                filter.setClassifier(new BayesNet());
                break;
            case ("NaiveBayes"):
                filter.setClassifier(new NaiveBayes());
                break;
            case ("J48"):
                filter.setClassifier(new J48());
                break;
            case ("LMT"):
                filter.setClassifier(new LMT());
                break;
            case ("REPTree"):
                filter.setClassifier(new REPTree());
                break;
            case ("RandomTree"):
                filter.setClassifier(new RandomTree());
                break;

            default:
                break;
        }

    }

    public void normal()throws Exception {
        
        switch (x) {
            case ("RandomForest"):
                filter.setClassifier(new RandomForest());
                break;
            case ("BayesNet"):
                filter.setClassifier(new BayesNet());
                break;
            case ("NaiveBayes"):
                filter.setClassifier(new NaiveBayes());
                break;
            case ("J48"):
                filter.setClassifier(new J48());
                break;
            case ("LMT"):
                filter.setClassifier(new LMT());
                break;
            case ("REPTree"):
                filter.setClassifier(new REPTree());
                break;
            case ("RandomTree"):
                filter.setClassifier(new RandomTree());
                break;

            default:
                break;
        }

    }

    public void replaceMiss()throws Exception {
        
        switch (x) {
            case ("RandomForest"):
                filter.setClassifier(new RandomForest());
                break;
            case ("BayesNet"):
                filter.setClassifier(new BayesNet());
                break;
            case ("NaiveBayes"):
                filter.setClassifier(new NaiveBayes());
                break;
            case ("J48"):
                filter.setClassifier(new J48());
                break;
            case ("LMT"):
                filter.setClassifier(new LMT());
                break;
            case ("REPTree"):
                filter.setClassifier(new REPTree());
                break;
            case ("RandomTree"):
                filter.setClassifier(new RandomTree());
                break;

            default:
                break;
        }

    }

}
