package com.giants3.lanvideo.server.service;

import com.giants3.lanvideo.data.Movie;
import com.giants3.lanvideo.data.MovieGroup;
import com.giants3.lanvideo.data.RemoteData;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidleen29 on 2018/10/24.
 */
@Service
public class MovieService extends AbstractService {


    public static final String API_MOVIE_DOWNLOAD = "api/movie/download/";

    public List<Movie> list(String category) {
        String categoryPath = "";

        for (int i = 0; i < categoryNames.length; i++) {

            if (categoryNames[i].equalsIgnoreCase(category)) {
                categoryPath = categoryPaths[i];
            }

        }
        if (StringUtils.isEmpty(categoryPath)) return new ArrayList<>();


        File directory = new File(categoryPath);

        //遍历查找所有的视频文件。

        List<Movie> movies = new ArrayList<>();
        readAllMovie(directory, movies);


        for (Movie movie : movies) {

            final String originPath = movie.getVideoUrl();
            String restPath = originPath.substring(categoryPath.length());

            final String replace = API_MOVIE_DOWNLOAD + category + "/" + encode(restPath.replace(String.valueOf(File.separatorChar), FILE_SEPARATOR));
            movie.setVideoUrl(replace);
        }


        return movies;
    }

    private String encode(String path) {

        try {
            return URLEncoder.encode(path, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {

            return path;
        }
    }

    public void readAllMovie(File directory, List<Movie> movies) {

        for (File file : directory.listFiles()) {
            if (file.isDirectory()) readAllMovie(file, movies);

            if (file.isHidden()) continue;
            final String filePath = file.getAbsolutePath().toLowerCase();

            if (filePath.endsWith(".mp4")) {  //|| filePath.endsWith(".mkv")

                Movie movie = new Movie();
                movie.setVideoUrl(file.getAbsolutePath());
                movie.setTitle(file.getName());
                movie.setCardImageUrl(file.getName());
                movies.add(movie);


            }


        }

    }


    public String getResourceFilePath(String category, String fileName) {


        String categoryPath = "";

        for (int i = 0; i < categoryNames.length; i++) {

            if (categoryNames[i].equals(category)) {
                categoryPath = categoryPaths[i];
            }

        }
        if (StringUtils.isEmpty(categoryPath)) return null;

        fileName = fileName.replace(FILE_SEPARATOR, File.separator);


        return categoryPath + File.separator + fileName;
    }


    public static final String FILE_SEPARATOR = "___";


    final String[] categoryPaths = {
            "I:\\movie\\漫威\\"
            ,"G:\\movie\\日剧\\我的恐怖妻子\\"
            , "F:\\movie\\"
            , "G:\\movie\\test\\"
            , "I:\\movie\\"

    };
    final String[] categoryNames = {

            "漫威系列"
            ,"日剧-我的恐怖妻子"
            , "Fmovie"
            , "GmovieTest"
            , "Imovie"

    };

    public RemoteData<String> getCategories() {


        return wrapArrayData(categoryNames);
    }



    public  List<MovieGroup> listGroups()
    {


        List<MovieGroup> groups=new ArrayList<>();

        for (String category:categoryNames)
        {


            MovieGroup movieGroup=new MovieGroup();
            movieGroup.title=category;
            movieGroup.movies=list(category);
            groups.add(movieGroup);
        }





        return groups;

    }

}
