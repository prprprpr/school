package bs;

import java.util.ArrayList;

public class pearsonCorrelationScore {
	private float[] xData;
	private float[] yData;
	private float xMeans;
	private float yMeans;
	/**
     * 求解皮尔逊的分子
     */
    private float numerator;
    /**
     * 求解皮尔逊系数的分母
     */
    private float denominator;
    /**
     * 计算最后的皮尔逊系数
     */
    private float pearsonCorrelationScore;
    
    public pearsonCorrelationScore(ArrayList<point> points){
    	this.xData=getXDatas(points).get(0);
    	this.yData=getXDatas(points).get(1);
    	this.xMeans=this.getMeans(xData);
    	this.yMeans=this.getMeans(yData);
    	this.numerator=this.generateNumerator();
    	this.denominator=this.generateDenomiator();
    	this.pearsonCorrelationScore=this.numerator/this.denominator;
    }
    
    public ArrayList<float[]> getXDatas(ArrayList<point> points){
    	ArrayList<float[]> rs=new ArrayList<float[]>();
    	float[] x=new float[points.size()];
    	float[] y=new float[points.size()];
    	for(int i=0;i<points.size();i++){
    		x[i]=points.get(i).getX();
    		y[i]=points.get(i).getY();
    	}
    	rs.add(x);
    	rs.add(y);
    	return rs;
    }
    
    private float generateNumerator(){
    	float sum=0.0f;
    	for(int i=0;i<xData.length;i++){
    		sum+=(xData[i]-xMeans)*(yData[i]-yMeans);
    	}
    	return sum;
    }
    
    private float generateDenomiator(){
    	float xSum=0.0f;
    	for(int i=0;i<xData.length;i++){
    		xSum+=(xData[i]-xMeans)*(xData[i]-xMeans);
    	}
    	
    	float ySum=0.0f;
    	for(int i=0;i<yData.length;i++){
    		ySum+=(yData[i]-yMeans)*(yData[i]-yMeans);
    	}
    	
    	return (float) (Math.sqrt(xSum)*Math.sqrt(ySum));
    }
    
    private float getMeans(float[] datas){
    	float sum=0.0f;
    	for(int i=0;i<datas.length;i++){
    		sum+=datas[i];
    	}
    	return sum/datas.length;
    }
    
    public float getPearsonCorrelationScore(){
    	return this.pearsonCorrelationScore;
    }
    
    public static void main(String[] args){
//    	ArrayList<point> a=new ArrayList<point>();
//    	a.add(new point(1,1));
//    	a.add(new point(1,1));
//    	a.add(new point(1,3));
//        pearsonCorrelationScore score = new pearsonCorrelationScore(a);
//        Double b=(double) score.getPearsonCorrelationScore();
//        System.out.println(b.isNaN());
    }
}
