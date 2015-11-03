package com.simpletech.wifiprobe.service;

import com.simpletech.wifiprobe.model.Visit;
import com.simpletech.wifiprobe.model.entity.BrandValue;

import java.util.Date;
import java.util.List;

/**
 * ͳ��API Service
 * Created by ���b on 2015/9/25.
 */
public interface DeviceModelStatisticsService {

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
