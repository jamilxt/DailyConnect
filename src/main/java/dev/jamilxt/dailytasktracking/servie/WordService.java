package dev.jamilxt.dailytasktracking.servie;

import dev.jamilxt.dailytasktracking.model.WordApiResponse;
import dev.jamilxt.dailytasktracking.model.WordDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class WordService {

    private static final Logger logger = LoggerFactory.getLogger(WordService.class);
    private final RestTemplate restTemplate;
    private final Map<String, WordDefinition> cache = new HashMap<>();
    private LocalDate lastFetchDate = null;

    private static final String[] SAMPLE_WORDS = {
            "hope", "love", "peace", "joy", "freedom", "strength", "courage", "wisdom"
    };

    public WordService() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(1000); // 5 seconds connect timeout
        factory.setReadTimeout(1000);    // 5 seconds read timeout
        this.restTemplate = new RestTemplate(factory);
    }

    public WordDefinition getDailyWord() {
        LocalDate today = LocalDate.now();
        if (lastFetchDate == null || !lastFetchDate.equals(today)) {
            logger.info("Fetching new daily word for {}", today);
            WordDefinition word = fetchWordFromApi();
            cache.put("daily_word", word);
            lastFetchDate = today;
        }
        return cache.getOrDefault("daily_word", new WordDefinition("Error", "Could not fetch word"));
    }

    private WordDefinition fetchWordFromApi() {
        String randomWord = SAMPLE_WORDS[new Random().nextInt(SAMPLE_WORDS.length)];
        String url = "https://api.dictionaryapi.dev/api/v2/entries/en/" + randomWord;

        try {
            long startTime = System.currentTimeMillis();
            logger.info("Fetching definition for word: {}", randomWord);
            DictionaryApiResponse[] response = restTemplate.getForObject(url, DictionaryApiResponse[].class);
            long duration = System.currentTimeMillis() - startTime;
            logger.info("API call took {} ms", duration);

            if (response != null && response.length > 0 && !response[0].getMeanings().isEmpty()) {
                String definition = response[0].getMeanings().get(0).getDefinitions().get(0).getDefinition();
                return new WordDefinition(randomWord, definition);
            } else {
                logger.warn("No valid definition found for {}", randomWord);
                return new WordDefinition(randomWord, "Definition not available");
            }
        } catch (Exception e) {
            logger.error("Failed to fetch word from API: {}", e.getMessage());
            return new WordDefinition(randomWord, "Definition not available due to API error");
        }
    }

    private static class DictionaryApiResponse {
        private String word;
        private List<Meaning> meanings;

        public String getWord() { return word; }
        public void setWord(String word) { this.word = word; }
        public List<Meaning> getMeanings() { return meanings; }
        public void setMeanings(List<Meaning> meanings) { this.meanings = meanings; }

        private static class Meaning {
            private List<Definition> definitions;

            public List<Definition> getDefinitions() { return definitions; }
            public void setDefinitions(List<Definition> definitions) { this.definitions = definitions; }
        }

        private static class Definition {
            private String definition;

            public String getDefinition() { return definition; }
            public void setDefinition(String definition) { this.definition = definition; }
        }
    }
}