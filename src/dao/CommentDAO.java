package dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Comment;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class CommentDAO {
    private HashMap<Integer, Comment> comments;

    public CommentDAO() {
        comments = new HashMap<>();
        examples();
        loadAll();
    }

    private HashMap<Integer, Comment> loadAll() {
        Gson gson = new Gson();
        Type token = new TypeToken<HashMap<Integer, Comment>>(){}.getType();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("files/comments.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.comments = gson.fromJson(br, token);
        return this.comments;
    }

    private void writeAll() {

        Gson gson = new Gson();
        try {
            FileWriter fw = new FileWriter("files/comments.json");
            gson.toJson(this.comments, fw);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void examples() {
        Comment c1 = new Comment();
        c1.setId(1);
        c1.setRating(5);
        c1.setApproved(0);
        c1.setDeleted(false);
        c1.setCustomer("c1");
        c1.setText("nesto");
        c1.setEvent(1);

        comments.put(c1.getId(), c1);
        writeAll();
    }

    public void create(String username, String text, String eventId, String rating) {
        Comment comment = new Comment();
        comment.setRating(Integer.parseInt(rating));
        comment.setText(text);
        comment.setCustomer(username);
        comment.setEvent(Integer.parseInt(eventId));
        comment.setId(NextId());
        comment.setDeleted(false);
        comment.setApproved(0);

        comments.put(comment.getId(), comment);
        writeAll();
    }

    private int NextId() {
        return Collections.max(this.comments.keySet()) + 1;
    }

    public ArrayList<Comment> getCommentsForEvent(int id) {
        ArrayList<Comment> retval = new ArrayList<>();
        for(Comment c : comments.values()) {
            if(c.getEvent() == id) {
                retval.add(c);
            }
        }

        return retval;
    }
}
