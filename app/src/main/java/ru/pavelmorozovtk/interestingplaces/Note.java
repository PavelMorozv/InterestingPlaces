package ru.pavelmorozovtk.interestingplaces;

public class Note {
    private int _id;
    private String _title;
    private String _description;
    private String _point;

    public Note(){

    }
    public Note(int id, String title, String description, String point){
        _id = id;
        _title = title;
        _description = description;
        _point = point;
    }

    public Note(String title, String description, String point)
    {
        _title = title;
        _description = description;
        _point = point;
    }

    public void setId(int id) { _id = id; }
    public void setTitle(String title) { _title = title; }
    public void setDescription(String description) { _description = description; }
    public void setPoint(String point) { _point = point; }

    public int getid() { return _id; }
    public String getTitle() { return _title; }
    public String getDescription() { return _description ; }
    public String getPoint() { return _point; }
    public double[] getPointDoubleArr() {
        String[] temp = _point.split("|");
        return new double[]{Double.parseDouble(temp[0]), Double.parseDouble(temp[1])};
    }
}
