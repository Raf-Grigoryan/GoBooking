package org.example.gobookingcommon.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Agree {

    int id;
    boolean agree;
}
