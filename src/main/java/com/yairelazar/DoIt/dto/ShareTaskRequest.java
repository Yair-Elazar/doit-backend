package com.yairelazar.DoIt.dto;

import lombok.Data;
import java.util.List;

@Data
public class ShareTaskRequest {
    private Long taskId;
    private List<String> usernames;
}
