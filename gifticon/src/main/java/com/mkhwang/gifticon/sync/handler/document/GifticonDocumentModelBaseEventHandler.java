package com.mkhwang.gifticon.sync.handler.document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkhwang.gifticon.sync.handler.AbstractCdcEventHandler;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Slf4j
public abstract class GifticonDocumentModelBaseEventHandler extends AbstractCdcEventHandler {
  public GifticonDocumentModelBaseEventHandler(ObjectMapper objectMapper) {
    super(objectMapper);
  }

  protected LocalDateTime parseTimestampToLocalDateTime(Object timestamp) {
    if (timestamp == null) {
      return null;
    }

    if (timestamp instanceof String) {
      try {
        return LocalDateTime.parse((String) timestamp);
      } catch (Exception e) {
        try {
          return LocalDateTime.parse((String) timestamp, DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception e2) {
          log.warn("Failed to parse timestamp string: {}", timestamp);
          return null;
        }
      }
    } else if (timestamp instanceof Number) {
      try {
        long microseconds = ((Number) timestamp).longValue();
        long milliseconds = microseconds / 1000;

        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(milliseconds),
                ZoneId.systemDefault());
      } catch (Exception e) {
        log.warn("Failed to parse timestamp number: {}", timestamp);
        return null;
      }
    }

    return null;
  }
}