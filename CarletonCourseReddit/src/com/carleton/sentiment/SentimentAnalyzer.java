package com.carleton.sentiment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.carleton.ccr.util.ListUtils;

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

	public String getSentimentFromPost(String text) {
		Annotation annotation = pipeline.process(text);
        List<CoreMap> sentences = (List) annotation.get(CoreAnnotations.SentencesAnnotation.class);
        ArrayList<String> sentiments = new ArrayList<String>();
        for (CoreMap sentence : sentences) {
            String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            sentiments.add(sentiment);
            System.out.println(sentiment + "\t" + sentence);
        }
        return ListUtils.mostCommon(sentiments);
	}
	public static void main(String[] args) {
		SentimentAnalyzer sa = SentimentAnalyzer.getInstance();
		sa.getSentimentFromPost("I really hate everything, why bother. I really love shindigs!");
	}
}
