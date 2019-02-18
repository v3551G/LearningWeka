package PreProcess;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

import weka.core.Attribute;

//import com.sun.org.apache.bcel.internal.classfile.Attribute;

import weka.core.Instances;
import weka.core.converters.ArffLoader;
/**
 * 
 * @author Administrator
 * @function: reorganzie powersupply to simulate concept drift
 * @author:qkk
 * @date: 2018/5/17
 *
 */
public class PowerSupplyPreProcess {
	public static void main(String[] args) throws IOException {
		String path = "G:\\dataRepository\\SPASC dataset";
		String dName = "powersupplyNorm.arff";
		
		String wdName = "powersupplyNorm-reorganize-rec.arff";
		/**
		 * concept change process: f1->f1->f1+f3->f3+f4->f3->f3+f6->f6->f4->f6->f6+f5->f5->f5+f7->f7->f5->f7->f7+f9->f9->f9+f12->f12->f9
		 */
		ArffLoader loader = new ArffLoader();
		loader.setFile(new File(path + "\\" + dName));
		Instances datas = loader.getDataSet();
		Instances structure = loader.getStructure();
		datas.setClassIndex(datas.numAttributes()-1);
		structure.setClassIndex(datas.numAttributes()-1);
		
		int numInst = datas.numInstances();
		
		int[] count = new int[datas.numClasses()];
		// initialization
		ArrayList<ArrayList<Integer>> eCIndex = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> temp = new ArrayList<Integer>();
		
		for (int i=0; i<datas.numClasses(); ++i) {
			eCIndex.add(new ArrayList<Integer>());
		}
		// record count and eCIndex
		for (int i=0; i<numInst; ++i) {
			int clsValue = (int) datas.instance(i).classValue();
			count[clsValue]++;
			eCIndex.get(clsValue).add(i);
		}
//		System.out.println(" each class frequency: ");
//		print(count);
		
		// 
		Instances newStruct = new Instances(structure);
		ArrayList<String> vlist = new ArrayList<String>();
		vlist.add("1");vlist.add("3");vlist.add("4");vlist.add("5");vlist.add("6");vlist.add("7"); vlist.add("9");vlist.add("12");
//		System.out.println("xx");
		Attribute newcls = new Attribute("classx", vlist);
//		System.out.println("xx");
		
		newStruct.insertAttributeAt(newcls, structure.numAttributes());
		newStruct.setClassIndex(newStruct.numAttributes()-1);
		newStruct.deleteAttributeAt(newStruct.numAttributes()-2);
		System.out.println(newStruct.toString());
		
		
		newStruct.setRelationName("reorganize powersupply,concept change process: f1->f1->f1+f3->f3+f4->f3->f3+f6->f6->f4->f6->f6+f5->f5->f5+f7->f7->f5->f7->f7+f9->f9->f9+f12->f12->f9");
		Instances newDatas = new Instances(newStruct);
		
		int seed = 1234567;
		int batchNumber = 400;
		int hafBatchNumber = 200;
		
		// f1
		Random random = new Random(seed);
		ArrayList<Integer> fd1 = eCIndex.get(1);
		for (int i=0; i<batchNumber; ++i) {
			int de = fd1.remove(random.nextInt(fd1.size()));
			newDatas.add(datas.instance(de));
			System.out.println(datas.instance(de).toString());
		}
		// f1
		random = new Random(seed);
		for (int i=0; i<batchNumber; ++i) {
			int de = fd1.remove(random.nextInt(fd1.size()));
			newDatas.add(datas.instance(de));
			System.out.println(datas.instance(de).toString());
		}
		// f1+f3
		temp.clear();
		random = new Random(seed);
		for (int i=0; i<hafBatchNumber; ++i) {
			int de = fd1.remove(random.nextInt(fd1.size()));
			//newDatas.add(datas.instance(de));
			//System.out.println(datas.instance(de).toString());
			temp.add(de);
		}
		ArrayList<Integer> fd3 = eCIndex.get(3);
		for (int i=0; i<hafBatchNumber; ++i) {
			int de = fd3.remove(random.nextInt(fd3.size()));
			//newDatas.add(datas.instance(de));
			//System.out.println(datas.instance(de).toString());
			temp.add(de);
		}
		random = new Random(seed);
		for (int i=0; i<batchNumber; ++i) {
			int de = temp.remove(random.nextInt(temp.size()));
			newDatas.add(datas.instance(de));
			System.out.println(datas.instance(de).toString());
		}
		// f3+f4
		temp.clear();
		random = new Random(seed);
		for (int i=0; i<hafBatchNumber; ++i) {
			int de = fd3.remove(random.nextInt(fd3.size()));
//			newDatas.add(datas.instance(de));
//			System.out.println(datas.instance(de).toString());
			temp.add(de);
		}
		ArrayList<Integer> fd4 = eCIndex.get(4);
		for (int i=0; i<hafBatchNumber; ++i) {
			int de = fd4.remove(random.nextInt(fd4.size()));
//			newDatas.add(datas.instance(de));
//			System.out.println(datas.instance(de).toString());
			temp.add(de);
		}
		random = new Random(seed);
		for (int i=0; i<batchNumber; ++i) {
			int de = temp.remove(random.nextInt(temp.size()));
			newDatas.add(datas.instance(de));
			System.out.println(datas.instance(de).toString());
		}
		// f3
		random = new Random(seed);
		for (int i=0; i<batchNumber; ++i) {
			int de = fd3.remove(random.nextInt(fd3.size()));
			newDatas.add(datas.instance(de));
			System.out.println(datas.instance(de).toString());
		}
		// f3 + f6
		temp.clear();
		random = new Random(seed);
		for (int i=0; i<hafBatchNumber; ++i) {
			int de = fd3.remove(random.nextInt(fd3.size()));
//			newDatas.add(datas.instance(de));
//			System.out.println(datas.instance(de).toString());
			temp.add(de);
		}
		ArrayList<Integer> fd6 = eCIndex.get(6);
		for (int i=0; i<hafBatchNumber; ++i) {
			int de = fd6.remove(random.nextInt(fd6.size()));
//			newDatas.add(datas.instance(de));
//			System.out.println(datas.instance(de).toString());
			temp.add(de);
		}
		random = new Random(seed);
		for (int i=0; i<batchNumber; ++i) {
			int de = temp.remove(random.nextInt(temp.size()));
			newDatas.add(datas.instance(de));
			System.out.println(datas.instance(de).toString());
		}
		// f6
		random = new Random(seed);
		for (int i=0; i<batchNumber; ++i) {
			int de = fd6.remove(random.nextInt(fd6.size()));
			newDatas.add(datas.instance(de));
			System.out.println(datas.instance(de).toString());
		}
		// f4
		random = new Random(seed);
		for (int i=0; i<batchNumber; ++i) {
			int de = fd4.remove(random.nextInt(fd4.size()));
			newDatas.add(datas.instance(de));
			System.out.println(datas.instance(de).toString());
		}
		// f6
		random = new Random(seed);
		for (int i=0; i<batchNumber; ++i) {
			int de = fd6.remove(random.nextInt(fd6.size()));
			newDatas.add(datas.instance(de));
			System.out.println(datas.instance(de).toString());
		}
		// f6+f5
		temp.clear();
		random = new Random(seed);
		for (int i=0; i<hafBatchNumber; ++i) {
			int de = fd6.remove(random.nextInt(fd6.size()));
//			newDatas.add(datas.instance(de));
//			System.out.println(datas.instance(de).toString());
			temp.add(de);
		}
		ArrayList<Integer> fd5 = eCIndex.get(5);
		for (int i=0; i<hafBatchNumber; ++i) {
			int de = fd5.remove(random.nextInt(fd5.size()));
//			newDatas.add(datas.instance(de));
//			System.out.println(datas.instance(de).toString());
			temp.add(de);
		}
		random = new Random(seed);
		for (int i=0; i<batchNumber; ++i) {
			int de = temp.remove(random.nextInt(temp.size()));
			newDatas.add(datas.instance(de));
			System.out.println(datas.instance(de).toString());
		}
		// f5
		random = new Random(seed);
		for (int i=0; i<batchNumber; ++i) {
			int de = fd5.remove(random.nextInt(fd5.size()));
			newDatas.add(datas.instance(de));
			System.out.println(datas.instance(de).toString());
		}
		// f5+ f7
		temp.clear();
		random = new Random(seed);
		for (int i=0 ;i<hafBatchNumber; ++i) {
			int de = fd5.remove(random.nextInt(fd5.size()));
//			newDatas.add(datas.instance(de));
//			System.out.println(datas.instance(de).toString());
			temp.add(de);
		}
		ArrayList<Integer> fd7 = eCIndex.get(7);
		for (int i=0; i<hafBatchNumber; ++i) {
			int de = fd7.remove(random.nextInt(fd7.size()));
//			newDatas.add(datas.instance(de));
//			System.out.println(datas.instance(de).toString());
			temp.add(de);
		}
		random = new Random(seed);
		for (int i=0; i<batchNumber; ++i) {
			int de = temp.remove(random.nextInt(temp.size()));
			newDatas.add(datas.instance(de));
			System.out.println(datas.instance(de).toString());
		}
		// f7
		random = new Random(seed);
		for (int i=0; i<batchNumber; ++i) {
			int de = fd7.remove(random.nextInt(fd7.size()));
			newDatas.add(datas.instance(de));
			System.out.println(datas.instance(de).toString());
		}
		// f5
		random = new Random(seed);
		for (int i=0; i<batchNumber; ++i) {
			int de = fd5.remove(random.nextInt(fd5.size()));
			newDatas.add(datas.instance(de));
			System.out.println(datas.instance(de).toString());
		}
		// f7
		random = new Random(seed);
		for (int i=0; i<batchNumber; ++i) {
			int de = fd7.remove(random.nextInt(fd7.size()));
			newDatas.add(datas.instance(de));
			System.out.println(datas.instance(de).toString());
		}
		// f7+ f9
		temp.clear();
		random = new Random(seed);
		for (int i=0; i<hafBatchNumber; ++i) {
			int de =fd7.remove(random.nextInt(fd7.size()));
//			newDatas.add(datas.instance(de));
//			System.out.println(datas.instance(de).toString());
			temp.add(de);
		}
		ArrayList<Integer> fd9 = eCIndex.get(9);
		for (int i=0; i<hafBatchNumber; ++i) {
			int de = fd9.remove(random.nextInt(fd9.size()));
//			newDatas.add(datas.instance(de));
//			System.out.println(datas.instance(de).toString());
			temp.add(de);
		}
		random = new Random(seed);
		for (int i=0; i<batchNumber; ++i) {
			int de = temp.remove(random.nextInt(temp.size()));
			newDatas.add(datas.instance(de));
			System.out.println(datas.instance(de).toString());
		}
		// f9
		random = new Random(seed);
		for (int i=0; i<batchNumber; ++i) {
			int de = fd9.remove(random.nextInt(fd9.size()));
			newDatas.add(datas.instance(de));
			System.out.println(datas.instance(de).toString());
		}
		
		// f9+f12
		temp.clear();
		random = new Random(seed);
		for (int i=0; i<hafBatchNumber; ++i) {
			int de = fd9.remove(random.nextInt(fd9.size()));
//			newDatas.add(datas.instance(de));
//			System.out.println(datas.instance(de).toString());
			temp.add(de);
		}
		ArrayList<Integer> fd12 = eCIndex.get(12);
		for (int i=0; i<hafBatchNumber; ++i) {
			int de = fd12.remove(random.nextInt(fd12.size()));
//			newDatas.add(datas.instance(de));
//			System.out.println(datas.instance(de).toString());
			temp.add(de);
		}
		random = new Random(seed);
		for (int i=0; i<batchNumber; ++i) {
			int de = temp.remove(random.nextInt(temp.size()));
			newDatas.add(datas.instance(de));
			System.out.println(datas.instance(de).toString());
		}
		
		// f12
		random = new Random(seed);
		for (int i=0; i<batchNumber; ++i) {
			int de = fd12.remove(random.nextInt(fd12.size()));
			newDatas.add(datas.instance(de));
			System.out.println(datas.instance(de).toString());
		}
		// f9
		random = new Random(seed);
		for (int i=0; i<batchNumber; ++i) {
			int de = fd9.remove(random.nextInt(fd9.size()));
			newDatas.add(datas.instance(de));
			System.out.println(datas.instance(de).toString());
		}
		
		// Write data to file
//		ArffSaver saver = new ArffSaver();
//		saver.setInstances(newDatas);
//		saver.setDestination(new File(path + "\\" + wdName));
//		saver.writeBatch();
		
		PrintStream oPrintStream = new PrintStream(new File(path + "\\" + wdName));
		System.out.println(newDatas.numInstances());
//		oPrintStream.print(newDatas.toString());
		System.out.println(" the data has been successfully writed into file");
	}
	
	public static void print(int[] v) {
		String str = "";
		for (int i=0; i<v.length; ++i) {
			str += v[i] + "\t";
		}
		System.out.println(str);
	}
}
