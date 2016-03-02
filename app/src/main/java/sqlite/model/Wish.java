package sqlite.model;

/**
 * Created by jeanlee on 2015/1/14.
 */

public class Wish {
    private String wishName;
    private int status;
    private long id;

    public Wish()
    {
        this.wishName=null;
        this.status=0;
    }
    public Wish(String taskName, int status) {
        super();
        this.wishName = taskName;
        this.status = status;

    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getWishName() {
        return wishName;
    }
    public void setWishName(String wishName) {
        this.wishName = wishName;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

}