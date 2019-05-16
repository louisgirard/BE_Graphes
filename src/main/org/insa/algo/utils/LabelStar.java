package org.insa.algo.utils;

import org.insa.algo.AbstractInputData.Mode;
import org.insa.algo.shortestpath.ShortestPathData;
import org.insa.graph.Node;

public class LabelStar extends Label {

	private double costEstimated;
	
	public LabelStar() {
		super();
		this.costEstimated = Double.POSITIVE_INFINITY;
	}
	
	public LabelStar(Node node, ShortestPathData data) {
		super(node);
		if (data.getMode() == Mode.TIME) {
			double speed = Math.max(data.getMaximumSpeed(), data.getGraph().getGraphInformation().getMaximumSpeed())*1000.0/3600.0;
			this.costEstimated = node.getPoint().distanceTo(data.getDestination().getPoint())/speed;
		}else {
			this.costEstimated = node.getPoint().distanceTo(data.getDestination().getPoint());
		}
	}
	
	public double getTotalCost() {
		return (this.costEstimated + super.getCost());
	}

}
