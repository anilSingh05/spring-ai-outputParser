package spring_ai_outputParser;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class SongsController {

    private final ChatClient chatClient;


    public SongsController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    /**
     *
     * @param artist
     * @return
     */
    @GetMapping("/songs-by-artist")
    public List<String> getSongsByArtistInFormattedOutPut(@RequestParam(value = "artist", defaultValue = "Kumar Shanu") String artist){

        var message  = """
                Please give  me a list of top 10 songs for the artist {artist} . If you  don't know the answer, just say
                I don't know!
                {format}
                """;

        ListOutputConverter outputConverter = new ListOutputConverter(new DefaultConversionService());

        PromptTemplate promptTemplate =  new PromptTemplate(message, Map.of("artist", artist, "format", outputConverter.getFormat()));
        Prompt prompt = promptTemplate.create();
        ChatResponse chatResponse = chatClient.prompt(prompt).call().chatResponse();
        return outputConverter.convert(chatResponse.getResult().getOutput().getContent());

    }

    /**
     *
     * @param artist
     * @return
     */
    @GetMapping("/songs-pk")
    public String getSongsByArtist(@RequestParam(value = "artist", defaultValue = "Kishor Kumar") String artist){

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
