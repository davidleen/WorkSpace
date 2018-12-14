package com.giants3.reader.server.repository_second;
//

import com.giants3.reader.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 用户
 * Created by davidleen29 on 2014/9/17.
 */
public interface BookRepository<T extends Book> extends JpaRepository<T, Long> {


    List<T> findByNameLike(String name);


}
