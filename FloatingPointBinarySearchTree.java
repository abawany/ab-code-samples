import java.io.*;
import java.lang.Math;

class FloatingPointBinarySearchTree {

	class TreeNode {
		TreeNode left;
		TreeNode right;
		float val;
		public TreeNode(TreeNode left, TreeNode right, float val) {
			this.left=left;
			this.right=right;
			this.val=val;
		}
	}

	private TreeNode root;
	public FloatingPointBinarySearchTree() {}
	
	// attrib: http://en.wikipedia.org/wiki/Binary_search_tree
	// uses iteration to add a new node to the BST
	public void add(float val) {
		// special case for first add
		if (this.root==null) 
			this.root=new TreeNode(null, null, val);
		else {
			TreeNode current=this.root;
			while (true) {
				if (this.compareFloat(val, current.val)==-1) {
					if (current.left==null) {
						current.left=new TreeNode(null, null, val);
						break;
					}
					else {
						current=current.left;
						continue;
					}
				}
				else {
					if (current.right==null) {
						current.right=new TreeNode(null, null, val);
						break;
					}
					else {
						current=current.right;
						continue;
					}
				}
			}
		}
	}
		
	// attrib: http://en.wikipedia.org/wiki/Tree_traversal
	public boolean contains(float val) {
		boolean found=false;
		
		TreeNode current=this.root;
		if (current==null) return found;
		
		while (!found && current!=null) {
			if (this.compareFloat(val, current.val)==-1) {
				current=current.left;
			}
			else if (this.compareFloat(val, current.val)==1) {
				current=current.right;
			}
			else {
				found =true;
			}
		}
		
		return found;
	}
	
	// attrib: http://stackoverflow.com/questions/2896013/manipulating-
	// and-comparing-floating-points-in-java
	// returns 0 if a == b, -1 if a < b, 1 if a > b
	private float delta=0.00000001f;
	protected int compareFloat(float a, float b) {
		if (Math.abs(a-b) < delta) 
			return 0;
		else if (a<b)
			return -1;
		else if (a>b)
			return 1;
		else
			throw new IllegalArgumentException("Unable to compare " + a + " and " + b);
	}
	
	// populate the BST with some test values and then try to find them
	public static void testBst() {
		int testId=0;
		
		System.out.println("Test " + testId++ + ": try to find in a null tree");
		FloatingPointBinarySearchTree tree=new FloatingPointBinarySearchTree();
		boolean found=tree.contains(4.9f);
		evaluate("Test" + testId, found, false, 4.9f);
		
		System.out.println("Test " + testId++ + ": add an element, and find it");
		float testVector1[] = {1.0f};
		tree=buildTestBst(testVector1);
		found=tree.contains(testVector1[0]);
		evaluate("Test" + testId, found, true, testVector1[0]);
		
		System.out.println("Test " + testId++ + ": add a few elements , try to find non-existent");
		float testVector2[] = {1.0f, 2.0f, 3.0f};
		tree=buildTestBst(testVector2);
		found=tree.contains(4.9f);
		evaluate("Test" + testId, found, false, 4.9f);
		
		System.out.println("Test " + testId++ + ": add a few elements and try to find one that exists");
		float testVector3[] = {1.0f, 2.0f, 3.0f, 4.0f, 4.1f, 4.2f, 4.9f, 5.0f, 5.2f};
		tree=buildTestBst(testVector3);
		found=tree.contains(testVector3[6]);
		evaluate("Test" + testId, found, true, testVector3[6]);
		
		System.out.println("Test " + testId++ + ": add a few elements and try to find an approximate value");
		float testVector4[] = {1.0f, 2.0f, 3.0f, 4.0f, 4.1f, 4.2f, 4.9f, 5.0f, 5.2f};
		tree=buildTestBst(testVector4);
		found=tree.contains(2.1f*2.0f);
		evaluate("Test" + testId, found, true, 2.1f*2.0f);
	}
	
	protected static FloatingPointBinarySearchTree buildTestBst(float[] testVector) {
		int len=testVector.length;
		FloatingPointBinarySearchTree tree=new FloatingPointBinarySearchTree();
		
		for (int i=0; i<len; ++i) {
			tree.add(testVector[i]);
		}
		
		return tree;
	}
	
	protected static void evaluate(String testId, boolean actual, boolean expected, 
		float value) {
		if (actual==expected)
			System.out.print("*PASS*: ");
		else 
			System.out.print("*FAIL*: ");
		System.out.println(testId + " actual:" + actual + " expected:" + expected 
			+ " " + value);
	}
	
	public static void main(String []args) {
		testBst();
	}
}