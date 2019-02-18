package Filter;

import java.io.File;
import java.io.PrintStream;

//import weka.classifiers.evolutionary.common.DataSet;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;
//import weka.gui.beans.DataSink;

//import javax.tools.Tool;

public class normalize {
	public static void main(String[] args) throws Exception {
//		String rfile = "G:/dataRepository/SPASC dataset/interchangingRBF.arff";
//		String wfile = "G:/dataRepository/interchangingRBFNorm.arff";
		
//		String rfile = "G:/dataRepository/SPASC dataset/letter.arff";
//		String wfile = "G:/dataRepository/letterNorm.arff";
		
//		String rfile = "G:/dataRepository/SPASC dataset/rialto.arff";
//		String wfile = "G:/dataRepository/rialtoNorm.arff";
		
		String rfile = "G:\\dataRepository\\SPASC dataset\\airlines-bin.arff";
		String wfile = "G:\\dataRepository\\SPASC dataset\\airlines-bin-Norm.arff";   // run successfully on server with xmx:20480M,
		
		normalizeMy(rfile, wfile);
	}
	
	public static void normalizeMy(String rfileName, String wfileName) throws Exception {
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
//        DataSource.write(wfileName, newInstances);
//        ArffSaver saver;
//		try {
//			saver = new ArffSaver();
//			saver.setInstances(newInstances);
//			saver.setFile(new File(wfileName));
//			//saver.setDestination(new File(wfileName));
//			saver.writeBatch();
////        DataSink.write(wfileName, newInstances);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
        PrintStream oPrintStream = new PrintStream(new File(wfileName));
        oPrintStream.print(newInstances.toString());
        System.out.println("have down.");
//        System.out.println("" + saver.toString());
	}
}
