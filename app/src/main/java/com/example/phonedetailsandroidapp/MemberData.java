package com.example.phonedetailsandroidapp;


import android.widget.Toast;

public class MemberData {

    private String fbphdevid;
    private Integer fbphcharglevel;
    private String fbphchrgestatus;
    private String fbtimestamp;
    private String fbcurrdate;


    public MemberData()
    {


    }
    public String getfbphdevid() {
        return fbphdevid;
    }

    public void setfbphdevid(String devid) {
        fbphdevid = devid;
    }

    public String getfbphchrgestatus() {
        return fbphchrgestatus;
    }

    public void setfbphchrgestatus(String chargestatus) {
        fbphchrgestatus = chargestatus;
    }

    public Integer getfbphcharglevel() {
        return fbphcharglevel;
    }

    public void setfbphcharglevel(Integer chargelev) {
        fbphcharglevel = chargelev;
    }




    public String getfbtimestamp() {
        return fbtimestamp;
    }

    public void setfbtimestamp(String timestamp) {
        fbtimestamp =timestamp;
    }

    public String getfbcurrdate() {
        return fbcurrdate;
    }

    public void setfbcurrdate(String currdate) { fbcurrdate=currdate;}

}

