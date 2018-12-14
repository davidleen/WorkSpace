package com.giants3.reader.server.controller;

import com.giants3.reader.entity.Book;
import com.giants3.reader.entity.ComicBook;
import com.giants3.reader.noEntity.ComicChapterInfo;
import com.giants3.reader.noEntity.RemoteData;
import com.giants3.reader.server.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2014/9/18.
 */
@Controller
@RequestMapping("/book")
public class BookController extends BaseController {


    @Autowired
    private BookService bookService;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Book> list(
    ) {


        return bookService.list();
    }


    @RequestMapping(value = "/listComic", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<ComicBook> listComic(
    ) {


        return bookService.listComic();
    }


    @RequestMapping(value = "/findComicChapters", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<ComicChapterInfo> findComicChapters(@RequestParam(value = "bookId", required = false, defaultValue = "-1") long bookId
    ) {
        return bookService.findComicChapters(bookId);

    }


}
