package com.electricity.dao;



public class Jsonservice {
	private DataDealDao dao=new DataDealDaoIml();
	//��ȡJson��״ͼ(һ������ һ��ָ�� ���г��е�ֵ)
	
	public String getBarChart(int timeid,int indexid){
		return dao.JsongetAllCityIndexTime(timeid, indexid);
	}
	//��ȡJson����ͼ(���г��� һ��ָ�� ���м��� )
	public String getLineChart(int indexid){
		
		return dao.JsonAllTimeAllCityIndex(indexid);
	}
	//��ȡ�Ĵ�ģ��÷ֵ�Json����
    public String getFourModual(int timeid){
    	return dao.getJsonAllModuleVAlue(timeid);
    }
    //��ȡÿ��ģ���µĸ���ָ��Json
    public String getIndexForOneModual(int moduleid,int timeid){
    	return dao.getJsonIndexFromModule(moduleid, timeid);
    }
   
    
}
