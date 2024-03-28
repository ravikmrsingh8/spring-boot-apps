package com.example.oauth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductDto(
        @JsonProperty("id") Integer id,
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("image") String image) {
}
