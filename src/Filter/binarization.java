package Filter;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Enumeration;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;

//import com.sun.org.apache.bcel.internal.classfile.Attribute;

import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.filters.unsupervised.attribute.MergeTwoValues;

/**
 * @des: used for binarization of dataset that contains discrete attribute  (one hot encoding)
 *       based on the idea that:  Find all discrete attributes and determine whether the number of discrete values of the attribute exceeds 2; 
 *            binary values exceeding 2 are used; otherwise, binary values are not used, which is similar to Binarizer in SKlearn of Python
 * @author qkk *
 */
public class binarization {
	
	/**
	 * takes 2 arguments: - the input ARFF file - the attribute index (starting
	 * with 1)
	 */
	public static void main(String[] args) throws Exception {
		binarization bina = new binarization();
		
		String srcPath = "C:\\Users\\Administrator\\Desktop\\Work\\S\\ArtificalDataSet\\initialDataset";  //run successfully on server with xmx:20480M, and binarization and normalization are executed Separately. 
		String desPath = "C:\\Users\\Administrator\\Desktop\\Work\\S\\ArtificalDataSet\\initialDataset";
		
		String[] s = new String[20];
		String[] p = new String[20];
		
		s[0] = "airlines.arff";
		p[0] = "airlines-bin.arff";
		
		s[1] = "poker-lsn.arff";
		p[1] = "poker-lsn-bin.arff";
		
		s[2] = "elecNormNew.arff";
		p[2] = "elecNormNew-bin.arff";
		
		s[3] = "Agrawal-abr.arff";
		p[3] = "Agrawal-abr-bin.arff";
		
		s[4] = "Agrawal-gra.arff";
		p[4] = "Agrawal-gra-bin.arff";
		
		s[5] = "Agrawal-mix.arff";
		p[5] = "Agrawal-mix-bin.arff";
		
		s[6] = "Sea-abr.arff";
		p[6] = "Sea-abr-bin.arff";
		
		s[7] = "Sea-gra.arff";
		p[7] = "Sea-gra-bin.arff";
		
		s[8] = "Sea-mix.arff";
		p[8] = "Sea-mix-bin.arff";
		
		s[9] = "Sine-abr.arff";
		p[9] = "Sine-abr-bin.arff";
		
		s[10] = "Sine-gra.arff";
		p[10] = "Sine-gra-bin.arff";
		
		s[11] = "Sine-mix.arff";
		p[11] = "Sine-mix-bin.arff";
		
		s[12] = "Hyperplane-dramatic.arff";
		p[12] = "Hyperplane-dramatic-bin.arff";
		
		s[13]=  "Hyperplane-middle.arff";
		p[13] = "Hyperplane-middle-bin.arff";
		
		s[14] = "Hyperplane-subtle.arff";
		p[14] = "Hyperplane-subtle-bin.arff";
		
//		s[15] = "Stagger-abr.arff";
//		p[15] = "Stagger-abr-bin.arff";
//		
//		s[16] = "Stagger-gra.arff";
//		p[16] = "Stagger-gra-bin.arff";
//		
//		s[17] = "Stagger-mix.arff";
//		p[17] = "Stagger-mix-bin.arff";
		
		s[15] = "Waveform-mix.arff";
		p[15] = "Waveform-mix-bin.arff";
		
		s[16] = "Waveform-abr.arff";
		p[16] = "Waveform-abr-bin.arff";
	
		s[17] = "Waveform-gra.arff";
		p[17] = "Waveform-gra-bin.arff";
		
		s[18] = "shoppingDataCopy1.arff";
		p[18] = "shoppingDataCopy1-bin.arff";
		
		s[19] = "Agrawal-frequabr.arff";
		p[19] = "Agrawal-frequabr-bin.arff";
		
		int[] sIndex = new int[] {19};
//		int[] sIndex = new int[] {3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
		for (int i=0; i<sIndex.length; ++i) {
			System.out.println(" Manipulate data: " + s[sIndex[i]]);
			String src = s[sIndex[i]];
			String des = p[sIndex[i]];
			
//			bina.specialAttributeAllValue2Binarize(src);
			
			// 1. dictionary binarization 
//			bina.allAttrBinarizeAccordingFirstAttrValueMy(srcPath + "\\" + src, desPath + "\\" +  des);
		
			// 2. normalization
			String desNorm = des.substring(0, des.indexOf(".")) + "-Norm.arff";
			normalize.normalizeMy(desPath + "\\" + des, desPath + "\\" + desNorm);
			
		}

		System.out.println("finished.");
	}
	
	/**
	 * @des: my own implementation, dictionary binarization
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
		structure.setClassIndex(input.numAttributes()-1);
		input.setClassIndex(input.numAttributes()-1);
		
		System.out.println(input.instance(0).value(0));		
		
		Instances output = new Instances(structure);
		int attrNum = input.numAttributes();
		int numInstance = input.numInstances();
		int totalNumAttr = 0;
		
//		int count=0;
//		for (int i=0; i<attrNum; ++i) {
//			if (structure.attribute(i).isNominal() ) {
//				count += structure.attribute(i).numValues();
//			}else {
//				count ++;
//			}
//		}
//		System.out.println("count: " + count);
		
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
//		System.exit(-1);
		
		for (int in=0; in<numInstance /**/; ++in) {
			double[] inssV = new double[totalNumAttr + 1]; // value of new instance
			for(int i=0;i<attrNum-1;i++){ 
				int numVal = input.attribute(i).numValues();
				if(input.attribute(i).isNominal() && numVal>2){//��������Ϊ��ɢֵ���ͣ�����ɢֵ���������2������
//					//�����Ե�һ����ɢֵ
//					String firstVal = input.attribute(i).value(0);
//					for(int k=output.attribute(i).numValues()-1;k>1;k--){//�ϲ������滻�����Ե�ֵ
//						first = output.attribute(i).value(k-1);
//						second = output.attribute(i).value(k);
//			
//						merge = new MergeTwoValues();
//						merge.setAttributeIndex((i+1)+"");
//						merge.setFirstValueIndex(k+"");
//						merge.setSecondValueIndex(k+1+"");
//
//						merge.setInputFormat(output);
//						output = MergeTwoValues.useFilter(output, merge);
//						output.renameAttributeValue(output.attribute(i), first+"_"+second, "non_"+firstVal);
//					}
					int offset= (int) input.instance(in).value(i);
					inssV[attS[i] + offset] = 1.0;
				}else {
					inssV[attS[i]] = input.instance(in).value(i);
				}
			}
			inssV[totalNumAttr] = input.instance(in).value(input.classIndex());
			Instance inss = new DenseInstance(1.0, inssV);
			System.out.println("inss: " + inss.toString());
			output.add(inss);
		}
		
//		output.setRelationName(input.relationName());

		// save
//		ArffSaver saver = new ArffSaver();
//		saver.setInstances(output);
//		saver.setDestination(new File(desName));
//		saver.writeBatch();
		PrintStream oP =new PrintStream(new File(desName));
		oP.println(output.toString());
	}
	
	private void print(int[] attS) {
		// TODO Auto-generated method stub
		String str = "";
		for (int i=0; i<attS.length; ++i) {
			str += attS[i] + ",";
		}
		System.out.println(str);
	}

	/*
	 * @��������ɢ�����Խ��ж�ֵ������ֵ������������ɢֵ��ĵ�һ��ֵ, (�ô���ʽû�е���)
	 * 
	 * ����˼�룺
	 *      �ҳ�������ɢ�����ԣ��жϸ�������ɢֵ�������û�г���2������2�Ľ��ж�ֵ���������ö�ֵ����
	 *      ���н��ж�ֵ�������ԣ��������ݸ�������ɢֵ���еĵ�һ��Ϊ��׼�������Ķ���non+��һ��
	 *      �����㷨������ͬ��
	 * 
	 * bug: ��ֵ������arff�ļ����һ�����������ֵ������������ᱨ����Ϊ���һ������ֵ�����Բ����޸ġ�
	 * 
	 */
	public void allAttrBinarizeAccordingFirstAttrValue(String srcName, String desName) throws Exception{
		String curVal;
		String first;
		String second;
		MergeTwoValues merge;
		
		ArffLoader loader = new ArffLoader();
		loader.setFile(new File(srcName));
		Instances input = loader.getDataSet();
		input.setClassIndex(input.numAttributes()-1);
		
		Instances output = new Instances(input);
		
		int attrNum = output.numAttributes();
		
		for(int i=0;i<attrNum;i++){
			int numVal = input.attribute(i).numValues();
			if(input.attribute(i).isNominal() && numVal>2){//��������Ϊ��ɢֵ���ͣ�����ɢֵ���������2������
				//�����Ե�һ����ɢֵ
				String firstVal = input.attribute(i).value(0);
				for(int k=output.attribute(i).numValues()-1;k>1;k--){//�ϲ������滻�����Ե�ֵ
					first = output.attribute(i).value(k-1);
					second = output.attribute(i).value(k);
		
					merge = new MergeTwoValues();
					merge.setAttributeIndex((i+1)+"");
					merge.setFirstValueIndex(k+"");
					merge.setSecondValueIndex(k+1+"");

					merge.setInputFormat(output);
					output = MergeTwoValues.useFilter(output, merge);
					output.renameAttributeValue(output.attribute(i), first+"_"+second, "non_"+firstVal);
				}
			}
		}

		output.setRelationName(input.relationName());

		// save
//		ArffSaver saver = new ArffSaver();
//		saver.setInstances(output);
//		saver.setDestination(new File(desName));
//		saver.writeBatch();
		PrintStream oP =new PrintStream(new File(desName));
		oP.println(output.toString());
	}
	
	/*
	 * @��ĳ���ض�������������ɢֵ����ж�ֵ��
	 * 
	 * ����˼�룺
	 *     ȷ��Ҫ��ֵ�����ض���ĳ�����ԣ�Ȼ����ݸ���������ɢֵ��ĸ������ֱ���ж�ֵ������
	 *     ��������ĵĲ��־��� ���޸�����ֵ���ֵ�� �� ���ϲ�������������ǵ�ʵ�ַ���
	 *     �ֱ��ǣ� renameAttributeValue �� MergeTwoValues���е���ط�����
	 *     MergeTwoValues���е���Ҫ������ 
	 *               setAttributeIndex��
	 *               setFirstValueIndex��
	 *               setSecondValueIndex��
	 *               setInputFormat
	 *     �ϲ�����Ҫ��Instances���¸�ֵ������MergeTwoValues.useFilter(XXX,YYY);          
	 *
	 */
	public void specialAttributeAllValue2Binarize(String srcName) throws Exception{
		Instances output;
		ArffSaver saver;
		int i;
		Enumeration enm;
		String currValue;
		String value;
		int attIndex;
		String filename;
		int renamed;
		MergeTwoValues merge;
		int index;

		// load input
		ArffLoader loader = new ArffLoader();
		loader.setFile(new File(srcName));
		Instances input = loader.getDataSet();
		input.setClassIndex(input.numAttributes() - 1);

		// generate output_files
		attIndex = 0;

		for (i = 0; i < input.attribute(attIndex).numValues(); i++) {//ѭ�������±�ΪattIndex���Ե�����ֵ���󣬸���ÿ������ֵ���ж�ֵ������

			output = new Instances(input);//�õ�һ����������ֵ��ʱ���޸ĸ���
			currValue = input.attribute(attIndex).value(i);//����������ɢֵ�����һһ��ȡ��

			// rename values
			enm = input.attribute(attIndex).enumerateValues();
			renamed = -1;
			
			while (enm.hasMoreElements()) {
				value = enm.nextElement().toString();
				System.out.println("enm  value:"+value);
				if (!value.equals(currValue)) {
					index = output.attribute(attIndex).indexOfValue(value);//�õ�������ֵ�����Բ����б��е��±�
					// rename the first not-value, others are merged with this
					// one then
					if (renamed == -1) {//��һ���޸�����ֵ
						renamed = index;//�޸ı�־λ
						output.renameAttributeValue(output.attribute(attIndex),
								value, "not_" + currValue);//�޸����Բ����б��Ӧ�±괦��ֵ��
					} else {//�ϲ��Ժ��curValue�����Բ����б��е�ֵ��
						merge = new MergeTwoValues();
						merge.setAttributeIndex(1+"");
						merge.setFirstValueIndex("" + (renamed + 1));
						merge.setSecondValueIndex("" + (index + 1));
						merge.setInputFormat(output);
						output = MergeTwoValues.useFilter(output, merge);
						
						//���Դ�ӡ�ºϲ�������ֵ��ֵ��
//						System.out.println("merged value:"+output.attribute(attIndex).value(renamed));
						
						//�ٴκϲ�ʱΪʲôʹ��"not_" + currValue + "_" + value��������"not_" + currValue+value��
						//��ο������ӡ�����ݣ���ΪĬ�Ϻϲ�ʱ���м����»�������
						output.renameAttributeValue(output.attribute(attIndex)
									,"not_" + currValue + "_" + value, "not_" + currValue);
					}
				}
			}

			// save file
			System.out.println("curValue:"+currValue);
			output.setRelationName(input.relationName() + "-" + currValue + "-and-not_" + currValue);
			
			//�ַ����滻��������ʽ��Ӧ��$��ʾ����λ��
			filename = srcName.replaceAll(".[Aa][Rr][Ff][Ff]$", "-"+ currValue + ".arff");
			saver = new ArffSaver();
			saver.setInstances(output);
			
			//�����ļ��ķ�ʽ�����֣�setFile��setDestination�������棬setDestination�����Ա�����
			saver.setFile(new File(filename));
//			saver.setDestination(new File(fileName));
			
			saver.writeBatch();
		}
		System.out.println("saying done....");
   }

	public ArrayList<String> getBinaryValue() {
		ArrayList<String> vArrayList = new  ArrayList<String>();
		vArrayList.add(0 + "");
		vArrayList.add(1 +  "");

		return vArrayList;
	}

}
