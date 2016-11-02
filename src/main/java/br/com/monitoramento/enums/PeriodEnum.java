package br.com.monitoramento.enums;

/**
 * Created by Marcolino on 02/11/2016.
 */
public enum PeriodEnum {

    MINUTO(1, "minuto"),
    HORA(2, "hora"),
    DIA(3, "dia"),
    SEMANA(4, "semana"),
    MES(5, "mes");

    private final Integer id;
    private final String desc;

    PeriodEnum(Integer id, String desc){
        this.id = id;
        this.desc = desc;
    }

    public Integer getId() {
        return id;
    }

    public java.lang.String getDesc() {
        return desc;
    }

}
