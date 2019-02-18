import java.util.ArrayList;
import java.util.Random;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSink;
import Classify.createClassifier;
import Classify.metaClassifier;
import Cluster.cluster;
import DataIO.loadData;
import DataIO.loadData.DBQueryObject;
import DataIO.saveData;
import Filter.filter;
import Filter.optionalHandler;
import Instance.myInstances;
import Serialization.serialization;
import Visualization.plot;

/*
 *  Main ,used for test
 */
public class TestAll {
	public static String PATH = "E:\\Program Files\\Weka-3-8\\data\\";
	public static String OPATH = "G:\\softWork\\learningWekaOutputPath\\";
	
    public static void main(String[] args) throws Exception {
    	// test the filter class
    	//testFilter();
    	
    	// test the myInstance class
    	//testMyInstance();
    	
    	// test the createClassifier class
    	//testCreateClassifier();
    	
    	// test the loadFile class
    	//testLoadFile();
    	
    	// test the saveFile class
    	testSaveFile();
    	
    	// test the optionalHandler class
    	//testOptionalHandler();
    	
    	// test the cluster class
    	//testCluster();
    	
    	// test the metaClassifier
    	//testMetaClassifier();
    	
    	// test the visualization, e.g. plot
    	//testPlot();
    	
    	// test the serialization class
    	//testSerialization();
    	
    	// test other
    	//temp();
        //System.out.println("xx");
    	
    	//testS_DCopy();
    	//testCharInt();
    }
    
    public static void testCharInt() {
    	char c = 8;
    	System.out.println(c);
    }
    
    public static void testSerialization() throws Exception {
    	
    	String trainfile = PATH + " ";
    	String testfile  = PATH + " ";
    	String wfile = OPATH + " ";
    	
    	serialization.HeadlerSerialization(trainfile, testfile, wfile);
    	
    	String rfile = PATH + " ";
    	serialization.ModelSerialization(rfile, wfile);
    	
    }
    
    public static void testLoadFile() throws Exception {
    	String rfile = PATH + " ";

    	loadData.loadArffFile1(rfile);
    	loadData.loadArffFile1_2(rfile);
    	loadData.loadArffFile2(rfile);
    	
    	DBQueryObject obj = null;   // 待考虑，如何只定义一次
		loadData.loadDBData1(obj);
    	loadData.loadDBData2(obj);
    	
    	String xrfffile = PATH + " .xrff";
    	loadData.loadXrffData1(xrfffile);
    	loadData.loadXrffData2(xrfffile);
    }
    
    
    public static void testSaveFile() throws Exception {
    	String s1 = "sea2-balance.arff";
    	String s2 = "sea2-no-balance.arff";
    	String s3 = "sea1.arff";
    	
    	PATH = "G:\\dataRepository\\MOA generator\\";
    	OPATH = "G:\\dataRepository\\MOA generator\\";
    	
//    	String rfile = PATH + s1;
//    	String wfile = OPATH + "sea2-balance.csv";
//    	
//    	String rfile = PATH + s2;
//    	String wfile = OPATH + "sea2-no-balance.csv";
  	
//    	String rfile = PATH + s3;
//    	String wfile = OPATH + "sea1.csv";
//    	String rfile = "G:\\dataRepository\\MOA generator\\hp1-3d-5noise.arff";
//    	String wfile = "G:\\dataRepository\\MOA generator\\hp1-3d-5noise.csv";
    	//String rfile = "G:\\dataRepository\\MOA generator\\sea80000-8concept.arff";
    	//String wfile = "G:\\dataRepository\\MOA generator\\sea80000-8concept.csv";
    	
    	String rfile = "G:\\dataRepository\\MOA generator\\sea5000-10concept-balance.arff";
    	String wfile = "G:\\dataRepository\\MOA generator\\sea5000-10concept-balance.csv";
    	
    	//saveData.saverfAf2CSV1(rfile, wfile);
    	saveData.saveArff2CSV2(rfile, wfile);
    	
//    	String rfileName = "G:\\dataRepository\\MOA generator\\mySEAData-numPerdrift500-maxNumber5000-seeds1.csv";
//    	String wfileName = "G:\\dataRepository\\MOA generator\\mySEAData-numPerdrift500-maxNumber5000-seeds1.arff";
//    	saveData.saveCSV2Arff(rfileName, wfileName);
    	
    	
    	//DBSaveObject obj = null;    // 待考虑
		//saveData.saveArff2DBBatch(rfile, obj);
    	//saveData.saveArff2DBIncremental(rfile, obj);
    }
    
    public static void testOptionalHandler() throws Exception {
    	String rfile = PATH + " ";
    	
    	optionalHandler.filterData1(rfile);
    	optionalHandler.filterData2(rfile);
    	optionalHandler.filterData3(rfile);
    }
    
    
    public static void testFilter() throws Exception {
    	 String trainfile = PATH + "\\segment-challenge.arff";
    	 String testfile  = PATH + "\\segment-test.arff";
    	 
    	 //double crate = filter.filterOnTheFly(trainfile, testfile);
    	 //System.out.println("the accuracy rate on the test data is:" + crate);
    	 
    	 String wfileName = "G:\\dataRepository\\SPASC result\\segment_man1.arff";
    	 Instances[] resInstances = filter.filterBatch(trainfile, testfile);
    	 DataSink.write(wfileName, resInstances[0]);
    	 
    }
    
    
    public static void testMyInstance() throws Exception {
    	String rfile = PATH  + "\\segment-test.arff";
    	
    	//myInstances.createInstances();
    	
        myInstances.instancesAnalysis(rfile);
    	
    }
    
    
    public static void testCreateClassifier() throws Exception {
    	String trainfile = PATH + " ";
    	String testfile  = PATH + " ";
    	
    	String rfile = PATH + "\\ionosphere.arff";
    	String wfile = OPATH + "\\ionosphere_crossValidation_testResult.arff";
    	
    	createClassifier.BatchClassifer(trainfile);
    	createClassifier.IncrementalClassifier(trainfile);
    	
    	createClassifier.distributionForTest(trainfile, testfile);
    	
    	createClassifier.cvPrediction(rfile, wfile);
    }
    
    
    public static void testMetaClassifier() throws Exception {
    	String rfile =  PATH + " ";
    	
    	metaClassifier.useFilter(rfile);
    	metaClassifier.useLowerLevel(rfile);
        metaClassifier.useMetaClassifier(rfile);
        
    } 
    
    public static void testCluster() throws Exception {
    	String rfile = PATH + " ";
    	
    	// clustering
    	cluster.IncrementalCluster(rfile);
    	cluster.BatchCluster(rfile);
    	// clustering evaluation
    	cluster.ClusterEvaluation1(rfile);
    	cluster.ClusterEvaluationWithCV(rfile);
    	//
    	cluster.ClassesToCluster(rfile);
    	
    	String trainfile = PATH + " ";
    	String testfile  = PATH + " ";
    	cluster.ClusterDistribution(trainfile, testfile);
 
    }
    
    
    public static void testPlot() throws Exception {
    	String rfile = PATH + " ";
    	
    	plot.PlotBayesNet(rfile);
    	plot.PlotTreeBasedJ48(rfile);
    	plot.ROCBasedNB(rfile);
    	
    }
    
    /*
     * 理解深拷贝与浅拷贝；
     *    Java 里面是传引用， 对与浅拷贝与深拷贝的理解应该达到两个对象各个层次上（对于复杂的对象）到底是不是同一个对象，
     *    这样想，如果想要创建一个完全的深拷贝， 则除过基本的类型int, double,char 等之外，其余所有的类型元素（包括Integer）都必须使用new 来进行创建。
     *    问题在于有的时候想要利用浅拷贝， 不完全深拷贝， 深拷贝， 做到心中有数， 没必要使用深拷贝时，尽量别用。 
     */
    public static void testS_DCopy() {
    	int i=1;
    	int  j=2;
    	double[] k = new double[] {2, 4, 6, 8, 10};
    	ObjA oa = new ObjA(i, j, k);
    	
    	char c = 'A';
    	ObjB  ob = new ObjB(oa, c);
    	
    	ObjB ob2 = ob; // 
    	Print(oa);
    	Print(ob);
    	Print(ob);
    	Print("ob == ob2?:" + ob.equals(ob2));
    	
    	ob2.c = 'B';
    	Print("ob2.c == ob.c ?: " + (ob2.c == ob.c) + "it indicate it manipulate the same object!");
    	ObjB  ob3 = new ObjB(ob);
    	Print(ob3);
    	Print("ob.equals(ob3)? :" + ob.equals(ob3) + "it indicate it manipulate the same object!" );
    	int i2 = 2;
    	int j2 = 3;
    	double[]  k2 = new double[]{3, 5, 6, 8};
    	//ob3.obj = new ObjA(i2, j2, k2);
    	ob3.obj.i = 111;
    	ob3.c = 'C';
    	Print("ob.c == ob3.c ? :" + (ob.c == ob3.c));
    	Print("ob.obj.equals(ob3.obj) ?:" + ob.obj.equals(ob3.obj));
    	Print("ob.obj.i == ob3.obj.i ? :"  + (ob.obj.i == ob3.obj.i));
    }
   
    // use for small test
    public static void temp() throws Exception {
    	String fileString = PATH + "\\segment-challenge.arff";
    	
    	/*
    	// 测试getStructure的相关功能
    	ArffLoader loader = new ArffLoader();
    	loader.setFile(new File(fileString));
    	
    	Instances dataStructure = loader.getStructure();
    	dataStructure.setClassIndex(dataStructure.numAttributes()-1);
    	System.out.println("" + dataStructure.numInstances());
    	System.out.println(dataStructure.numClasses());
    	System.out.println(dataStructure.get(0).numClasses());
    	*/
    	
    	/*
    	Instances dataInstances = loadData.loadArffFile1_2(fileString);
    	// Instance以及Instances的属性信息
    	System.out.println(dataInstances.numInstances());
    	dataInstances.setClassIndex(dataInstances.numAttributes()-1);
    	System.out.println(dataInstances.get(0).classValue());
    	System.out.println(dataInstances.classIndex());
    	Instance data = dataInstances.get(0);
    	System.out.println(data.numValues());
    	System.out.println(data.numClasses());
    	System.out.println(data.classValue());
    	
    
        // 统计各个类别所占的比例
    	int[] classcount = new int[data.numClasses()];
    	for (int i=0; i<dataInstances.numInstances(); ++i) {
    		classcount[(int) dataInstances.get(i).classValue()]++;
    	}
    	int total = 0;
    	for (int i=0; i<classcount.length; ++i) {
    		total += classcount[i];
    	}
    	System.out.println("total: " + total + "numInstances:" + dataInstances.numInstances());
    	
    	//
    	ArrayList<Instance> arrData = new ArrayList<Instance>();
    	arrData.add(dataInstances.get(0));
    	arrData.add(dataInstances.get(1));
    	
    	System.out.println("num of class:" + arrData.get(0).numClasses() );
    	System.out.println("num of class:" + dataInstances.numClasses());
    	*/
    	
    	
    	// test Util.*
    	
    	/*
    	double[] db= new double[10];
    	for (int i=0; i<db.length; ++i) {
    		db[i] = (i*199+3)%2009;
    	}
    	System.out.println("1. " + db);
    	System.out.println("2.Utils.* : " + Utils.arrayToString(db));
    	*/
    	
    	/*
    	String rfile = PATH + "\\segment-challenge.arff";
    	Instances data = loadData.loadArffFile1_2(rfile);
    	Instances headler = new Instances(data, 0 );
    	System.out.println("headler：" + headler);
    	System.out.println("headler.size:" + headler.size());
    	*/
    	/*
    	for (int i=0; i<0; ++i) {
    		System.out.println("ZZZ");
    	}
    	System.out.println("xxx");
    	*/
    	
    	/*
        int b = 4;
        int c = b + 'A';
        System.out.println(c + "");
        */
    	
    	/*
    	int[] cluCount = new int[]{0};
    	int[] cluCount2 = new int[8];  // 局部变量默认初始化为0， 不同于C++
    	
    	System.out.println("cluCount.length:" + cluCount.length);
    	for (int i=0; i<cluCount2.length; ++i) {
    		System.out.println("elem" + i + ":" + cluCount2[i]);
    	}
    	*/
    	
    	/*
    	Random rd = new Random(2234);
    	for (int i=0; i<10; ++i) {
    		int d = rd.nextInt(10);
    		System.out.println(d + " ");
    	}
    	*/
    	
    	/*
    	// 从数据集截取部分数据
    	int start = 10;
    	int end = 20;
    	Instances data = loadData.loadArffFile1_2(fileString);
    	InterceptionFromInss(data, start, end);
    	
    	ArffLoader loader = new ArffLoader();
    	loader.setFile(new File(fileString));
    	Instances dataStructure = loader.getStructure();
    	Instances insss = InterceptionFromInss(dataStructure,data, start, end);
    	PrintInstances(insss);
    	*/
    	
    	/*
    	//String[] sp = new String[3];
    	String[] sp = null;
    	if (sp == null) {
    		Print("sp is null");
    	}else {
    		Print("sp is not null");
    	}
    	
    	// test whether a new object
    	//DBJ[] dbj = new DBJ[2];
    	DBJ[] dbj = null;
    	if (dbj == null) {
    		Print("dbj is null");
    	}else {
    		Print("dbj is not null");
    	}
    	
    	int[] fir  = new int[2];
    	int[] sec  = new int[] {2,3};
    	if (fir == null) {
    		Print("fir is null");
    	}else {
    		Print("fir is not null");
    		Print("fir:" + fir[0] + " " + fir[1]);
    	}
    	Print("after:");
        fir = sec;
        if (fir == null) {
    		Print("fir is null");
    	}else {
    		Print("fir is not null");
    		Print("fir:" + fir[0] + " " + fir[1]);
    	}
    	*/
    	
    	double fraction = 0.1;
    	int batchSize = 100;
    	boolean[] lables = getHasLabels(fraction, batchSize);
        Print(lables);
    }
    
    // 
    public static boolean[] getHasLabels(double fraction, int batchSize) {
    	boolean[] labels = new boolean[batchSize];
    	ArrayList<Integer> indexs = new ArrayList<Integer>();
    	
    	for(int i=0; i<batchSize; ++i) {
    		indexs.add(new Integer(i));
    	}
    	
    	int labeledCount = (int) (fraction * batchSize);
    	
    	for (int i=0; i<labeledCount; ++i) {
    		Random rand = new Random(i);
    		int d = indexs.remove(rand.nextInt(indexs.size()));
    	    labels[d] = true;
    	    System.out.print(d + " ");  //
    	}
    		
    	return labels;	
    }
    
    public static Instances InterceptionFromInss(Instances inss, int start, int end) {
		Instances newInss = new Instances(inss);
		
		//PrintInstances(newInss);
		Print("newInstance.numInstances():" + newInss.numInstances());
		//Instances newInss2 = inss.getStructure();
    	return inss;
    	
    }
    
    // 截取
    public static Instances InterceptionFromInss(Instances structure, Instances inss, int start, int end) {
		Print("strcuture.numInstances():" + structure.numInstances());
    	for (int i=start; i<=end; ++i) {
    		structure.add(inss.instance(i));
    	}
    	Print("structure.numInstances():" + structure.numInstances());
    	
    	return structure;
    	
    }
    
    public static void  PrintInstances(Instances data) {
    	for (int i=0; i<data.numInstances(); ++i) {
    		System.out.println(data.instance(i));
    	} 
    }
    
    public static void Print(String s) {
    	System.out.println(s);
    }  
    
    public static void Print(boolean[] ls) {
    	String str = "";
    	for (int i=0; i<ls.length; ++i) {
    		str += ls[i] + "  ";
    	}
    	Print(str);
    }
   
    public static void Print(Object obj) {
    	System.out.println(obj);
    }
}

// 同一个java文件里面最多只能有一个
class ObjA{
	int i;
	int j;
	double[] k;
	
	public ObjA(int i, int j, double[] k) {
		this.i = i;
		this.j = j;
		this.k = new double[k.length];
		for (int s=0; s<k.length; ++s) {
			this.k[s] = k[s];
		}
	}
}

class ObjB {
	ObjA obj;
	char c;
	
	public ObjB(ObjA obj, char c) {
		this.obj = obj;
		this.c = c;
	}
    
    // shadow copy
	public ObjB(ObjB ob2) {
		// TODO Auto-generated constructor stub
		this.obj = ob2.obj;
		this.c = ob2.c;
	}
	
	/*
	// deep copy
	public  ObjB(ObjB ob2) {
		this.obj = new ObjA(ob2.obj.i, ob2.obj.j, ob2.obj.k);
		this.c = ob2.c;
		//return null;	
	}
	*/
}




