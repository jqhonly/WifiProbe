package com.simpletech.wifiprobe.dao;

import com.simpletech.wifiprobe.model.Visit;
import com.simpletech.wifiprobe.model.entity.BrandValue;

import java.util.Date;
import java.util.List;

/**
 * ����ͳ�� ��Dao�ӿ�
 * @author ���b
 * @date 2015-09-21 17:03:53 �й���׼ʱ��
 */
public interface DeviceModelStatisticsDao {

	/**
	 * mac�ķ��ʼ�¼
	 *
	 * @param idshop ��վID
	 * @param start  ��ʼʱ��
	 * @param end    ����ʱ��
	 * @return mac�ķ��ʼ�¼
	 */
	List<BrandValue> brand(String idshop, Date start, Date end) throws Exception;

}
