package com.sptp.backend;

import com.sptp.backend.keyword.repository.Keyword;
import com.sptp.backend.keyword.repository.KeywordRepository;
import com.sptp.backend.memberkeyword.repository.MemberKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit();

    }
    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final KeywordRepository keywordRepository;

        public void dbInit(){
            List<Keyword> keywordList = new ArrayList<>();
            keywordList.add(new Keyword("유화"));
            keywordList.add(new Keyword("심플한"));
            keywordList.add(new Keyword("세련된"));
            keywordList.add(new Keyword("모던한"));
            keywordList.add(new Keyword("서양화"));
            keywordList.add(new Keyword("변화의"));
            keywordList.add(new Keyword("비판적인"));
            keywordList.add(new Keyword("동양화"));
            keywordList.add(new Keyword("미디어아트"));
            keywordList.add(new Keyword("풍경화"));
            keywordList.add(new Keyword("화려한"));

            for (Keyword keyword : keywordList) {
                keywordRepository.save(keyword);
            }
        }
    }
}
