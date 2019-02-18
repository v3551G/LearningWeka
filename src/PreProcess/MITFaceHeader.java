package PreProcess;

/**
 * 
 * @author Administrator
 * @funtion: print MITFace header
 *
 */
public class MITFaceHeader {
	public static void main(String[] args) {
		
		int featureNum = 361;
		System.out.println("@relation 'MITFace dataset'\n");
		for (int i=0; i<featureNum; ++i) {
			System.out.println("@attribute attr" + i + " real");
		}
		System.out.println("@attribute class {0,1}\n");
		System.out.println("@data");
	}
}
