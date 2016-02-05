import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class Tester {
	@Test
	public static void main(String[] args)
	{
		BinomialHeap meldingHeap = new BinomialHeap();
		assertHeapSanity(meldingHeap, 0, Integer.MAX_VALUE);
		List<Integer> meldedItems = new ArrayList<Integer>();
		
		for (Object testAsObject : Tester.allTests) {
			@SuppressWarnings("unchecked")
			List<Integer> test = (List<Integer>)testAsObject;
			runFullSuiteWithItems(test);
			
			BinomialHeap newHeap = createTreeWithArray(test);
			meldedItems.addAll(test);
			meldingHeap.meld(newHeap);
			assertHeapValidity(newHeap, test); // Make sure the newHeap didn't change.
			assertHeapSanity(meldingHeap, meldedItems.size(), Collections.min(meldedItems));
		}
		
		assertHeapValidity(meldingHeap, meldedItems);
	}
	
	private static void runFullSuiteWithItems(List<Integer> items) {
		assertHeapValidity(createTreeWithInserts(items), items);
		assertHeapValidity(createTreeWithArray(items), items);
	}
	
	private static BinomialHeap createTreeWithInserts(List<Integer> items) {
		BinomialHeap result = new BinomialHeap();
		int min = Integer.MAX_VALUE;
		
		assertHeapSanity(result, 0, min);
		for (int i = 0; i < items.size(); i++) {
			int itemToAdd = items.indexOf(i);
			min = Math.min(min, itemToAdd);
			result.insert(itemToAdd);
			assertHeapSanity(result, i+1, min);
		}
		
		return result;
	}
	
	private static BinomialHeap createTreeWithArray(List<Integer> items) {
		BinomialHeap result = new BinomialHeap();
		assertHeapSanity(result, 0, Integer.MAX_VALUE);
		
		int[] itemsToAdd = new int[items.size()];
		for (int i = 0; i < itemsToAdd.length; i++) {
			itemsToAdd[i] = items.get(0);
		}
		
		result.arrayToHeap(itemsToAdd);
		assertHeapSanity(result, itemsToAdd.length, Collections.min(items));
		
		return result;
	}
	
	// Test the validity of the tree by deleting all the items, one by one.
	private static void assertHeapValidity(BinomialHeap bheap, List<Integer> items) {
		List<Integer> sortedItems = new ArrayList<Integer>(items);
		Collections.sort(sortedItems);
		
		assertHeapSanity(bheap, sortedItems.size(), sortedItems.get(0));
		for (int i = sortedItems.size() - 1; i >= 0; i--) {
			bheap.deleteMin();
			sortedItems.remove(0);
			assertHeapSanity(bheap, i, i == 0 ? Integer.MAX_VALUE : sortedItems.get(0));
		}
	}
	
	// Test the sanity of the tree without deleting any items.
	private static void assertHeapSanity(BinomialHeap bheap, int numOfItems, int expectedMin)
	{
		assert bheap.empty() == (numOfItems == 0);
		assert bheap.size() == numOfItems;
		assert bheap.isValid();
		
		if (numOfItems > 0) {
			assert bheap.findMin() == expectedMin;
			
			boolean[] binaryRep = bheap.binaryRep();
			int minRank = bheap.minTreeRank();
			assert binaryRep.length == (int)(Math.log(numOfItems) / Math.log(2)) + 1;
			int computedSize = 0;
			for (int i = 0; i < binaryRep.length; i++) {
				if (i < minRank) {
					assert binaryRep[i] == false;
				}
				else if (i == minRank) {
					assert binaryRep[i] == true;
				}
				
				if (binaryRep[i]) {
					computedSize += Math.pow(2, i);
				}
			}
			
			assert computedSize == numOfItems;
		}
	}
	
	private static List<Integer> listFromArray(int[] array) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		for (int i = 0; i < array.length; i++) {
			result.add(array[i]);
		}
		
		return result;
	}
	
	
	
	private static List<Integer> orderedTest = listFromArray(new int[]
			{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
	private static List<Integer> reversedTest = listFromArray(new int[]
			{9, 8, 7, 6, 5, 4, 3, 2, 1, 0});
	private static List<Integer> shuffledTest = listFromArray(new int[]
			{5, 3, 2, 0, 4, 7, 1, 9, 8, 6});
	private static List<Integer> shuffledWithReps = listFromArray(new int[]
			{4, 8, 5, 4, 0, 9, 9, 7, 8, 0, 7, 2, 2, 6, 1, 1, 6, 3, 3, 5});
	private static List<Integer> bigTestWithReps = listFromArray(new int[]
			{387, 129, 486, 361, 337, 427, 51, 3, 197, 23, 162, 367, 187, 335, 290, 451,
			70, 309, 42, 212, 297, 192, 366, 189, 418, 142, 385, 118, 263, 83, 491, 240,
			255, 190, 109, 357, 36, 415, 167, 400, 7, 433, 477, 289, 43, 181, 414, 69,
			10, 227, 247, 230, 210, 475, 212, 419, 451, 486, 128, 457, 339, 443, 100, 370,
			37, 444, 194, 391, 496, 425, 127, 65, 339, 363, 377, 238, 19, 136, 221, 270,
			318, 217, 39, 324, 203, 385, 349, 314, 275, 16, 477, 11, 413, 393, 184, 41,
			168, 317, 15, 371, 470, 43, 85, 299, 274, 163, 75, 458, 258, 499, 100, 82,
			467, 398, 135, 171, 200, 421, 468, 76, 25, 260, 319, 116, 195, 427, 54, 428,
			467, 415, 413, 354, 451, 294, 410, 151, 443, 418, 294, 384, 144, 264, 278, 347,
			458, 26, 294, 330, 395, 180, 115, 302, 490, 270, 318, 233, 244, 143, 486, 483,
			46, 172, 131, 79, 235, 74, 169, 68, 471, 292, 159, 133, 470, 225, 229, 465,
			70, 480, 464, 266, 335, 154, 315, 114, 97, 404, 426, 380, 244, 126, 382, 293,
			208, 262, 7, 92, 191, 48, 93, 180, 478, 345, 236, 73, 120, 300, 447, 272,
			355, 480, 2, 135, 252, 111, 46, 449, 57, 197, 364, 9, 135, 293, 256, 205,
			117, 389, 0, 236, 71, 1, 332, 98, 429, 172, 178, 71, 352, 281, 136, 21,
			493, 287, 218, 455, 207, 24, 346, 32, 40, 445, 484, 295, 255, 403, 331, 409,
			230, 466, 88, 234, 8, 310, 76, 31, 84, 96, 259, 251, 91, 152, 120, 332,
			102, 437, 229, 345, 469, 291, 250, 341, 436, 340, 350, 72, 409, 271, 379, 296,
			50, 461, 13, 239, 4, 372, 355, 195, 405, 146, 277, 116, 275, 70, 260, 454,
			228, 237, 401, 368, 55, 463, 245, 389, 462, 190, 426, 382, 264, 425, 291, 321,
			488, 38, 44, 40, 468, 412, 433, 43, 353, 408, 212, 485, 453, 354, 381, 286,
			16, 28, 26, 120, 244, 178, 65, 51, 272, 141, 146, 237, 275, 344, 429, 448,
			3, 442, 4, 493, 288, 13, 490, 33, 246, 51, 2, 243, 187, 198, 202, 441,
			369, 464, 338, 298, 230, 362, 164, 305, 173, 255, 193, 269, 277, 134, 136, 412,
			63, 495, 428, 479, 350, 214, 456, 439, 364, 2, 28, 166, 452, 388, 211, 284,
			148, 392, 113, 452, 337, 224, 118, 149, 400, 416, 227, 321, 67, 180, 436, 132,
			179, 334, 328, 143, 193, 365, 378, 239, 386, 5, 132, 352, 486, 362, 237, 450,
			252, 406, 226, 170, 80, 433, 115, 111, 495, 480, 477, 339, 396, 448, 320, 217,
			467, 107, 163, 102, 112, 20, 469, 53, 153, 112, 244, 172, 346, 35, 258, 327,
			440, 382, 94, 294, 284, 67, 52, 122, 292, 434, 186, 443, 448, 244, 94, 179,
			343, 58, 254, 161, 223, 136, 233, 85, 343, 251, 264, 293, 427, 283, 382, 306,
			241, 279, 273, 482, 153, 147, 464, 1, 21, 12, 206, 59, 174, 250, 103, 448,
			137, 412, 494, 297, 364, 45, 238, 200, 154, 472, 435, 347, 463, 128, 271, 215,
			141, 78, 408, 59, 457, 381, 395, 473, 97, 9, 20, 34, 1, 274, 343, 443,
			99, 322, 218, 292, 386, 216, 414, 156, 145, 145, 303, 51, 323, 477, 395, 119,
			107, 407, 430, 426, 417, 481, 326, 239, 422, 109, 472, 151, 69, 332, 64, 443,
			429, 396, 248, 423, 238, 386, 171, 275, 125, 388, 472, 296, 313, 434, 376, 186,
			424, 49, 230, 111, 253, 63, 358, 373, 115, 367, 129, 494, 53, 195, 104, 305,
			165, 307, 432, 295, 11, 133, 338, 218, 339, 378, 200, 394, 329, 304, 6, 279,
			138, 406, 375, 388, 80, 261, 229, 416, 54, 73, 414, 160, 258, 132, 86, 435,
			138, 305, 269, 307, 378, 462, 45, 261, 456, 325, 152, 205, 323, 280, 403, 376,
			103, 207, 64, 27, 308, 296, 457, 211, 87, 427, 184, 149, 478, 204, 421, 329,
			498, 183, 419, 351, 450, 350, 39, 175, 29, 380, 64, 218, 388, 53, 110, 12,
			382, 420, 38, 170, 259, 106, 386, 48, 286, 344, 301, 56, 391, 319, 68, 148,
			431, 140, 25, 162, 356, 174, 334, 138, 381, 74, 409, 405, 342, 346, 487, 121,
			44, 101, 286, 272, 474, 268, 57, 196, 411, 297, 387, 242, 234, 411, 198, 486,
			231, 489, 383, 222, 495, 407, 30, 477, 220, 311, 439, 212, 241, 187, 5, 369,
			14, 395, 312, 104, 219, 451, 267, 213, 342, 117, 165, 249, 446, 13, 410, 9,
			440, 95, 399, 149, 212, 394, 122, 297, 108, 255, 7, 62, 286, 249, 240, 253,
			226, 125, 123, 209, 95, 107, 309, 438, 484, 167, 188, 81, 4, 221, 450, 485,
			36, 156, 123, 196, 377, 58, 338, 432, 301, 133, 445, 190, 366, 479, 211, 48,
			90, 364, 394, 199, 334, 324, 398, 331, 232, 401, 77, 476, 420, 390, 105, 189,
			193, 378, 430, 337, 295, 336, 203, 216, 373, 152, 223, 130, 100, 247, 260, 315,
			450, 371, 198, 165, 90, 31, 57, 460, 379, 491, 424, 99, 403, 497, 366, 333,
			40, 201, 374, 294, 265, 360, 176, 160, 416, 317, 353, 334, 65, 204, 105, 301,
			61, 312, 290, 285, 374, 34, 42, 403, 439, 363, 356, 197, 22, 429, 465, 243,
			69, 132, 162, 122, 109, 177, 357, 176, 130, 167, 182, 483, 157, 203, 121, 246,
			178, 304, 120, 278, 139, 89, 103, 83, 359, 155, 276, 404, 171, 124, 167, 200,
			459, 261, 18, 440, 47, 174, 185, 164, 19, 257, 316, 314, 27, 397, 17, 385,
			127, 3, 134, 72, 68, 351, 282, 283, 60, 304, 86, 236, 32, 383, 50, 295,
			440, 84, 206, 482, 14, 181, 169, 493, 118, 461, 266, 41, 219, 69, 348, 5,
			33, 389, 158, 125, 6, 294, 492, 150, 66, 248, 268, 320, 402, 325, 489, 423,
			243, 373, 55, 88, 228, 93, 232, 126
	});
	private static Object[] allTests = new Object[]
			{orderedTest, reversedTest, shuffledTest, shuffledWithReps, bigTestWithReps};
}
