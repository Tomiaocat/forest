package com.rymcu.vertical.web.api.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rymcu.vertical.core.result.GlobalResult;
import com.rymcu.vertical.core.result.GlobalResultGenerator;
import com.rymcu.vertical.dto.admin.TopicTagDTO;
import com.rymcu.vertical.dto.admin.UserRoleDTO;
import com.rymcu.vertical.entity.Role;
import com.rymcu.vertical.entity.Tag;
import com.rymcu.vertical.entity.Topic;
import com.rymcu.vertical.entity.User;
import com.rymcu.vertical.service.RoleService;
import com.rymcu.vertical.service.TagService;
import com.rymcu.vertical.service.TopicService;
import com.rymcu.vertical.service.UserService;
import com.rymcu.vertical.util.Utils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ronger
 * */
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;
    @Resource
    private TopicService topicService;
    @Resource
    private TagService tagService;

    @GetMapping("/users")
    public GlobalResult<Map<String, Object>> users(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows){
        PageHelper.startPage(page, rows);
        List<User> list = userService.findAll();
        PageInfo<User> pageInfo = new PageInfo<>(list);
        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("users", pageInfo.getList());
        Map<String, Number> pagination = new HashMap<>(3);
        pagination.put("pageSize",pageInfo.getPageSize());
        pagination.put("total",pageInfo.getTotal());
        pagination.put("currentPage",pageInfo.getPageNum());
        map.put("pagination", pagination);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/user/{idUser}/role")
    public GlobalResult<List<Role>> userRole(@PathVariable Integer idUser){
        List<Role> roles = roleService.findByIdUser(idUser);
        return GlobalResultGenerator.genSuccessResult(roles);
    }

    @GetMapping("/roles")
    public GlobalResult<Map<String, Object>> roles(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows){
        PageHelper.startPage(page, rows);
        List<Role> list = roleService.findAll();
        PageInfo<Role> pageInfo = new PageInfo<>(list);
        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("roles", pageInfo.getList());
        Map pagination = Utils.getPagination(pageInfo);
        map.put("pagination", pagination);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PatchMapping("/user/update-role")
    public GlobalResult<Map> updateUserRole(@RequestBody UserRoleDTO userRole){
        Map map = userService.updateUserRole(userRole.getIdUser(),userRole.getIdRole());
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PatchMapping("/user/update-status")
    public GlobalResult<Map> updateUserStatus(@RequestBody User user){
        Map map = userService.updateStatus(user.getIdUser(),user.getStatus());
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PatchMapping("/role/update-status")
    public GlobalResult<Map> updateRoleStatus(@RequestBody Role role){
        Map map = roleService.updateStatus(role.getIdRole(),role.getStatus());
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PostMapping("/role/post")
    public GlobalResult<Map> addRole(@RequestBody Role role){
        Map map = roleService.saveRole(role);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PutMapping("/role/post")
    public GlobalResult<Map> updateRole(@RequestBody Role role){
        Map map = roleService.saveRole(role);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/topics")
    public GlobalResult<Map<String, Object>> topics(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows){
        PageHelper.startPage(page, rows);
        List<Topic> list = topicService.findAll();
        PageInfo<Topic> pageInfo = new PageInfo<>(list);
        Map<String, Object> map = new HashMap<>(2);
        map.put("topics", pageInfo.getList());
        Map pagination = Utils.getPagination(pageInfo);
        map.put("pagination", pagination);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/topic/{topicUri}")
    public GlobalResult topic(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows,@PathVariable String topicUri){
        if (StringUtils.isBlank(topicUri)) {
            return GlobalResultGenerator.genErrorResult("数据异常!");
        }
        Map map = topicService.findTopicByTopicUri(topicUri,page,rows);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/topic/detail/{idTopic}")
    public GlobalResult<Topic> topicDetail(@PathVariable Integer idTopic){
        Topic topic = topicService.findById(idTopic.toString());
        return GlobalResultGenerator.genSuccessResult(topic);
    }

    @GetMapping("/topic/unbind-topic-tags")
    public GlobalResult unbindTopicTags(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows, HttpServletRequest request){
        Integer idTopic = Integer.valueOf(request.getParameter("idTopic"));
        String tagTitle = request.getParameter("tagTitle");
        PageHelper.startPage(page, rows);
        List<Tag> list = topicService.findUnbindTagsById(idTopic, tagTitle);
        PageInfo<Tag> pageInfo = new PageInfo<>(list);
        Map<String, Object> map = new HashMap<>(2);
        map.put("tags", pageInfo.getList());
        Map pagination = Utils.getPagination(pageInfo);
        map.put("pagination", pagination);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PostMapping("/topic/bind-topic-tag")
    public GlobalResult bindTopicTag(@RequestBody TopicTagDTO topicTag){
        Map map = topicService.bindTopicTag(topicTag);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @DeleteMapping("/topic/unbind-topic-tag")
    public GlobalResult unbindTopicTag(@RequestBody TopicTagDTO topicTag){
        Map map = topicService.unbindTopicTag(topicTag);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PostMapping("/topic/post")
    public GlobalResult<Map> addTopic(@RequestBody Topic topic){
        Map map = topicService.saveTopic(topic);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PutMapping("/topic/post")
    public GlobalResult<Map> updateTopic(@RequestBody Topic topic){
        Map map = topicService.saveTopic(topic);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/tags")
    public GlobalResult<Map<String, Object>> tags(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows){
        PageHelper.startPage(page, rows);
        List<Tag> list = tagService.findAll();
        PageInfo<Tag> pageInfo = new PageInfo<>(list);
        Map<String, Object> map = new HashMap<>(2);
        map.put("tags", pageInfo.getList());
        Map pagination = Utils.getPagination(pageInfo);
        map.put("pagination", pagination);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @DeleteMapping("/tag/clean-unused")
    public GlobalResult<Map<String, Object>> cleanUnusedTag(){
        Map map = tagService.cleanUnusedTag();
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/tag/detail/{idTag}")
    public GlobalResult<Tag> tagDetail(@PathVariable Integer idTag){
        Tag tag = tagService.findById(idTag.toString());
        return GlobalResultGenerator.genSuccessResult(tag);
    }

    @PostMapping("/tag/post")
    public GlobalResult<Map> addTag(@RequestBody Tag tag){
        Map map = tagService.saveTag(tag);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PutMapping("/tag/post")
    public GlobalResult<Map> updateTag(@RequestBody Tag tag){
        Map map = tagService.saveTag(tag);
        return GlobalResultGenerator.genSuccessResult(map);
    }

}
