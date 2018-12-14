package  com.giants3.reader.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by davidleen29 on 2018/5/14.
 */
@Entity
@DiscriminatorValue("2")
public class ComicBook  extends  Book{

    public String comicFilePath;
}
