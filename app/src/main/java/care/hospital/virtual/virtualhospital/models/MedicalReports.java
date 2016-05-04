package care.hospital.virtual.virtualhospital.models;

import java.io.File;

/**
 * Created by semsem on 23/04/16.
 */
public class MedicalReports {
    private int id;
    private String name;
    private File report;

    public MedicalReports(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getReport() {
        return report;
    }

    public void setReport(File report) {
        this.report = report;
    }

    public String toString(){
        return getName();
    }
}
