package sample.subproject.hibernate.models;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "test", schema = "main", catalog = "")
public class TestEntity
{
    private Integer id;
    private Object title;

    @Basic
    @Column(name = "id")
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    @Basic
    @Column(name = "title")
    public Object getTitle()
    {
        return title;
    }

    public void setTitle(Object title)
    {
        this.title = title;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestEntity that = (TestEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }
}
