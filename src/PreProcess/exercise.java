package PreProcess;

import java.util.ArrayList;
import java.util.Random;

public class exercise {
	public static void main(String[] args) {
		ArrayList<Integer> ve = new ArrayList<Integer>();
		ve.add(21); ve.add(34); ve.add(35); ve.add(65); ve.add(213); ve.add(54); ve.add(33);
		
		int seed = 123;
		Random random = new Random(seed);
		
		for (int i=0; i<7; ++i) {
			int de = ve.remove(random.nextInt(ve.size()));
			System.out.println(de);
		}
	}
}
