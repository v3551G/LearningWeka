import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;

/**
 * @all example for calling K-Means implememnted by Weka
 * @author Administrator
 * @date: 2017.12.11
 *
 */
public class CallAlgorithm {
	 public Instances structure;
	 
	 public Map<Integer, Vector<Integer>>  kmeansCluster(Instance[] batch, double numClu, boolean[] hasLabels) throws Exception {	
    	 Instances ins = new Instances(structure);
    	 int insNum = batch.length;
    	 for (int i=0; i<batch.length; ++i) {
    		 ins.add((Instance)batch[i].copy());
    	 }
    	 
    	 SimpleKMeans KM = null;  
         //  
         KM = new SimpleKMeans();  
         KM.setNumClusters((int)numClu);
         System.out.println("ins.numInstances(): " + ins.numInstances());
         System.out.println("ins.numInstances(): ");
         System.out.println("batch[0].toString(): " + batch[0].toString());
         System.out.println("ins.instance(0): " + ins.instance(0).toString());
         
         KM.buildClusterer(ins);  
         //System.out.println(KM.preserveInstancesOrderTipText());  
         //System.out.println(KM.toString());  
      
         Instances okm = KM.getClusterCentroids();
         int cluNum = KM.numberOfClusters();
         Instance[] res = new Instance[cluNum]; // cluster center
         for (int i=0; i<okm.numInstances(); ++i) {
         	res[i] = okm.instance(i);
         }
         
         double[] clusterInfo = new double[insNum];
         for (int i=0; i<insNum; ++i) {
        	 int minIndex = 0;
             double minDist = getDistance(batch[i], res[0]);
        	 for (int j=1; j<cluNum; ++j) {
        		 double tmp;
        		 if ( (tmp= getDistance(batch[i], res[j])) < minDist) {
        			 minIndex = j;
        			 minDist = tmp;
        		 }
        	 }
        	 clusterInfo[i] = minIndex; 
         }
         
		 Map<Integer, Vector<Integer>> ret = new HashMap<Integer, Vector<Integer>>();
		 for (int i=0; i<cluNum; ++i) {
			 Vector<Integer> elmeV = new Vector<Integer>();
			 for(int j=0; j<clusterInfo.length; ++j) {
				 if (clusterInfo[j] == (i+1)) {
					 elmeV.add(j);
				 }
			 }
		     ret.put(i, elmeV);
		 }
		
		 return ret;
    }
	
	/**
	 * @function: euclidean metric
	 * @param instance
	 * @param instance2
	 * @return
	 */
	public double getDistance(Instance instance, Instance instance2) {
		// TODO Auto-generated method stub
		int ret = 0;
		for (int i=0; i<instance.numAttributes()-1; ++i) {
			ret += Math.pow(instance.value(i)-instance2.value(i), 2);
		}
		return Math.sqrt(ret);
	} 
}
