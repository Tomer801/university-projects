# Movie Recommendation System (C++) – Intro2CS Assignment

Implementation of a **Recommendation System** in C++ that supports movie recommendations using both **content-based filtering** and **collaborative filtering (CF)**.

---

## Files
- **Movie.h / Movie.cpp** – Defines `Movie` class with name & year, comparison operators, hash/equal for use in maps, and output formatting.【221†source】【222†source】
- **RecommendationSystem.h / RecommendationSystem.cpp** – Core recommendation engine. Stores movies with feature vectors and supports content-based and CF recommendations, as well as predicting ratings.【223†source】【224†source】
- **RecommendationSystemLoader.h / RecommendationSystemLoader.cpp** – Loads movies from a file and initializes a `RecommendationSystem`. Each line contains movie name, year, and feature values.【216†source】【225†source】
- **User.h / User.cpp** – Represents a user with name, ratings map, and reference to a `RecommendationSystem`. Provides methods for adding movies, getting recommendations, and predicting ratings.【217†source】【218†source】
- **UsersLoader.h / UsersLoader.cpp** – Loads users and their ratings from a file, linking them to the `RecommendationSystem`. Handles "NA" ratings gracefully.【219†source】【220†source】

---

## Features
- **Add Movie**: insert a new movie with features into the system.【223†source】
- **Get Movie**: retrieve existing movie by name and year.【223†source】
- **Recommend by Content**: suggests a movie based on cosine similarity between user profile and unseen movies.【223†source】
- **Recommend by Collaborative Filtering (CF)**: suggests movies by comparing similarities between movies rated by the user.【223†source】
- **Predict Movie Score**: predicts the rating a user would give to a movie using top-k similar movies.【223†source】

---

## Build
Compile all `.cpp` files with a C++11+ compiler. Example:
```bash
g++ -std=c++11 -Wall -Wextra -Werror -O2 Movie.cpp User.cpp UsersLoader.cpp RecommendationSystem.cpp RecommendationSystemLoader.cpp -o recommender
```

---

## Usage

### Loading Movies
Provide a file where each line has the format:
```
<name>-<year> <feature1> <feature2> ... <featureN>
```
Example:
```
Inception-2010 8.5 9.0 7.5
```

### Loading Users
Provide a file where the first line lists movies, and subsequent lines list each user with ratings or "NA":
```
Inception-2010 Matrix-1999 Avatar-2009
Alice 9 NA 8
Bob 7 10 NA
```

### Running Recommendations
```cpp
auto rs = RecommendationSystemLoader::create_rs_from_movies("movies.txt");
auto users = UsersLoader::create_users("users.txt", rs);

User& alice = users[0];
sp_movie rec1 = alice.get_rs_recommendation_by_content();
sp_movie rec2 = alice.get_rs_recommendation_by_cf(3);
double pred = alice.get_rs_prediction_score_for_movie("Matrix", 1999, 3);
```

---

## Example Output
```
Recommendation by content: Avatar (2009)
Recommendation by CF: Inception (2010)
Predicted score for Matrix (1999): 8.7
```

---

## License
Educational use. Add a license if you plan to publish broadly.
