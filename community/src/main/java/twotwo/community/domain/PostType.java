package twotwo.community.domain;

import java.util.List;

public enum PostType {
    QNA, FEED, ALL;

    public List<PostType> getMyTypes(){
        if(this.equals(ALL))
            return List.of(FEED, QNA);
        return List.of(this);
    }
}
