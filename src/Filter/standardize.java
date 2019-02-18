package Filter;

import java.io.File;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSink;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;
/*
 *  Standardization:
 *      only numeric attribute, consider mixture attribute for the next step 
 *  
 *    Z-score standardization,  assumption:the dataSet are conform to normalize distribution, e.g miu = 0, sigma = 1
 *    min-max standardization,  note that when all the value in an attribute are the same, the results on the attribute will be "?"
 *    
 *  Note: 
 *     the Z-score standardization created by myself is the same with the method "filter.filterBatch()" by test 
 */
public class standardize {
    String rfileName = "";
    String wfileNameString = "";
    
    public standardize() {
    	
    }
    
    public static  Instances Z_score(Instances data) {
    	Instances newData = new Instances(data);
    	
    	double[] mean = new double[data.instance(0).numAttributes()-1];
    	// compute mean
    	for (int i=0; i<mean.length; ++i) {
    		mean[i] = data.meanOrMode(i);
    	}
    	// compute sigama
    	double[] accum = new double[data.instance(0).numAttributes()-1];
    	for (int i=0; i<data.numInstances(); ++i) {
    		for (int j=0; j<mean.length; ++j) {
    			accum[j] += Math.pow(data.instance(i).value(j)-mean[j], 2);
    		}
    	}
    	for (int i=0; i<accum.length; ++i) {
    		accum[i] = Math.sqrt( accum[i] / (data.numInstances()-1) );
    	}
    	// generate data
    	for (int i=0; i<newData.numInstances(); ++i) {
    		for (int j=0; j<mean.length; ++j) {
    			newData.instance(i).setValue(j, (newData.instance(i).value(j)-mean[j])/accum[j]);
    		}
    	}
    	return newData;
    }
    
    public static void Z_scoure(String wfileName, Instances data) throws Exception {
    	Instances datas = Z_score(data);
    	DataSink.write(wfileName, datas);
    }
    
    public static void Z_scoure(String wfileName, String rfileName) throws Exception {
    	ArffLoader loader = new ArffLoader();
    	loader.setFile(new File(rfileName));
    	Instances data = loader.getDataSet();
    	
    	Instances datas = Z_score(data);
    	
    	DataSink.write(wfileName, datas);
    }
    
    public static Instances min_max_std(Instances data) {
    	Instances datas = new Instances(data);
    	
    	double[] min = new double[data.numAttributes()-1];
    	double[] max = new double[data.numAttributes()-1];
    	double[] fm  = new double[data.numAttributes()-1];
    	for (int i=0; i<data.numInstances(); ++i) {
    		for (int j=0; j<min.length; ++j) {
    			Instance d = data.instance(i);
    			if (d.value(j) < min[j]) {
    				min[j] = d.value(j);
    			}
    			if (d.value(j) > max[j]) {
    				max[j] = d.value(j);
    			}
    		}
    	}
    	
    	for (int i=0; i<min.length; ++i) {
    		fm[i] = max[i] - min[i];
    	}
    	
    	for (int i=0; i<data.numInstances(); ++i) {
    		for (int j=0; j<min.length; ++j) {
    			datas.instance(i).setValue(j, (datas.instance(i).value(j)-min[j])/fm[j]);
    		}
    	}
		return datas;
    }
    
    public static void min_max_std(String wfileName, Instances data) throws Exception {
    	Instances datas = min_max_std(data);
    	
    	DataSink.write(wfileName, datas);
    }
    
    public static void min_max_std(String wfileName, String rfileName) throws Exception {
    	ArffLoader loader = new ArffLoader();
    	loader.setFile(new File(rfileName));
    	Instances data = loader.getDataSet();
    	
    	Instances datas = min_max_std(data);
    	
    	DataSink.write(wfileName, datas);
     }
    
    /**
	 * normalization
	 * @param rfileName
	 * @param wfileName
	 * @throws Exception
	 * @date: 2017.12.5
	 */
	public static void normalize(String rfileName, String wfileName) throws Exception {
		ArffLoader loader = new ArffLoader();
		loader.setFile(new File(rfileName));
		Instances instances = loader.getDataSet();
		instances.setClassIndex(instances.numAttributes()-1);
		
	    System.out.println("normalize...");  
        Normalize norm = new Normalize();  
        norm.setInputFormat(instances);  
          
        Instances newInstances = Filter.useFilter(instances, norm);  
        //instances.toString();    
        System.out.println("save to new file...");  
        //DataSource.write(wfileName, newInstances);
        ArffSaver saver = new ArffSaver();
        saver.setInstances(newInstances);
        saver.setFile(new File(wfileName));
        //saver.setDestination(new File(wfileName));
        saver.writeBatch();
        
        System.out.println("have down.");
	}
	
    // test 
    public static void main(String args[]) throws Exception {
    	String readFileName, writeFileName;
//    	readFileName = "E:\\Program Files\\Weka-3-8\\data\\segment-challenge.arff";
//    	writeFileName = "G:\\dataRepository\\SPASC result\\segment_man2.arff";
    	
//    	readFileName = "G:\\dataRepository\\SPASC dataset\\NEweather.arff";
//    	writeFileName = "G:\\dataRepository\\SPASC dataset\\NEweatherNorm.arff";
    	
//    	readFileName = "G:\\dataRepository\\SPASC dataset\\powersupply.arff";
//    	writeFileName = "G:\\dataRepository\\SPASC dataset\\powersupplyNorm.arff";
    	
//    	readFileName = "G:\\dataRepository\\SPASC dataset\\Outdoor.arff";
//    	writeFileName = "G:\\dataRepository\\SPASC dataset\\OutdoorNorm.arff";
    	
//    	readFileName = "G:\\dataRepository\\SPASC dataset\\sea1.arff";
//    	writeFileName = "G:\\dataRepository\\SPASC dataset\\sea1Norm.arff";
    	
//     	readFileName = "G:\\dataRepository\\SPASC dataset\\sensor.arff";
//    	writeFileName = "G:\\dataRepository\\SPASC dataset\\sensorNorm.arff";
    	
//    	readFileName = "G:\\dataRepository\\SPASC dataset\\spambase.arff";
//    	writeFileName = "G:\\dataRepository\\SPASC dataset\\spambaseNorm.arff";
    	
    	readFileName = "G:\\dataRepository\\SPASC dataset\\sea-vl.arff";
    	writeFileName = "G:\\dataRepository\\SPASC dataset\\seaNorm-vl.arff";
    	
//    	// 1. 标准化
//    	Z_scoure(writeFileName, readFileName);
//    	
//    	//2. 
//    	min_max_std(writeFileName, readFileName);
//    	
//    	//3. 使用weka的标准化Standarder
//    	ArffLoader loader  =new ArffLoader();
//    	loader.setFile(new File(readFileName));
//    	Instances datas = loader.getDataSet();
//    	
//    	
//    	// 4.只能对double[] 进行标准化
//    	double[] probs = new double[8];
//    	Utils.normalize(probs);
//    	System.out.println("finished.");
    	
    	// 5. normalize
    	normalize(readFileName,writeFileName); // 奇怪！相同的代码在testAccuracy中就始终无法将转换后的数据写入文件
        
    }
    
}
