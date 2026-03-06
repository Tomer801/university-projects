//
// Created on 2/21/2022.
//
#ifndef USERFACTORY_H
#define USERFACTORY_H

#include <vector>
#include <string>
#include <memory>

#include "RecommendationSystem.h"

/**
* The UsersLoader class is responsible for loading user data from a file and
* creating User objects with their movie rankings.
*/

class UsersLoader {
public:
UsersLoader() = delete;

/**
* Reads user data from a specified file and creates a
* list of User objects.
*
* @param users_file_path The path to the file containing user data.
* @param rs A shared pointer to the recommendation
* system to associate with the users.
* @return A vector of User objects created from the input file.
* @throws std::runtime_error if the file cannot be opened.
*/
static std::vector<User> create_users(const std::string& users_file_path,
                         std::shared_ptr<RecommendationSystem> rs);

};

#endif //USERFACTORY_H
