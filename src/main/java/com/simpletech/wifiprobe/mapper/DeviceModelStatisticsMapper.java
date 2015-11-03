package com.simpletech.wifiprobe.mapper;

import com.simpletech.wifiprobe.model.entity.BrandValue;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * ͳ��Mapper�ӿ�
 * Created by ���b on 2015/9/29.
 */
public interface DeviceModelStatisticsMapper {

    /**
     * ͳ���豸��Ϣ
     *
     * @param idshop ����ID
     * @param start  ��ʼ
     * @param end    ����ʱ��
     * @return �豸�ķ��ʼ�¼
     */
    @Select("SELECT end_brand name, COUNT(id) vt,COUNT(DISTINCT end_brand) uv  FROM t_visit WHERE idshop=#{idshop} AND (create_time BETWEEN #{start} AND #{end}) group by name")
    List<BrandValue> brand(@Param("idshop") String idshop, @Param("start") Date start, @Param("end") Date end) throws Exception;

    /**
     * ͳ������������visit|uv|pv��
     * @param idshop ����ID
     * @param start  ��ʼʱ��
     * @param end    ����ʱ��
     * @return ��������
     */
    @Select("SELECT COUNT(id) vt,COUNT(DISTINCT end_brand) uv  FROM t_visit WHERE idshop=#{idshop} AND (create_time BETWEEN #{start} AND #{end}) ")
    BrandValue coutBrand(@Param("idshop") String idshop, @Param("start") Date start, @Param("end") Date end) throws Exception;

}
