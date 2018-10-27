package com.giants3.reader.server.repository;
//

import com.giants3.reader.entity.ComicChapterFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *  漫画章节文件
* Created by davidleen29 on 2014/9/17.
*/
public interface ComicChapterFileRepository extends JpaRepository<ComicChapterFile,Long> {


//    List<ComicChapterFile> findByBookIdEqualsAndChapterIdEqualsOrderByIndexDesc(long bookId, long chapterId);
}
