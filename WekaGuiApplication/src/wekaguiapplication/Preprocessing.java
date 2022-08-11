
package wekaguiapplication;

import weka.filters.AllFilter;
import weka.filters.MultiFilter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;


public class Preprocessing {
    
    AllFilter allFilter = new AllFilter();
    MultiFilter multiFilter = new MultiFilter();
    
    AttributeSelection attributeSelect = new AttributeSelection();
    Normalize normal = new Normalize();
    
    ReplaceMissingValues replaceMissing = new ReplaceMissingValues();
    
    Remove remove = new Remove();
    
    
    
    public void allFilt(){
        ;
    }
    public void multiFilt(){
        
    }
    
    public void attrSel(){
    
       
    }
    public void normal(){
        
    }
    public void replaceMiss(){
        
    }
    
}
