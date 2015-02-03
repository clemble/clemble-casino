package com.clemble.server.tag.controller;

import com.clemble.casino.tag.ClembleTag;
import com.clemble.casino.tag.service.PlayerTagService;
import com.clemble.server.tag.repository.ServerPlayerTagsRepository;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static com.clemble.casino.WebMapping.PRODUCES;
import static com.clemble.casino.tag.TagWebMapping.*;

/**
 * Created by mavarazy on 2/3/15.
 */
@RestController
public class PlayerTagServiceController implements PlayerTagService {

    final private ServerPlayerTagsRepository tagsRepository;

    public PlayerTagServiceController(ServerPlayerTagsRepository tagsRepository) {
        this.tagsRepository = tagsRepository;
    }

    @Override
    public Set<ClembleTag> myTags() {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.GET, value = MY_TAGS, produces = PRODUCES)
    public Set<ClembleTag> myTags(@CookieValue("player") String player) {
        return getTags(player);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GET_TAGS, produces = PRODUCES)
    public Set<ClembleTag> getTags(String player) {
        return tagsRepository.findOne(player).getTags();
    }
}
