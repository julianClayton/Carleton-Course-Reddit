package com.carleton.ccr.crawler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.carleton.ccr.db.DatabaseManager;
import com.carleton.ccr.parsing.CourseParser;
import com.carleton.ccr.util.ConfigurationLoader;
import com.carleton.sentiment.SentimentAnalyzer;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.PublicContribution;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.pagination.DefaultPaginator;
import net.dean.jraw.references.SubmissionReference;
import net.dean.jraw.tree.CommentNode;
import net.dean.jraw.tree.RootCommentNode;

public class RedditCrawler {
	
	public static void main (String[] args){
		runParser();
	}
	
	public static void runParser(){
		
		DatabaseManager db = DatabaseManager.getInstance();
		UserAgent userAgent = new UserAgent("CarletonUCourseParser", "com.carleton.ccr", "v1.0.0", "carletoncr");
		
		Properties crawlerProps = ConfigurationLoader.loadCrawlerConfig();
		Credentials credentials = Credentials.script(crawlerProps.getProperty("username"), crawlerProps.getProperty("userpassword"),
			    "NfnzPcNUbB-JZQ", "rfqo_FnfRMfLnnwN-pAA6t5YctE");
		
		// This is what really sends HTTP requests
		NetworkAdapter adapter = new OkHttpNetworkAdapter(userAgent);

		// Authenticate and get a RedditClient instance
		RedditClient reddit = OAuthHelper.automatic(adapter, credentials);
		
		DefaultPaginator<Submission> paginator = reddit
			    .subreddit("CarletonU")
			    .posts()
			    .limit(50)
			    .sorting(SubredditSort.TOP)
			    .build();
		
		List<Listing<Submission>> topAllTime = paginator.accumulate(3);
	
		
		DefaultPaginator<Submission> paginator2 = reddit
			    .subreddit("CarletonU")
			    .posts()
			    .limit(50)
			    .sorting(SubredditSort.NEW)
			    .build();
		
		List<Listing<Submission>> newPosts = paginator2.accumulate(2);
		
		DefaultPaginator<Submission> paginator3 = reddit
			    .subreddit("CarletonU")
			    .posts()
			    .limit(50)
			    .sorting(SubredditSort.HOT)
			    .build();
		
		List<Listing<Submission>> hotPosts = paginator3.accumulate(2);
		
		topAllTime.addAll(newPosts);
		topAllTime.addAll(hotPosts);
		
		CourseParser parser = CourseParser.getInstance();
		SentimentAnalyzer sa = SentimentAnalyzer.getInstance();
		for (Listing<Submission>  page: topAllTime) {
			for (Submission s : page){
				if (db.getDocument(s.getId()) == true){
					continue;
				}
				Post currPost = new Post(s.getId(), s.getUrl(), s.getSelfText(), s.getTitle());
				currPost.addTags(parser.parsePost(currPost.getTitle()));
				currPost.addTags(parser.parsePost(currPost.getText()));
				
				String allText = s.getTitle() + " " + s.getSelfText();
				
				String sentiment= sa.getSentimentFromPost(allText);
			    currPost.setSentiment(sentiment);
				
			    db.addPostToDb(currPost);
			    
				SubmissionReference subRef = reddit.submission(s.getId());
				RootCommentNode root = subRef.comments();

				// walkTree() returns a Kotlin Sequence. Since we're using Java, we're going to have to
				// turn it into an Iterator to get any use out of it.
				Iterator<CommentNode<PublicContribution<?>>> it = root.walkTree().iterator();
				ArrayList<String> tags = CourseParser.getInstance().parsePost(currPost.getText());
				while (it.hasNext()) {
				    // A PublicContribution is either a Submission or a Comment.
				    PublicContribution<?> thing = it.next().getSubject();
				    // Do something with each Submission/Comment
				    Comment currComment = new Comment(thing.getId(), s.getId(), thing.getBody());
				    currComment.setParentId(currPost.getId());
				    String url = s.getUrl() + thing.getId();
				    currComment.setUrl(url);
				    
				    currComment.addTags(tags);
				    currComment.addTags(currPost.getTags());
				    
				    if (currComment.getText()!=null) {
					    ArrayList<String> newTags = parser.parsePost(currComment.getText());
				    	currComment.addTags(newTags);
				    	
				    	String comSent = sa.getSentimentFromPost(currComment.getText());
				    	currComment.setSentiment(comSent);
				    }
				    
				    db.addCommentToDb(currComment);
				}
			}
		}
		
	}
}
