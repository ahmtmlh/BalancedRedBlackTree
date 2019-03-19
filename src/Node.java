public class Node<K extends Comparable<K>,V>{
	private K key;
	private V value;
	protected Node<K,V> parent, left, right;
	protected boolean color;

	public Node(K key, V value, boolean color){
		this.key = key;
		this.value = value;
		parent = left = right = null;
		this.color = color;
	}

	// Accessor Methods

	public K getKey() { return key; }
	public V getValue() { return value; }

}