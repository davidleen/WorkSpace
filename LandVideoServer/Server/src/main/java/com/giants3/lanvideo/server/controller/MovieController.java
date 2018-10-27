package com.giants3.lanvideo.server.controller;

import com.giants3.lanvideo.data.Movie;

import com.giants3.lanvideo.data.RemoteData;

import com.giants3.lanvideo.server.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;

/**
 * Created by Administrator on 2014/9/18.
 */
@Controller
@RequestMapping("/movie")
public class MovieController extends BaseController {




    @Autowired
    private MovieService movieService;




    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Movie> list( @RequestParam(value = "category", required = false, defaultValue = "") String category
    ) {


          return  movieService.list(category);
    }


    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<String> categories(
    ) {



        return movieService.getCategories();


    }




    @RequestMapping(value = "/download/{category}/{name:.+}", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource downloadCategoryFile(@PathVariable String category, @PathVariable(value = "name") String fileName ) {





        String fileAbsolutePath=movieService.getResourceFilePath(category,fileName);
        if(StringUtils.isEmpty(fileAbsolutePath)) return null;
        return new FileSystemResource(fileAbsolutePath);




    }




}
