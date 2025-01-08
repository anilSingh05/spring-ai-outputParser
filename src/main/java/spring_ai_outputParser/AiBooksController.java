package spring_ai_outputParser;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/books")
public class AiBooksController {

    private final ChatClient chatClient;

    public AiBooksController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    /**
     *
     * @param author
     * @return
     */
    @GetMapping("/by-author")
    public  Author getBooksByAuthor(@RequestParam(value = "author",  defaultValue = "Shiv Khera") String author){
        var  promptMessage = """
                Generate a list of books written  by author {author}. If you aren't positive that a book belongs
                to  this author please don't  include it.
                {format}
                """;
        var outputParser =  new BeanOutputConverter<>(Author.class);

        PromptTemplate promptTemplate = new PromptTemplate(promptMessage, Map.of("author", author, "format", outputParser.getFormat()));
        Prompt  prompt = promptTemplate.create();
        ChatResponse chatResponse = chatClient.prompt(prompt).call().chatResponse();
        return outputParser.convert(chatResponse.getResult().getOutput().getContent());
    }

    /**
     *
     * @param author
     * @return
     */
    @GetMapping("/author")
    public Map<String, Object> getAuthorsSocialLinks(@RequestParam(value = "author", defaultValue = "Shiv Khera") String author){
        var message = """
                Generate a list of links for the author {author}. Include the authors name  as the key and any social network
                links  as value
                {format}
                """;

        MapOutputConverter outputConverter = new MapOutputConverter();
        String format =  outputConverter.getFormat();

        PromptTemplate promptTemplate =  new PromptTemplate(message, Map.of("author", author, "format", format));
        Prompt prompt = promptTemplate.create();
        ChatResponse chatResponse = chatClient.prompt(prompt).call().chatResponse();
        return outputConverter.convert(chatResponse.getResult().getOutput().getContent());

    }

}
