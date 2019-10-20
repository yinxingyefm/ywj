package com.electricity.dao;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.electricity.entity.Citys;
import com.electricity.entity.Datas;
import com.electricity.entity.Indexs;
import com.electricity.entity.IndexsOfModule;
import com.electricity.entity.Module;
import com.electricity.entity.Times;
import com.electricity.entity.Zhexian;
import com.electricity.entity.Zhuzhuang;
import com.electricity.tool.JDBCUtils;

public class DataDealDaoIml implements DataDealDao {
	@Override
	public Citys getCitysById(int cid) {
		Citys citys=null;
		QueryRunner queryRunner = JDBCUtils.getQueryRunner();
		String sql ="select * from city_table where cid=?";
		try {
			citys = queryRunner.query(sql,new BeanHandler<Citys>(Citys.class),cid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return citys;
	}
	@Override
	public Times getTimesById(int tid) {
	  Times time=null;
	  QueryRunner queryRunner = JDBCUtils.getQueryRunner();
	  String sql="select * from time_table where Tid=?";
	  try {
	  time = queryRunner.query(sql,new BeanHandler<Times>(Times.class),tid);
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
		return time;
	}
	@Override
	public Indexs getIndexsById(int id) {
		Indexs index=null;
		QueryRunner queryRunner = JDBCUtils.getQueryRunner();
		String sql="select * from zb_table where Iid=?";
		try {
			index= queryRunner.query(sql,new BeanHandler<Indexs>(Indexs.class),id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return index;
	}
	@Override
	public ArrayList<Datas> getAllCityIndexTime(int timeid, int indexid) {
		List<Datas> list=new ArrayList<Datas>();
		QueryRunner queryRunner = JDBCUtils.getQueryRunner();
		String sql="SELECT * FROM data_table  WHERE  timeid=? and indexid=?";
		Object[] params={timeid,indexid};
		try {
			 list = queryRunner.query(sql,new BeanListHandler<Datas>(Datas.class),params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return (ArrayList<Datas>) list;
	}
	@Test
	public String JsongetAllCityIndexTime(int timeid, int indexid) {
		String result=null;
		JSONObject jsonObject=new JSONObject();
		DataDealDao dao=new DataDealDaoIml();
		Indexs index = dao.getIndexsById(indexid);
		Times time = dao.getTimesById(timeid);
		HashMap<String,Object> map=new HashMap<>();
		map.put("indexInfo", index);
		map.put("indexCycle",time);
		ArrayList<Datas> list = dao.getAllCityIndexTime(timeid, indexid);
		ArrayList< Zhuzhuang> Zhulist=new  ArrayList<Zhuzhuang>();
		for (Datas datas : list) {
		    int cid =datas.getCityid();
			String cityName = dao.getCitysById(cid).getcName();
			float data = datas.getDatas();
			Zhuzhuang z=new Zhuzhuang();
			z.setCityName(cityName);
			z.setDatas(data);
			Zhulist.add(z);
		}
		map.put("cityScore",Zhulist);
		jsonObject.put("barData", map);
		result=JSON.toJSONString(jsonObject);
		return result;
	}
	public ArrayList<Datas>  getAllTimeCityIndex(int indexid, int cityid) {
		List<Datas>  list =new ArrayList<Datas>();
		QueryRunner queryRunner = JDBCUtils.getQueryRunner();
		String sql="SELECT * FROM data_table WHERE indexid=? AND cityid=?";
		Object[] params={indexid,cityid};
		try {
			list=  queryRunner.query(sql,new BeanListHandler<Datas>(Datas.class),params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (ArrayList<Datas>) list;
	}
	public ArrayList<ArrayList<Datas>> getAllTimeAllCityIndex(int indexid) {
		
		List<ArrayList<Datas>>  list=new ArrayList<ArrayList<Datas>>();
		List<Datas> list2=new ArrayList<Datas>();
		DataDealDao dao=new DataDealDaoIml();
		for (int i = 1; i <= 18; i++) {
			list2=dao.getAllTimeCityIndex(indexid,i);
			list.add((ArrayList<Datas>) list2);
		}
		return (ArrayList<ArrayList<Datas>>) list;
	}
	public String JsonAllTimeAllCityIndex(int indexid) {
		List<Object>  list=new ArrayList<Object>();
		List<Datas> list2=new ArrayList<Datas>();
		HashMap<String,Object> map=new HashMap<String, Object>();
		JSONObject jsonObject=new JSONObject();
		String result=null;
		DataDealDao dao=new DataDealDaoIml();
		Indexs index = dao.getIndexsById(indexid);
		map.put("indexInfo",index);
		//��18�����н��б���
		for (int i = 1; i <=22; i++) {
		//��ȡÿ���������м��ȵ�ֵ
			list2=dao.getAllTimeCityIndex(indexid,i);
		//�Ի�ȡ��ֵ���мӹ����
			String	Cityname=null;
			ArrayList<Float> dataslist=new ArrayList<Float>();
			ArrayList<String> timeslist=new ArrayList<String>(); 
			for (Datas datas : list2) {
				Citys city = dao.getCitysById(datas.getCityid()); 
			    Cityname=city.getcName();
				datas.setCityName(Cityname);
				Float d = datas.getDatas();
				dataslist.add(d);
				String Time=dao.getTimesById(datas.getTimeid()).getTime();
				timeslist.add(Time);
			}
			Zhexian zhexian=new Zhexian();
			zhexian.setCityName(Cityname);
			zhexian.setDataList(dataslist);
			zhexian.setTimesList(timeslist);
		    list.add(zhexian);
		    String cid=String.valueOf(i);
		}
		map.put("cityScore", list);
		jsonObject.put("lineData",map);
		result=JSON.toJSONString(jsonObject);
		return result;
	}
	public Module getModuleById(int id) {
		// TODO Auto-generated method stub
		QueryRunner queryRunner = JDBCUtils.getQueryRunner();
		Module module=null;
		String sql="SELECT * FROM module_table where id=?";
		try {
			 module = queryRunner.query(sql,new BeanHandler<Module>(Module.class),id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return module;
	}
	@Override
	public ArrayList<Indexs> getIndexsInModule(int moduleid) {
		// TODO Auto-generated method stub
		QueryRunner queryRunner = JDBCUtils.getQueryRunner();
		String sql="SELECT * FROM zb_table WHERE moduleid=?";
		Object[] params={moduleid};
		List<Indexs> list=new ArrayList<Indexs>();
	    try {
			list = queryRunner.query(sql,new BeanListHandler<Indexs>(Indexs.class),params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		return (ArrayList<Indexs>) list;
	}
	@Override
	public ArrayList<Datas> getIndexInModuleOneTime(int timeid, int moduleid) {
		// TODO Auto-generated method stub
		DataDealDao dao=new DataDealDaoIml();
		QueryRunner queryRunner = JDBCUtils.getQueryRunner();
		ArrayList<Datas> list =new ArrayList<Datas>();
		ArrayList<Indexs> indexslist = dao.getIndexsInModule(moduleid);
		for (Indexs indexs2 : indexslist) {
			int indexid=indexs2.getIid();
			String sql="SELECT * FROM data_table WHERE indexid=? and timeid=? and cityid=9";
			Object[] params={indexid,timeid};
			try {
				Datas data = queryRunner.query(sql,new BeanHandler<Datas>(Datas.class),params);
			    list.add(data);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}
	@Override
	public float getValue(float datas, float Allscore, float weight) {
	  float value=0;
	  value=(datas/Allscore)*weight;
	  int   scale  = 3;//����λ��  
	  int   roundingMode  =  4;//��ʾ�������룬����ѡ��������ֵ��ʽ������ȥβ���ȵ�.  
	  BigDecimal   bd  =   new  BigDecimal((double)value);  
	  bd   =  bd.setScale(scale,roundingMode);  
	  value   =  bd.floatValue(); 
	  return value;
	}
	@Override
	public String getJsonIndexFromModule(int moduleid, int timeid) {
		DataDealDao dao=new DataDealDaoIml();
		HashMap<String,Object> map=new HashMap<String, Object>();
		String result=null;
		ArrayList<Datas> listdata=dao.getIndexInModuleOneTime(timeid, moduleid);
		Times times = dao.getTimesById(timeid);
		map.put("indexCycle",times);
		Module module = dao.getModuleById(moduleid);
		map.put("Module",module);
		ArrayList<IndexsOfModule> list=new ArrayList<IndexsOfModule>();
		for (Datas datas : listdata) {
			if(datas!=null){
				float d1 = datas.getDatas();
				int indexid=datas.getIndexid();
				Indexs index = dao.getIndexsById(indexid);
				String indexname=index.getIndexName();
				datas.setIndexName(indexname);
				float weight=index.getQweight();
				float allscore=100;
			    float value=dao.getValue(d1, allscore, weight);
			    datas.setDatas(value);
			    IndexsOfModule indexsOfModule=new IndexsOfModule(allscore, value, indexname, weight);
			    list.add(indexsOfModule);
			}
		}
		map.put("IndexValue",list);
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("IndexsOfModule", map);
	    result=	JSON.toJSONString(jsonObject);
		return result;
	}
	@Override
	public ArrayList<Module> getAllModules() {
		QueryRunner queryRunner = JDBCUtils.getQueryRunner();
		String sql="SELECT * FROM module_table";
		List<Module> list=new ArrayList<Module>();
		try {
			 list = queryRunner.query(sql,new BeanListHandler<Module>(Module.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (ArrayList<Module>) list;
	}
	public String getJsonAllModuleVAlue(int timeid) {
		HashMap<String,Object> map=new HashMap<String, Object>();
		String result=null;
		DataDealDao dao=new DataDealDaoIml();
		Times times = dao.getTimesById(timeid);
		map.put("times",times);
		ArrayList<Module> listModules = dao.getAllModules();
		for (Module module : listModules) {
			int moduleid=module.getId();
			//���ÿ��ģ����Сָ����к�
			float sum = dao.getIndexSumForOneModel(moduleid, timeid);
			module.setSum(sum);
			float weight=module.getWeight();
			//ģ��ÿ��ģ�������
			float Allscore=200;
			module.setAllscore(Allscore);
			float value = dao.getValue(sum, Allscore, weight);
			module.setValue(value);
		}
		map.put("listModules", listModules);
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("AllModul",map);
		result=JSON.toJSONString(jsonObject);
		return result;
	}
	
	@Override
	public float getIndexSumForOneModel(int moduleid, int timeid) {
		float sum=0;
		DataDealDao dao=new DataDealDaoIml();
		ArrayList<Datas> listdata=dao.getIndexInModuleOneTime(timeid, moduleid);
		System.out.println(listdata.toString());
		for (Datas datas : listdata) {
			if(datas!=null){
				if(datas.getDatas()!=null){
					float d1 = datas.getDatas();
					System.out.println(d1);
					sum=sum+d1;
				}
			}
			
		}
		System.out.println("listsize:"+listdata.size());
		System.out.println("sum:"+sum);
		return sum;
	}
}
