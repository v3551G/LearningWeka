package Classify;

import java.util.Random;

//import org.ietf.jgss.Oid;

import DataIO.loadData;
//import Filter.filter;
//import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.classifiers.trees.J48;
//import weka.clusterers.ClusterEvaluation;
import weka.core.Instances;
import weka.core.Utils;
//import weka.filters.Filter;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

/*
 * the definition of meta classifier
 * 注意：
 *    此文件中需要调用不同包下的同名类，为了避免引入冲突， 只能在定义是带上报名。
 */
public class metaClassifier {
	// 使用元分类器
    public static void useMetaClassifier(String rfile) throws Exception {
    	Instances data = loadData.loadArffFile1_2(rfile);
        
    	if (data.classIndex() == -1 ) {
    		data.setClassIndex(data.numAttributes()-1);
    	}
    	
    	AttributeSelectedClassifier acls = new AttributeSelectedClassifier();
    	CfsSubsetEval eval = new CfsSubsetEval();
    	GreedyStepwise search = new GreedyStepwise();
    	search.setSearchBackwards(true);
    	
    	J48 base = new J48();
    	acls.setClassifier(base);
    	acls.setEvaluator(eval);
    	acls.setSearch(search);
    	
    	int fold = 10;
    	int seed = 1234;
    	Evaluation evalu = new Evaluation(data);
    	evalu.crossValidateModel(acls, data, fold, new Random(seed));
    	
    	myPrintln(evalu.toSummaryString());
    }
    
    // 使用过滤器
    public static void useFilter(String rfile) throws Exception {
    	Instances data = loadData.loadArffFile1_2(rfile) ;
    	
    	if (data.classIndex() == -1) {
    		data.setClassIndex(data.numAttributes()-1);
    	}
    	
    	//AttributeSelectedClassifier acs = new AttributeSelectedClassifier();\
    	// 注意此处包的导入： import weka.filters.supervised.attribute.AttributeSelection;
    	// 相当于过滤器， 而不是平常所使用的import weka.attributeSelection.AttributeSelection;
    	weka.filters.supervised.attribute.AttributeSelection filter = new AttributeSelection();  // 此处不是元分类器
 
    	CfsSubsetEval eval = new CfsSubsetEval();
    	GreedyStepwise search = new GreedyStepwise();
    	search.setSearchBackwards(true);
    	    	
    	filter.setEvaluator(eval);
    	filter.setSearch(search);
    	filter.setInputFormat(data);
    	
    	Instances newData = Filter.useFilter(data, filter);
    	
    	myPrintln(newData);
    }
    
    // 使用底层API
    public static void useLowerLevel(String rfile) throws Exception {
    	Instances data = loadData.loadArffFile1_2(rfile);
        
    	if (data.classIndex() == -1) {
    		data.setClassIndex(data.numAttributes()-1);
    	}
    	
    	weka.attributeSelection.AttributeSelection attsel = new weka.attributeSelection.AttributeSelection();
    	CfsSubsetEval eval = new CfsSubsetEval();
    	GreedyStepwise search = new GreedyStepwise();
    	search.setSearchBackwards(true);
    	
    	attsel.setEvaluator(eval);
    	attsel.setSearch(search);
    	//filter.setInputFormat(data);
    	attsel.SelectAttributes(data);
        
    	int[] indices = attsel.selectedAttributes();
    	
    	myPrintln(Utils.arrayToString(indices));
    }
    
    
    public static void myPrintln(String s) {
    	System.out.println(s);
    }
    
    public static void myPrintln(Instances data) {
    	System.out.println(data);
    }
}
