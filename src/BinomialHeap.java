
/**
 * BinomialHeap
 *
 * An implementation of binomial heap over non-negative integers.
 * Based on exercise from previous semester.
 */
public class BinomialHeap {

	private int size;
	private BTList roots = new BTList();
	
	
	/**
	 * public class HeapNode
	 * 
	 * If you wish to implement classes other than BinomialHeap
	 * (for example HeapNode), do it in this file, not in 
	 * another file 
	 *  
	 */
	private static class BinomialTree {
		
		private int rank;
		private int value;
		private BTList children;
		private BinomialTree parent;
		
		
		public BinomialTree(int value) {
			this.value = value;
			children = new BTList();
		}
		
		/**
		 * @pre this.degree == other.degree
		 * 
		 */
		public BinomialTree link(BinomialTree other) {
			if (this.value <= other.value) {
				return link(this, other);
			} else {
				return link(other, this);
			}
		}
		
		private BinomialTree link(BinomialTree upper, BinomialTree lower) {
			upper.children.add(lower);
			upper.rank++;
			lower.parent = upper;
			upper.parent = null;
			
			return upper;
		}
		
	}
	
	
   /**
    * public boolean empty()
    *
    * precondition: none
    * 
    * The method returns true if and only if the heap
    * is empty.
    *   
    */
    public boolean empty() {
    	return this.size == 0;
    }
		
   /**
    * public void insert(int value)
    *
    * Insert value into the heap 
    *
    */
    public void insert(int value) {   
    	this.insert(new BinomialTree(value));
    	this.size++;
    }

    private void insert(BinomialTree item) {
    	int i = item.rank;
    	
    	do {
    		if (roots.get(i) == null) {
    			roots.add(item);
    			break;
    		} else {
    			item = item.link(roots.get(i));
    			roots.remove(i++);;
    		}
    	}
    	while (true);
    }
    
   /**
    * public void deleteMin()
    *
    * Delete the minimum value
    *
    */
    public void deleteMin() {
    	BinomialTree min = this.findMinTree();
    	this.roots.remove(min.rank);;
    	this.size--;
    	
    	for (int i = 0; i < min.children.length(); i++) {
    		this.insert(min.children.get(i));
    	}
    }

    public void deleteAll() {
    	for (int i = 0; i < this.size; i++) {
    		this.deleteMin();
    	}
    }
    
   /**
    * public int findMin()
    *
    * Return the minimum value
    *
    */
    public int findMin() {
    	BinomialTree min = this.findMinTree();
    	
    	if (min == null) {
    		return -1;
    	}
    	return min.value;
    }
    
    private BinomialTree findMinTree() {
    	
    	if (this.empty()) {
    		return null;
    	}
    	BinomialTree min = this.roots.get(this.minTreeRank());
    	
    	for (int i = min.rank + 1; i < this.roots.length(); i++) {
    		BinomialTree root = this.roots.get(i);
    		
    		if (root != null) {
    			if (root.value < min.value) {
    				min = root;
    			}
    		}
    	}
    	return min;
    }
    
   /**
    * public void meld (BinomialHeap heap2)
    *
    * Meld the heap with heap2
    *
    */
    public void meld (BinomialHeap other) {
    	
    	for (int i = 0; i < other.roots.length(); i++) {
    		if (other.roots.get(i) != null) {
    			this.insert(other.roots.get(i));
    		}
    	}
    }

   /**
    * public int size()
    *
    * Return the number of elements in the heap
    *   
    */
    public int size() {
    	return this.size;
     }
    
   /**
    * public int minTreeRank()
    *
    * Return the minimum rank of a tree in the heap.
    * 
    */
    public int minTreeRank() {
    	
    	if (this.empty()) {
    		return -1;
    	}
    	
    	BinomialTree min = this.roots.get(0);
    	for (int i = 0; i < this.roots.length();) {
    		if (min != null) {
    			break;
    		}
			min = this.roots.get(++i);
    	}
    	return min.rank;
    }
	
	   /**
    * public boolean[] binaryRep()
    *
    * Return an array containing the binary representation of the heap.
    * 
    */
    public boolean[] binaryRep() {
		boolean[] arr = new boolean[this.roots.length()];
		
		for (int i = 0; i < this.roots.length(); i++) {
			arr[i] = (roots.get(i) != null);
		}
        return arr;
    }

   /**
    * public void arrayToHeap()
    *
    * Insert the array to the heap. Delete previous elements in the heap.
    * 
    */
    public void arrayToHeap(int[] array) {
    	this.deleteAll();
    	
        for (int i = 0; i < array.length; i++) {
        	this.insert(array[i]);
        }
    }
	
   /**
    * public boolean isValid()
    *
    * Returns true if and only if the heap is valid.
    *   
    */
    public boolean isValid() {
    	boolean isValid = true;
    	
    	for (int i = 0; i < this.roots.length(); i++) {
    		isValid = isValid && isValid(this.roots.get(i));
    	}
    	return isValid;
    }
    
    private boolean isValid(BinomialTree item) {
    	if (item == null) {
    		return true;
    	}
    	
    	if (item.children.length() == 0) {
    		return true;
    	}
    	boolean isValid = true;
    	
    	for (int i = 0; i < item.children.length(); i++) {
    		isValid = isValid && isValid(item.children.get(i));
    	}
    	return isValid;
    }
    
	/**
	 * Helper classes
	 */
	
	// partial implementation of a list using dynamic (doubling) arrays
	private static class BTList {
		// the initial array length
		private static final int INITIAL_LENGTH = 16;
		// the factor by which to increase the array once its full
		private static final int INCREASE_FACTOR = 2;

		
		private BinomialTree[] storageArray;
		// Most Significant Bit + 1
		private int size;
		
		public BTList() {
			this.storageArray = new BinomialTree[INITIAL_LENGTH];
			this.size = 0;
		}
		
		// adds an item to the end of the list
		// worst case - O(n), amortized - O(1)
		public void add(BinomialTree item) {
			
			if ((this.storageArray.length < 2*this.size) || (this.storageArray.length <= item.rank)) {
				int sizeFactor = Math.max(this.storageArray.length, item.rank);
				BinomialTree[] increasedArray = new BinomialTree[sizeFactor * BTList.INCREASE_FACTOR];
				
				for (int i = 0; i < this.size; i++) {
					increasedArray[i] = this.storageArray[i];
				}
				
				this.storageArray = increasedArray;
			}
			this.storageArray[item.rank] = item;
			
			if (this.size <= item.rank) {
				this.size = item.rank + 1;
			}
		}

		public void remove(int index) {
			this.storageArray[index] = null;
			
			if (this.size - 1 == index) {
				for (int i = index; i >= 0; i--) {
					if (this.storageArray[i] == null) {
						this.size--;
					} else {
						break;
					}
				}
			}
		}
		
		// returns the size of the list - O(1)
		public int length() {
			return this.size;
		}
		
		// returns an item at a given index in the list - O(1)
		public BinomialTree get(int index) {
			return this.storageArray[index];
		}

	}
}
