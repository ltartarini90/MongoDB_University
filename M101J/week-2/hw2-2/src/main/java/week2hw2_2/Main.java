/**
 * Created by luca on 25/08/14.
 */

package week2hw2_2;

import java.net.UnknownHostException;
import com.mongodb.*;
import org.bson.types.BasicBSONList;

public class Main {
    public static void main(String[] args) throws UnknownHostException {
        MongoClient client =
                new MongoClient(new ServerAddress("localhost", 27017));

        DB database = client.getDB("students");
        DBCollection collection = database.getCollection("grades");

        BasicDBObject findQuery = new BasicDBObject("type", "homework");
        DBCursor grades = collection.find(findQuery);
        BasicDBObject sortQuery = new BasicDBObject();
        sortQuery.append("student_id", -1);
        sortQuery.append("score", -1);
        grades.sort(sortQuery);
        DBObject g = grades.next();
        Integer prevStudentID = (Integer) g.get("student_id");
        Double minGrade = (Double) g.get("score");
        while (grades.hasNext()) {
            Integer studentID = (Integer) g.get("student_id");
            Double grade = (Double) g.get("score");
            if (studentID != prevStudentID) {
                BasicDBObject removeQuery = new BasicDBObject();
                removeQuery.append("student_id", prevStudentID);
                removeQuery.append("score", minGrade);
                collection.remove(removeQuery);
                minGrade = grade;
            }
            else {
                if (grade < minGrade) {
                    minGrade = grade;
                }
            }
            prevStudentID = studentID;
            g = grades.next();
        }
    }
}