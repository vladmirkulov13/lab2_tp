import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;
import server.ServerClients;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImportCountMovingTest {

        @Test
        public void validation() throws ValidationException, IOException {
            JSONObject jsonSchema = new JSONObject(
                    new JSONTokener(new FileInputStream("schema.json")));
            File file = new File("countMoving.json");
            ServerClients.importingSaveState(file);
            JSONObject jsonSubject = new JSONObject(new JSONTokener
                    (new FileInputStream(file)));
            Schema schema = SchemaLoader.load(jsonSchema);
            schema.validate(jsonSubject);
        }

}
