
#ifndef EX5_MOVIE_H
#define EX5_MOVIE_H

#include <iostream>
#include <memory>

#define HASH_START 17


// Forward declaration of the Movie class
class Movie;

typedef std::shared_ptr<Movie> sp_movie; // define your smart pointer

/**
 * those declartions and typedefs are given to you and
 * should be used in the ex
 */
typedef std::size_t (*hash_func)(const sp_movie& movie);
typedef bool (*equal_func)(const sp_movie& m1,const sp_movie& m2);

std::size_t sp_movie_hash(const sp_movie& movie);

bool sp_movie_equal(const sp_movie& m1,const sp_movie& m2);

class Movie
{
 std::string name_;
 int year_;  // The name of the movie
 public:   // The release year of the movie
 /**
 * Constructor for creating a Movie object.
 * @param name The name of the movie.
 * @param year The release year of the movie.
 */
 Movie(const std::string& name, int year);


 /**
  * Overloaded output stream operator for printing movie details.
  * @param os The output stream object.
  * @param movie The movie object to print.
  * @return The output stream with movie details.
  */
 friend std::ostream& operator<<(std::ostream& os,
  const Movie& movie);


/**
* Overloaded operator to compare two movies.
* Used for sorting purposes.
* @param movie The other movie to compare.
* @return True if the current movie is less than the other
* , otherwise false.
*/
 bool operator<(const Movie& movie) const;


 /**
  * Retrieves the name of the movie.
  * @return A string containing the movie name.
  */
 std::string get_name() const;

 /**
 * Retrieves the release year of the movie.
 * @return An integer representing the release year.
 */
 int get_year() const;

};


#endif //EX5_MOVIE_H
