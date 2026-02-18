#include "UsersLoader.h"
#include <fstream>
#include <sstream>
#include <stdexcept>
#include <iostream>

std::vector<User> UsersLoader::create_users(const std::string& users_file_path,
                                          std::shared_ptr<RecommendationSystem> rs) {
    std::ifstream file(users_file_path);
    if (!file) {
        throw std::runtime_error("Error: Unable to open users file.");
    }
    std::vector<User> users;
    std::string line;
    // Read the first line (movie names)
    std::getline(file, line);
    std::istringstream movieStream(line);
    std::vector<sp_movie> movie_names;
    std::string movie_name;
    // Extract movie names and years
    while (movieStream >> movie_name) {
        std::string name = movie_name.substr(0,movie_name.find('-'));
        std::string id = movie_name.substr(movie_name.find('-')+1);
        int year = std::stoi(movie_name.substr(movie_name.find('-') + 1));
        movie_names.push_back(rs->get_movie(name, year));
    }
    // Read subsequent lines (user names and ratings)
    while (std::getline(file, line)) {
        std::istringstream iss(line);
        std::string user_name;
        iss >> user_name;

        rank_map rankings(0, sp_movie_hash, sp_movie_equal);
        double score;
        // Read movie ratings for the user
        for (const auto& movie : movie_names) {
            std::string value;
            iss >> value;
            if (value != "NA") {
                score = std::stod(value);
                if (score<= 0 || score > 10) {
                    throw std::runtime_error("Error: Invalid score.");
                }
                rankings.insert(make_pair(movie, score));
            }
        }
        // Create a new User object and add it to the list
        users.emplace_back(user_name, rankings, rs);
    }

    return users;
}
