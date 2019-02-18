package Classify;

import java.io.File;
import java.util.Random;

import moa.classifiers.AbstractClassifier;
import moa.options.AbstractOptionHandler;

//import moa.classifiers.AbstractClassifier;
//import moa.classifiers.Classifier;

import DataIO.loadData;
import weka.associations.NominalItem;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.OptionHandler;
import weka.core.Utils;
import weka.core.converters.ArffLoader;
import weka.core.converters.ConverterUtils.DataSink;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AddClassification;

/*
 * create classifier, e.g select classifier and training by the data
 */
public class createClassifier {
     public static void BatchClassifer(String rfile) throws Exception {
    	 Instances data = loadData.loadArffFile1_2(rfile);
    	 
    	 data.setClassIndex(data.numAttributes()-1);
    	 
    	 String[] options = new String[1];
    	 options[0] = "-U";
    	 
    	 J48 tree = new J48();
    	 tree.setOptions(options);
    	 tree.buildClassifier(data);
    	 
    	 System.out.println(tree);
    	 
     }
     
     public static void IncrementalClassifier(String rfile) throws Exception {
    	 // Instances data = loadData.loadArffFile1_2(rfile);
    	 
    	 // data.setClassIndex(data.numAttributes()-1);
    	 ArffLoader loader = new ArffLoader();
    	 loader.setFile(new File(rfile));
    	 
    	 Instances structure = loader.getStructure();
    	 structure.setClassIndex(structure.numAttributes()-1);
    	 
    	 NaiveBayesUpdateable nb = new NaiveBayesUpdateable();
    	 nb.buildClassifier(structure);
    	 
    	 Instance ins;
    	 while((ins = loader.getNextInstance(structure)) != null) {
    		 nb.updateClassifier(ins);
    	 }
    	 
    	 System.out.println("the naiveBayes classifier:" + nb); 
    }
    
    // ����������������ֲ�
    public static void distributionForTest(String trainfile, String testfile) throws Exception {
    	Instances train = loadData.loadArffFile1_2(trainfile);
    	Instances test  = loadData.loadArffFile1_2(testfile);
    	
    	if (train.equalHeaders(test)) {
    		new Exception("the training dataSet and testing dataSet are not in the same format");
    	}
    	
    	train.setClassIndex(train.numAttributes()-1);
    	test.setClassIndex(test.numAttributes()-1);
    	
    	//NaiveBayesUpdateable nb = new NaiveBayesUpdateable();
    	//nb.buildClassifier(train);
    	J48 classifier = new J48();
    	classifier.buildClassifier(train);
    	
    	double predict;
		double[] dis;
    	for (int i=0; i<test.numInstances(); ++i) {
    		predict = classifier.classifyInstance(test.instance(i));
    		dis = classifier.distributionForInstance(test.instance(i));
    		
    		myPrintln("" + (i+1));
    		myPrintln("-");
    		myPrintln(test.instance(i).toString(test.classIndex()));
    		myPrintln("-");
    		myPrintln(test.classAttribute().value((int) predict));
    		myPrintln("-");
    		if (predict == test.instance(i).classValue()) {
    			myPrintln("��");
    		}else {
    			myPrintln("��");
    		}
    		myPrintln("-");
    		myPrintln(Utils.arrayToString(dis));
    		myPrintln("");	
    	}
    } 
    
    // ������֤
    public static void cvPrediction(String rfile, String wfile) throws Exception {
    	Instances data = loadData.loadArffFile1_2(rfile);
    	
    	data.setClassIndex(data.numAttributes()-1);
    	
    	String[] tmpOptions = new String[2];
    	tmpOptions[0] = "-C";
    	tmpOptions[1] = "0.25";
    	String clsName = "weka.classifiers.trees.j48";
    	
    	Classifier classifier = (Classifier) Utils.forName(Classifier.class, clsName, tmpOptions);
    	
    	int seed = 1234;
    	Random random = new Random(seed);
    	Instances newData = new Instances(data);
    	newData.randomize(random);
    	
    	int fold = 10;
    	if (data.classAttribute().isNominal()) {
    		newData.stratify(fold);
    	}
    	
    	Instances predData = null;
    	Evaluation evaluation = new Evaluation(newData);
    	for (int i=0; i<fold; ++i) {
    		Instances train = newData.trainCV(fold, i);
    		Instances test  = newData.testCV(fold, i);
    		
    		Classifier clsCopy = weka.classifiers.AbstractClassifier.makeCopy(classifier);
    		clsCopy.buildClassifier(train);
    		evaluation.evaluateModel(clsCopy, test);
    		
    		AddClassification filter = new AddClassification(); // ����������
    		filter.setClassifier(classifier); // ���÷�����
    		filter.setOutputDistribution(true);
    		filter.setOutputClassification(true);
    		filter.setOutputErrorFlag(true);
    		filter.setInputFormat(train); // ��������ʵ���ĸ�ʽ
    		
    		Filter.useFilter(train,  filter);  // ��������������ʵ�����Ͻ��й���
    		Instances pred = Filter.useFilter(test, filter); // ��������������ʵ�����Ͻ��й���
    		
    		if (predData == null) {
    			predData = new Instances(pred, 0);
    		}    		
    		for (int j=0; j<pred.numInstances(); ++j) {
    			predData.add(pred.instance(j));
    		}
    	}
    	myPrintln(" ");
    	myPrintln("�������ӿ�����");
    	if (classifier instanceof OptionHandler) {
    		myPrintln("��������" + classifier.getClass().getName() + " "
    				+ Utils.joinOptions(((OptionHandler) classifier).getOptions()) );
    	}else {
    		myPrintln("��������" + classifier.getClass().getName());
    		myPrintln("���ݼ���" + data.relationName());
    		myPrintln("����:" + fold);
    		myPrintln("���������:" + seed);
    		myPrintln(evaluation.toSummaryString("===" + fold + "�۽�����֤===", false));
    		
    		DataSink.write(wfile, predData);  // ������д���ļ�
    	}
    }
    
    public static void myPrintln(String s) {
    	System.out.println(s);
    }
     
     
     
     
}
