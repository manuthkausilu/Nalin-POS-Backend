package com.shashi.possysytembackend.service;

import com.shashi.possysytembackend.dto.LogDTO;
import com.shashi.possysytembackend.dto.Response;

public interface LogService {
    Response createLog(LogDTO logDto);
    Response getLogById(Long id);
    Response getAllLogs();
    Response updateLog(Long id, LogDTO logDto);
    Response deleteLog(Long id);
}
