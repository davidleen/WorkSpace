package  com.giants3.reader.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by davidleen29 on 2018/5/13.
 */
@Entity(name = "T_ComicChapterFile")
public class ComicChapterFile {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    public long chapterId;
    public long bookId;
    public int  itemIndex;
    public String url;
    public int width;
    public int height;

}
