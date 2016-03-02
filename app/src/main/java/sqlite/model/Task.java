package sqlite.model;

/**
 * Created by jeanlee on 2015/1/14.
 */

public class Task {
    private String taskName;
    private int status;
    private long id;
    private String do_date;

    public Task()
    {
        this.taskName=null;
        this.status=0;
    }
    public Task(String taskName, int status,String do_date) {
        super();
        this.taskName = taskName;
        this.status = status;
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
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public void setDateAt(String do_date){
        this.do_date=do_date;
    }
    public String getDateAt(){return do_date;}

}
