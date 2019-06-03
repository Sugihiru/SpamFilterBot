package DataProgram;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ParsingMessageFromFile {
    private List<String> messages;

    private void lexer(Path path, Charset charset)
    {
        try {
            Files.lines(path, charset)
                    .map(line -> line.split("\\W+"))
                    .forEach(a ->
                    {
                        for (String message : a) {
                            if (!message.equals(""))
                                messages.add(message);
                        }
                    });
        }
        catch (Exception ex)
        {
            System.out.println("Error with the file. Try again with a valid file");
            System.exit(-1);
        }
    }

    public List<String> getMessages()
    {
        return this.messages;
    }

    ParsingMessageFromFile(String path)
    {
        this.messages = new ArrayList<>();
        lexer(Paths.get(path), Charset.forName("ISO-8859-1"));
    }
}
