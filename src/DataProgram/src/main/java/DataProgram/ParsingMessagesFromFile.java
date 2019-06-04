package DataProgram;

import javafx.application.Platform;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ParsingMessagesFromFile {
    private List<String> messages;
    private long nb = 0;
    private Pattern hourFound;

    private void parser(Path path)
    {
        hourFound = Pattern.compile(".*([01]?[0-9]|2[0-3]):[0-5][0-9].*");

        try {

            Files.lines(path, StandardCharsets.UTF_8)
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
            Platform.exit();
            System.exit(-1);
        }
    }

    List<String> getMessages()
    {
        return this.messages;
    }

    ParsingMessagesFromFile(String path)
    {
        this.messages = new ArrayList<>();
        parser(Paths.get(path));
    }
}
