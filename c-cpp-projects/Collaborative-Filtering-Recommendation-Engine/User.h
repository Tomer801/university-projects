//
// Created on 2/20/2022.
//

#ifndef USER_H
#define USER_H
#include <unordered_map>
#include <vector>
#include <string>
#include "Movie.h"

#include "RecommendationSystem.h"


typedef std::unordered_map<sp_movie, double,
hash_func,equal_func> rank_map;

class RecommendationSystem;

class User
{
std::string name_;
rank_map rankings_;
std::shared_ptr<RecommendationSystem> recommendation_system_;

public:
/**
* Constructor to initialize a user with a name, rankings,
* and a recommendation system.
* @param name The name of the user.
* @param rankings A map containing movie ratings.
* @param rs_p Shared pointer to the recommendation system.
*/
User(const std::string& name,const rank_map& rankings,
std::shared_ptr<RecommendationSystem> rs_p);
/**
* Retrieves the user's name.
* @return A string representing the user's name.
*/
std::string get_name() const;
/**
* Retrieves the user's movie rankings.
* @return A map of movies and their respective ratings.
*/
rank_map get_rank() const;
/**
* Overloaded output stream operator for printing user details.
* @param os The output stream object.
* @param user The user object to print.
* @return The output stream with user details.
*/
friend std::ostream& operator<<(std::ostream& os, const User& user);


/**
* function for adding a movie to the DB
* @param name name of movie
* @param year year it was made
* @param features a vector of the movie's features
* @param rate the user rate for this movie
*/
void add_movie_to_user(const std::string &name, int year,
                    const std::vector<double> &features,
                    double rate);




/**
* returns a recommendation according to the movie's content
* @return recommendation
*/
sp_movie get_rs_recommendation_by_content();

/**
* returns a recommendation according to the
* similarity recommendation method
* @param k the number of the most similar movies to calculate by
* @return recommendation
*/
sp_movie get_rs_recommendation_by_cf(int k) const;

/**
* predicts the score for a given movie
* @param name the name of the movie
* @param year the year the movie was created
* @param k the parameter which represents the number of
* the most similar movies to predict the score by
* @return predicted score for the given movie
*/
double get_rs_prediction_score_for_movie(const std::string &name,
    int year, int k) const;


};



#endif //USER_H
