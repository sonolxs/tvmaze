package com.example.tvmaze.repository;

import com.example.tvmaze.model.CommentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<CommentDocument, String> {
    List<CommentDocument> findByShowId(Long showId);
    List<CommentDocument> findByShowIdIn(List<Long> showIds);
}