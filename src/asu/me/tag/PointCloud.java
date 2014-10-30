package asu.me.tag;

import processing.core.PVector;

public class PointCloud {
	private PVector[][] cloudPoints;
	private int cloudAngle;
	private CartesianPlane area;
	private int certaintySum;
	
	public PointCloud(CartesianPlane inCP){
		area = inCP;
		cloudPoints = new PVector[4][2];
	}
	
	public void fetchPoints(){
		int numSum = 0;
		int denominator = 0;
		for(int quadrant = 0; quadrant < 4; quadrant++){
			if (area.getQuadrantCertainty(quadrant) != Double.MAX_VALUE){
				cloudPoints[quadrant] = area.getQuadrantPoints(quadrant);
				numSum += area.getQuadrantCertainty(quadrant);
			}
			else{
				cloudPoints[quadrant] = null;
			}
		}
		certaintySum = numSum;
	}
	
	public PVector[] getPoints(){
		PVector[] ret = new PVector[2];
		float certaintyWeight = 0;
		for(int quadrant = 0; quadrant < 4; quadrant++){
			if(cloudPoints[quadrant] != null){
				certaintyWeight = (float) (area.getQuadrantCertainty(quadrant)/certaintySum);
				ret[0] = PVector.mult(area.getQuadrantPoints(quadrant)[0], certaintyWeight);
				ret[1] = PVector.mult(area.getQuadrantPoints(quadrant)[1], certaintyWeight);
			}
		}
		return ret;
	}
}
