package controller;

import com.google.gson.Gson;
import model.Comment;
import model.Customer;
import model.User;
import spark.Request;
import spark.Response;
import spark.Route;

public class UserController {

    private Gson gson = new Gson();


    public Route login() {
        Comment comment = new Comment();
        comment.setText("jeje");
        comment.setRating((short) 2);
        return (request, response) -> {
            response.type("application/json");
            response.body(gson.toJson(comment));
            System.out.println(gson.toJson(comment));
            return gson.toJson(new Object() {
                final String role = "USER"; final boolean active = true;});
        };
    }

}
