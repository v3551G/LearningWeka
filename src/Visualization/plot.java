package Visualization;

import java.awt.BorderLayout;
import java.util.Random;

import javax.swing.JFrame;

//import moa.classifiers.Classifier;
//import moa.classifiers.bayes.NaiveBayes;
import DataIO.loadData;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.ThresholdCurve;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.Utils;
import weka.gui.graphvisualizer.GraphVisualizer;
import weka.gui.treevisualizer.NodePlace;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;
import weka.gui.visualize.PlotData2D;
import weka.gui.visualize.ThresholdVisualizePanel;

/*
 *  the plot clas ,including ROC, Tree struccture, 
 */
public class plot {
     
	 /*
	  *  ���ӻ�ROC����
	  */
	 public static void ROCBasedNB(String rfile) throws Exception {
		 Instances data = loadData.loadArffFile1_2(rfile);
		 data.setClassIndex(data.numAttributes()-1);
		 
		 // �����������Լ�����ģ��
		 int seed = 1234;
		 int fold = 10;
		 Classifier cls = new NaiveBayes();  // ע��weka �� moa������
		 Evaluation eval = new Evaluation(data);
		 eval.crossValidateModel(cls, data, fold, new Random(seed));
		 //////// plot
		 //1. ���ɿɻ��Ƶ�����
		 int classsIndex = 0;
		 ThresholdCurve tc = new ThresholdCurve();
		 Instances curve = tc.getCurve(eval.predictions(), classsIndex);
		 
		 //2. �����ݷ����ͼ����
		 PlotData2D plotData = new PlotData2D(curve);
		 plotData.setPlotName("ROC");
		 plotData.addInstanceNumberAttribute();
		 
		 //3. ����ͼ������������ӻ����
		 ThresholdVisualizePanel tsVp = new ThresholdVisualizePanel();
		 tsVp.setROCString("(Area under ROC= " 
				 + Utils.doubleToString(ThresholdCurve.getROCArea(curve), 4)+ ")" );;
		 tsVp.setName(curve.relationName());
		 
		 boolean[] cp = new  boolean[curve.numInstances()];
		 for (int i=0; i<cp.length; ++i) {
			 cp[i] = true;
		 }
		 plotData.setConnectPoints(cp);
		 tsVp.addPlot(plotData);
		 
		 //4. �����ӻ���������JFrame
		 final JFrame jFrame = new JFrame("WEKA ROC:" + tsVp.getName());
		 jFrame.setSize(500, 400);
		 jFrame.getContentPane().setLayout(new BorderLayout());
		 jFrame.getContentPane().add(tsVp, BorderLayout.CENTER);
		 jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		 jFrame.setVisible(true);
	 }
	 
	 /*
	  * NayesNet ���ӻ�
	  */
	 public static void PlotBayesNet(String rfile) throws Exception {
		 Instances data = loadData.loadArffFile1_2(rfile);
		 data.setClassIndex(data.numAttributes()-1);
		 
		 BayesNet bNet = new BayesNet();
		 bNet.buildClassifier(data);
		 
		 GraphVisualizer gv = new GraphVisualizer();
		 gv.readBIF(bNet.graph());
		 
		 JFrame jFrame = new JFrame("BayesNet ���ӻ���");
		 jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		 jFrame.setSize(500, 400);
		 jFrame.getContentPane().setLayout(new BorderLayout());
		 jFrame.getContentPane().add(gv, BorderLayout.CENTER); 
		 jFrame.setVisible(true);
		 
		 gv.layoutGraph();
	 }
	 
	 /*
	  *  ���Ŀ��ӻ�
	  */
	 public static void PlotTreeBasedJ48(String rfile) throws Exception {
		 Instances data = loadData.loadArffFile1_2(rfile);
		 data.setClassIndex(data.numAttributes()-1);
		 
		 J48 cls = new J48();
		 cls.buildClassifier(data);
		 
		 TreeVisualizer tv = new TreeVisualizer(null, cls.graph(), new PlaceNode2());
		 
		 JFrame jFrame = new JFrame("J48�����������ӻ�");
		 jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		 jFrame.setSize(500, 400);
		 jFrame.getContentPane().setLayout(new BorderLayout());
		 jFrame.getContentPane().add(tv,BorderLayout.CENTER);
		 
		 jFrame.setVisible(true);
		 
		 tv.fitToScreen();
	 }
}
