#include  "RecommendationSystemLoader.h"



std::unique_ptr<RecommendationSystem> RecommendationSystemLoader::
create_rs_from_movies(const std::string& filepath) {
    std::ifstream file(filepath);
    if (!file.is_open()) {
        throw std::runtime_error("Error: Unable to open file.");
    }
    // Create a new recommendation system
    auto rs = std::make_unique<RecommendationSystem>(RecommendationSystem());
    std::string line;
    std::string name;
    int year;
    std::vector<double> movieFeatures;
    // Read the file line by line
    while (std::getline(file, line)) {
        movieFeatures.clear();
        // Extract the movie's name and year
        name = line.substr(0, line.find('-'));
        year = std::stoi(line.substr(line.find('-')
            + 1, line.find(' ') - line.find('-') - 1));

        // Extract the feature values after the year
        line = line.substr(line.find(' ') + 1);
        std::istringstream fetu(line);

        double fet;
        while (fetu>>fet) {
            if (fet<= 0 || fet>10){
            throw std::runtime_error("Error: Invalid movie fetu.");
            }
            movieFeatures.push_back(fet);
        }
        // Add the movie to the recommendation system
        rs->add_movie_to_rs(name, year, movieFeatures);
    }

    return rs;
}
