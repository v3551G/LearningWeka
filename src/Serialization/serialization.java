package Serialization;

import DataIO.loadData;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;

/*
 *  the definition of serialization class
 */
public class serialization {
    
	// ģ�����л�
	public static void ModelSerialization(String rfile, String wfile) throws Exception {
		Instances data = loadData.loadArffFile1_2(rfile);
		data.setClassIndex(data.numAttributes()-1);
		
		Classifier cls = new J48();
		cls.buildClassifier(data);
		
		// ���л�
		SerializationHelper.write(wfile, cls);
		myPrintln("���л��ɹ���");
		// �����л�
		Classifier cls2 = (Classifier) SerializationHelper.read(wfile);
		myPrintln("�����л��ɹ���");
		myPrintln(cls);
		//ModelSerialization(rfile, wfile);
	}
	
    // ͷ��Ϣ���л�
	public static void HeadlerSerialization(String trainfile, String testfile,  String wfile) throws Exception {
		Instances data = loadData.loadArffFile1_2(trainfile);
		data.setClassIndex(data.numAttributes()-1);
		
		Classifier cls = new J48();
		cls.buildClassifier(data);
		
		// ���л�
		Instances headler = new Instances(data, 0);
		SerializationHelper.writeAll(wfile, new Object[] {cls, headler});
		
		Instances test = loadData.loadArffFile1_2(testfile);
		test.setClassIndex(test.numAttributes()-1);
		
		// �����л�
		Object[] objects = SerializationHelper.readAll(wfile);
		Classifier scls = (Classifier) objects[0];
		Instances sheadler = (Instances) objects[1];
		
		if (!data.equalHeaders(test)) {
			new Exception("the test dataSet and the model are not in the ame format");
		}
		myPrintln("�����л��� ������:");
		myPrintln(scls);
		myPrintln("�����л��� ͷ��Ϣ:");
		myPrintln(sheadler);
	}
	
	public static void myPrintln(String s) {
		System.out.println(s);
	}
	
	public static void myPrintln(Classifier cls) {
		System.out.println(cls);
	}
	
	public static void myPrintln(Instances ins) {
		System.out.println(ins);
	}
}
