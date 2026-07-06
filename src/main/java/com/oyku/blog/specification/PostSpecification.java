package com.oyku.blog.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.oyku.blog.dto.request.post.SearchPostRequest;
import com.oyku.blog.entity.Post;

import jakarta.persistence.criteria.Predicate;

public class PostSpecification {

    public static Specification<Post> search(SearchPostRequest request) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (request.getStatus() != null) {
                predicates.add(
                        cb.equal(root.get("status"), request.getStatus()));
            }

            if (request.getCategoryId() != null &&
                !request.getCategoryId().isBlank()) {

                predicates.add(
                        cb.equal(root.get("category").get("id"),
                                 request.getCategoryId()));
            }

            if (request.getKeyword() != null &&
                !request.getKeyword().isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("title")),
                                "%" + request.getKeyword().toLowerCase() + "%"
                        ));
            }

            if (request.getAuthor() != null &&
                !request.getAuthor().isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("authorName")),
                                "%" + request.getAuthor().toLowerCase() + "%"
                        ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
