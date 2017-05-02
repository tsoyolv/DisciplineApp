package com.olts.discipline.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olts.react.Comment;
import com.olts.react.React;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * @author Benjamin Winterberg
 */
@Controller
public class ReactMainController {

    private CommentService service;

    private React react;

    private ObjectMapper mapper;

    @Autowired
    public ReactMainController(CommentService service) {
        this.service = service;
        this.react = new React();
        this.mapper = new ObjectMapper();
    }

    @RequestMapping("/react")
    public String index(Map<String, Object> model) throws Exception {
        List<Comment> comments = service.getComments();
        String commentBox = react.renderCommentBox(comments);
        String data = mapper.writeValueAsString(comments);
        model.put("content", commentBox);
        model.put("data", data);
        return "react_example";
    }

}
