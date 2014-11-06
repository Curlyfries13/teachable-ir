package asu.me.tag;

import processing.core.PVector;

public class PointCloud {
	private PVector[][] cloudPoints;
	private int cloudAngle;
	private CartesianPlane area;
	private double[] cloudCertainty;
	public PointCloud(CartesianPlane inCP){
		area = inCP;
		cloudPoints = new PVector[4][2];
		cloudCertainty = new double[4];
	}
	
	public void fetchPoints(){
		for(int quadrant = 0; quadrant < 4; quadrant++){
			if (area.getQuadrantCertainty(quadrant) != Double.MAX_VALUE){
				cloudPoints[quadrant] = area.getQuadrantPoints(quadrant);
			}
			cloudCertainty[quadrant] = area.getQuadrantCertainty(quadrant);
		}
	}
	
	//returns the vectors of two points determined by point cloud
	public PVector[] getPoints(){
		PVector[][] intermediate = new PVector[4][2];
		PVector[] ret = new PVector[2];
		//since a higher certainty is a worse track we take the inverse as the weight in the track
		float certaintyWeight;
		//used for the average
		float certaintySum = 0;
		for(int quadrant = 0; quadrant < 4; quadrant++){
			if(cloudCertainty[quadrant] != Double.MAX_VALUE){
				certaintyWeight = (float) (1/cloudCertainty[quadrant]);
				intermediate[quadrant][0] = PVector.mult(area.getQuadrantPoints(quadrant)[0], certaintyWeight);
				intermediate[quadrant][1] = PVector.mult(area.getQuadrantPoints(quadrant)[1], certaintyWeight);
				certaintySum += certaintyWeight;
				ret[0] = PVector.add(ret[0], intermediate[quadrant][0]);
				ret[1] = PVector.add(ret[1], intermediate[quadrant][1]);
			}
		}
		if(certaintySum == 0.0){
			//untracked!!
		}
		ret[0] = PVector.div(ret[0],certaintySum);
		ret[1] = PVector.div(ret[1],certaintySum);
		return ret;
	}
}
