package implement.reader;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

final public class Reader {
    private final static Reader INSTANCE = new Reader();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Reader() {
    }

    /**
     * returneaza instanta de Reader
     */
    public static Reader getInstance() {
        return INSTANCE;
    }

    /**
     * citeste datele de intrare si returneaza un obiect de tip Input
     */
    public Input readData(final File file, final Input input) throws IOException {
        return this.objectMapper.readValue(file, input.getClass());
    }
}
