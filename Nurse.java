package com.example.moyowanga;
import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.Map;

public class Nurse {
    public String showNurseDate;
    public String showNurseName;
    public String showNurseNotes;

    public Nurse(){

    }



    public Nurse( String showNurseDate,String showNurseName, String showNurseNotes) {

        this.showNurseDate=showNurseDate;
        this.showNurseName = showNurseName;
        this.showNurseNotes = showNurseNotes;
    }

    public String getShowNurseDate() {
        return showNurseDate;
    }



    public String getShowNurseName() {
        return showNurseName;
    }


    public String getShowNurseNotes() {
        return showNurseNotes;
    }


}
