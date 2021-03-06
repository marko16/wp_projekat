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
import dto.TicketDTO;
import dto.UserDTO;
import model.*;
import spark.Filter;
import spark.QueryParamsMap;
import spark.Request;
import spark.Session;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.google.gson.Gson;

import javax.imageio.ImageIO;

public class WebApplication {

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
    private static UserController uc = new UserController();

    private static LocationDAO locationDAO = new LocationDAO();
    private static CustomerDAO customerDAO = new CustomerDAO();
    private static SalesmanDAO salesmanDAO = new SalesmanDAO();
    private static AdminDAO adminDAO = new AdminDAO();
    private static EventDAO eventDAO = new EventDAO();
    private static TicketDAO ticketDAO = new TicketDAO();
    private static CommentDAO commentDAO = new CommentDAO();

    public static void main(String[] args) throws Exception {
        port(8080);

        staticFiles.externalLocation(new File("./static").getCanonicalPath());

        get("/test", (req, res) -> "Works");

        get("/profile", (req, res) -> {
            String username = req.queryParams("username");

            Admin admin = adminDAO.findOne(username);
            if(admin != null) {
                return gson.toJson(admin);
            }
            Salesman salesman = salesmanDAO.findOne(username);
            if(salesman != null) {
                return gson.toJson(salesman);
            }
            Customer customer = customerDAO.findOne(username);
            if(customer != null) {
                return gson.toJson(customer);
            }
            return null;
        });

        post("/editProfile", (req, res) -> {
            String role = req.queryParams("role");
            String username = req.queryParams("username");
            String firstName = req.queryParams("firstName");
            String lastName = req.queryParams("lastName");
            String birthday = req.queryParams("birthday");
            String gender = req.queryParams("gender");



            if(role.equals("admin")) {
                return adminDAO.editProfile(username, firstName, lastName, birthday, gender);
            }
            if(role.equals("salesman")) {
                return salesmanDAO.editProfile(username, firstName, lastName, birthday, gender);
            }
            if(role.equals("customer")) {
                return customerDAO.editProfile(username, firstName, lastName, birthday, gender);
            }
            return false;
        });

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
                } else {
                    if(salesmanDAO.login(uname, lozinka) != null) {
                        if(!salesmanDAO.login(uname, lozinka).isBlocked()) {
                            username = uname;
                            response.add(username);
                            response.add("salesman");
                        } else {
                            username = "blocked";
                            response.add(username);
                        }
                    } else {
                        response.add(username);

                    }
                }
            }
            return gson.toJson(response);

        });

        get("/users", (req, res) -> {
            ArrayList<UserDTO> users = salesmanDAO.getUsersAdmin();
            users.addAll(customerDAO.getUsersAdmin());
            return gson.toJson(users);
        });

        get("/events", (req, res) -> gson.toJson(eventDAO.getAvailableEvents()));

        get("/eventAdmin", (req, res) -> gson.toJson(eventDAO.getAdminEvents()));

        post("/activateEvent", (req, res) -> {
            int id = Integer.parseInt(req.queryParams("event"));
            return eventDAO.activate(id);
        });

        get("/eventSalesman", (req, res) -> {
            String salesman = req.queryParams("salesman");
            return gson.toJson(eventDAO.getAvailableEventsForSalesman(salesman));
        });

        get("/event", (req, res) -> {
            return gson.toJson(eventDAO.findOne(Integer.parseInt(req.queryParams("id"))));
        });

        get("/ticketsSalesman", (req, res) -> {
            String usernameSalesman = req.queryParams("salesman");
            HashMap<Integer, Event> events = eventDAO.loadAll();
            ArrayList<TicketDTO> ticketsDTO = ticketDAO.getTicketsOfSalesmanEvents(usernameSalesman, events);
            return gson.toJson(ticketsDTO);
        });

        get("/ticketsAdmin", (req, res) -> {
            String usernameSalesman = req.queryParams("admin");
            HashMap<Integer, Event> events = eventDAO.loadAll();
            ArrayList<TicketDTO> ticketsDTO = ticketDAO.getTickets(events);
            return gson.toJson(ticketsDTO);
        });

        post("/deleteTicket", (req, res) -> {
            String ticketId = req.queryParams("ticket");
            return ticketDAO.delete(ticketId);
        });

        post("/deleteEvent", (req, res) -> {
            int id = Integer.parseInt(req.queryParams("event"));
            return eventDAO.delete(id);
        });

        post("/block", (req, res) -> {
            String role = req.queryParams("role");
            String username = req.queryParams("username");
            if(role.equals("CUSTOMER")) {
                return customerDAO.block(username);
            } else if(role.equals("SALESMAN")) {
                return salesmanDAO.block(username);
            } else return false;
        });

        post("/unblock", (req, res) -> {
            String role = req.queryParams("role");
            String username = req.queryParams("username");
            if(role.equals("CUSTOMER")) {
                return customerDAO.unblock(username);
            } else if(role.equals("SALESMAN")) {
                return salesmanDAO.unblock(username);
            } else return false;
        });

        get("/ticketsUser", (req, res) -> {
            String customerUsername = req.queryParams("customer");
            HashMap<Integer, Event> events = eventDAO.loadAll();
            ArrayList<TicketDTO> ticketsDTO = ticketDAO.getTicketsOfCustomer(customerUsername, events);
            return gson.toJson(ticketsDTO);
        });

        post("/cancelReservation", (req, res) -> {
            String id = req.queryParams("id");
            if(id == null) return "No event has been selected!";

            Ticket ticket = ticketDAO.findOne(id);
            if(!ticket.isReserved()) {
                return "Reservation already canceled!";
            }

            Event event = eventDAO.findOne(ticket.getEvent());

            Date date = new Date();
            DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, +7);
            Date toDate1 = cal.getTime();

            if(event.getStartTime().before(toDate1)) {
                return "Cannot cancel reservation of event that is 7 days due or passed!";
            }

            ticketDAO.cancelReservation(id);
            return "You have successfully canceled this reservation!";
        });

        post("/editEvent", (req, res) -> {
            Gson gsonReq = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm").create();
            EventDTO eventDTO = gsonReq.fromJson(req.body(), EventDTO.class);

            Location location = eventDTO.getLocation();
            location.setId(locationDAO.nextId());

            Event event = eventDTO.getEvent();
            event.setId(eventDAO.nextId());
            event.setLocation(location);
            String poster = "";
            boolean posterChosen = true;

            if(event.getStartTime() == null) {
                Event e = eventDAO.findOne(event.getId());
                event.setStartTime(e.getStartTime());
            }

            byte[] imageByte = Base64.getDecoder().decode(poster);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            BufferedImage image = ImageIO.read(bis);
            bis.close();
            String posterName = "e" + event.getId() + ".png";

            if(eventDAO.isLocationAvailable(event, location)) {
                if (image != null) {
                    System.out.println("hehe");
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

        post("/reserve", (req, res) -> {
            String username = req.queryParams("username");
            String eventId = req.queryParams("eventId");
            String amount = req.queryParams("amount");
            String ticketType = req.queryParams("ticketType");

            Customer customer = customerDAO.findOne(username);
            if(customer == null) return -2;
            Event event = eventDAO.findOne(Integer.parseInt(eventId));

            if(event.getAvailableTickets() < Integer.parseInt(amount)) {
                return -1;
            }

            ArrayList<String> tickets = ticketDAO.createOrder(username, eventId, amount, ticketType, event.getRegularPrice());
            customerDAO.addPoints(tickets, ticketType, amount, event.getRegularPrice(), username);
            eventDAO.adjustCapacity(Integer.parseInt(amount), event.getId());
            return 0;
        });

        post("/postComment", (req, res) -> {
            String eventId = req.queryParams("eventId");
            String username = req.queryParams("username");
            String text = req.queryParams("text");
            String rating = req.queryParams("rating");


            System.out.println(username);
            commentDAO.create(username, text, eventId, rating);
            return true;
        });

        get("/eventComments", (req, res) -> {
            String id = req.queryParams("id");
            int id2 = Integer.parseInt(id);
            return commentDAO.getCommentsForEvent(id2);
        });

        post("/registration", (req, res) -> {
            Gson gsonReg = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

            Customer customer = gsonReg.fromJson(req.body(), Customer.class);
            User user = customerDAO.loadAll().getOrDefault(customer.getUsername(), null);
            user = adminDAO.loadAll().getOrDefault(customer.getUsername(), null);
            user = salesmanDAO.loadAll().getOrDefault(customer.getUsername(), null);
            if(user == null) {
                customerDAO.addCustomer(customer);
                return true;
            }
            else {
                return false;
            }
        });

        post("/registrationSalesman", (req, res) -> {
            Gson gsonReg = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

            Salesman salesman = gsonReg.fromJson(req.body(), Salesman.class);
            Salesman s = salesmanDAO.findOne(salesman.getUsername());
            if(s == null) {
                salesmanDAO.addSalesman(salesman);
                return true;
            }
            else {
                return false;
            }
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
                admin.setPassword(newPassword);
                adminDAO.writeAll();
                return true;
            }
            return false;
        });

        get("/searchEvents", (req, res) -> {
            String search = req.queryParams("search");

            if(search.equals("null")) return null;
            return gson.toJson(eventDAO.search(search));
        });

        get("/searchTickets", (req, res) -> {
            String search = req.queryParams("search");

            if(search.equals("null") || search.trim().isEmpty()) return null;
            return gson.toJson(ticketDAO.search(search));
        });

        get("/searchUsers", (req, res) -> {
            String search = req.queryParams("search");

            if(search.equals("null") || search.trim().isEmpty()) return null;

            ArrayList<UserDTO> users = salesmanDAO.search(search);
            users.addAll(customerDAO.search(search));
            return gson.toJson(users);
        });

        post("/addEvent", (req, res) -> {
            Gson gsonReq = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm").create();
            EventDTO eventDTO = gsonReq.fromJson(req.body(), EventDTO.class);

            Location location = eventDTO.getLocation();
            location.setId(locationDAO.nextId());

            Event event = eventDTO.getEvent();
            event.setId(eventDAO.nextId());
            event.setLocation(location);
            String poster = "";
            boolean posterChosen = true;
            System.out.println(event.getPoster());

            if(event.getPoster().equals("")) {
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
                    File outputFile = new File(System.getProperty("user.dir") + "\\static\\images\\" + posterName);
                    ImageIO.write(image, "png", outputFile);
                    event.setPoster("images/" + posterName);
                }
                event.setActive(false);
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
