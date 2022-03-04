package com.urlShortener.shorturl.service;

import antlr.StringUtils;
import com.google.common.hash.Hashing;
import com.urlShortener.shorturl.model.Url;
import com.urlShortener.shorturl.model.UrlDto;
import com.urlShortener.shorturl.model.UrlResponseDto;
import com.urlShortener.shorturl.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
public class UrlServiceImpl implements UrlService
{
    @Autowired
    private UrlRepository urlRepository;

    @Override
    public Url generateShortLink(UrlDto urlDto)
    {
        if(urlDto.getUrl().length()!=0)
        {
            String encodedUrl = encodeUrl(urlDto.getUrl());
            Url urlToPersist = new Url();
            urlToPersist.setCreationDate(LocalDateTime.now());
            urlToPersist.setOriginalUrl(urlDto.getUrl());
            urlToPersist.setShortLink(encodedUrl);
            urlToPersist.setExpirationDate(getExpirationDate(urlDto.getExpirationDate(), urlToPersist.getCreationDate()));
            Url urlToRet = persistShortLink(urlToPersist);

            if(urlToRet!=null)
                return urlToRet;
            return null;
        }
        return null;
    }

    private LocalDateTime getExpirationDate(String expirationDate, LocalDateTime creationDate)
    {
        if(expirationDate == null || expirationDate.length() == 0){
            return creationDate.plusSeconds(120);
        }

        LocalDateTime expDate = LocalDateTime.parse(expirationDate);
        return expDate;
    }

    private String encodeUrl(String url)
    {

        String encodedUrl="";
        LocalDateTime time = LocalDateTime.now();
        encodedUrl = Hashing.murmur3_32_fixed().hashString(url.concat(time.toString()), StandardCharsets.UTF_8).toString();
        return encodedUrl;
    }

    @Override
    public Url persistShortLink(Url url)
    {
        Url urlToRet = urlRepository.save(url);
        return urlToRet;
    }

    @Override
    public Url getEncodedUrl(String url)
    {
        Url urlToRet = urlRepository.findByShortLink(url);
        return urlToRet;
    }

    @Override
    public void deleteShortLink(Url url) {
        urlRepository.delete(url);
    }
}
