package DataProgram;

import javafx.application.Platform;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ParsingMessagesFromFile {
    private List<String> messages;
    private long nb = 0;
    private Pattern hourFound;
    private List<String> banList;

    private boolean checkBanList(String line)
    {
        for (String ban : banList)
        {
            if (line.contains(ban))
                return false;
        }
        return true;
    }

    private void parser(Path path)
    {
        hourFound = Pattern.compile("\\d+ \\w+ \\d{4}, \\d{1,2}:\\d{2} [AP]M");

        try {
            Files.lines(path, StandardCharsets.UTF_8)
                    .map(line -> line.split(": ", 2))
                    .forEach(container ->
                    {
                        this.nb += 1;
                        String message;
                        if (container.length > 1) {
                            message = container[1];
                            if (!(nb < 3) && checkBanList(message)) {
                                messages.add(message);
                            }
                        }
                        else
                        {
                            System.out.println("enter");
                            if (container.length > 0) {
                                System.out.println(container[0]);
                                Matcher hour = hourFound.matcher(container[0]);
                                if (messages.size() > 0 && !hour.matches() && checkBanList(container[0])) {
                                    System.out.println("done");
                                    messages.set(messages.size() - 1, messages.get(messages.size() - 1) + "\n" + container[0]);
                                }
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

    private void initBanList()
    {
        this.banList = Arrays.asList("Emoticons", "Photo", "Video");
    }

    ParsingMessagesFromFile(String path)
    {
        this.messages = new ArrayList<>();
        initBanList();
        parser(Paths.get(path));
    }
}
