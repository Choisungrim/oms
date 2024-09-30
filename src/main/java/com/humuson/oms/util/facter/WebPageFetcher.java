package com.humuson.oms.util.facter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

public class WebPageFetcher {

    public String fetchWebPageSource(String url) throws IOException {
        // 주어진 URL에서 HTML 코드 가져오기
        Document doc = Jsoup.connect(url).get();
        return doc.html(); // HTML 소스 반환
    }

    public void fetchAssets(Document doc) {
        // CSS 및 JavaScript 파일 링크 가져오기
        List<Element> cssLinks = doc.select("link[rel=stylesheet]");
        List<Element> jsLinks = doc.select("script[src]");

        // CSS 파일 링크 출력
        cssLinks.forEach(link -> System.out.println("CSS: " + link.attr("href")));

        // JavaScript 파일 링크 출력
        jsLinks.forEach(link -> System.out.println("JavaScript: " + link.attr("src")));
    }
}

