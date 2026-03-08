// User.cpp
#include "User.h"

User::User(const std::string& name,const rank_map& rankings,std::shared_ptr<RecommendationSystem> rs_p)
: name_(name),rankings_(0, sp_movie_hash, sp_movie_equal), recommendation_system_(rs_p) {
    rankings_.insert(rankings.begin(),rankings.end());
}

std::string User::get_name() const {
    return name_;
}

rank_map User::get_rank() const {
    return rankings_;
}

void User::add_movie_to_user(const std::string &name, int year,
    const std::vector<double> &features, double rate) {
    sp_movie ptr = recommendation_system_->get_movie(name,year);
    if(ptr == nullptr) {
        recommendation_system_->add_movie_to_rs(name, year, features);
        ptr = recommendation_system_->get_movie(name,year);

    }
    rankings_[ptr] = rate;
}

sp_movie User::get_rs_recommendation_by_content() {
    return recommendation_system_-> recommend_by_content(*this);

}

sp_movie User::get_rs_recommendation_by_cf(int k) const {
    return recommendation_system_-> recommend_by_cf(*this, k);
}

double User::get_rs_prediction_score_for_movie(const std::string &name, int year, int k) const {
       sp_movie movie = recommendation_system_->get_movie(name, year);
       return recommendation_system_->predict_movie_score(*this, movie, k);
}
std::ostream& operator<<(std::ostream& os, const User& user) {
    std::cout <<"name: "<< user.name_ << std::endl;
    os << *(user.recommendation_system_)<<std::endl;
    return os;

}
