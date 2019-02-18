package Instance;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

import DataIO.loadData;

public class myInstances {
    /*
     * 
     */
	// 构建一个实例
	public static void createInstances() throws ParseException {
		ArrayList<Attribute> atts;
		ArrayList<Attribute> attsRel;
		
		ArrayList<String> attVals;
		ArrayList<String> attValsRel;
		
		Instances data;
		Instances dataRel;
		
		// 1.设置属性;
		atts = new ArrayList<Attribute>();
		atts.add(new Attribute("att1"));   // -- 1.numerical
		
		attVals = new ArrayList<String>();
		for (int i=0; i<10; ++i) {
			attVals.add("val" + (i+1));
		}
		atts.add(new Attribute("att2",attVals)); // -- 2.nominal
		
		atts.add(new Attribute("att3", (ArrayList<String>)null)); // -- 3.string
		
		atts.add(new Attribute("att4","yyyy-MM-dd"));  // -- 4. date
		
		attsRel = new ArrayList<Attribute>();
		attsRel.add(new Attribute("att5.1")); // -- -- 5.1 numeric
		attValsRel = new ArrayList<String>();
		for (int i=0; i<10; ++i) {
			attValsRel.add("val5." + (i+1));
		}
		attsRel.add(new Attribute("att5.2", attValsRel)); //-- -- 5.2 nominal
		dataRel = new Instances("att5", attsRel, 0);
		
		atts.add(new Attribute("att5", dataRel, 0));  // -- 5. relational，关系型，（连个属性之间的关系）
		
		// 2. 创建Instancces 对象
		data = new Instances("MyRelation", atts, 0);
		
		// 3. 添加数据
		double[] values;
		values = new double[data.numAttributes()];
		values[0] = Math.PI;    // 数值型                    
		values[1] = attVals.indexOf("val2");  // 标称型
		values[2] = data.attribute(2).addStringValue("A string.");  // 字符串型
		values[3] = data.attribute(3).parseDate("2013-07-23");  // 日期型
		
		dataRel = new Instances(data.attribute(4).relation(), 0); // -- 关系型
		double[] valsRel;
		valsRel = new double[2];
		valsRel[0] = Math.PI + 1;
		valsRel[1] = attValsRel.indexOf("val5.4");
		dataRel.add(new DenseInstance(1.0, valsRel));
		
		valsRel = new double[2];
		valsRel[0] = Math.PI + 2;
		valsRel[1] = attValsRel.indexOf("val5.3");
		dataRel.add(new DenseInstance(1.0, valsRel));
		
		values[4] = data.attribute(4).addRelation(dataRel);
		
		data.add(new DenseInstance(1.0, values)); // 添加第一条数据
		//////////////////////////////////////////////
		values = new double[data.numAttributes()];
		values[0] = Math.PI + 3;
		values[1] = attVals.indexOf("val3");
		values[2] = data.attribute(2).addStringValue("B a string.");
		values[3] = data.attribute(3).parseDate("2017-03-31");
		
		dataRel = new Instances(data.attribute(4).relation(), 0);
		valsRel = new double[2];
		valsRel[0] = Math.PI + 23;
		valsRel[1] = attValsRel.indexOf("val5.5");
		dataRel.add(new DenseInstance(1.0, valsRel));
		
		valsRel = new double[2];
		valsRel[0] = Math.PI - 2;
		valsRel[1] = attValsRel.indexOf("val5.1");
		dataRel.add(new DenseInstance(1.0, valsRel));
		
		values[4] = data.attribute(4).addRelation(dataRel);
		
		data.add(new DenseInstance(1.0, values));  // 添加第二条数据
		
		// 4. 输出
		System.out.println("num of Instances:" + data.numInstances());
		System.out.println(data);
		
	}
	
	
	public static void instancesAnalysis(String rfile) throws Exception {
		// 1.Instance Instances的区别及各自的方法分析
		myPrintln("1. the difference between instances.getStructure and instances.getDataSet");
		Instances data = loadData.loadArffFile1_2(rfile);
		
		myPrintln(data + "");
		
		myPrintln("data.numInstance: " + data.numInstances());
		myPrintln("data.numAttribute : " + data.numAttributes());
		myPrintln("data.classIndex: " + data.classIndex() 
				+ "data.instance(0).classIndex(): " + data.instance(0).classIndex());
		
		ArffLoader loader =  new ArffLoader();
		loader.setFile(new File(rfile));
		Instances data2 = loader.getStructure(); // 区分loader.getDataSet()
		myPrintln("data2.numInstances():" + data2.numInstances());
		myPrintln("data2:" + data2);
		myPrintln("data2:"  + data2.numAttributes());
		
		// 2. 
		myPrintln("2. the function of instance");
		Instance ins = data2.instance(2);
		myPrintln("instance 2:" + ins);
		myPrintln("instance 2.attr(2)" + ins.value(2));
		myPrintln(" " + ins.classIndex() + " ins.classValue: " + ins.classValue());
		//myPrintln(" " + data2.)
		
		
		
	}
	
	public static void  myPrintln(String s) {
		System.out.println(s);
	}
}
