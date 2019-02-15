package com.web.community.domain.projection;

import com.web.community.domain.Board;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "getOnlyTitle", types = {Board.class})
public interface BoardOnlyTitle {
    String getTitle();
}
