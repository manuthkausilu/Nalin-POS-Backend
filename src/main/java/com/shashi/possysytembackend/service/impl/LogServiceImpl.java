package com.shashi.possysytembackend.service.impl;

import com.shashi.possysytembackend.dto.LogDTO;
import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.entity.Log;
import com.shashi.possysytembackend.repository.LogRepository;
import com.shashi.possysytembackend.repository.UserRepository;
import com.shashi.possysytembackend.service.LogService;
import com.shashi.possysytembackend.exception.OurException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogServiceImpl implements LogService {
    private final LogRepository logRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public LogServiceImpl(LogRepository logRepository, ModelMapper modelMapper, UserRepository userRepository) {
        this.logRepository = logRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Override
    public Response createLog(LogDTO logDto) {
        Response response = new Response();
        try {
            Log log = new Log();
            log.setAction(Log.Action.valueOf(String.valueOf(logDto.getAction())));
            log.setTargetTableName(logDto.getTargetTableName());
            log.setTargetId(logDto.getTargetId());
            log.setPerformedBy(userRepository.findById(logDto.getPerformedById()).orElseThrow(() -> new OurException("User not found")));
            log.setDetails(logDto.getDetails());
            Log saved = logRepository.save(log);
            response.setStatusCode(201);
            response.setMessage("Log created successfully");
            response.setLogDTO(modelMapper.map(saved, LogDTO.class));
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error creating log: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getLogById(Long id) {
        Response response = new Response();
        try {
            Log log = logRepository.findById(id.intValue()).orElseThrow(() -> new OurException("Log not found"));
            response.setStatusCode(200);
            response.setMessage("Log fetched successfully");
            response.setLogDTO(modelMapper.map(log, LogDTO.class));
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching log: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllLogs() {
        Response response = new Response();
        try {
            List<LogDTO> logDTOs = logRepository.findAll().stream()
                    .map(l -> modelMapper.map(l, LogDTO.class))
                    .collect(Collectors.toList());
            response.setStatusCode(200);
            response.setMessage("Logs fetched successfully");
            response.setLogDTOList(logDTOs);
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching logs: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateLog(Long id, LogDTO logDto) {
        Response response = new Response();
        try {
            Log existing = logRepository.findById(id.intValue()).orElseThrow(() -> new OurException("Log not found"));
            existing.setAction(Log.Action.valueOf(String.valueOf(logDto.getAction())));
            existing.setTargetTableName(logDto.getTargetTableName());
            existing.setTargetId(logDto.getTargetId());
            existing.setPerformedBy(userRepository.findById(logDto.getPerformedById()).orElseThrow(() -> new OurException("User not found")));
            existing.setActionDate(java.time.LocalDateTime.now());
            existing.setDetails(logDto.getDetails());
            Log updated = logRepository.save(existing);
            response.setStatusCode(200);
            response.setMessage("Log updated successfully");
            response.setLogDTO(modelMapper.map(updated, LogDTO.class));
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating log: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteLog(Long id) {
        Response response = new Response();
        try {
            // Check if log exists before deleting
            logRepository.findById(id.intValue())
                    .orElseThrow(() -> new OurException("Log not found"));
            logRepository.deleteById(id.intValue());
            response.setStatusCode(200);
            response.setMessage("Log deleted successfully");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting log: " + e.getMessage());
        }
        return response;
    }
}
