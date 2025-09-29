
#ifndef RECOMMENDATIONSYSTEMLOADER_H
#define RECOMMENDATIONSYSTEMLOADER_H
#include <fstream>
#include <memory>
#include "RecommendationSystem.h"

class RecommendationSystemLoader {
public:
static std::unique_ptr<RecommendationSystem>

/**
 * Loads movie data from a file and creates a recommendation system.
 *
 * @param filepath The path to the input file containing movie data.
 * @return A unique pointer to a RecommendationSystem
 * object populated with movies.
 * @throws std::runtime_error if the file cannot
 * be opened or data is invalid.
 */
create_rs_from_movies(const std::string& filepath);



RecommendationSystemLoader() = delete; // בנאי פרטי למניעת יצירה
};



#endif // RECOMMENDATIONSYSTEMLOADER_H
