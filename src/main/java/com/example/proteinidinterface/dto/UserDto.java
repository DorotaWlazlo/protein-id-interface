package com.example.proteinidinterface.dto;

import com.example.proteinidinterface.model.Search;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Long id;
    private String email;
    private String username;
    private String token;
    private List<Search> searches;
}
