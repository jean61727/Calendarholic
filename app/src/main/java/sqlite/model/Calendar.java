package sqlite.model;

/**
 * Created by jeanlee on 2015/1/20.
 */
public class Calendar {
    private String taskName;
    private String time;
    private long id;
    private String do_date;

    public Calendar()
    {
        this.taskName=null;
        this.time=null;
    }
    public Calendar(String taskName, String time,String do_date) {
        super();
        this.taskName = taskName;
        this.time=time;
        this.do_date=do_date;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getTaskName() {
        return taskName;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public void setDateAt(String do_date){
        this.do_date=do_date;
    }
    public String getDateAt(){return do_date;}

}