package implement.output_writer;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import implement.ChildrenOutput;

import java.io.File;
import java.io.IOException;

final public class Writer {
    private final static Writer INSTANCE = new Writer();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectWriter writer = this.objectMapper.writer(new DefaultPrettyPrinter());

    private Writer() {
    }

    /**
     * returneaza instanta obiectului Writer
     */
    public static Writer getInstance() {
        return INSTANCE;
    }

    /**
     * scrie datele de output pentru un fisier dat
     */
    public void writeData(final File file, final ChildrenOutput children) throws IOException {
        writer.writeValue(file, children);
    }

}
