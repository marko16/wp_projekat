package rest;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import controller.UserController;
import spark.Request;
import spark.Session;
import java.io.File;

import com.google.gson.Gson;

public class WebApplication {

    private static Gson gson = new Gson();
    private static UserController uc = new UserController();
    public static void main(String[] args) throws Exception {
        port(8080);

        staticFiles.externalLocation(new File("./static").getCanonicalPath());

        get("/test", (req, res) -> "Works");

        post("/auth/login", uc.login());
    }

}
