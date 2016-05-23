package org.openmrs.module.smsreminder.modelo;

import org.openmrs.BaseOpenmrsData;

import java.io.Serializable;
import java.sql.Time;

/**
 * Created by nelson.mahumane on 03-06-2015.
 * Esta class representa a entidade responsavel pela configuração do dispositivo de envio de SMS
 * é nesta entidade onde são definidas as sms,a porta usada pelo moodem o priodo do envio de sms se o agendamento for automatico.
 */
public class Device extends BaseOpenmrsData implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer deviceId;
    private String port;
    private  String schedule;
    private String smsCenter;
    private String text;

    public Device() {
    }


    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getId() {
        return getDeviceId();
    }

    public void setId(Integer id) {
        setDeviceId(id);
    }


    public String getSmsCenter() {
        return smsCenter;
    }

    public void setSmsCenter(String smsCenter) {
        this.smsCenter = smsCenter;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
