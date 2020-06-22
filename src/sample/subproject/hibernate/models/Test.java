package sample.subproject.hibernate.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Test
{
    @Id
    Integer id;
    @Column
    String title;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    @Override
    public String toString()
    {
        return "ID=" + id + " title=" + title;
    }
}
