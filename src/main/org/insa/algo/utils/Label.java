package org.insa.algo.utils;

import org.insa.graph.Arc;
import org.insa.graph.Node;

public class Label {
	private Node currentNode;	
	private boolean mark;	
	private float cost;	
	private Arc father;

	public Label() {
		this.currentNode = null;
		this.mark = false;
		this.cost = 100000000000000f;
		this.father = null;		
	}

	//getter
	public float getCost() {
		return this.cost;
	}
	public Node getCurrentNode(){
		return this.currentNode;
	}
	public boolean getMark() {
		return this.mark;
	}
	public Arc getFather() {
		return this.father;
	}
	//setter
	public void setCost(float cost) {
		this.cost = cost;
	}
	public void setCurrentNode(Node node) {
		this.currentNode = node;
	}
	public void setMark() {
		this.mark = true;
	}

}