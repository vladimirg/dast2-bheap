
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
				return BinomialTree.link(this, other);
			} else {
				return BinomialTree.link(other, this);
			}
		}
		
		private static BinomialTree link(BinomialTree upper, BinomialTree lower) {
			upper.children.add(lower);
			upper.rank++;
			
			return upper;
		}
		
		/**
		 * @return deep copy of myTree
		 * 
		 */
	    public static BinomialTree safeTree(BinomialTree myTree) {
	    	
	    	if (myTree == null) {
	    		return null;
	    	}
			
			BinomialTree resultedTree = new BinomialTree(myTree.value);
			resultedTree.rank = myTree.rank;
			
			for (int i = 0; i < myTree.children.length(); i++) {
				resultedTree.children.add(BinomialTree.safeTree(myTree.children.get(i)));
			}
			return resultedTree;
	    }
	        
	    /**
	     * @return true iff for every node in the tree (item): parent value <= child value
	     * 
	     */
	    public static boolean isValid(BinomialTree item) {
	    	if (item == null) {
	    		return true;
	    	}
	    	
	    	if (item.children.length() == 0) {
	    		return true;
	    	}
	    	boolean isValid = true;
	    	
	    	for (int i = 0; i < item.children.length(); i++) {
	    		BinomialTree child = item.children.get(i);
	    		isValid = isValid && BinomialTree.isValid(child) && item.value <= child.value;
	    	}
	    	return isValid;
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

    /**
     * Insert whole tree into the heap
     * 
     */
    private void insert(BinomialTree item) {
    	
    	if (item != null) {
        	int i = item.rank;
        	
        	do {
        		if (this.roots.get(i) == null) {
        			this.roots.add(item);
        			break;
        		}
        		item = item.link(this.roots.get(i));
    			this.roots.remove(i++);
        	}
        	while (true);
    	}
    }
    
    /**
     * public void deleteMin()
     *
     * Delete the minimum value
     *
     */
    public void deleteMin() {
    	
    	if (!this.empty()) {
        	
        	BinomialTree min = this.findMinTree();
        	this.roots.remove(min.rank);
        	this.size--;
        	
        	for (int i = 0; i < min.children.length(); i++) {
        		this.insert(min.children.get(i));
        	}
    	}
    }

    private void deleteAll() {
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
    	
    	if (this.empty()) {
    		return -1;
    	}
    	return this.findMinTree().value;
    }
    
    /**
     * @return the tree with the minimum 
     * 
     */
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
    public void meld(BinomialHeap other) {
    	BinomialHeap safeOther = BinomialHeap.safeHeap(other);
    	this.size += safeOther.size;

    	for (int i = 0; i < safeOther.roots.length(); i++) {
    		this.insert(safeOther.roots.get(i));
    	}
    }
    
	/**
	 * @return deep copy of myHeap
	 * 
	 */
    private static BinomialHeap safeHeap(BinomialHeap myHeap) {
    	BinomialHeap resultedHeap = new BinomialHeap();
    	resultedHeap.size = myHeap.size;
    	
    	for (int i = 0; i < myHeap.roots.length(); i++) {
    		resultedHeap.roots.add(BinomialTree.safeTree(myHeap.roots.get(i)));	
    	}
    	return resultedHeap;
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
    	
    	for (int i = 0; i < this.roots.length(); i++) {
			min = this.roots.get(i);
    		if (min != null) {
    	    	break;
    		}
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
    		BinomialTree root = this.roots.get(i);
    		
    		if (root != null) {
    			isValid = isValid && BinomialTree.isValid(root) && root.rank == i;
    		}
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
			
			if (item != null) {
				
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
		}

		// removes an item from the list
		public void remove(int index) {
			this.storageArray[index] = null;
			
			if (index == this.size - 1) {
				for (int i = index; i >= 0; i--) {
					if (this.storageArray[i] != null) {
						break;
					}
					this.size--;
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
