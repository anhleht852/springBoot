package com.example.webblog.repositories;

import com.example.webblog.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Iterable<Post> getAllByAccountAccountId(Long accountId);
    Iterable<Post> getAllByCategoryCategoryId(Long categoryId);
    Iterable<Post> getAllByTypeTypeId(Long typeId);


    @Query("SELECT p FROM Post p WHERE p.briefContent LIKE %:searchTerm% OR p.title LIKE %:searchTerm%")
    Iterable<Post> getAllByTitleContaining(@Param("searchTerm") String brief_content);

    //Iterable<Post> getAllByBriefContent(String briefContent); or lấy 1 trong 2 ,and cả 2 đều thỏa mới lấy
}