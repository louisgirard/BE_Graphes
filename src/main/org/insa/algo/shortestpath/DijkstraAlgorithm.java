package org.insa.algo.shortestpath;

import java.util.ArrayList;
import java.util.List;

import org.insa.algo.utils.*;
import org.insa.graph.*;
import org.insa.algo.AbstractSolution.Status;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        ShortestPathData data = getInputData();
        
        ShortestPathSolution solution = null;
        
        BinaryHeap<Label> labelHeap = new BinaryHeap<Label>();
        ArrayList<Label> tabLabel = new ArrayList<Label>();
        
        Label labelCourant = new Label();
        Label labelFils = new Label();
        
        //initialisation
        for (int i = 0; i < data.getGraph().size(); i++) {
        	tabLabel.add(new Label(data.getGraph().get(i)));
        }
        tabLabel.get(0).setCost(0);
        labelCourant = tabLabel.get(0); //recupere le sommet
        labelHeap.insert(labelCourant);
        
        //tant qu'il existe des sommets non marqués, tant que le tas n'est pas vide
        while(!labelHeap.isEmpty()) {
        	labelCourant = labelHeap.deleteMin();
        	//marque le sommet courant à TRUE
        	tabLabel.get(labelCourant.getCurrentNode().getId()).setMark();
        	//on regarde tous les successeurs du sommet/label courant
        	for (Arc a : labelCourant.getCurrentNode().getSuccessors()) {
        		labelFils = tabLabel.get(a.getDestination().getId());
        		//si la marque du premier successeur du sommet courant est false
        		if (!labelFils.getMark()) {
        			if (labelFils.getCost() > (labelCourant.getCost() + a.getLength())) {
        				labelFils.setCost(labelCourant.getCost() + a.getLength());
        				labelHeap.insert(labelFils);
        				labelFils.setFather(a);
        				//mettre dans le tableau de label
        				tabLabel.set(a.getDestination().getId(), labelFils);
        			}
        		}
        	}
        }
        
        List<Arc> arcs = new ArrayList<Arc>();
        boolean fin = false;
        
        labelCourant = tabLabel.get(data.getDestination().getId());
        while(!fin) {
    		arcs.add(0, labelCourant.getFather());
        	if (data.getOrigin().equals(labelCourant.getFather().getOrigin())) {
        		fin = true;
        	}else {
        		labelCourant = tabLabel.get(labelCourant.getFather().getOrigin().getId());
        	}
        }
        
        //check le feasible, !!!! attention, dijkstra ne marche que pour les valeurs positives !!!!
        solution = new ShortestPathSolution(data,Status.FEASIBLE,new Path(data.getGraph(),arcs));
        return solution;
    }

}
