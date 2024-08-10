package implement.action.recommand;

import fileio.Writer;
import org.json.simple.JSONArray;
import java.io.IOException;

abstract class Recomand {
    /**
     *functie pentru a adauga rezultatul in fisierul de output
     */
    public void add_in_file(int id, JSONArray array, Writer writer, StringBuilder message) throws IOException {
        array.add(writer.writeFile(id, "", message.toString()));
    }
}