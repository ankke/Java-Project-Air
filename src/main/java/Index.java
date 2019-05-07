public class Index {
    private int id;
    private String stCalcDate;
    private stIndexLevel stIndexLevel;

    public stIndexLevel getStIndexLevel() {
        return stIndexLevel;
    }

    public void setstIndexLevel(stIndexLevel stIndexLevel) {
        this.stIndexLevel = stIndexLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getstCalcDate() {
        return stCalcDate;
    }

    public void setStCalcDate(String stCalcDate) {
        this.stCalcDate = stCalcDate;
    }
}
