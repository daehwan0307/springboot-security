package com.hospital.review.service;

import com.hospital.review.domain.User;
import com.hospital.review.domain.dto.UserDto;
import com.hospital.review.domain.dto.UserJoinRequest;
import com.hospital.review.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto join(UserJoinRequest request){
        //비즈니스 로직 - 회원 가입
        //회원 userName(id) 중복 check
        //중복이면 회원가입 x -> Exception(예외) 발생 / Exception(예외) : 중복검사 / 에러:스택오버 플로우, 배열길이 넘어가면 오류 나는거.
        //있으면 에러처리
        userRepository.findByUserName(request.getUserName())
                .ifPresent(user -> {
                    throw new RuntimeException("해당 userName이 중복 됩니다.");
                });

        //회원가입 .save()
        User savedUser = userRepository.save(request.toEntity());
        return UserDto.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .email(savedUser.getEmailAddress())
                .build();
    }
}

