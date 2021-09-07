package rest;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import controller.UserController;
import dao.*;
import model.Customer;
import model.Event;
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
    private static LocationDAO locationDAO = new LocationDAO();
    private static EventDAO eventDAO = new EventDAO();
    private static TicketDAO ticketDAO = new TicketDAO();

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
            if(customerDAO.login(uname, lozinka) != null) {
                if(!customerDAO.login(uname,lozinka).isBlocked()) {
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
                if(adminDAO.login(uname, lozinka) != null) {
                    username = uname;
                    response.add(username);
                    response.add("admin");
                }else {
                    if(salesmanDAO.login(uname, lozinka) != null) {
                        if(!salesmanDAO.login(uname, lozinka).isBlocked()) {
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

        get("/events", (req, res) -> {
           return gson.toJson(eventDAO.getAvailableEvents());
        });

        get("/event", (req, res) -> {
            return gson.toJson(eventDAO.findOne(Integer.parseInt(req.queryParams("id"))));
        });

        post("/reserve", (req, res) -> {
            String username = req.queryParams("username");
            String eventId = req.queryParams("eventId");
            String amount = req.queryParams("amount");
            String ticketType = req.queryParams("ticketType");

            Customer customer = customerDAO.findOne(username);
            Event event = eventDAO.findOne(Integer.parseInt(eventId));

            ArrayList<String> tickets = ticketDAO.createOrder(username, eventId, amount, ticketType, event.getRegularPrice());
            customerDAO.addPoints(tickets, ticketType, amount, event.getRegularPrice(), username);
            return true;
        });
    }

}
