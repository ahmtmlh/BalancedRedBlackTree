@SuppressWarnings({"rawtypes" , "unchecked"})
public class BalancedRedBlackTree<K extends Comparable<K>, V>{

	private Node root;
	private Node lastNode;

	private static final boolean BLACK = true;
	private static final boolean RED = false;
	private static final boolean LINE = true;						   // Node position to determine how the double red problem will be solved
	private static final Node LEAF = new Node(null, null, BLACK);      // LEAF node for comparison

	public BalancedRedBlackTree(){
		root = null;
		lastNode = null;
	}

	private void rotateRight(Node node, boolean position){
		if(position == LINE){
			Node temp = node.left;
			node.left = temp.right;
			if(node.left != null) node.left.parent = node;
			temp.right = node;
			temp.parent = node.parent;
			if(node.parent != null){
				if(node.parent.left == node)
					node.parent.left = temp;
				else
					node.parent.right = temp;
			}
			node.parent = temp;
			node.color = !node.color;
			temp.color = !temp.color;
			if(node == root) 
				root = temp;
		}
		else{
			Node temp = node.left.right;
			Node left = temp.left;
			Node right = temp.right;
			temp.right = node;
			temp.left = node.left;
			temp.parent = node.parent;
			if(node.parent != null){
				if(node.parent.left == node)
					node.parent.left = temp;
				else
					node.parent.right = temp;
			}
			node.parent = temp;
			node.left.parent = temp;
			node.left.right = left;
			node.left = right;
			node.color = !node.color;
			temp.color = !temp.color;
			if(node == root)	
				root = temp;
		}
	}
	private void rotateLeft(Node node, boolean position){
		if(position == LINE){
			Node temp = node.right;
			node.right = temp.left;
			if(node.right != null) node.right.parent = node;
			temp.left = node;
			temp.parent = node.parent;
			if(node.parent != null){
				if(node.parent.left == node)
					node.parent.left = temp;
				else
					node.parent.right = temp;
			}
			node.parent = temp;
			node.color = !node.color;
			temp.color = !temp.color;
			if(node == root)
				root = temp;
		}
		else{
			Node temp = node.right.left;
			Node left = temp.left;
			Node right = temp.right;
			temp.left = node;
			temp.right = node.right;
			temp.parent = node.parent;
			if(node.parent != null){
				if(node.parent.left == node)
					node.parent.left = temp;
				else
					node.parent.right = temp;
			}
			node.parent = temp;
			node.right.parent = temp;
			node.right.left = right;
			node.right = left;
			node.color = !node.color;
			temp.color = !temp.color;
			if(node == root)	
				root = temp;
		}
	}

	private void flipColors(Node node){
		node.color = !node.color;
		node.left.color = !node.left.color;
		node.right.color = !node.right.color;
	}
	//To keep the tree balanced in the terms of tree terminology. Height difference only kept max 1,
	//where normal red-black tree has the heigth difference of 2 between left and rigth branches.
	private void balanceHeight(){
		Node temp = root;
		while(temp != lastNode){
			int leftHeight = height(temp.left), rightHeight = height(temp.right);
			if(leftHeight >= 0 && rightHeight >= 0 && Math.abs(leftHeight - rightHeight) > 1){
				if(rightHeight > leftHeight){
					rotateLeft(temp , LINE);
				}
				else{
					rotateRight(temp , LINE);
				}
				temp = root;
			}
			int comparator = lastNode.getKey().compareTo(temp.getKey());
			if(comparator < 0){
				temp = temp.left;
			}
			else if(comparator > 0){
				temp = temp.right;
			}
			else
				break;
		}
	}
	// For Debugging
	public void inOrder(){
		inOrder(root);
	}
	private void inOrder(Node node){
		if(node != null){
			System.out.println(node.getKey() + " "+ node.getValue() + " " + (node.color ? "BLACK" : "RED"));
			inOrder(node.left);
			
			inOrder(node.right);
		}
	}

	private boolean isRed(Node node){
		if(node == null || node == LEAF){
			return false;
		}
		return node.color == RED;
	}

	private Node uncle(Node n){
		try{
			if(n.parent.parent.right == n.parent){
				return n.parent.parent.left == null ? LEAF: n.parent.parent.left;
			}
			else{
				return n.parent.parent.right == null ? LEAF: n.parent.parent.right;
			}
		}catch(NullPointerException e){
			return LEAF;						// If the uncle or parent is not found
		}
	}

	private int height(Node node){				//Used in height differences
		if (node == null){
			return -1;
		}
		else{
			return 1 + Math.max(height(node.left), height(node.right));
		}
	}

	public void put(K key, V value) {
		if(value == null){
			System.out.println("Illegal Argument");
			return;
		}

		put(root,key,value,(root == null ? 0 : key.compareTo((K) root.getKey()) ));
		//balanceHeight();
		root.color = BLACK;
	}
	private void put(Node node, K key, V value, int comparator){
		//Put the node as the first element of the tree
		if(root == null){
			root = new Node(key,value,RED);
			lastNode = root;
			return;
		}
		else{
			//Go left branch
			if(comparator < 0){
				//Can't go any deeper, put the node at the end of the left-subtree
				if(node.left == null){
					node.left = new Node(key, value, RED);
					node.left.parent = node;
					lastNode = node.left;
				}
				else
					put(node.left,key,value,key.compareTo((K) node.left.getKey()));
			}
			//Go right branch
			else if(comparator > 0){
				//Can't go any deeper from the right branch. Put the node at the end of the right-subtree
				if(node.right == null){
					node.right = new Node(key,value,RED);
					node.right.parent = node;
					lastNode = node.right;
				}
				else
					put(node.right,key,value,key.compareTo((K) node.right.getKey()));
			}
			else{
				System.out.println("Stated key: " + key.toString() + " is already in the tree. Please enter a valid one");
			}

		}

		// CONTROL STATEMENTS

		if(isRed(node.right) && isRed(node.right.right)){
			if(isRed(uncle(node.right.right)))
				flipColors(node);
			else
				rotateLeft(node , LINE);
		}
		if(isRed(node.left) && isRed(node.left.left)){
			if(isRed(uncle(node.left.left)))
				flipColors(node);
			else
				rotateRight(node , LINE);
		}
		if(isRed(node.right) && isRed(node.right.left)){
			if(isRed(uncle(node.right.left)))
				flipColors(node);
			else
				rotateLeft(node, !LINE);
		}
		if(isRed(node.left) && isRed(node.left.right)){
			if(isRed(uncle(node.left.right)))
				flipColors(node);
			else
				rotateRight(node , !LINE);
		}
	}

	public void search(K key) {
		System.out.println();
		Node temp = root;
		while(temp != null){
			int comparator = key.compareTo((K) temp.getKey());
			if(comparator < 0){
				temp = temp.left;
			}
			else if(comparator > 0){
				temp = temp.right;
			}
			else{
				System.out.println("Value: " + temp.getValue());

				System.out.println("Color: " + (temp.color ? "BLACK" : "RED"));
				if(temp.parent != null)
					System.out.println("Parent Key: " + temp.parent.getKey().toString() + " --> Parent Color: " + (temp.parent.color ? "BLACK" : "RED"));
				else
					System.out.println("This node has no parent!");

				if(temp.parent != null && temp.parent.parent != null)
					System.out.println("Grandparent Key: " + temp.parent.parent.getKey().toString() + " --> Grandparent Color: " + (temp.parent.parent.color ? "BLACK" : "RED"));
				else
					System.out.println("This node has no grandparent!");

				Node uncle = uncle(temp);
				if(uncle != LEAF)
					System.out.println("Uncle Key: " + uncle.getKey().toString() + " --> Uncle Color: " + (uncle.color ? "BLACK" : "RED"));
				else
					System.out.println("This node has no uncle!");
				return;
			}
		}
		System.out.println("There is no such node in tree!");
	}
}