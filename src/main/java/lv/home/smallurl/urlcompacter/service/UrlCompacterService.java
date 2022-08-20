package lv.home.smallurl.urlcompacter.service;

import org.springframework.stereotype.Service;

@Service
public class UrlCompacterService {
    public String compressIp(String url) {
        // Function to get integer ID back from a short url 
        int id = 0; // initialize result 

        // A simple base conversion logic 
        for (int i = 0; i < url.length(); i++) {
            if ('a' <= url.charAt(i) &&
                    url.charAt(i) <= 'z')
                id = id * 62 + url.charAt(i) - 'a';
            if ('A' <= url.charAt(i) &&
                    url.charAt(i) <= 'Z')
                id = id * 62 + url.charAt(i) - 'A' + 26;
            if ('0' <= url.charAt(i) &&
                    url.charAt(i) <= '9')
                id = id * 62 + url.charAt(i) - '0' + 52;
        }
        return String.valueOf(id);
    }
}
