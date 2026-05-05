import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import file.MovieDB;

class MovieTriviaTest {

	// instance of movie trivia object to test
	MovieTrivia mt;
	// instance of movieDB object
	MovieDB movieDB;

	@BeforeEach
	void setUp() throws Exception {
		// initialize movie trivia object
		mt = new MovieTrivia();

		// set up movie trivia object
		mt.setUp("moviedata.txt", "movieratings.csv");

		// get instance of movieDB object from movie trivia object
		movieDB = mt.movieDB;
	}

	@Test
	void testSetUp() {
		assertEquals(6, movieDB.getActorsInfo().size(),
				"actorsInfo should contain 6 actors after reading moviedata.txt.");
		assertEquals(7, movieDB.getMoviesInfo().size(),
				"moviesInfo should contain 7 movies after reading movieratings.csv.");

		assertEquals("meryl streep", movieDB.getActorsInfo().get(0).getName(),
				"\"meryl streep\" should be the name of the first actor in actorsInfo.");
		assertEquals(3, movieDB.getActorsInfo().get(0).getMoviesCast().size(),
				"The first actor listed in actorsInfo should have 3 movies in their moviesCasted list.");
		assertEquals("doubt", movieDB.getActorsInfo().get(0).getMoviesCast().get(0),
				"\"doubt\" should be the name of the first movie in the moviesCasted list of the first actor listed in actorsInfo.");

		assertEquals("doubt", movieDB.getMoviesInfo().get(0).getName(),
				"\"doubt\" should be the name of the first movie in moviesInfo.");
		assertEquals(79, movieDB.getMoviesInfo().get(0).getCriticRating(),
				"The critics rating for the first movie in moviesInfo is incorrect.");
		assertEquals(78, movieDB.getMoviesInfo().get(0).getAudienceRating(),
				"The audience rating for the first movie in moviesInfo is incorrect.");
	}

	@Test
	void testInsertActor() {

		// try to insert new actor with new movies
		mt.insertActor("test1", new String[] { "testmovie1", "testmovie2" }, movieDB.getActorsInfo());
		assertEquals(7, movieDB.getActorsInfo().size(),
				"After inserting an actor, the size of actorsInfo should have increased by 1.");
		assertEquals("test1", movieDB.getActorsInfo().get(movieDB.getActorsInfo().size() - 1).getName(),
				"After inserting actor \"test1\", the name of the last actor in actorsInfo should be \"test1\".");
		assertEquals(2, movieDB.getActorsInfo().get(movieDB.getActorsInfo().size() - 1).getMoviesCast().size(),
				"Actor \"test1\" should have 2 movies in their moviesCasted list.");
		assertEquals("testmovie1",
				movieDB.getActorsInfo().get(movieDB.getActorsInfo().size() - 1).getMoviesCast().get(0),
				"\"testmovie1\" should be the first movie in test1's moviesCasted list.");

		// try to insert existing actor with new movies
		mt.insertActor("   Meryl STReep      ", new String[] { "   DOUBT      ", "     Something New     " },
				movieDB.getActorsInfo());
		assertEquals(7, movieDB.getActorsInfo().size(),
				"Since \"meryl streep\" is already in actorsInfo, inserting \"   Meryl STReep      \" again should not increase the size of actorsInfo.");

		// look up and inspect movies for existing actor
		// note, this requires the use of properly implemented selectWhereActorIs method
		// you can comment out these two lines until you have a selectWhereActorIs
		// method
		assertEquals(4, mt.selectWhereActorIs("meryl streep", movieDB.getActorsInfo()).size(),
				"After inserting Meryl Streep again with 2 movies, only one of which is not on the list yet, the number of movies \"meryl streep\" appeared in should be 4.");
		assertTrue(mt.selectWhereActorIs("meryl streep", movieDB.getActorsInfo()).contains("something new"),
				"After inserting Meryl Streep again with a new Movie \"     Something New     \", \"something new\" should appear as one of the movies she has appeared in.");

		assertEquals(4,
				mt.selectWhereActorIs("   meryl streep   ", movieDB.getActorsInfo()).size(),
				"Actor lookup should ignore leading and trailing spaces.");
		
		assertEquals(0,
				mt.selectWhereActorIs("not a real actor", movieDB.getActorsInfo()).size(),
				"Searching for an actor not in the database should return an empty list.");

	}

	@Test
	void testInsertRating() {

		// try to insert new ratings for new movie
		mt.insertRating("testmovie", new int[] { 79, 80 }, movieDB.getMoviesInfo());
		assertEquals(8, movieDB.getMoviesInfo().size(),
				"After inserting ratings for a movie that is not in moviesInfo yet, the size of moviesInfo should increase by 1.");
		assertEquals("testmovie", movieDB.getMoviesInfo().get(movieDB.getMoviesInfo().size() - 1).getName(),
				"After inserting a rating for \"testmovie\", the name of the last movie in moviessInfo should be \"testmovie\".");
		assertEquals(79, movieDB.getMoviesInfo().get(movieDB.getMoviesInfo().size() - 1).getCriticRating(),
				"The critics rating for \"testmovie\" is incorrect.");
		assertEquals(80, movieDB.getMoviesInfo().get(movieDB.getMoviesInfo().size() - 1).getAudienceRating(),
				"The audience rating for \"testmovie\" is incorrect.");

		// try to insert new ratings for existing movie
		mt.insertRating("doubt", new int[] { 100, 100 }, movieDB.getMoviesInfo());
		assertEquals(8, movieDB.getMoviesInfo().size(),
				"Since \"doubt\" is already in moviesInfo, inserting ratings for it should not increase the size of moviesInfo.");

		// look up and inspect movies based on newly inserted ratings
		// note, this requires the use of properly implemented selectWhereRatingIs
		// method
		// you can comment out these two lines until you have a selectWhereRatingIs
		// method
		assertEquals(1, mt.selectWhereRatingIs('>', 99, true, movieDB.getMoviesInfo()).size(),
				"After inserting a critic rating of 100 for \"doubt\", there should be 1 movie in moviesInfo with a critic rating greater than 99.");
		assertTrue(mt.selectWhereRatingIs('>', 99, true, movieDB.getMoviesInfo()).contains("doubt"),
				"After inserting the rating for \"doubt\", \"doubt\" should appear as a movie with critic rating greater than 99.");

		// inserting an existing movie with different case/extra spaces should still update it
		mt.insertRating("   DOUBT   ", new int[] { 88, 89 }, movieDB.getMoviesInfo());
		assertTrue(mt.selectWhereRatingIs('=', 88, true, movieDB.getMoviesInfo()).contains("doubt"),
				"Movie names should ignore case and extra spaces when updating ratings.");

		// invalid ratings should not add a new movie
		mt.insertRating("badmovie", new int[] { -1, 80 }, movieDB.getMoviesInfo());
		assertEquals(8, movieDB.getMoviesInfo().size(),
				"Invalid ratings should not add a new movie to moviesInfo.");

		// ratings array with wrong length should do nothing
		mt.insertRating("anotherbadmovie", new int[] { 90 }, movieDB.getMoviesInfo());
		assertEquals(8, movieDB.getMoviesInfo().size(),
				"A ratings array that does not have exactly 2 elements should not change moviesInfo.");
	}

	@Test
	void testSelectWhereActorIs() {
		assertEquals(3, mt.selectWhereActorIs("meryl streep", movieDB.getActorsInfo()).size(),
				"The number of movies \"meryl streep\" has appeared in should be 3.");
		assertEquals("doubt", mt.selectWhereActorIs("meryl streep", movieDB.getActorsInfo()).get(0),
				"\"doubt\" should show up as first in the list of movies \"meryl streep\" has appeared in.");

		// actor name should ignore case
		assertEquals(3,
		        mt.selectWhereActorIs("MERYL STREEP", movieDB.getActorsInfo()).size(),
		        "Actor lookup should ignore capitalization.");

		// actor name with extra spaces should still work
		assertEquals(3,
		        mt.selectWhereActorIs("   meryl streep   ", movieDB.getActorsInfo()).size(),
		        "Actor lookup should ignore leading and trailing spaces.");

		// searching for an actor not in the database should return empty list
		assertEquals(0,
		        mt.selectWhereActorIs("random actor", movieDB.getActorsInfo()).size(),
		        "Searching for an unknown actor should return an empty list.");
	}

	@Test
	void testSelectWhereMovieIs() {
		assertEquals(2, mt.selectWhereMovieIs("doubt", movieDB.getActorsInfo()).size(),
				"There should be 2 actors in \"doubt\".");
		assertEquals(true, mt.selectWhereMovieIs("doubt", movieDB.getActorsInfo()).contains("meryl streep"),
				"\"meryl streep\" should be an actor who appeared in \"doubt\".");
		assertEquals(true, mt.selectWhereMovieIs("doubt", movieDB.getActorsInfo()).contains("amy adams"),
				"\"amy adams\" should be an actor who appeared in \"doubt\".");

		// lookup should ignore capitalization
		assertEquals(2, mt.selectWhereMovieIs("DOUBT", movieDB.getActorsInfo()).size(),
				"Movie lookup should ignore capitalization.");

		// lookup should ignore leading and trailing spaces
		assertEquals(2, mt.selectWhereMovieIs("   doubt   ", movieDB.getActorsInfo()).size(),
				"Movie lookup should ignore leading and trailing spaces.");

		// movie not in database should return empty list
		assertEquals(0, mt.selectWhereMovieIs("fake movie", movieDB.getActorsInfo()).size(),
				"Looking up a movie not in the database should return an empty list.");
	}

	@Test
	void testSelectWhereRatingIs() {
		assertEquals(6, mt.selectWhereRatingIs('>', 0, true, movieDB.getMoviesInfo()).size(),
				"There should be 6 movies where critics rating is greater than 0.");
		assertEquals(0, mt.selectWhereRatingIs('=', 65, false, movieDB.getMoviesInfo()).size(),
				"There should be no movie where audience rating is equal to 65.");
		assertEquals(2, mt.selectWhereRatingIs('<', 30, true, movieDB.getMoviesInfo()).size(),
				"There should be 2 movies where critics rating is less than 30.");

		// check that a movie with exact critic rating 79 is returned
		assertTrue(mt.selectWhereRatingIs('=', 79, true, movieDB.getMoviesInfo()).contains("doubt"),
				"\"doubt\" should appear when searching for critic rating = 79.");

		// check that only one movie has audience rating greater than 90
		assertEquals(1, mt.selectWhereRatingIs('>', 90, false, movieDB.getMoviesInfo()).size(),
				"There should be 1 movie where audience rating is greater than 90.");

		// check that no movies have critic rating less than 0
		assertEquals(0, mt.selectWhereRatingIs('<', 0, true, movieDB.getMoviesInfo()).size(),
				"There should be no movies where critic rating is less than 0.");
	}

	@Test
	void testGetCoActors() {
		assertEquals(2, mt.getCoActors("meryl streep", movieDB.getActorsInfo()).size(),
				"\"meryl streep\" should have 2 co-actors.");
		assertTrue(mt.getCoActors("meryl streep", movieDB.getActorsInfo()).contains("tom hanks"),
				"\"tom hanks\" was a co-actor of \"meryl streep\".");
		assertTrue(mt.getCoActors("meryl streep", movieDB.getActorsInfo()).contains("amy adams"),
				"\"amy adams\" was a co-actor of \"meryl streep\".");

		// meryl streep should have amy adams as a co-actor through doubt
		assertTrue(mt.getCoActors("meryl streep", movieDB.getActorsInfo()).contains("amy adams"),
				"\"amy adams\" should be a co-actor of \"meryl streep\".");

		// the actor should not appear in their own co-actor list
		assertFalse(mt.getCoActors("meryl streep", movieDB.getActorsInfo()).contains("meryl streep"),
				"An actor should not appear in their own co-actor list.");

		// actor lookup should ignore case and extra spaces
		assertTrue(mt.getCoActors("   MERYL STREEP   ", movieDB.getActorsInfo()).contains("amy adams"),
				"Actor lookup should ignore case and extra spaces when finding co-actors.");

		// actor not in database should return empty list
		assertEquals(0, mt.getCoActors("not a real actor", movieDB.getActorsInfo()).size(),
				"An actor not in the database should return an empty co-actor list.");
	}

	@Test
	void testGetCommonMovie() {
		assertEquals(1, mt.getCommonMovie("meryl streep", "tom hanks", movieDB.getActorsInfo()).size(),
				"\"tom hanks\" and \"meryl streep\" should have 1 movie in common.");
		assertTrue(mt.getCommonMovie("meryl streep", "tom hanks", movieDB.getActorsInfo()).contains("the post"),
				"\"the post\" should be a common movie between \"tom hanks\" and \"meryl streep\".");

		// same actor compared with themselves should return all their movies
		assertEquals(3, mt.getCommonMovie("meryl streep", "meryl streep", movieDB.getActorsInfo()).size(),
				"Comparing the same actor with themselves should return all their movies.");

		// actor lookup should ignore case and extra spaces
		assertTrue(mt.getCommonMovie("   MERYL STREEP   ", "amy adams", movieDB.getActorsInfo()).contains("doubt"),
				"Actor names should ignore case and extra spaces when finding common movies.");

		// actors with no overlap should return empty list
		assertEquals(0, mt.getCommonMovie("meryl streep", "test1", movieDB.getActorsInfo()).size(),
				"Actors with no movies in common should return an empty list.");
	}

	@Test
	void testGoodMovies() {
		assertEquals(3, mt.goodMovies(movieDB.getMoviesInfo()).size(),
				"There should be 3 movies that are considered good movies, movies with both critics and audience rating that are greater than or equal to 85.");
		assertTrue(mt.goodMovies(movieDB.getMoviesInfo()).contains("jaws"),
				"\"jaws\" should be considered a good movie, since it's critics and audience ratings are both greater than or equal to 85.");

		// after inserting a movie where both ratings are at least 85, it should be included
		mt.insertRating("great movie", new int[] { 90, 92 }, movieDB.getMoviesInfo());
		assertTrue(mt.goodMovies(movieDB.getMoviesInfo()).contains("great movie"),
				"A movie with both ratings at least 85 should be included in goodMovies.");

		// after inserting a movie where only one rating is at least 85, it should not be included
		mt.insertRating("mixed movie", new int[] { 90, 70 }, movieDB.getMoviesInfo());
		assertFalse(mt.goodMovies(movieDB.getMoviesInfo()).contains("mixed movie"),
				"A movie should not be included unless both ratings are at least 85.");

		// after inserting a movie where both ratings are below 85, it should not be included
		mt.insertRating("bad movie", new int[] { 60, 50 }, movieDB.getMoviesInfo());
		assertFalse(mt.goodMovies(movieDB.getMoviesInfo()).contains("bad movie"),
				"A movie with both ratings below 85 should not be included in goodMovies.");
	}

	@Test
	void testGetCommonActors() {
		assertEquals(1, mt.getCommonActors("doubt", "the post", movieDB.getActorsInfo()).size(),
				"There should be one actor that appeared in both \"doubt\" and \"the post\".");
		assertTrue(mt.getCommonActors("doubt", "the post", movieDB.getActorsInfo()).contains("meryl streep"),
				"The actor that appeared in both \"doubt\" and \"the post\" should be \"meryl streep\".");

		// same movie compared with itself should return all actors in that movie
		assertEquals(2, mt.getCommonActors("doubt", "doubt", movieDB.getActorsInfo()).size(),
				"Comparing the same movie with itself should return all actors in that movie.");

		// movie lookup should ignore case and extra spaces
		assertTrue(mt.getCommonActors("   DOUBT   ", "arrival", movieDB.getActorsInfo()).contains("amy adams"),
				"Movie names should ignore case and extra spaces when finding common actors.");

		// movies with no actors in common should return empty list
		assertEquals(0, mt.getCommonActors("jaws", "doubt", movieDB.getActorsInfo()).size(),
				"Movies with no actors in common should return an empty list.");
	}

	@Test
	void testGetMean() {
		assertEquals(67.9, mt.getMean(movieDB.getMoviesInfo())[0], 0.1, "The mean of all critics ratings is incorrect.");

		// check that the returned array has two values
		assertEquals(2, MovieTrivia.getMean(movieDB.getMoviesInfo()).length,
				"getMean should return an array with 2 values.");

		// check that the critic mean is positive
		assertTrue(MovieTrivia.getMean(movieDB.getMoviesInfo())[0] > 0,
				"The critic mean rating should be greater than 0.");

		// check that the audience mean is positive
		assertTrue(MovieTrivia.getMean(movieDB.getMoviesInfo())[1] > 0,
				"The audience mean rating should be greater than 0.");

		// after inserting a movie, the mean should still return 2 values
		mt.insertRating("test movie", new int[] {80, 90}, movieDB.getMoviesInfo());
		assertEquals(2, MovieTrivia.getMean(movieDB.getMoviesInfo()).length,
				"getMean should still return an array of length 2 after inserting a movie.");
	}
}
