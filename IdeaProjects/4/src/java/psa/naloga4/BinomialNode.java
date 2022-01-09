package psa.naloga4;

import java.util.Vector;

public class BinomialNode {
	public Vector<BinomialNode> children;
	public int key;
	
	public BinomialNode(int key) {
		this.key = key;
		children = new Vector<BinomialNode>();
	}
	
	public boolean addChild(BinomialNode child) {
		return children.add(child);
	}
	
	public Vector<BinomialNode> getChilds() {
		return this.children;
	}
	
	public int getKey() {
		return this.key;
	}
	
}
