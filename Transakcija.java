package isplata;

public class Transakcija {
    
    private Double suma;
    private Integer dan;
    private Integer mesec;
    private Integer godina;

    public Transakcija(Double suma, Integer dan, Integer mesec, Integer godina) {
        this.suma = suma;
        this.dan = dan;
        this.mesec = mesec;
        this.godina = godina;
    
    }


    @Override
    public String toString() {
        return getSuma() + " " + getDan() + "." + getMesec() + "." + getGodina() + "." ;
    }
    
    

    public Double getSuma() {
        return suma;
    }

    public void setSuma(Double suma) {
        this.suma = suma;
    }

    public Integer getDan() {
        return dan;
    }

    public void setDan(Integer dan) {
        this.dan = dan;
    }

    public Integer getMesec() {
        return mesec;
    }

    public void setMesec(Integer mesec) {
        this.mesec = mesec;
    }

    public Integer getGodina() {
        return godina;
    }

    public void setGodina(Integer godina) {
        this.godina = godina;
    }  
}
