package implement;

import java.io.IOException;
import java.util.ArrayList;

import entertainment.Season;
import common.Constants;
import fileio.*;
import fileio.Input;
import implement.action.Action;
import implement.action.command.Favorite;
import implement.action.command.Rating;
import implement.action.command.View;
import implement.action.query.Actor_Query;
import implement.action.query.Movie_Query;
import implement.action.query.Series_Query;
import implement.action.query.User_Query;
import implement.action.recommand.Premium_recommand;
import implement.action.recommand.Standard_recommand;
import org.json.simple.JSONArray;


@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class Implement {
    Input input;
    Writer writer;
    JSONArray array;

    /**
     *constructor obiect implement
     */
    public Implement(Input input, Writer write, JSONArray array) {
        this.input = input;
        this.writer = write;
        this.array = array;
    }

    /**
     *preia data din input si rezolva in functie de actiune
     */
    public void Solver() throws IOException {
        ArrayList<Actor> actorList = new ArrayList<>();
        for (ActorInputData t : input.getActors()) {
            Actor actor = new Actor();
            actor.setAwards(t.getAwards());
            actor.setDescription(t.getCareerDescription());
            actor.setMovies(t.getFilmography());
            actor.setName(t.getName());
            actorList.add(actor);
        }
        ArrayList<Movie> movieList = new ArrayList<>();
        for (MovieInputData t : input.getMovies()) {
            Movie movie = new Movie();
            movie.setName(t.getTitle());
            movie.setDuration(t.getDuration());
            movie.setActors(t.getCast());
            movie.setGenres(t.getGenres());
            movie.setYear(t.getYear());
            movieList.add(movie);
        }
        ArrayList<Series> seriesList = new ArrayList<>();
        for (SerialInputData t : input.getSerials()) {
            Series series = new Series();
            series.setYear(t.getYear());
            series.setCast(t.getCast());
            series.setGenres(t.getGenres());
            series.setName(t.getTitle());
            series.setSeasons(t.getSeasons());
            seriesList.add(series);
        }
        ArrayList<User> usersList = new ArrayList<>();
        for (UserInputData t : input.getUsers()) {
            User user = new User();
            user.setName(t.getUsername());
            user.setSubscription(t.getSubscriptionType());
            user.setHistory(t.getHistory());
            user.setFavourite(t.getFavoriteMovies());
            usersList.add(user);
        }
        ArrayList<Action> actionsList = new ArrayList<>();
        for (ActionInputData t : input.getCommands()) {
            Action action = new Action();
            action.setId(t.getActionId());
            action.setAction_type(t.getActionType());
            action.setCriteria(t.getCriteria());
            action.setNumber(t.getNumber());
            action.setObjectType(t.getObjectType());
            action.setFilters(t.getFilters());
            action.setSeasonNumber(t.getSeasonNumber());
            action.setType(t.getType());
            action.setUsername(t.getUsername());
            action.setSortType(t.getSortType());
            action.setTitle(t.getTitle());
            action.setGrade(t.getGrade());
            action.setGenre(t.getGenre());
            actionsList.add(action);
        }
        for (Movie movie : movieList) {
            movie.getRatings().add(0.0);
        }
        for (Series series : seriesList) {
            for (Season s : series.getSeasons()) {
                s.getRatings().add(0.0);
            }
        }
        for (Action action : actionsList) {
            switch (action.getAction_type()) {
                case Constants.COMMAND:
                    switch (action.getType()) {
                        case Constants.FAVORITE:
                            Favorite favorite = new Favorite();
                            User user1 = favorite.search_for_user(action.getUsername(), usersList);
                            favorite.add_in_favorite(user1, action.getTitle());
                            favorite.add_in_file(action.getId(), array, writer, favorite.message);
                            break;
                        case Constants.VIEW:
                            View view = new View();
                            User user2 = view.search_for_user(action.getUsername(), usersList);
                            view.add_in_viewed(user2, action.getTitle());
                            view.add_in_file(action.getId(), array, writer, view.message);
                            break;
                        case Constants.RATING:
                            Rating rating = new Rating();
                            System.out.println();
                            User user3 = rating.search_for_user(action.getUsername(), usersList);
                            if (action.getSeasonNumber() == 0) {
                                Movie movie = rating.search_for_movie(action.getTitle(), movieList);
                                rating.giveRating(user3, movie, action.getGrade());
                            } else {
                                Series series = rating.search_for_series(action.getTitle(), seriesList);
                                rating.giveRating(user3, series, action.getGrade(),
                                        action.getSeasonNumber() - 1);
                            }
                            rating.add_in_file(action.getId(), array, writer, rating.message);
                            break;
                        default:
                            break;
                    }
                    break;
                case Constants.QUERY:
                    if (action.getObjectType() != null) {
                        switch (action.getObjectType()) {
                            case Constants.ACTORS:
                                Actor_Query query1 = new Actor_Query();
                                StringBuilder msg1 = query1.get_message(query1.get_list_by_filters(action.getFilters(),
                                        actorList, action.getCriteria(), action.getNumber(), action.getSortType(),
                                        movieList, seriesList));
                                query1.add_in_file(action.getId(), array, writer, msg1);
                                break;
                            case Constants.MOVIES:
                                Movie_Query query2_1 = new Movie_Query();
                                StringBuilder msg2_1;
                                msg2_1 = query2_1.get_message(query2_1.get_movie_list_by_filters(
                                        action.getFilters(), movieList, action.getCriteria(), action.getNumber(),
                                        action.getSortType(), usersList));
                                query2_1.add_in_file(action.getId(), array, writer, msg2_1);
                                break;
                            case Constants.SHOWS:
                                Series_Query query2_2 = new Series_Query();
                                StringBuilder msg2_2;
                                msg2_2 = query2_2.get_message(query2_2.get_series_list_by_filters(
                                        action.getFilters(), seriesList, action.getCriteria(), action.getNumber(),
                                        action.getSortType(), usersList));
                                query2_2.add_in_file(action.getId(), array, writer, msg2_2);
                                break;
                            case Constants.USERS:
                                User_Query query3 = new User_Query();
                                StringBuilder msg3 = query3.get_message(query3.get_list(usersList,
                                        action.getSortType(), action.getNumber()));
                                query3.add_in_file(action.getId(), array, writer, msg3);
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                case Constants.RECOMMENDATION:
                    if(action.getType() != null) {
                        switch (action.getType()) {
                            case Constants.STANDARD:
                                Standard_recommand recommand1 = new Standard_recommand();
                                StringBuilder msg1 = recommand1.get_message(recommand1.get_standard_recommand(
                                                action.getUsername(), movieList, seriesList,
                                                Constants.STANDARD, usersList), Constants.STANDARD);
                                recommand1.add_in_file(action.getId(), array, writer, msg1);
                                break;
                            case Constants.BEST_UNSEEN:
                                Standard_recommand recommand2 = new Standard_recommand();
                                StringBuilder msg2 = recommand2.get_message(recommand2.get_standard_recommand(
                                        action.getUsername(), movieList, seriesList, Constants.BEST_UNSEEN,
                                        usersList), Constants.BEST_UNSEEN);
                                recommand2.add_in_file(action.getId(), array, writer, msg2);
                                break;
                            case Constants.FAVORITE:
                                Premium_recommand recommand3 = new Premium_recommand();
                                StringBuilder msg3 = recommand3.get_message(recommand3.get_premium_recommand(
                                        action.getUsername(), movieList, seriesList, Constants.FAVORITE,
                                        usersList, action.getGenre()), Constants.FAVORITE);
                                recommand3.add_in_file(action.getId(), array, writer, msg3);
                                break;
                            case Constants.POPULAR:
                                Premium_recommand recommand4 = new Premium_recommand();
                                StringBuilder msg4 = recommand4.get_message(recommand4.get_premium_recommand(
                                        action.getUsername(), movieList, seriesList, Constants.POPULAR,
                                        usersList, action.getGenre()), Constants.POPULAR);
                                recommand4.add_in_file(action.getId(), array, writer, msg4);
                                break;
                            case Constants.SEARCH:
                                Premium_recommand recommand5 = new Premium_recommand();
                                StringBuilder msg5 = recommand5.get_message(recommand5.get_premium_recommand(
                                        action.getUsername(), movieList, seriesList, Constants.SEARCH,
                                        usersList, action.getGenre()), Constants.SEARCH);
                                recommand5.add_in_file(action.getId(), array, writer, msg5);
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                default:
            }
        }
    }
}
