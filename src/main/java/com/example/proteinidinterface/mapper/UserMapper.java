package com.example.proteinidinterface.mapper;

import com.example.proteinidinterface.dto.UserDto;
import com.example.proteinidinterface.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public static UserDto toUserDto (User user){
        UserDto userDto = new UserDto();
//        if (user.getFolders() != null) {
//            List<FolderDto> folderDtos = new ArrayList<>();
//            for (Folder folder : user.getFolders()) {
//                FolderDto folderDto = FolderMapper.toFolderDto(folder);
//                folderDtos.add(folderDto);
//                userDto.setFolders(folderDtos);
//            }
//        }

        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}
