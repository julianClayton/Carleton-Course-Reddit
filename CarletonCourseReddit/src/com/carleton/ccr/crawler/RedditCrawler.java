package com.carleton.ccr.crawler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.carleton.ccr.db.DatabaseManager;
import com.carleton.ccr.parsing.Parser;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.PublicContribution;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.Subreddit;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.models.TimePeriod;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.pagination.BarebonesPaginator;
import net.dean.jraw.pagination.DefaultPaginator;
import net.dean.jraw.pagination.Paginator;
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
		
		Credentials credentials = Credentials.script("carletoncr", "potato123",
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
			
		ArrayList<com.carleton.ccr.crawler.Comment> allComments = new ArrayList<com.carleton.ccr.crawler.Comment>();
		Parser parser = Parser.getInstance();
		for (Listing<Submission>  page: topAllTime) {
			for (Submission s : page){
				if (db.getDocument(s.getId()) == true){
					continue;
				}
				
				Post currPost = new Post(s.getId(), s.getUrl(), s.getTitle(), s.getSelfText());
				currPost.addTags(parser.parsePost(currPost.getTitle()));
				currPost.addTags(parser.parsePost(currPost.getText()));
				db.addPostToDb(currPost);
				//allPosts.add(currPost);
				SubmissionReference subRef = reddit.submission(s.getId());
				RootCommentNode root = subRef.comments();

				// walkTree() returns a Kotlin Sequence. Since we're using Java, we're going to have to
				// turn it into an Iterator to get any use out of it.
				Iterator<CommentNode<PublicContribution<?>>> it = root.walkTree().iterator();
				ArrayList<String> tags = Parser.getInstance().parsePost(currPost.getText());
				while (it.hasNext()) {
				    // A PublicContribution is either a Submission or a Comment.
				    PublicContribution<?> thing = it.next().getSubject();
				    // Do something with each Submission/Comment
				    Comment currComment = new Comment(thing.getId(), s.getId(), thing.getBody());
				    String url = s.getUrl() + thing.getId();
				    currComment.setUrl(url);
				    currComment.addTags(tags);
				    if (currComment.getText()!=null) {
					    ArrayList<String> newTags = parser.parsePost(currComment.getText());
				    	currComment.addTags(newTags);
				    }
				    db.addCommentToDb(currComment);
				    allComments.add(currComment);
				}
			}
		}
		
	}
}
