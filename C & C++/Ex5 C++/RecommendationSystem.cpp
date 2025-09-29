#include "RecommendationSystem.h"
/**
 * Comparator struct used for sorting movies in a priority queue.
 * The movies are sorted in descending order of similarity.
 */
struct CompareMovies {
    bool operator()(const std::pair<double, sp_movie>& a,
                    const std::pair<double, sp_movie>& b) const {
        return a.first > b.first;
    }
};
/**
 * Multiplies each element of a feature vector by a scalar value.
 * @param v The vector to be multiplied.
 * @param scalar The scalar value.
 * @return A new vector containing the multiplied values.
 */
std::vector<double> scalar_multiply(const std::vector<double>& v, double scalar) {
    std::vector<double> result(v.size());
    for (size_t i = 0; i < v.size(); ++i) {
        result[i] = v[i] * scalar;
    }
    return result;
}

/**
 * Computes the dot product of two feature vectors.
 * @param v1 The first vector.
 * @param v2 The second vector.
 * @return The dot product of the vectors.
 * @throws std::invalid_argument if the vectors are not of equal size.
 */
double dot_product(const std::vector<double>& v1, const std::vector<double>& v2) {
    if (v1.size() != v2.size()) {
        throw std::invalid_argument("Vectors must have the same size for dot product.");
    }

    double sum = 0.0;
    for (size_t i = 0; i < v1.size(); ++i) {
        sum += v1[i] * v2[i];
    }
    return sum;
}
/**
 * Adds two feature vectors element-wise.
 * @param v1 The first vector.
 * @param v2 The second vector.
 * @return A new vector containing the sum of both vectors.
 * @throws std::invalid_argument if the vectors are not of equal size.
 */
std::vector<double> add_vectors(const std::vector<double>& v1,
    const std::vector<double>& v2) {
    if (v1.size() != v2.size()) {
        throw std::invalid_argument("Vectors must have the same size for addition.");
    }

    std::vector<double> result(v1.size());
    for (size_t i = 0; i < v1.size(); ++i) {
        result[i] = v1[i] + v2[i];
    }
    return result;
}
/**
 * Computes the sum of squares of a vector.
 * @param v The input vector.
 * @return The square root of the sum of squares of the vector elements.
 */
double sum_of_squares(const std::vector<double>& v) {
    double sum = 0.0;
    for (size_t i = 0; i < v.size(); ++i) {
        sum += v[i] * v[i];
    }
    return std::sqrt(sum);
}
/**
 * Calculates the cosine similarity between two feature vectors.
 * @param v1 The first vector.
 * @param v2 The second vector.
 * @return The cosine similarity between the two vectors.
 */
double cs(const std::vector<double>& v1 , const std::vector<double>& v2) {
    double mone = dot_product(v1, v2);
    double mechane = sum_of_squares(v1) * sum_of_squares(v2);
    return mone / mechane;
}

double avg(const User& user) {
    rank_map rm = user.get_rank();
    double sum = 0;
    for (const auto& rank : rm) {
        sum += rank.second;
    }
    return sum / rm.size();
}

RecommendationSystem::RecommendationSystem()
: movies_(0, sp_movie_hash, sp_movie_equal)
{

}



sp_movie RecommendationSystem::add_movie_to_rs(const std::string &name, int year,
    const std::vector<double> &features) {
    sp_movie movie = std::make_shared<Movie>(name, year);
    movies_[movie] = features;
    return movie;
}


sp_movie RecommendationSystem::get_movie(const std::string &name, int year) const {
    auto it = movies_.find( std::make_shared<Movie>(name, year));
    if (it != movies_.end()) {
        return it->first;
    }
    return nullptr;
}

std::ostream &operator<<(std::ostream &os, const RecommendationSystem &rs) {
    std::set<Movie> movies;
    for (const auto &movie: rs.movies_) {
        movies.insert(*(movie.first));
    }
    for (const auto& m: movies) {
        os << m ;
    }
    return os;


}

sp_movie RecommendationSystem::recommend_by_content(const User &user_rankings) {
    // Calculate the user's average rating for the movies they have rated
    double avrgage = avg(user_rankings);
    // Create a new rank map to store the user's ratings
    rank_map user_rv(user_rankings.get_rank());
    // List of movies the user has rated (V)
    std::vector<sp_movie> seen_vec;
    // List of movies the user has not rated (U)
    std::vector<sp_movie> unseen_vec;//U
    // Subtract the average rating from each movie rating
    for (const auto &rank: user_rankings.get_rank()) {
        user_rv[rank.first] = rank.second - avrgage;}
    // Separate movies into seen and unseen categories
    bool seen = false;
    for (const auto &rs: movies_){
        seen = false;
        for (const auto &m: user_rankings.get_rank()) {
            if ( sp_movie_equal(m.first,rs.first)) {
                seen_vec.push_back(rs.first);
                seen = true;
                break;}
        }
        if (!seen) {unseen_vec.push_back(rs.first);
        }
    }
    // Multiply each seen movie's feature vector by the adjusted rating
    std::vector<std::vector<double>> results;
    for (size_t i = 0;i < seen_vec.size();i++ ) {
        results.push_back(scalar_multiply(movies_[seen_vec[i]], user_rv[seen_vec[i]]));
    }
    // Sum up the weighted feature vectors of all seen movies
    std::vector<double> result = results[0];
    sp_movie movie1 = unseen_vec[0];
    for (size_t i = 1;i < seen_vec.size();i++ ) {
        result = add_vectors(result, results[i]);
    }
    // Find the unseen movie with the highest cosine similarity
    bool first_loop = true;
    double fav ;
    for (const auto &m: unseen_vec ) {
        double temp = cs(movies_[m], result);
         if (first_loop) {
             first_loop = false;
             fav = temp;
         }else if (fav < (temp)) {
            fav = temp;
            movie1 = m;
        }
    }
    return movie1;
}

sp_movie RecommendationSystem::recommend_by_cf(const User &user, int k) {
    double max_arg = 0.0;
    sp_movie highestscore = nullptr;
    // Iterate through all movies in the system

    for (const auto &rs: movies_) {
        auto it = user.get_rank().find(rs.first);
        // If the movie has not been rated by the user

        if (it == user.get_rank().end()) {
            // If the movie has not been rated by the user

            double a = predict_movie_score(user ,rs.first,k);
            // Keep track of the highest predicted score
            if (max_arg < a) {
                max_arg = a;
                highestscore = rs.first;
            }

        }
    }
    return highestscore;
}

double RecommendationSystem::predict_movie_score(const User &user,
    const sp_movie &movie, int k) {
    std::priority_queue<std::pair<double, sp_movie>,
                      std::vector<std::pair<double, sp_movie>>,
                      CompareMovies> cs_heap;
    int count = 0;
    // Iterate through all movies rated by the user
    for (const auto &m : user.get_rank()) {

        // Calculate cosine similarity between the given movie and the rated movie
         double fav = cs(movies_[movie], movies_[m.first]);
        if (count < k) {
            count ++;
            cs_heap.push({fav, m.first});
        } else if (fav > cs_heap.top().first) {
            cs_heap.pop();
            cs_heap.push({fav, m.first});
        }
    }
    // Calculate the weighted sum of ratings based on similarity
    double mone = 0.0;
    double mechane = 0.0;
    while (!cs_heap.empty()) {
        auto top_movie = cs_heap.top();
        cs_heap.pop();
        mone += user.get_rank()[top_movie.second] * top_movie.first;
        mechane += top_movie.first;
    }
    // Return the predicted rating (weighted average)
    return mone / mechane;
}
