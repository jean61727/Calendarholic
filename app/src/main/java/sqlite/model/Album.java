package sqlite.model;

/**
 * Created by jeanlee on 2015/1/20.
 */
public class Album{
    private String descrip;
    private String title;
    private long id;
    private String do_date;
    private byte[] photo;
    public Album()
    {
        this.descrip=null;
        this.title=null;
    }
    public Album(String title, String descrip, String do_date) {
        super();
        this.descrip = descrip;
        this.title = title;
        this.do_date=do_date;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title= title;
    }
    public String getDescrip() {
        return descrip;
    }
    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }
    public void setDateAt(String do_date){
        this.do_date=do_date;
    }
    public String getDateAt(){
        return do_date;
    }
    public void setPhoto(byte[] photo){
        this.photo=photo;
    }
    public byte[] getPhoto(){
        return photo;
    }
}