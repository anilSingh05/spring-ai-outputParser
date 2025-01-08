package spring_ai_outputParser;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SongsController {

    private final ChatClient chatClient;


    public SongsController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/songs")
    public String songOutputParser(@RequestParam(value = "artist", defaultValue = "Kishor Kumar") String artist){

        var message  = """
                Please give  me a list of top 10 songs for the artist {artist} . If you  don't know the answer, just say
                I don't know the Answer!
                """;

        PromptTemplate promptTemplate =  new PromptTemplate(message, Map.of("artist", artist));
        Prompt prompt = promptTemplate.create();
        ChatResponse chatResponse = chatClient.prompt(prompt).call().chatResponse();
        return chatResponse.getResult().getOutput().getContent();

    }
}
