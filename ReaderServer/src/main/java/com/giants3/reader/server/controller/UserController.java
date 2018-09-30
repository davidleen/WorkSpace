package com.giants3.reader.server.controller;

import com.giants3.reader.entity.User;
import com.giants3.reader.exception.HdException;
import com.giants3.reader.noEntity.RemoteData;
import com.giants3.reader.server.repository.UserRepository;
import com.giants3.reader.server.service.UserService;
import com.giants3.reader.server.utils.Constraints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Administrator on 2014/9/18.
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private UserService userService;


    @RequestMapping("/delete/{userId}")
    public String deleteUser(@PathVariable("userId") Long userId) {

        userRepository.delete(userRepository.findOne(userId));

        return "redirect:/";
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addUser(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, BindingResult result) {

        userRepository.save(user);

        return "redirect:/";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<User> list(

            @RequestParam(value = "type", required = false, defaultValue = "-1") int userType
    ) {

        if (userType == -1)

            return wrapData(userService.findAll());

        return wrapData(userService.getSalesman());
    }


    @RequestMapping(value = "/addOne", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<User> addOne(


    ) {

        try {
            userService.addOne();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return wrapData();


    }


    @RequestMapping(value = "/saveList", method = RequestMethod.POST)

    public
    @ResponseBody
    RemoteData<User> saveList(@RequestBody List<User> users) {


        try {
            userService.saveUserList(users);
            return wrapData(userService.list());
        } catch (HdException e) {
            return wrapError(e.getMessage());
        }

    }


}
