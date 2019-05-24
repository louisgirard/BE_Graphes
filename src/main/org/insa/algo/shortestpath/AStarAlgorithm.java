package org.insa.algo.shortestpath;

import org.insa.algo.utils.Label;
import org.insa.algo.utils.LabelStar;
import org.insa.graph.Node;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    @Override
    protected Label newLabel(Node node, ShortestPathData data) {
		return new LabelStar(node,data);
	}
    
    @Override
    protected Label newLabel() {
		return new LabelStar();
	}
}
