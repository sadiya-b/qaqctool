package corecentra.qaqctool.fields;

import javax.persistence.*;

@Entity
@Table(name = "taskfields")
public class TaskFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String fieldname;

    @Column(nullable = true, unique = false, length = 50)
    private String fieldtype;

    @Column(nullable = true, unique = false)
    private Long fieldlimit;

    //region - getters setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFieldname() {
        return fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    public String getFieldtype() {
        return fieldtype;
    }

    public void setFieldtype(String fieldtype) {
        this.fieldtype = fieldtype;
    }

    public Long getFieldlimit() {
        if(fieldlimit == null){
            setFieldlimit(0L);
        }
        return fieldlimit;
    }

    public void setFieldlimit(Long fieldlimit) {
        this.fieldlimit = fieldlimit==null?0L:fieldlimit;
    }
    //endregion
}
