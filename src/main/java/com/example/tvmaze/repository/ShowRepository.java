package com.example.tvmaze.repository;

import com.example.tvmaze.model.ShowDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRepository extends MongoRepository<ShowDocument, Long> {
}