package com.giants3.hd.server.controller;

import com.giants3.hd.entity.User;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.BufferData;
import com.giants3.hd.noEntity.MessageInfo;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.service.*;
import com.giants3.hd.server.utils.Constraints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
    private BufferDataService bufferDataService;


    @Autowired
    private UserService userService;


    @Autowired
    GlobalDataService globalDataService;


    @Autowired
    private ErpService erpService;

    @Autowired
    private ErpWorkService erpWorkService;


    @RequestMapping("/delete/{userId}")
    public String deleteUser(@PathVariable("userId") Long userId) {


        userService.delete(userId);
        return "redirect:/";
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addUser(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, BindingResult result) {


        userService.save(user);
        return "redirect:/";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<User> list(

            @RequestParam(value = "type", required = false, defaultValue = "-1") int userType
    ) {

        if (userType == -1)

            return wrapData(userService.list());

        return wrapData(userService.getSalesman());
    }


    @RequestMapping(value = "/saveList", method = RequestMethod.POST)

    public
    @ResponseBody
    RemoteData<User> saveList(@RequestBody List<User> users) {


        try {
            ;
            return wrapData(userService.saveUserList(users));
        } catch (HdException e) {
            return wrapError(e.getMessage());
        }

    }


    @RequestMapping(value = "/initData", method = RequestMethod.POST)

    public
    @ResponseBody
    RemoteData<BufferData> initData(@RequestBody User user) {


        return getInitData(user.id);


    }

    /**
     * 提供移动端  省略很多数据
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getAppInitData", method = RequestMethod.GET)

    public
    @ResponseBody
    RemoteData<BufferData> getAppInitData(@RequestParam(value = "userId") long userId) {


        return bufferDataService.getConfigData(userId, false);

    }


    /**
     * 提供移动端  省略很多数据
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getInitData", method = RequestMethod.GET)

    public
    @ResponseBody
    RemoteData<BufferData> getInitData(@RequestParam(value = "userId") long userId) {


        return bufferDataService.getConfigData(userId, true);

    }


    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)

    public
    @ResponseBody
    RemoteData<Void> updatePassword(@RequestBody String[] map) {


        return userService.updatePassword(map);

    }

    @RequestMapping(value = "/updatePassword2", method = RequestMethod.GET)

    public
    @ResponseBody
    RemoteData<Void> updatePassword2(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user,
                                     @RequestParam(value = "oldPassword") String oldPassword, @RequestParam(value = "newPassword") String newPassword)

    {

        return userService.updatePassword(user, oldPassword, newPassword);
    }


    @RequestMapping(value = "/newMessage", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<MessageInfo> newMessage(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user) {


        int count = erpService.getUnHandleWorkFlowMessageCount(user);

        MessageInfo messageInfo = new MessageInfo();
        messageInfo.newWorkFlowMessageCount = count;
         messageInfo.unCompletedMonitoredWorkFlowCount=erpWorkService.getUnCompletedMonitoredWorkFlowCount(user);


        return wrapData(messageInfo);

    }

}
