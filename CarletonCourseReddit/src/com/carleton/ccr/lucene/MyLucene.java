package com.carleton.ccr.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.carleton.ccr.crawler.Comment;
import com.carleton.ccr.crawler.Post;
import com.carleton.ccr.crawler.Submission;
import com.carleton.ccr.db.DatabaseManager;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;


public class MyLucene {

	private static final String INDEX_DIR =  "/Users/julianclayton/Documents/workspace/COMP-4601-final-project/CarletonCourseReddit/Lucene";
	private static FSDirectory dir;
	private static IndexWriter	writer;
	
	private static final String DOC_ID = "docId";
	private static final String TITLE = "title";
	private static final String TYPE = "type";
	private static final String CONTENT = "content";
	private static final String TAGS = "tags";
	private static final String URL = "url";


	public static void indexLucene(DBCursor cursor){

	try	{	
		dir	=	FSDirectory.open(new File(INDEX_DIR));	
		Analyzer	analyzer	=	new	StandardAnalyzer(Version.LUCENE_45);	
		IndexWriterConfig iwc	=	new	IndexWriterConfig(Version.LUCENE_45, analyzer);	
		iwc.setOpenMode(OpenMode.CREATE);	
		writer = new IndexWriter(dir, iwc);	
		
		while(cursor.hasNext()){	
			indexADoc(cursor.next());	
		}
		
	} catch	(Exception	e)	{	
		e.printStackTrace();	
		}	finally	{	
			try	{	
			 	if	(writer	!=	null)	{	
					writer.close();	
			 	}
			 	if	(dir	!=	null)	{
					dir.close();	
			 	}
			 } catch (IOException	e)	{	
					e.printStackTrace();	
			 	
			 }
		}	
	}
	
	private static void indexADoc(DBObject object) throws IOException	{	
		
		if (object.get("tags") == null || object.get("content") == null){
			return;
		}
		
		try{
			Document lucDoc	=	new	Document();	

			String docId = object.get("id").toString();
			String type = (String)object.get("type");
			ArrayList<String> tags = (ArrayList<String>) object.get("tags");
			
			String tagsString = "";
			for (String t : tags){
				tagsString += t + " " ;
			}
		
			lucDoc.add(new	StringField(DOC_ID, docId, Field.Store.YES));
			lucDoc.add(new	StringField(TYPE, type, Field.Store.YES));
			lucDoc.add(new TextField(TAGS, tagsString, Field.Store.YES));
			
			writer.addDocument(lucDoc);
			
		}catch(Exception e){
			System.out.println("-------Error:  "+e);
			e.printStackTrace();
		}
	}	

	public	static ArrayList<Submission> query(String searchStr)	{	
		try	{	
		    IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(INDEX_DIR)));
			IndexSearcher searcher = new IndexSearcher(reader);
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_45);
			QueryParser parser = new QueryParser(Version.LUCENE_45, TAGS, analyzer);
			Query q = parser.parse(searchStr);
			TopDocs results = searcher.search(q, 100);
		 	
		 	ScoreDoc[]	hits =	results.scoreDocs;
		 	
		 	ArrayList<Submission> courseComments = new ArrayList<Submission>();	
			
		 	
		 	for	(ScoreDoc hit :	hits)	{	
			 	Document indexDoc = searcher.doc(hit.doc);	
			 	String id = indexDoc.get(DOC_ID);
			 	
			 	if	(id	!=	null) {	
			 		String type = indexDoc.get(TYPE);
			 		
			 		if (type.equals("post")){
			 			Post post = DatabaseManager.getInstance().getPost(id);
			 			courseComments.add(post);
				 	} else {
				 		Comment com = DatabaseManager.getInstance().getComment(id);
				 		courseComments.add(com);
				 	}
				 } 		
		 	}
		 	reader.close();
		 	return	courseComments;
		} catch (Exception e)	{	
			e.printStackTrace();
		}	
		return	null;	
	}
	
	public static void main (String[] args){
		//MyLucene.indexLucene(DatabaseManager.getInstance().getAllDocCursor());
		//ArrayList<Submission> results = MyLucene.query("comp");
		
		/*for (Submission r : results){
			System.out.println(r);
		}*/
	}
	
	
}
