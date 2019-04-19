package org.insa.algo.utils;

import org.insa.graph.Arc;
import org.insa.graph.Node;

public class Label implements Comparable<Label> {
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
	
	public Label(Node node) {
		this.currentNode = node;
		this.mark = false;
		this.cost = (float)Double.POSITIVE_INFINITY;
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
	public void setFather(Arc a) {
		this.father = a;
	}

	public int compareTo(Label l) {
		if (this.cost < l.getCost()) {
			return -1;
		}else if (this.cost == l.getCost()) {
			return 0;
		}else { //this.cost > l.getCost()
			return 1;
		}
	}
	
	

}