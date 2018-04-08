package com.carleton.sentiment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

public class SentimentAnalyzer {

	private static SentimentAnalyzer instance;
	private Properties props;
	StanfordCoreNLP pipeline;
	
	public static SentimentAnalyzer getInstance() {
		if (instance == null) {
			instance = new SentimentAnalyzer();
		}
		return instance;
	}
	public SentimentAnalyzer() {
		props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment");
        pipeline = new StanfordCoreNLP(props);

	}
	public static <T> T mostCommon(List<T> list) {
	    Map<T, Integer> map = new HashMap<>();

	    for (T t : list) {
	        Integer val = map.get(t);
	        map.put(t, val == null ? 1 : val + 1);
	    }
	    Entry<T, Integer> max = null;
	    for (Entry<T, Integer> e : map.entrySet()) {
	        if (max == null || e.getValue() > max.getValue())
	            max = e;
	    }
	    return max.getKey();
	}
	public String getSentimentFromPost(String text) {
		Annotation annotation = pipeline.process(text);
        List<CoreMap> sentences = (List) annotation.get(CoreAnnotations.SentencesAnnotation.class);
        ArrayList<String> sentiments = new ArrayList<String>();
        for (CoreMap sentence : sentences) {
            String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            sentiments.add(sentiment);
            System.out.println(sentiment + "\t" + sentence);
        }
        return mostCommon(sentiments);
	}
	public static void main(String[] args) {
		SentimentAnalyzer sa = SentimentAnalyzer.getInstance();
		sa.getSentimentFromPost("I really hate everything, why bother. I really love shindigs!");
	}
}
