package rest;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;
import static spark.route.HttpMethod.after;

import com.google.gson.GsonBuilder;
import controller.UserController;
import dao.*;
import dto.EventDTO;
import model.*;
import spark.Filter;
import spark.Request;
import spark.Session;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Base64;

import com.google.gson.Gson;

import javax.imageio.ImageIO;

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
                } else {
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

            if(event.getAvailableTickets() < Integer.parseInt(amount)) {
                return false;
            }

            ArrayList<String> tickets = ticketDAO.createOrder(username, eventId, amount, ticketType, event.getRegularPrice());
            customerDAO.addPoints(tickets, ticketType, amount, event.getRegularPrice(), username);
            eventDAO.adjustCapacity(Integer.parseInt(amount), event.getId());
            return true;
        });

        post("/registration", (req, res) -> {
            Gson gsonReg = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

            Customer customer = gsonReg.fromJson(req.body(), Customer.class);
            customerDAO.addCustomer(customer);
            return true;
        });

        post("/registrationSalesman", (req, res) -> {
            Gson gsonReg = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

            Salesman salesman = gsonReg.fromJson(req.body(), Salesman.class);
            salesmanDAO.addSalesman(salesman);
            return true;
        });

        get("/getUser", (req, res) -> {
            String username = req.queryParams("username");
            Customer customer = customerDAO.findOne(username);
            if(customer != null) return gson.toJson(customer);
            Salesman salesman = salesmanDAO.findOne(username);
            if(salesman != null) return gson.toJson(salesman);

            return gson.toJson(adminDAO.findOne(username));
        });

        post("/changePassword", (req, res) -> {
            String username = req.queryParams("username");
            String newPassword = req.queryParams("password");
            Customer customer = customerDAO.findOne(username);
            if(customer != null) {
                customer.setPassword(newPassword);

                return true;
            }
            Salesman salesman = salesmanDAO.findOne(username);
            if(salesman != null) {
                salesman.setPassword(newPassword);
                return true;
            }
            Admin admin = adminDAO.findOne(username);
            if(admin != null) {
                System.out.println("usao");
                admin.setPassword(newPassword);
                adminDAO.writeAll();
                return true;
            }
            return false;
        });

        post("/addEvent", (req, res) -> {
            Gson gsonReq = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm").create();
            EventDTO eventDTO = gsonReq.fromJson(req.body(), EventDTO.class);

            Location location = eventDTO.getLocation();
            location.setId(locationDAO.nextId());

            Event event = eventDTO.getEvent();
            event.setId(eventDAO.nextId());
            event.setLocation(location.getId());
            String poster = "";
            boolean posterChosen = true;

            if(event.getPoster() == null) {
                posterChosen = false;
                event.setPoster("images/e1.jfif");
            }
            else {
                poster = event.getPoster().split(",")[1];
            }
            byte[] imageByte = Base64.getDecoder().decode(poster);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            BufferedImage image = ImageIO.read(bis);
            bis.close();
            String posterName = "e" + event.getId() + ".png";

            if(eventDAO.isLocationAvailable(event, location)) {
                if (posterChosen) {
                    File outputFile = new File(System.getProperty("user.dir") + "\\static\\images" + posterName);
                    ImageIO.write(image, "png", outputFile);
                    event.setPoster("images/" + posterName);
                }
                event.setActive(true);
                eventDAO.add(event);
            }
            else {
                return false;
            }

            locationDAO.add(location);
            salesmanDAO.addEventToSalesman(event);
            return true;
        });
    }

}
