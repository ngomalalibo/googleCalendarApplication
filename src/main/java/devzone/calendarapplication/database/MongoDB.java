package devzone.calendarapplication.database;

import java.util.*;

import com.google.api.services.calendar.model.Event;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import devzone.calendarapplication.model.EventEntity;
import org.bson.Document;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

//Manages connection to MongoDB database and creates collections
public class MongoDB {
	private static MongoClient mongoClient;
	private static Datastore dataStore;

	static Logger logger = LoggerFactory.getLogger(MongoDB.class);

	// @Value("${spring.data.mongodb.uri}")
	// private static String mongoDBUrl;
	// @Value("${mongoDb.database}")
	// private static String database;

	private static String mongoDBUrl = "mongodb+srv://project1:YxhAWI55nUImLa2h@cluster0-fcg0z.mongodb.net/admin";
	
	//mongodb://[username:password@]host1[:port1][,host2[:port2],...[,hostN[:portN]]][/[database][?options]]44
	
	//mongodb://db1.example.net:27017,db2.example.net:2500/?replicaSet=test
	//private static String mongoDBUrl = "mongodb://localhost:27017";
	private static String database = "calendardb";

	public static void startDB() {
		logger.info("**************** startDB ****************");
		mongoClient = new MongoClient(new MongoClientURI(mongoDBUrl));
		dataStore = new Morphia().createDatastore(mongoClient, database);
		dataStore.ensureIndexes();
		createCollections();
	}

	public static void stopDB() {
		logger.info("**************** stopDB ****************");
		if (mongoClient != null)
			mongoClient.close();
		mongoClient = null;
		dataStore = null;
	}

	public static Datastore getDS() {
		return dataStore;
	}

	public static MongoDatabase getDb() {
		return mongoClient.getDatabase(database);
	}

	public static void createCollections() {
		logger.info("**************** createCollections ****************");
		MongoIterable<String> collections = getDb().listCollectionNames();
		HashSet<String> cols = new HashSet<>();
		for (String j : collections) {
			cols.add(j);
		}

		if (!cols.contains("users")) {
			getDb().createCollection("users");
		}
		if (!cols.contains("events")) {
			getDb().createCollection("events");
		}
	}
	
	/*public static List<Event> getEvents() {
		return getDS().createQuery(EventEntity.class).asList();
	}*/
	
	private static void findAndPrint(MongoCollection<Document> coll) {
		FindIterable<Document> cursor = coll.find();
		
		for (Document d : cursor)
			System.out.println(d);
	}

}