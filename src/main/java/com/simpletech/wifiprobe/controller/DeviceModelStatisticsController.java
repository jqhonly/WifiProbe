package com.simpletech.wifiprobe.controller;

import com.simpletech.wifiprobe.model.constant.Period;
import com.simpletech.wifiprobe.model.entity.PeriodValue;
import com.simpletech.wifiprobe.service.DeviceModelStatisticsService;
import com.simpletech.wifiprobe.service.StatisticsService;
import com.simpletech.wifiprobe.util.AfReflecter;
import com.simpletech.wifiprobe.util.AfStringUtil;
import com.simpletech.wifiprobe.util.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * �豸��������ͳ�ƽӿ�
 * Created by ���b on 2015/9/25.
 */
@RestController
@RequestMapping("api/statistics")
public class DeviceModelStatisticsController {

    @Autowired
    DeviceModelStatisticsService service;

    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyyMMddHHmmss"), true));
    }

    /**
     * �Զ���ʱ�λ�ȡ�����豸����API
     *
     * @param shopId ����ID
     * @param offset ƫ�� 0=���� -1=���� 1=���� -2 2 -3...
     * @param span   ��� [day|week|month|year]
     * @param start  ��ʼʱ�� ("yyyyMMddHHmmss")
     * @param end    ����ʱ�� ("yyyyMMddHHmmss")
     * @return eventͳ������
     */
    @RequestMapping("deviceModel/shop/{shopId:\\d+}")
    public Object brand(@PathVariable String shopId,Integer offset, Period span, Date start, Date end) throws Exception {
        end = timeEnd(end, span, offset);
        start = timeStart(start, span, offset);
        return service.brand(shopId,start, end);

    }

    /**
     * ���ʱ��ֶκ�����
     * @param period ʱ������ [ʱ|��|��|��]
     * @param start  ��ʼʱ��
     * @param end    ����ʱ��
     */
    private void doCheckPeriod(Period period, Date start, Date end) throws ServiceException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        int count = 0;
        while (calendar.getTime().before(end)) {
            count++;
        }
        if (count > 200) {
            throw new ServiceException("������ƫ�������ʱ�������ԣ�");
        }
    }

    /**
     * �������ںͱ��˼��㿪ʼʱ��
     *
     * @param start  ��ʼʱ��
     * @param span   ʱ����
     * @param offset ƫ��
     * @return ��ʼʱ��
     */
    private Date timeStart(Date start, Period span, Integer offset) throws ParseException {
        if (span != null && offset != null /*&& !Period.hour.equals(span)*/) {
            DateFormat format = span.getFormat();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(format.parse(format.format(calendar.getTime())));
            calendar.add(span.getField(), offset);
            return calendar.getTime();
        }
        if (start == null) {
            return timeStart(new Date(), Period.year, -1000);
        }
        return start;
    }

    /**
     * �������ںͱ��˼������ʱ��
     *
     * @param end    ����ʱ��
     * @param span   ʱ����
     * @param offset ƫ��
     * @return ����ʱ��
     */
    private Date timeEnd(Date end, Period span, Integer offset) throws Exception {
        if (span != null && offset != null/* && !Period.hour.equals(span)*/) {
            DateFormat format = span.getFormat();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(format.parse(format.format(calendar.getTime())));
            calendar.add(span.getField(), offset + 1);
            return calendar.getTime();
        }
        if (end == null) {
            return timeEnd(new Date(), Period.year, 1000);
        }
        return end;
    }

    /**
     * �������
     *
     * @param list ���ݿ���Ч�����б�
     * @return ��������
     */
    private <T extends PeriodValue> List<T> fulldata(List<T> list, DateFormat format, int field, Date start, Date end, Class<T> clazz) {
        Map<String, T> map = tomap(list);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        List<T> nlist = new ArrayList<>();
        while (calendar.getTime().before(end)) {
            String keytime = format.format(calendar.getTime());
            T value = map.get(keytime);
            if (value == null) {
                value = AfReflecter.newInstance(clazz);
                value.setEmpty();
                value.setDate(keytime);
                value.setTime(calendar.getTime());
                nlist.add(value);
            } else {
                nlist.add(value);
                map.remove(keytime);
            }
            calendar.add(field, 1);
        }
        for (Map.Entry<String, T> entry : map.entrySet()) {
            nlist.add(entry.getValue());
        }
        return nlist;
    }

    /**
     * ��listתΪmap �������
     *
     * @param list ���ݿ���Ч�����б�
     * @return map
     */
    private <T extends PeriodValue> Map<String, T> tomap(List<T> list) {
        Map<String, T> map = new LinkedHashMap<>();
        for (T value : list) {
            map.put(value.getDate(), value);
        }
        return map;
    }

    /**
     * �� int shopId ת�� string idshop
     *
     * @param shopId  ��վID
     * @param subshop ����Ŀ
     * @return idshop
     */
    private String getIdSite(int shopId, String subshop) {
        if (AfStringUtil.isNotEmpty(subshop)) {
            String format = "%d AND idsubshop='%s'";
            return String.format(format, shopId, subshop);
        }
        return String.valueOf(shopId);
    }
}
