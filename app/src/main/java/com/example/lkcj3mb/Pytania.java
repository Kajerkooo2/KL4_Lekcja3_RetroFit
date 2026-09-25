package com.example.lkcj3mb;

public class Pytania {
    private String tresc;
    private String odpA;
    private String odpB;
    private String odpC;
    private int poprawna;

    public Pytania(String tresc, String odpA, String odpB, String odpC, int poprawna) {
        this.tresc = tresc;
        this.odpA = odpA;
        this.odpB = odpB;
        this.odpC = odpC;
        this.poprawna = poprawna;
    }

    public String getTresc() { return tresc; }
    public String getOdpA() { return odpA; }
    public String getOdpB() { return odpB; }
    public String getOdpC() { return odpC; }
    public int getPoprawna() { return poprawna; }
}
