
public class Exer {
	public static void main(String[] args) {
		Boolean flag = true;
		System.out.println(flag);
		
		change(flag);
		
		System.out.println(flag);
	}
	
	public static void change(Boolean flag) {
//		flag = false;
		flag = new Boolean(false);
		System.out.println(flag);
	}
}
