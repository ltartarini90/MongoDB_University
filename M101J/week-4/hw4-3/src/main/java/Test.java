import com.mongodb.*;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringEscapeUtils;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.setPort;
public class Test {

    public static void main(String[] args) throws IOException {
        MongoClient c =  new MongoClient(new MongoClientURI("mongodb://localhost"));
        DB db = c.getDB("photosharing");
        int i =0;
        DBCollection album = db.getCollection("albums");
        DBCollection image = db.getCollection("images");

        DBCursor cur = image.find();
        cur.next();

        while (cur.hasNext()){
            Object id = cur.curr().get("_id");
            DBCursor curalbum = album.find(new BasicDBObject("images", id));
            if(!curalbum.hasNext()){
                image.remove(new BasicDBObject("_id", id));
            }
            cur.next();
        }
    }
}