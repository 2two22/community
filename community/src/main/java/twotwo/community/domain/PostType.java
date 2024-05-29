package twotwo.community.domain;

import java.util.List;

public enum PostType {
    QUESTION, FEED, ALL;

    public List<PostType> getMyTypes(){
        if(this.equals(ALL))
            return List.of(FEED, QUESTION);
        return List.of(this);
    }
}
