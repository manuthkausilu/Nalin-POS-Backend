package com.shashi.possysytembackend.dto;

import com.shashi.possysytembackend.entity.Log;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LogDTO {
    private Integer logId;
    private Log.Action action;
    private String targetTableName;
    private String targetId;
    private Integer performedById;
    private LocalDateTime actionDate;
    private String details;
}

