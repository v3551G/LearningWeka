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
	
	  // 添加一个标称属性, 并随机初始化
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
      
      // 添加一个数值属性， 并随机初始化
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
      
      // 对训练集和测试机分别进行“过滤”————标准化
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
      
      // 即时过滤，并且使用元分类器在训练集上训练, 然后在测试集上测试， 返回测试准确率
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
		  fc.buildClassifier(train);  // 构建分类器以及分类之前必须要设置分类属性
		  
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
		  System.out.println(test.classAttribute());         // 该数据集的类别信息
		  System.out.println(test.instance(0).classIndex()); // 类别属性索引号
		  System.out.println(test.instance(0).classValue()); // 该实例的类别值索引
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


