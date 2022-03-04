package com.urlShortener.shorturl.repository;

import com.urlShortener.shorturl.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long>
{
    public Url findByShortLink(String shortLink);
}
