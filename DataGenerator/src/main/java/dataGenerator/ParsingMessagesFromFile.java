package dataGenerator;

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
    private long countLineFile = 0;
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

    private void parser(String[] container)
    {
        Pattern dateFound = Pattern.compile("\\d+ \\w+ \\d{4}, \\d{1,2}:\\d{2} [AP]M");
        this.countLineFile += 1;
        String message;
        // by splitting and keeping only container[1], we remove header of each message,
        // like, "15 October 2018, 10:43 AM, Thomas : "
        if (container.length > 1) {
            message = container[1];
            // skip 3 first lines and banned message
            if (!(countLineFile < 3) && checkBanList(message)) {
                messages.add(message);
            }
        }
        else
        {
            //if line don't contain header, it means either it's a sequence of the precedent message or it's a date
            if (container.length > 0) {
                Matcher date = dateFound.matcher(container[0]);
                // check if message is not empty and is not a date and is not a banned message,
                // then added to precedent message
                if (messages.size() > 0 && !date.matches() && checkBanList(container[0]))
                    messages.set(messages.size() - 1, messages.get(messages.size() - 1) + "\n" + container[0]);
            }
        }
    }

    private void lexer(Path path)
    {
        try {
            // read line by line the file, split by ": ", then give it to parser
            Files.lines(path, StandardCharsets.UTF_8)
                    .map(line -> line.split(": ", 2))
                    .forEach(this::parser);
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
        lexer(Paths.get(path));
    }
}
