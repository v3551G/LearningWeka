package IKAnalyizer_exper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

import weka.core.Utils;

public class test_IKAnalyizer {
	/**
	   * Main method.
	   */
	  public static void main(String[] options) {

	    try {

	      // Read message file into string.
	      String messageName = Utils.getOption('m', options);
	      if (messageName.length() == 0) {
	        throw new Exception("Must provide name of message file.");
	      }
	      FileReader m = new FileReader(messageName);
	      StringBuffer message = new StringBuffer();
	      int l;
	      while ((l = m.read()) != -1) {
		     message.append((char)l);
	      }
	      m.close();
	      
	      // check whether it's chinese
	      boolean isEnglish = Utils.getFlag("E", options);
	      if (!isEnglish) {
	    	  Analyzer ikAnalyzer = new IKAnalyzer();
	    	  Reader reader = new StringReader(messageName.toString());
	    	  TokenStream stream = ikAnalyzer.tokenStream("", reader);
	    	  CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);
	    	  message = new StringBuffer();
	    	  
	    	  while(stream.incrementToken()) {
	    		  message.append( termAtt.toString() + "");
	    	  }
	      }
	    
	      // Check if class value is given.
	      String classValue = Utils.getOption('c', options);
	      
	      // If model file exists, read it, otherwise create new one.
	      String modelName = Utils.getOption('t', options);
	      if (modelName.length() == 0) {
		      throw new Exception("Must provide name of model file.");
	      }
	      MessageClassifier messageCl;
	      try {
		      ObjectInputStream modelInObjectFile = 
		      new ObjectInputStream(new FileInputStream(modelName));
		      messageCl = (MessageClassifier) modelInObjectFile.readObject();
		      modelInObjectFile.close();
	      } catch (FileNotFoundException e) {
		      messageCl = new MessageClassifier();
	      }
	      
	      // Check if there are any options left
	      //Utils.checkForRemainingOptions(options);
	      
	      // Process message.
	      if (classValue.length() != 0) {
	          messageCl.updateData(message.toString(), classValue);
	      } else {
	          messageCl.classifyMessage(message.toString());
	      }
	      
	      // Save message classifier object.
	      ObjectOutputStream modelOutObjectFile = 
		             new ObjectOutputStream(new FileOutputStream(modelName));
	      modelOutObjectFile.writeObject(messageCl);
	      modelOutObjectFile.close();
	    } catch (Exception e) {
	         e.printStackTrace();
	    }
	  }
}
