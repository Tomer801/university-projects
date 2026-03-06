
#ifndef RECOMMENDATIONSYSTEM_H
#define RECOMMENDATIONSYSTEM_H
#include "User.h"

#include <algorithm>
#include <queue>
#include <set>
#include <sstream>
#include <cmath>
class  User;

class RecommendationSystem
{
std::unordered_map<sp_movie, std::vector<double>, hash_func, equal_func> movies_;








public:
/**
* Default constructor that initializes the recommendation system.
*/
RecommendationSystem();
/**
* Adds a movie to the recommendation system with its associated
* features.@param name The name of the movie.
* @param year The release year of the movie.
* @param features A vector representing the movie's features.
* @return A shared pointer to the added movie.
*/
sp_movie add_movie_to_rs(const std::string &name,
    int year, const std::vector<double> &features);
/**
* Retrieves a movie from the recommendation system
* based on its name and release year.
* @param name The name of the movie.
* @param year The release year of the movie.
* @return A shared pointer to the movie if found, otherwise nullptr.
*/
sp_movie get_movie(const std::string &name, int year) const;
/**
* Overloaded output stream operator to print all
* movies in the recommendation system.
* @param os The output stream object.
* @param r The recommendation system instance to print.
* @return The output stream with movie details.
*/
friend std::ostream& operator<<(std::ostream &os,
    const RecommendationSystem &r);

/**
* Provides a recommendation based on content
* similarity for a given user.
* @param user_rankings The user object containing
* their movie rankings.
* @return A shared pointer to the recommended movie.
*/
sp_movie recommend_by_content(const User& user_rankings);
/**
* Provides a recommendation based on collaborative
* filtering for a given user.
* @param user The user for whom the recommendation is made.
* @param k The number of most similar movies to consider.
* @return A shared pointer to the recommended movie.
*/
sp_movie recommend_by_cf(const User &user, int k);
/**
* Predicts the rating a user would give to a specific
* movie based on similar movies.
* @param user_rankings The user object containing their movie rankings.
* @param movie The movie for which to predict the rating.
* @param k The number of similar movies to consider.
* @return The predicted rating for the given movie.
*/
double predict_movie_score(const User &user_rankings,
    const sp_movie &movie, int k);

};

#endif // RECOMMENDATIONSYSTEM_H
