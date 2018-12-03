package dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClusterAnalysis {
	class ComparatorDp implements Comparator<DataPoint> {
		public int compare(DataPoint arg0, DataPoint arg1) {
			if(arg0 == null && arg1 == null) {  
			    return 0;  
			}  
			if(arg0 == null) {  
			    return -1;  
			}  
			if(arg1 == null) {  
			    return 1;  
			}  
			double temp = arg0.getReachableDistance()
					- arg1.getReachableDistance();
			int a = 0;
			if (temp < 0) {
				a = -1;
			} else if(temp>0){
				a = 1;
			}
			return a;
		}
	}
	public static String getCenterPoint(ArrayList<DataPoint> points){
		Set<Integer> set=new HashSet<>();
		int total=points.size();
		double lat=0;
		double lng=0;
		for(int i=0;i<points.size();i++){
			lat+=points.get(i).getLat()*Math.PI/180;
			lng+=points.get(i).getLng()*Math.PI/180;
			set.add(points.get(i).getIndex());
		}
		lat/=total;
		lng/=total;
		String index="";
		for(int ind:set){
			index+=ind+",";
		}
		return lat*180/Math.PI+","+lng*180/Math.PI+":"+index.substring(0, index.length()-1);
	}
	public List<DataPoint> startAnalysis(List<DataPoint> dataPoints,
			double radius, int ObjectNum,ArrayList<String> stayCenterPoints) {
		List<DataPoint> dpList = new ArrayList<DataPoint>();// 结果队列
		List<DataPoint> dpQue = new ArrayList<DataPoint>();// 样本
		
		ArrayList<DataPoint> stayPoints=new ArrayList<>();
		int total = 0;
		while (total < dataPoints.size()) {
			if (isContainedInList(dataPoints.get(total), dpList) == -1) {
				List<DataPoint> tmpDpList = isKeyAndReturnObjects(
						dataPoints.get(total), dataPoints, radius, ObjectNum);
				if (tmpDpList != null && tmpDpList.size() > 0) {
					DataPoint newDataPoint = new DataPoint(
							dataPoints.get(total));
					dpQue.add(newDataPoint);
				}
			}
			while (!dpQue.isEmpty()) {
				DataPoint tempDpfromQ = dpQue.remove(0);
				DataPoint newDataPoint = new DataPoint(tempDpfromQ);
				dpList.add(newDataPoint);
				List<DataPoint> tempDpList = isKeyAndReturnObjects(tempDpfromQ,
						dataPoints, radius, ObjectNum);
//				System.out.println(newDataPoint.getName() + ":"
//						+ newDataPoint.getReachableDistance());
				stayPoints.add(newDataPoint);
				if (tempDpList != null && tempDpList.size() > 0) {
					for (int i = 0; i < tempDpList.size(); i++) {
						DataPoint tempDpfromList = tempDpList.get(i);
						int indexInList = isContainedInList(tempDpfromList,
								dpList);
						int indexInQ = isContainedInList(tempDpfromList, dpQue);
						if (indexInList == -1) {
							if (indexInQ > -1) {
								int index = -1;
								for (DataPoint dataPoint : dpQue) {
									index++;
									if (index == indexInQ) {
										if (dataPoint.getReachableDistance() > tempDpfromList
												.getReachableDistance()) {
											dataPoint
													.setReachableDistance(tempDpfromList
															.getReachableDistance());
										}
									}
								}
							} else {
								dpQue.add(new DataPoint(tempDpfromList));
							}
						}
					}

					// TODO：对Q进行重新排序
					Collections.sort(dpQue, new ComparatorDp());
				}
			}
			if(!stayPoints.isEmpty()){
//				stayCenterPoints.add(getCenterPoint(stayPoints));//取停留点中心点
				for(DataPoint point:stayPoints){
					stayCenterPoints.add(point.getLat()+","+point.getLng());
				}
				stayPoints.clear();
			}
//			System.out.println("------total : "+ total);
			total++;

		}
		if(!stayPoints.isEmpty()){
//			stayCenterPoints.add(getCenterPoint(stayPoints));//取停留点中心点
			for(DataPoint point:stayPoints){
				stayCenterPoints.add(point.getLat()+","+point.getLng());
			}
			stayPoints.clear();
		}

		return dpList;
	}

	public void displayDataPoints(List<DataPoint> dps) {
		for (DataPoint dp : dps) {
			System.out.println(dp.getName() + ":" + dp.getReachableDistance());
		}
	}

	private int isContainedInList(DataPoint dp, List<DataPoint> dpList) {
		int index = -1;
		for (DataPoint dataPoint : dpList) {
			index++;
			if (dataPoint.getName().equals(dp.getName())) {
				return index;
			}
		}
		return -1;
	}

	private List<DataPoint> isKeyAndReturnObjects(DataPoint dataPoint,
			List<DataPoint> dataPoints, double radius, int ObjectNum) {// 找所有直接密度可达点
		List<DataPoint> arrivableObjects = new ArrayList<DataPoint>(); // 用来存储所有直接密度可达对象
		List<Double> distances = new ArrayList<Double>(); // 欧几里得距离
		double coreDistance; // 核心距离

		for (int i = 0; i < dataPoints.size(); i++) {
			DataPoint dp = dataPoints.get(i);
			double distance = getDistance(dataPoint, dp);
			if (distance <= radius) {
				distances.add(distance);
				arrivableObjects.add(dp);
			}
		}

		if (arrivableObjects.size() >= ObjectNum) {
			List<Double> newDistances = new ArrayList<Double>(distances);
			Collections.sort(distances);
			coreDistance = distances.get(ObjectNum - 1);
			for (int j = 0; j < arrivableObjects.size(); j++) {
				if (coreDistance > newDistances.get(j)) {
					if (newDistances.get(j) == 0) {
						dataPoint.setReachableDistance(coreDistance);
					}
					arrivableObjects.get(j).setReachableDistance(coreDistance);
				} else {
					arrivableObjects.get(j).setReachableDistance(
							newDistances.get(j));
				}
			}
			return arrivableObjects;
		}

		return null;
	}

	private double getDistance(DataPoint dp1, DataPoint dp2) {
		double distance = 0.0;
		double lat1 = dp1.getLat();
		double lng1	= dp1.getLng();
		double lat2 = dp2.getLat();
		double lng2	= dp2.getLng();
		distance= Math.pow((lat1 - lat2), 2)+Math.pow((lng1 - lng2), 2);
		distance = Math.pow(distance, 0.5)/180*Math.PI*6300000;
		return distance;
	}

	public static void main(String[] args) {
		
	}

}
