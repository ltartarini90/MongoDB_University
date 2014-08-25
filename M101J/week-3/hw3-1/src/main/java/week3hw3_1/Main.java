/**
 * Created by luca on 25/08/14.
 */

package week3hw3_1;

import java.net.UnknownHostException;
import com.mongodb.*;
import org.bson.types.BasicBSONList;

public class Main {
    public static void main(String[] args) throws UnknownHostException {
        MongoClient client =
                new MongoClient(new ServerAddress("localhost", 27017));

        DB database = client.getDB("school");
        DBCollection collection = database.getCollection("students");

        DBCursor students = collection.find();
        while(students.hasNext()) {
            double minScore=100;
            int minIndex = -1;
            DBObject student = students.next();
            BasicBSONList scores = (BasicBSONList) student.get("scores");
            for(int i = 0; i<scores.size(); i++){
                DBObject OBJ = (DBObject) scores.get(i);
                System.out.println(OBJ.get("type") + ": " + OBJ.get("score"));
                if(OBJ.get("type").equals("homework")) {
                    Double score = (Double) (OBJ.get("score"));
                    if(score < minScore) {
                        minIndex = i;
                        minScore = score;
                    }
                }

            }
            System.out.println("Min: " + minScore);
            scores.remove(minIndex);
            collection.update(new BasicDBObject("_id", student.get("_id")), new BasicDBObject("scores", scores));
        }
    }
}
