package DataIO;

import java.io.File;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;
import weka.core.converters.ConverterUtils.DataSink;
import weka.core.converters.DatabaseSaver;

/*
 *  save data definition
 */
public class saveData {
     /*
      *  该类实现了将数据保存为不同文件类型不同数据格式的功能
      */
	
	// wfile 的后缀应该为.csv
	public static void saveArff2CSV1(String rfile, String wfile) throws Exception {
		Instances data = loadData.loadArffFile1_2(rfile);
		
		DataSink.write(wfile, data);
		
		System.out.println("the dataSet haas been successfully writed to " + wfile);
	}
	
	public static void saveArff2CSV2(String rfile, String wfile) throws Exception {
		Instances data = loadData.loadArffFile1_2(rfile);
		
		CSVSaver saver = new CSVSaver();
		saver.setInstances(data);       // set the writting data
		saver.setFile(new File(wfile)); // set the destination file
		
		saver.writeBatch();
		
		System.out.println("the dataSet has been successfully writted to " + wfile);
	}
	
	/*
	 *  csv -> arff
	 */
	public static void saveCSV2Arff(String rfile, String wfile) throws Exception {
		
		CSVLoader loader = new CSVLoader();
		loader.setFile(new File(rfile));
		Instances data = loader.getDataSet();
		data.setClassIndex(data.numAttributes()-1);
		
		ArffSaver saver = new ArffSaver();
		saver.setInstances(data);
		saver.setFile(new File(wfile));
		saver.writeBatch();
		
		System.out.println("the dataSet has been successfully writted to " + wfile);
	}
    
	// batch style
	public static void saveArff2DBBatch(String rfile, DBSaveObject obj ) throws Exception {
        Instances data = loadData.loadArffFile1_2(rfile);
		
		DatabaseSaver saver = new DatabaseSaver();
		
		saver.setDestination(obj.url, "weka", "weka");
		saver.setTableName(obj.tableName);
		saver.setRelationForTableName(false);
		saver.setInstances(data);
		
		saver.writeBatch();
		
		System.out.println(" the dataSet has been successfully writted into sql");
	}
	
	// incremental style
	public static void saveArff2DBIncremental(String rfile, DBSaveObject obj) throws Exception {
		Instances data = loadData.loadArffFile1_2(rfile);
		
		DatabaseSaver saver = new DatabaseSaver();
		
		saver.setDestination(obj.url);
		saver.setTableName(obj.tableName);
		saver.setRelationForTableName(false);
		saver.setRetrieval(DatabaseSaver.INCREMENTAL);
		saver.setStructure(data);
		
		for (int i=0; i<data.numInstances(); ++i) {
			saver.writeIncremental(data.instance(i));
		}
		saver.writeIncremental(null); // indicate the write procedure has finished
		
		System.out.println("the data has been successfully writeed into sql");
	}
	
	// for convenient
	public class DBSaveObject {
		String userName;
		String password;
		String url;
		String sql;
		String tableName;
	}
	
	public DBSaveObject setDBObject(String userName, String password, String sql, String url, String tableName) {
		DBSaveObject object = new DBSaveObject();
		object.userName = userName;
		object.password = password;
		object.sql = sql;
		object.url = url;
		object.tableName = tableName;
		
		return object;
	}
	
	public static void main(String[] args) throws Exception {
		String rdataPath = "G:\\dataRepository\\FirstWork\\SPASC\\batch0-res.arff";
		String wdataPath = "G:\\dataRepository\\FirstWork\\SPASC\\batch0-res.csv";
		
		//saveCSV2Arff(rdataPath, wdataPath);
		saveArff2CSV2(rdataPath, wdataPath);
	}
}
