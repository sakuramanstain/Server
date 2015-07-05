package code;
import java.util.List;
import java.util.Map;
/*
 * 算法抽象类 寻找对应的地图ID函数 和 寻找匹配的采样点集合函数
 * 函数前两个参数为上传的本地采样得到的wifi数据强度数组和名称，后面的参数为数据库模块
 * */

public abstract  class Algorithm {
	public TrainSample m_TrainSample=null;
	abstract int find_map_id(List<String> wifi_ssid ,Map<String,Double> wifi_up,DatabaseManager m_DatabaseManager);
	abstract List<Integer> find_right_sample_ok(List<String> wifi_ssid ,Map<String,Double> wifi_up,int map_id,DatabaseManager m_DatabaseManager);
	abstract List<Integer> find_right_sample_ok_2(List<String> wifi_ssid ,Map<String,Double> wifi_up,int map_id_1,int map_id_2,DatabaseManager m_DatabaseManager);
	abstract Map<String,Double> find_coordinate(List<Integer> ok_sample_id,List<String> wifi_ssid,Map<String,Double> wifi_up,DatabaseManager m_DatabaseManager);
	abstract void initial_train();
	int train(DatabaseManager m_DatabaseManager){initial_train();m_TrainSample.train(m_DatabaseManager);return 0;}
}
