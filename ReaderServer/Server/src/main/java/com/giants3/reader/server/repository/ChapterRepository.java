package com.giants3.reader.server.repository;
//

import com.giants3.reader.entity.Book;
import com.giants3.reader.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 用户
* Created by davidleen29 on 2014/9/17.
*/
public interface ChapterRepository<T extends Chapter> extends JpaRepository<T ,Long> {


    List<T> findByNameLike(String name);
    List<T> findByBookIdEquals(long bookId);


}
