package DataProgram;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsingMessagesFromFile {
    private List<String> messages;
    private long nb = 0;
    private Pattern hourFound;

    private void parser(Path path, Charset charset)
    {
        hourFound = Pattern.compile(".*([01]?[0-9]|2[0-3]):[0-5][0-9].*");

        try {

            Files.lines(path, charset)
                    .map(line -> line.split("(AM,)|'(PM,)"))
                    .forEach(container ->
                    {
                        this.nb += 1;
                        for (String message : container) {
                            Matcher hour = hourFound.matcher(message);
                           if (!(message.equals("") ||
                                   hour.matches() ||
                                   message.contains("AM") ||
                                   message.contains("PM") ||
                                   nb < 5)) {
                               if (message.charAt(0) == ',')
                                   message = message.substring(1);
                               messages.add(message);
                           }
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

    ParsingMessagesFromFile(String path)
    {
        this.messages = new ArrayList<>();
        parser(Paths.get(path), Charset.forName("ISO-8859-1"));
    }
}
