# Collaborative Filtering Recommendation Engine

A C++ movie recommendation system implementing both content-based filtering and item-based collaborative filtering (CF), backed by cosine similarity and KNN rating prediction.

---

## Problem Solved

Given a catalog of movies described by feature vectors and a set of users with partial rating histories, recommend unseen movies and predict missing ratings using two complementary strategies: user-profile similarity and item-to-item similarity.

---

## Technical Highlights

| Challenge | How It Was Addressed |
|---|---|
| User-profile construction | Normalise each user's ratings to a zero-mean scale, then compute a weighted average of rated movies' feature vectors |
| Content-based recommendation | Cosine similarity between user profile vector and each unseen movie's feature vector; recommend argmax |
| Collaborative filtering | For each unseen movie, predict score using top-k most similar rated movies weighted by cosine similarity |
| Efficient movie lookup | `Movie` objects define a custom hash (`operator()`) and equality, allowing storage in `std::unordered_map` for O(1) average retrieval |
| Memory safety | Movies shared across users via `std::shared_ptr<Movie>` (`sp_movie`); no raw owning pointers |
| Missing ratings | "NA" entries in the user file are silently skipped; only rated movies contribute to the user profile |

---

## Recommendation Algorithms

### Content-Based Filtering
```
1. Build user profile P = Σ (normalised_rating_i × feature_vector_i)
2. For each unseen movie m: score(m) = cosine_similarity(P, feature_vector(m))
3. Return argmax
```

### Collaborative Filtering (KNN)
```
1. For target movie m, find k most similar rated movies by cosine similarity of feature vectors
2. Predicted rating = Σ (sim(m, mᵢ) × rating(mᵢ)) / Σ sim(m, mᵢ)
3. Recommend the unseen movie with highest predicted rating
```

---

## Class Overview

| Class | Responsibility |
|---|---|
| `Movie` | Name + year; custom hash/equality for unordered containers |
| `RecommendationSystem` | Movie database (feature vectors); implements both recommendation strategies |
| `User` | Ratings map; delegates recommendation requests to `RecommendationSystem` |
| `RecommendationSystemLoader` | Parses `<name>-<year> f1 f2 ... fN` movie files |
| `UsersLoader` | Parses user-ratings files; creates `User` objects linked to the system |

---

## Tech Stack & Concepts

- **Language:** C++ (C++11)
- **Key concepts:** Cosine similarity, KNN prediction, content-based and collaborative filtering, `std::shared_ptr`, custom hash functions, file parsing

---

## Build & Run

```bash
g++ -std=c++11 -Wall -Wextra -O2 \
    Movie.cpp User.cpp UsersLoader.cpp \
    RecommendationSystem.cpp RecommendationSystemLoader.cpp \
    -o recommender
```

**Movie file format:**
```
Inception-2010 8.5 9.0 7.5 6.0
Matrix-1999    9.0 8.0 9.5 7.0
```

**User file format (first line = movie list, then name + ratings):**
```
Inception-2010 Matrix-1999
Alice 9 NA
Bob   7 10
```
