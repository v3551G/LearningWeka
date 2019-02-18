package Filter;

import java.io.File;
import java.util.Random;

import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ArffLoader;
import weka.core.converters.ConverterUtils.DataSink;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import DataIO.loadData;

/**
 * @description: ���ˣ� �� ���ݼ����ԵĽǶ�
 * @author Administrator
 *
 */
public class optionalHandler {
     /*
      * �������ַ������ö����Ƴ����ݼ��ĵ�һ��
      */
	 // ͨ������ String[] ������options
	 public static Instances filterData1(String rfile) throws Exception {
		 Instances data = loadData.loadArffFile1_2(rfile);
		 Instances fdata = null;
		 
		 String[] options = new String[2];
		 options[0] = "-R";
		 options[1] = "1";
		 
		 Remove rm = new Remove();
		 rm.setOptions(options);
		 
	     rm.setInputFormat(data);
	     fdata = Filter.useFilter(data, rm);
	    
	     return fdata;
	 }
	 
	 // ͨ��Utils.splitOptions������options
	 public static Instances filterData2(String rfile) throws Exception {
		 Instances data = loadData.loadArffFile1_2(rfile);
		 Instances fdata = null;
		 
		 String[] options = Utils.splitOptions("-R 1");
		 
		 Remove rm = new Remove();
		 rm.setOptions(options);
		 
		 rm.setInputFormat(data);
		 fdata = Filter.useFilter(data, rm);
		 
		 return fdata;
	 }
	 
	 // ͨ��Remove�� set ����
	 public static Instances filterData3(String rfile) throws Exception {
		 Instances data = loadData.loadArffFile1_2(rfile);
		 Instances fdata = null;
		 
		 String[] options;
		 
		 Remove rm = new Remove();
		 rm.setAttributeIndices("1");
		 
		 rm.setInputFormat(data);
		 fdata = Filter.useFilter(data, rm);
		 
		 return fdata;
	 }
	
	 // ͨ�� shuffle ����ϴ�ƣ��õ������ݵ�һ������(���ظ�)
	 public static Instances shuffle(String rfile) throws Exception {
		Instances data = loadData.loadArffFile1_2(rfile);
		int seed = 1234;
		
		Instances dataTemp = new Instances(data);
		dataTemp.randomize(new Random(seed));
		
		return dataTemp;
	 }
	 
	 /*
	  * ����ظ�ϴ�ƣ��õ������ݵĶ������м�(���ظ�)
	  */
	 public static Instances[] shuffle(String rfile, int count) throws Exception {
		 int[] seeds = new int[count];
		 Instances[] datas = new Instances[count];
		 Instances dataTemp;
		 
		 Instances data = loadData.loadArffFile1_2(rfile);
		 	 
		 for (int i=0; i<count; ++i) {
			 seeds[i] = i+1;
		 }
		 
		 for (int i=0; i<count; ++i) {
			 dataTemp = new Instances(data);
			 
			 dataTemp.randomize( new Random(seeds[i]));
			 datas[i] = dataTemp; 
		 }
		 System.out.println("the randomized sequence of the dataSte has ben successfully created");
		 
		 return datas;
	 }
	 
	/**
	 * @description: �޳����ݼ��еķ���ֵ���ԣ�����׼����min-max��
	 * @param ifile
	 * @param ofile
	 * @throws Exception
	 */
     public static void filterDataSet(String ifile, String ofile) throws Exception{
   	  Instances dataOrigin;
   	  Instances dataRet;
   	  
   	  ArffLoader loader  = new ArffLoader();
   	  loader.setFile(new File(ifile));
   	  dataOrigin = loader.getDataSet();
   	  dataRet = loader.getStructure();
   	  
   	  // delete all the nominal attribute
   	  boolean[] isNumeric = new boolean[dataOrigin.numAttributes()];
   	  for (int i=isNumeric.length-2; i>=0; i--) {
   		  if (dataRet.attribute(i).isNumeric()) {
   			  isNumeric[i] = true;
   		  }else {
   			  isNumeric[i] = false;
   			  dataRet.deleteAttributeAt(i);
   			  dataOrigin.deleteAttributeAt(i);
   		  }
   	  }
   	  //DataSink.write(ofile, dataOrigin);
   	  
   	  // Standardization
//    Standardize filter = new Standardize();
//	  filter.setInputFormat(dataOrigin);
//	  dataRet = Filter.useFilter(dataOrigin, filter);
   	  
   	  //dataRet = standardize.Z_score(dataOrigin);
   	  dataRet = standardize.min_max_std(dataOrigin);
      DataSink.write(ofile, dataRet);
   	  
//   	  // write
//   	  for (int i=0; i<dataOrigin.numInstances(); ++i) {
//   		  Instance temp = null;
//   		  for (int j=0; j<dataOrigin.numAttributes()-1; ++j) {
//   			  if (isNumeric[j]) {
//   				  temp.setValue(j, dataOrigin.instance(i).value(j));
//   			  }
//   		  }
//   	  }
     }
     
	 /*
	  * @test
	  */
	 public static void main(String[] args) throws Exception {
		    String rfile = "G:\\dataRepository\\SPASC dataset\\KDDCup99 -copy.arff";
		    Instances datas;
		    // ɾ�����ݼ��еĵ�һ��(���ַ���)
	    	//datas = optionalHandler.filterData1(rfile);
	    	//datas = optionalHandler.filterData2(rfile);
	    	//datas optionalHandler.filterData3(rfile);
	    	//System.out.println(datas.instance(0));
		    
	    	// ���ɸ����ݼ���һ��������У����ظ���
		    //datas = optionalHandler.shuffle(rfile);
		    //System.out.println(datas.instance(0));
		    
		    // ���ɸ����ݼ��Ķ������У����ظ���
//		    Instances[] datass;
//		    int count = 2;
//		    datass = optionalHandler.shuffle(rfile, count);
//		    System.out.println(datass[0].instance(0));
//		    System.out.println(datass[1].instance(0));
//		    
		    // �޳����ݼ������з���ֵ���Բ���׼��
			String wfile = "G:\\dataRepository\\SPASC dataset\\kdd99modified.arff";
			//filterDataSet(rfile, wfile);
			
	 }
}
