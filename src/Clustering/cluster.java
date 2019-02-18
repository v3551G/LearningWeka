package Clustering;

import java.io.File;

import DataIO.loadData;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.Cobweb;
import weka.clusterers.DensityBasedClusterer;
import weka.clusterers.EM;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

/*
 *  the definition of cluster class
 */
public class cluster {
     
	 // 批量聚类
	 public static void BatchCluster(String rfile) throws Exception {
		 Instances data = loadData.loadArffFile1_2(rfile);
		 
		 String[] options = new String[2];
		 options[0] = "-I";
		 options[1] = "100";
		 
		 EM emcluster = new EM();
		 emcluster.setOptions(options);
		 
		 emcluster.buildClusterer(data);
		 
		 myPrintln(emcluster);
		 //System.out.println(emcluster); 
	 }
	 
	 // 增量聚类
	 public static void IncrementalCluster(String rfile) throws Exception {
		 //Instances data = loadData.loadArffFile1_2(rfile);
		 ArffLoader loader = new ArffLoader();
		 loader.setFile(new File(rfile));
		 Instances structure = loader.getStructure();
		 
		 Cobweb cb = new Cobweb();
		 cb.buildClusterer(structure);
		 Instance ins;
		while( (ins = loader.getNextInstance(structure)) != null) {
			cb.updateClusterer(ins);
		}
		
		myPrintln(cb);
	 }
	 
	 // 评价1
	 public static void ClusterEvaluation1(String rfile) throws Exception {
		 Instances data = loadData.loadArffFile1_2(rfile);
		 
		 String[] options = new String[2];
		 options[0] = "-t";
		 options[1] = "fileName";
		 
		 String output = ClusterEvaluation.evaluateClusterer(new EM() , options);
		 
		 myPrintln(output);
		 
	 }
	 
	 // 评价2
	 public static void ClusterEvaluationWithCV(String rfile) throws Exception {
		 Instances data = loadData.loadArffFile1_2(rfile);
		 
		 DensityBasedClusterer dbClusterer = new EM();
		 dbClusterer.buildClusterer(data);

		 ClusterEvaluation eval = new ClusterEvaluation();
		 eval.setClusterer(dbClusterer);
		 eval.evaluateClusterer(new Instances(data));
		 
		 // 下面两个异同
		 myPrintln(eval); 
		 System.out.println(eval.clusterResultsToString()); 
		 
		 int fold = 10;
		 int seed = 1234;
		 dbClusterer = new EM();
		 double likeliyhod = ClusterEvaluation.crossValidateModel(dbClusterer, data, fold, data.getRandomNumberGenerator(seed) );
		 
		 myPrintln("likelihod" + likeliyhod);
	 }
	 
	 // 簇的类别评价
	 public static void ClassesToCluster(String rfile) throws Exception {
		 Instances data = loadData.loadArffFile1_2(rfile);
		 data.setClassIndex(data.numAttributes()-1);
		 
		 Remove rm = new Remove();  // 设置过滤器
		 rm.setAttributeIndices(" " + (data.classIndex()-1));
		 rm.setInputFormat(data);
		 Instances newData = Filter.useFilter(data, rm);  // 过滤掉类别属性
		 
		 EM emcluster = new EM();
		 emcluster.buildClusterer(newData); // 聚类
		 
		 ClusterEvaluation cev = new ClusterEvaluation();
		 cev.setClusterer(emcluster);
		 cev.evaluateClusterer(data);
		 
		 myPrintln(cev);
	 }
	 
	 // 簇的分布评价
	 public static void ClusterDistribution(String trainfile, String testfile) throws Exception {
		 Instances train = loadData.loadArffFile1_2(trainfile);
		 Instances test  = loadData.loadArffFile1_2(testfile);
         
		 if (!train.equalHeaders(test)) {
			 new Throwable("the train dataSet and test dataSet are not in the same format!");
		 }
		 
		 EM emcluster = new EM();
		 emcluster.buildClusterer(train);
		 
		 double[] dis;
		 int pre;
		 for (int i=0; i<test.numInstances(); ++i) {
			 pre = emcluster.clusterInstance(test.instance(i));
			 dis = emcluster.distributionForInstance(test.instance(i));
			 
			 myPrint(" " + (i+1));
			 myPrint("--");
			 myPrint(pre + "");
			 myPrint("--");
			 myPrint(Utils.arrayToString(dis));
			 myPrintln("");
		 }
	 }
	 
	 public static void myPrintln(String s) {
		 System.out.println(s);
	 }
	 
	 public static void myPrint(String s) {
		 System.out.print(s);
	 }
	 public static void myPrintln(EM clu) {
		 System.out.println(clu);
	 }
	 
	 public static void myPrintln(Cobweb cb) {
		 System.out.println(cb);
	 }
	 
	 public static void myPrintln(ClusterEvaluation ev) {
		 System.out.println(ev);
	 }
}
