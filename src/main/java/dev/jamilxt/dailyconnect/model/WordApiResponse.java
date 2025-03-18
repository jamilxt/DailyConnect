package dev.jamilxt.dailyconnect.model;

import java.util.List;

public class WordApiResponse {
    private String word;
    private List<Result> results;

    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }
    public List<Result> getResults() { return results; }
    public void setResults(List<Result> results) { this.results = results; }

    public static class Result {
        private String definition;

        public String getDefinition() { return definition; }
        public void setDefinition(String definition) { this.definition = definition; }
    }
}
