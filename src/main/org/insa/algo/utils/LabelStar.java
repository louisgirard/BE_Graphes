package org.insa.algo.utils;

import org.insa.algo.AbstractInputData.Mode;
import org.insa.algo.shortestpath.ShortestPathData;
import org.insa.graph.GraphStatistics;
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
			double speed = data.getGraph().getGraphInformation().getMaximumSpeed();
			if (speed == GraphStatistics.NO_MAXIMUM_SPEED) {
				speed = 200;
			}
			this.costEstimated = node.getPoint().distanceTo(data.getDestination().getPoint())/(speed*1000.0f/3600.0f);
		}else {
			this.costEstimated = node.getPoint().distanceTo(data.getDestination().getPoint());
		}
	}
	
	public double getTotalCost() {
		return (this.costEstimated + super.getCost());
	}

}
