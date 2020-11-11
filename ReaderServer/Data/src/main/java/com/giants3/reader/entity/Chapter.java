package  com.giants3.reader.entity;

import javax.persistence.*;

/**
 * Created by davidleen29 on 2018/5/12.
 */
@Entity(name="T_Chapter")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type,chapterIndex", discriminatorType=DiscriminatorType.INTEGER)
@DiscriminatorValue("0")
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;


    public String name;

    public long bookId;


    public String bookName;

    public int chapterIndex;
    public String url;


    public long fileSize;

    public int volumeIndex;
    public String volumeName;



}
