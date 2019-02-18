package DataIO;

import java.io.File;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.converters.DatabaseLoader;
import weka.experiment.InstanceQuery;

/*
 *  input definition
 */
public class loadData {
     
    /*
     *  这是几种加载不同类型数据文件的不同实现方法，
     *  另外，数据本身加载也有批量方式 和增量方式 两种,待续
     */
	// 方式1
	public static Instances loadArffFile1(String fullFName) {
		Instances data = null;
		try {
			data = DataSource.read(fullFName);
			
			System.out.println("the data has " + data.numInstances() + "instances");
			//System.out.println(data);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("!read file error");
		}
		
		return data;
	}
	// 方式2
	public static Instances loadArffFile2(String fullFName) {
		Instances data = null;
		
		ArffLoader loader = new ArffLoader();
		try {
			loader.setFile(new File(fullFName));
			data = loader.getDataSet();
			
			System.out.println("the dataSet has " + data.numInstances() + "instances");
			System.out.println(data);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}
	
	// 该方式为1的简写形式
	public static Instances loadArffFile1_2(String fullFName) throws Exception {
		Instances data = new Instances(DataSource.read(fullFName));
		
		System.out.println("the dataSet has " + data.numInstances() + "instanes");
		//System.out.println(data);
		
		return data;
	}
	
	public static Instances loadXrffData1(String fullFName) {
		 Instances data = null;
		 try {
			data = DataSource.read(fullFName);
			
			System.out.println("the dataSet has " + data.numInstances() + "instances");
			System.out.println(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return data;
	}
	
	public static Instances loadXrffData2(String fullFName) {
		Instances data = null;
		
		ArffLoader loader = new ArffLoader();
		try {
			loader.setFile(new File(fullFName));
			data = loader.getDataSet();
			
			System.out.println("the dataSet has " + data.numInstances() + " instances");
			System.out.println(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}
	
	public static Instances loadDBData1(DBQueryObject obj) {
		InstanceQuery query = null;
		Instances data = null;
		
		try {
			query = new InstanceQuery();
			query.setUsername(obj.userName);
			query.setPassword(obj.passWord);
			query.setQuery(obj.Sql);
			query.setDatabaseURL(obj.url);
			
			data = query.retrieveInstances();
			
			if (data.classIndex() == -1) {
				data.setClassIndex(data.numAttributes()-1);
			}
			
			System.out.println("the dataSet has " + data.numInstances() + "instances");
			System.out.println(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
		
	}
	
	public static Instances loadDBData2(DBQueryObject obj) {
		 DatabaseLoader loader = null;
		 Instances data = null;
		 
		 try {
			loader = new DatabaseLoader();
			loader.setUser(obj.userName);
			loader.setPassword(obj.passWord);
			loader.setQuery(obj.Sql);
			loader.setUrl(obj.url);
			
			data = loader.getDataSet();
			
			System.out.println("the data has " + data.numInstances() + "instances");
			System.out.println(data);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}
	
	// for convenience, it can not be declared as public
	public class DBQueryObject {
		String userName;
		String passWord;
		String Sql;
		String url;
	}
	
	private DBQueryObject setDBQueryObject(String usrName,String password, String sql, String url) {
		DBQueryObject dbObject = new DBQueryObject();
		dbObject.userName = usrName;
		dbObject.passWord = password;
		dbObject.Sql = sql;
		dbObject.url = url;
		
		return dbObject;
		
	}
	
}
