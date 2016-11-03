package br.com.monitoramento.enums;

/**
 * Created by Marcolino on 02/11/2016.
 */
public enum PeriodEnum {

    MINUTO(1, "&minutes=1"),
    HORA(2, "&minutes=60&sum=60"),
    DIA(3, "&days=1&sum=daily"),
    SEMANA(4, "&days=7&sum=daily"),
    MES(5, "&days=30&sum=daily");

    private final Integer id;
    private final String param;

    PeriodEnum(Integer id, String param){
        this.id = id;
        this.param = param;
    }

    public Integer getId() {
        return id;
    }

    public String getParam() {
        return param;
    }

    public static PeriodEnum getById(Integer id) {
        for(PeriodEnum e : values()) {
            if(e.id.equals(id)) return e;
        }
        return null;
    }

}
