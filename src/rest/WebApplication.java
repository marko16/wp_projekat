package rest;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import controller.UserController;
import dao.AdminDAO;
import dao.CustomerDAO;
import dao.SalesmanDAO;
import spark.Request;
import spark.Session;
import java.io.File;
import java.util.ArrayList;

import com.google.gson.Gson;

public class WebApplication {

    private static Gson gson = new Gson();
    private static UserController uc = new UserController();

    private static CustomerDAO customerDAO = new CustomerDAO();
    private static SalesmanDAO salesmanDAO = new SalesmanDAO();
    private static AdminDAO adminDAO = new AdminDAO();

    public static void main(String[] args) throws Exception {
        port(8080);

        staticFiles.externalLocation(new File("./static").getCanonicalPath());

        get("/test", (req, res) -> "Works");

        post("/auth/login", uc.login());

        post("/login", (req, res)-> {
            String uname  = req.queryParams("username");
            String lozinka = req.queryParams("password");

            String username = " ";
            ArrayList<String> response = new ArrayList<String>();
            if(customerDAO.findOne(uname, lozinka) != null) {
                if(!customerDAO.findOne(uname,lozinka).isBlocked()) {
                    username = uname;
                    response.add(username);
                    response.add("customer");
                }else {
                    username = "blocked";
                    response.add(username);
                }
            }
            else
            {
                if(adminDAO.findOne(uname, lozinka) != null) {
                    username = uname;
                    response.add(username);
                    response.add("admin");
                }else {
                    if(salesmanDAO.findOne(uname, lozinka) != null) {
                        if(!salesmanDAO.findOne(uname, lozinka).isBlocked()) {
                            username = uname;
                            response.add(username);
                            response.add("salesman");
                        }else {
                            username = "blocked";
                            response.add(username);
                        }
                    }else {
                        response.add(username);

                    }

                }
            }
            return gson.toJson(response);

        });
    }

}
