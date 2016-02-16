
public class Measurements {
	public static void main(String[] args) {
		for (int m = 1000; m < 3001; m += 1000) {
			BinomialHeap heap = new BinomialHeap();
			int linkingOperations = 0;
			for (int i = 1; i < m; i++) {
				linkingOperations += heap.insert(i);
			}
			
			String beforeDeletion = binaryRepToStr(heap.binaryRep());
			heap.deleteMin();
			String afterDeletion = binaryRepToStr(heap.binaryRep());
			
			System.out.printf("%d %d %s %s\n", m, linkingOperations, beforeDeletion, afterDeletion);
		}
	}
	
	public static String binaryRepToStr(boolean[] binaryRep) {
		String result = "";
		for (boolean b : binaryRep) 
		{
			result = (b ? "1" : "0") + result;
		}
		
		return result;
	}
}
