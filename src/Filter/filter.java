package Filter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.Standardize;
import DataIO.loadData;

/*
 *  filter definition
 */
public class filter {
	
	  // ���һ���������, �������ʼ��
      public static Instances AddNominalAttr(String rfile, AttributeObject obj) throws Exception {
		  Instances data = loadData.loadArffFile1_2(rfile);
		  Instances fdata = new Instances(data);
		  
		  Add filter = new Add();
		  filter.setAttributeIndex(obj.attrIndex);
		  filter.setAttributeName(obj.attrName);
		  filter.setNominalLabels(obj.labels);
		  filter.setInputFormat(fdata);
		  
		  fdata = Filter.useFilter(fdata, filter);
		  
		  // set value on the created attribute
		  Random rand = new Random(1234);
		  int numClassValue = obj.labels.split(",").length;
		  for (int i=0; i<fdata.numInstances(); ++i) {
			  fdata.instance(i).setValue(fdata.numAttributes()-1, rand.nextInt(numClassValue));
		  }
    	  return fdata;
      }
      
      // ���һ����ֵ���ԣ� �������ʼ��
      public static Instances AddNumericAttr(String rfile, AttributeObject obj) throws Exception {
		  Instances data = loadData.loadArffFile1_2(rfile);
		  Instances fdata = new Instances(data);
		
		  Add filter = new Add();
		  filter.setAttributeIndex(obj.attrIndex);
		  filter.setAttributeName(obj.attrName);
		  filter.setInputFormat(fdata);
		
		  fdata = Filter.useFilter(fdata, filter);
		
		  //
		  Random rand = new Random();
		  for (int i=0; i<fdata.numInstances(); ++i) {
		      fdata.instance(i).setValue(fdata.numAttributes()-1, rand.nextDouble());
		  }
		
		  return fdata;  
      }
      
      // ��ѵ�����Ͳ��Ի��ֱ���С����ˡ�����������׼��
      public static Instances[] filterBatch(String trainfile, String testfile) throws Exception {
		 Instances train = loadData.loadArffFile1_2(trainfile);
		 Instances test  = loadData.loadArffFile1_2(testfile);
		 Instances[] ret = new Instances[2];
		 
		 Standardize filter = new Standardize();
		 filter.setInputFormat(train);
		 
		 ret[0] = Filter.useFilter(train, filter);
		 ret[1] = Filter.useFilter(test, filter);
		 
    	 return ret;
    	  
      }
      
      // ��ʱ���ˣ�����ʹ��Ԫ��������ѵ������ѵ��, Ȼ���ڲ��Լ��ϲ��ԣ� ���ز���׼ȷ��
      public static double filterOnTheFly(String trainfile, String testfile) throws Exception {
		  double corRate;
    	  Instances train = loadData.loadArffFile1_2(trainfile);
		  Instances test  = loadData.loadArffFile1_2(testfile);
		  if (!train.equalHeaders(test)) {
			  throw new Exception("the train and test file are not in the same attribute");
		  }
		  
		  J48 j48 = new J48();
		  j48.setUnpruned(true);
		  
		  Remove rm = new Remove();
		  rm.setAttributeIndices("1");
		  
		  FilteredClassifier fc = new FilteredClassifier();
		  fc.setFilter(rm);
		  fc.setClassifier(j48);
		  
		  train.setClassIndex(train.numAttributes()-1);
		  test.setClassIndex(test.numAttributes()-1);
		  fc.buildClassifier(train);  // �����������Լ�����֮ǰ����Ҫ���÷�������
		  
		  int corCount = 0;
		  for (int i=0; i<test.numInstances(); ++i) {
			  double predict = fc.classifyInstance(test.instance(i));
			  //System.out.println("predict:" + test.classAttribute().value((int) predict)
			  //	  + "   true:" + test.classAttribute().value((int) test.instance(i).classValue()));
			  
			  if (test.classAttribute().value((int) predict) 
					 .equals(test.classAttribute().value((int) test.instance(i).classValue()))) {
				  corCount ++;
			  }
		  }
		  System.out.println(test.classAttribute());         // �����ݼ��������Ϣ
		  System.out.println(test.instance(0).classIndex()); // �������������
		  System.out.println(test.instance(0).classValue()); // ��ʵ�������ֵ����
		  corRate = (double)corCount / test.numInstances();
		  
    	  return corRate;
    	  
      }
        
      /**
       * 
       * @param datas
       * @param ofile
       */
      public static void filterDataSet(Instances datas, String ofile){
    	  
      }
      // for convenient
      class AttributeObject {
    	  String attrName;
    	  String attrIndex;
    	  String labels;
	  }
	
	  public AttributeObject setAttributeObject(String attrName, String attrIndex, String labels) {
    	  AttributeObject aobj = new AttributeObject();
    	  aobj.attrName = attrName;
    	  aobj.attrIndex = attrIndex;
    	  aobj.labels = labels;
	    	  
	      return aobj;    	  
	  }
      
	  public static void main(String[] args) throws Exception {
		  String ifileName = "G:\\dataRepository\\SPASC dataset\\KDDCup99 -copy.arff";
		  String ofileName = "G:\\dataRepository\\SPASC dataset\\kdd99modified.arff";
		  
		  //filterDataSet(ifileName, ofileName);
		  String rfile = "G:\\dataRepository\\SPASC dataset\\email_data.arff";
		  ArffLoader loader = new ArffLoader();
		  
		  Instances datas = new Instances(new BufferedReader(new FileReader(rfile)));
		  //loader.setFile(new File(rfile));
		  //Instances datas = loader.getDataSet();
		  //Instances datas = loadData.loadArffFile2(rfile);
		  System.out.println(datas.instance(0));
		  
	  }
}


