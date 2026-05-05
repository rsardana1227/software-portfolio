import java.util.ArrayList;

import file.MovieDB;
import movies.Actor;
import movies.Movie;

/**
 * Movie trivia class providing different methods for querying and updating a movie database.
 */
public class MovieTrivia {
	
	/**
	 * Create instance of movie database
	 */
	MovieDB movieDB = new MovieDB();
	
	
	public static void main(String[] args) {
		
		//create instance of movie trivia class
		MovieTrivia mt = new MovieTrivia();
		
		//setup movie trivia class
		mt.setUp("moviedata.txt", "movieratings.csv");
	}
	
	/**
	 * Sets up the Movie Trivia class
	 * @param movieData .txt file
	 * @param movieRatings .csv file
	 */
	public void setUp(String movieData, String movieRatings) {
		//load movie database files
		movieDB.setUp(movieData, movieRatings);
		
		//print all actors and movies
		this.printAllActors();
		this.printAllMovies();		
	}
	
	/**
	 * Prints a list of all actors and the movies they acted in.
	 */
	public void printAllActors () {
		System.out.println(movieDB.getActorsInfo());
	}
	
	/**
	 * Prints a list of all movies and their ratings.
	 */
	public void printAllMovies () {
		System.out.println(movieDB.getMoviesInfo());
	}
	
	
	/**
	 * Returns all movies an actor appears in.
	 * Actor matching ignores case and extra spaces.
	 */
	public ArrayList<String> selectWhereActorIs(String actor, ArrayList<Actor> actorsInfo) {
		// create empty list in case actor is not found
		ArrayList<String> result = new ArrayList<String>();

		// normalize the actor name so case and spaces do not matter
		String targetActor = actor.trim().toLowerCase();

		// loop through all actors in the database
		for (Actor currentActor : actorsInfo) {
			// if the actor names match, return their movies
			if (currentActor.getName().equals(targetActor)) {
				return new ArrayList<String>(currentActor.getMoviesCast());
			}
		}

		// actor not found
		return result;
	}
	
	/**
	 * Inserts an actor and their movies into the database.
	 * If the actor already exists, only add movies that are not already listed.
	 * Matching should ignore case and leading/trailing spaces.
	 */
	public void insertActor(String actor, String[] movies, ArrayList<Actor> actorsInfo) {
		// normalize actor name
		String targetActor = actor.trim().toLowerCase();

		// check whether actor already exists
		for (Actor currentActor : actorsInfo) {
			if (currentActor.getName().equals(targetActor)) {
				// actor exists, so only add movies that are not already there
				for (String movie : movies) {
					String targetMovie = movie.trim().toLowerCase();
					if (!currentActor.getMoviesCast().contains(targetMovie)) {
						currentActor.getMoviesCast().add(targetMovie);
					}
				}
				return;
			}
		}

		// actor was not found, so create a new actor
		Actor newActor = new Actor(targetActor);
		for (String movie : movies) {
			String targetMovie = movie.trim().toLowerCase();
			if (!newActor.getMoviesCast().contains(targetMovie)) {
				newActor.getMoviesCast().add(targetMovie);
			}
		}
		actorsInfo.add(newActor);
	}
	
	/**
	 * Returns all actors who appeared in the given movie.
	 * Movie matching ignores case and extra spaces.
	 */
	public ArrayList<String> selectWhereMovieIs(String movie, ArrayList<Actor> actorsInfo) {
		// create empty list for matching actors
		ArrayList<String> result = new ArrayList<String>();

		// normalize movie name
		String targetMovie = movie.trim().toLowerCase();

		// loop through all actors
		for (Actor currentActor : actorsInfo) {
			// if this actor appeared in the movie, add their name
			if (currentActor.getMoviesCast().contains(targetMovie)) {
				result.add(currentActor.getName());
			}
		}

		return result;
		
	}
	
	/**
	 * Returns all movies whose critic or audience rating matches the given comparison.
	 * Valid comparison operators are '=', '<', and '>'.
	 * If the comparison is invalid or the rating is outside 0-100, return an empty list.
	 */
	public ArrayList<String> selectWhereRatingIs(char comparison, int rating, boolean isCritic,
			ArrayList<Movie> moviesInfo) {

		// create empty list for matching movies
		ArrayList<String> result = new ArrayList<String>();

		// invalid rating
		if (rating < 0 || rating > 100) {
			return result;
		}

		// invalid comparison operator
		if (comparison != '=' && comparison != '<' && comparison != '>') {
			return result;
		}

		// loop through all movies
		for (Movie currentMovie : moviesInfo) {
			int currentRating;

			// choose critic or audience rating
			if (isCritic) {
				currentRating = currentMovie.getCriticRating();
			} else {
				currentRating = currentMovie.getAudienceRating();
			}

			// compare ratings based on operator
			if (comparison == '=' && currentRating == rating) {
				result.add(currentMovie.getName());
			} else if (comparison == '<' && currentRating < rating) {
				result.add(currentMovie.getName());
			} else if (comparison == '>' && currentRating > rating) {
				result.add(currentMovie.getName());
			}
		}

		return result;
	}
	
	/**
	 * Inserts or updates ratings for a movie in the database.
	 * If the movie already exists, update its ratings.
	 * If the movie does not exist, add it to the end of moviesInfo.
	 */
	public void insertRating(String movie, int[] ratings, ArrayList<Movie> moviesInfo) {
		// make sure ratings array is usable
		if (ratings == null || ratings.length != 2) {
			return;
		}

		// make sure ratings are valid
		if (ratings[0] < 0 || ratings[0] > 100 || ratings[1] < 0 || ratings[1] > 100) {
			return;
		}

		// normalize movie name
		String targetMovie = movie.trim().toLowerCase();

		// check if movie already exists
		for (Movie currentMovie : moviesInfo) {
			if (currentMovie.getName().equals(targetMovie)) {
				// update existing ratings
				currentMovie.setCriticRating(ratings[0]);
				currentMovie.setAudienceRating(ratings[1]);
				return;
			}
		}

		// movie was not found, so add a new one
		Movie newMovie = new Movie(targetMovie, ratings[0], ratings[1]);
		moviesInfo.add(newMovie);
	}
	
	/**
	 * Returns all movies that both actors appeared in.
	 * Actor matching ignores case and extra spaces.
	 */
	public ArrayList<String> getCommonMovie(String actor1, String actor2, ArrayList<Actor> actorsInfo) {
		// get movie lists for both actors
		ArrayList<String> actor1Movies = selectWhereActorIs(actor1, actorsInfo);
		ArrayList<String> actor2Movies = selectWhereActorIs(actor2, actorsInfo);

		// store common movies
		ArrayList<String> result = new ArrayList<String>();

		// find overlap between the two lists
		for (String movie : actor1Movies) {
			if (actor2Movies.contains(movie)) {
				result.add(movie);
			}
		}

		return result;
	}
	
	/**
	 * Returns all actors who appeared in both movies.
	 * Movie matching ignores case and extra spaces.
	 */
	public ArrayList<String> getCommonActors(String movie1, String movie2, ArrayList<Actor> actorsInfo) {
		// get actor lists for both movies
		ArrayList<String> movie1Actors = selectWhereMovieIs(movie1, actorsInfo);
		ArrayList<String> movie2Actors = selectWhereMovieIs(movie2, actorsInfo);

		// store common actors
		ArrayList<String> result = new ArrayList<String>();

		// find overlap between the two lists
		for (String actor : movie1Actors) {
			if (movie2Actors.contains(actor)) {
				result.add(actor);
			}
		}

		return result;
	}
	
	/**
	 * Returns all co-actors of the given actor.
	 * A co-actor is any actor who appeared in at least one movie with the given actor.
	 * The actor themself should not be included.
	 */
	public ArrayList<String> getCoActors(String actor, ArrayList<Actor> actorsInfo) {
		// get all movies for the given actor
		ArrayList<String> actorMovies = selectWhereActorIs(actor, actorsInfo);

		// store co-actors
		ArrayList<String> result = new ArrayList<String>();

		// normalize actor name so we can exclude themself
		String targetActor = actor.trim().toLowerCase();

		// for each movie, find all actors in that movie
		for (String movie : actorMovies) {
			ArrayList<String> movieActors = selectWhereMovieIs(movie, actorsInfo);

			for (String currentActor : movieActors) {
				// do not include the actor themself, and avoid duplicates
				if (!currentActor.equals(targetActor) && !result.contains(currentActor)) {
					result.add(currentActor);
				}
			}
		}

		return result;
	}
	
	/**
	 * Returns all movies that have both critic and audience ratings of at least 85.
	 */
	public ArrayList<String> goodMovies(ArrayList<Movie> moviesInfo) {
		// store movies that are rated highly by both critics and audiences
		ArrayList<String> result = new ArrayList<String>();

		// loop through all movies
		for (Movie currentMovie : moviesInfo) {
			if (currentMovie.getCriticRating() >= 85 && currentMovie.getAudienceRating() >= 85) {
				result.add(currentMovie.getName());
			}
		}

		return result;
	}
	
	/**
	 * Returns the mean critic rating and mean audience rating for all movies.
	 * Index 0 stores the mean critic rating.
	 * Index 1 stores the mean audience rating.
	 */
	public static double[] getMean(ArrayList<Movie> moviesInfo) {
		// store sums for critic and audience ratings
		double criticSum = 0;
		double audienceSum = 0;

		// add up all ratings
		for (Movie currentMovie : moviesInfo) {
			criticSum += currentMovie.getCriticRating();
			audienceSum += currentMovie.getAudienceRating();
		}

		// create result array
		double[] result = new double[2];
		result[0] = criticSum / moviesInfo.size();
		result[1] = audienceSum / moviesInfo.size();

		return result;
	}
	
	
}
