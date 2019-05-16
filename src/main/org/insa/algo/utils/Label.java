package org.insa.algo.utils;

import org.insa.graph.Arc;
import org.insa.graph.Node;

public class Label implements Comparable<Label> {
	private Node currentNode;	
	private boolean mark;	
	private double cost;	
	private Arc father;

	public Label() {
		this.currentNode = null;
		this.mark = false;
		this.cost = Double.POSITIVE_INFINITY;
		this.father = null;	
	}
	
	public Label(Node node) {
		this.currentNode = node;
		this.mark = false;
		this.cost = Double.POSITIVE_INFINITY;
		this.father = null;		
	}

	//getter
	public double getCost() {
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
	public double getTotalCost() {
		return this.cost;
	}
	//setter
	public void setCost(double cost) {
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
		if (getTotalCost() < l.getTotalCost()) {
			return -1;
		}else if (getTotalCost() == l.getTotalCost()) {
			return 0;
		}else { //this.cost > l.getTotalCost()
			return 1;
		}
	}
	
	

}