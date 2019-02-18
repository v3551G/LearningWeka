package PreProcess;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;
import weka.filters.unsupervised.attribute.MergeTwoValues;

/**
 * 
 * @author Administrator
 * @debug: GC over limit!!!!!!!
 *
 */
public class CTRPreprocess {
	Instances datas;
	
	public CTRPreprocess (Instances datas) {
		this.datas = datas;
	}
	
    public static void main(String[] args) throws Exception {
	   //
	   String basePath = "C:\\Users\\Administrator\\Desktop\\dataset20180409\\CTRPrediction\\train";
	   String srcFileName = basePath +  "\\train.csv";
	   
	   // load
	   CSVLoader loader = new CSVLoader();
	   loader.setFile(new File(srcFileName));
	   Instances datas = loader.getDataSet();
	   Instances structure = loader.getStructure();
	   structure.setClassIndex(1);
	   datas.setClassIndex(1);
	   
	   Instances res = new Instances(structure); // used for save
	   
	   System.out.println("structure: " + structure.toString());
	   System.exit(-1);
	   
	   int count = 0;
	   for (int i=0; i<structure.numAttributes(); ++i) {
		   if (structure.attribute(i).isNominal()) {
			   count += structure.attribute(i).numValues();
		   }else {
		       count ++;
		   }
	   }
	   System.out.println("count: " + count);
	   CTRPreprocess ct = new CTRPreprocess(datas);
	   // downsample
	   int folds = 10;
	   int batchSize = datas.numInstances() / folds;
	   int seed = 1234;
	   Random random = new Random(seed);
	   
	   for (int i=0; i<folds; ++i) {
		   Instance[] batch = ct.getBatch(i*batchSize, (i+1)*batchSize-1);
		   ArrayList<Integer> pos = new ArrayList<Integer>();
		   ArrayList<Integer> neg = new ArrayList<Integer>();
		   for (int it=0; it<batch.length; ++it) {
			   if (batch[it].classValue() == 0) {
				   neg.add(it);
			   }else {
				   pos.add(it);
			   }
		   }
		   System.out.println("pos.size/neg.size: " + pos.size() / (double)neg.size());
		   
		   int extract = Math.min(pos.size(), 25000);
		   ArrayList<Integer> selPos = getSel(pos.size(), extract, random);
		   ArrayList<Integer> selNeg = getSel(neg.size(), extract, random);
		   //
		   for (int it=0; it<selPos.size(); ++it) {
			   res.add(batch[selPos.get(it)]);
		   }
		   for (int it=0; it<selNeg.size(); ++it) {
			   res.add(batch[selNeg.get(it)]);
		   }
		  
	   }
	   // save 
	   CSVSaver sav = new CSVSaver();
	   sav.setDestination(new File(basePath + "\\traindownsample.csv"));
	   // one-hot encoding 
	   ct.allAttrBinarizeAccordingFirstAttrValueMy(basePath + "\\traindownsample.csv", basePath + "\\train-ds-onehot.csv");
	 
	   // 处理完后，再手动调整类别属性的位置
    }

    public static ArrayList<Integer> getSel(int size, int extract, Random random) {
		// TODO Auto-generated method stub
    	ArrayList<Integer> set = new ArrayList<Integer>();
    	ArrayList<Integer> sel = new ArrayList<Integer>();
    	for (int i=0; i<size; ++i) {
    		set.add(i);
    	}
    	
    	for (int i=0; i<extract; ++i) {
    		int next = set.remove(random.nextInt(set.size()));
    		sel.add(next);
    	}
		return sel;
	}

	public  Instance[] getBatch(int start, int end) {
	   // TODO Auto-generated method stub
    	int num = end - start + 1;
    	Instance[] ins = new Instance[num];
        for (int i=0; i<num; ++i) {
        	ins[i] = datas.instance(i + start);
        }
	    
        return ins;
    }
	
	/**
	 * @Note: when maipulate multi-class dataset, it needs modification.
	 * @param srcName
	 * @param desName
	 * @throws Exception
	 */
	public void allAttrBinarizeAccordingFirstAttrValueMy(String srcName, String desName) throws Exception{
		String curVal;
		String first;
		String second;
		MergeTwoValues merge;
		
		ArffLoader loader = new ArffLoader();
		loader.setFile(new File(srcName));
		Instances input = loader.getDataSet();
		Instances structure = loader.getStructure();
		structure.setClassIndex(1);
		input.setClassIndex(1);
		
		System.out.println(input.instance(0).value(0));		
		
		int classIndex = 1; // for CTRPrediction dataset
		Instances output = new Instances(structure);
		int attrNum = input.numAttributes();
		int numInstance = input.numInstances();
		int totalNumAttr = 0;
				
		int[] attS  =new int[attrNum-1];  // updated attr start index
		for (int i=0; i<attS.length; ++i) {
			if (i==0) {
				attS[i] = 0;
			}else {
				attS[i] = totalNumAttr;
			}
			
			int numVal = structure.attribute(i).numValues();
			if (structure.attribute(i).isNominal() && numVal>2) {
				output.deleteAttributeAt(totalNumAttr);
				for (int ii=0; ii<numVal; ++ii) {
					ArrayList<String> val = getBinaryValue();
					Attribute att = new Attribute("attribute" + (totalNumAttr + ii), val);
					output.insertAttributeAt(att, attS[i] + ii);
				}
				totalNumAttr += numVal;
				
			}else {
				totalNumAttr ++;
			}
		}
		System.out.println("geader:" + output.toString());
		System.out.println("updated attr: " + output.numAttributes());
		System.out.println("totalNumAttr: " + totalNumAttr);
		print(attS);
		
		for (int in=0; in<numInstance /**/; ++in) {
			double[] inssV = new double[totalNumAttr + 1]; // value of new instance
			for(int i=0;i<attrNum;i++){
				int numVal = input.attribute(i).numValues();
				if(input.attribute(i).isNominal() && numVal>2){//处理属性为离散值类型，且离散值项个数大于2的属性
					int offset= (int) input.instance(in).value(i);
					inssV[attS[i] + offset] = 1.0;
				}else {
					inssV[attS[i]] = input.instance(in).value(i);
				}
			}
			Instance inss = new DenseInstance(1.0, inssV);
			System.out.println("inss: " + inss.toString());
			output.add(inss);
		}
		
		PrintStream oP =new PrintStream(new File(desName));
		oP.println(output.toString());
	}
	
	public ArrayList<String> getBinaryValue() {
		ArrayList<String> vArrayList = new  ArrayList<String>();
		vArrayList.add(0 + "");
		vArrayList.add(1 +  "");

		return vArrayList;
	}
	
	private void print(int[] attS) {
		// TODO Auto-generated method stub
		String str = "";
		for (int i=0; i<attS.length; ++i) {
			str += attS[i] + ",";
		}
		System.out.println(str);
	}
}
