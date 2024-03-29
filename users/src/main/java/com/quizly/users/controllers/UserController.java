package com.quizly.users.controllers;

import com.quizly.users.constants.SecurityConstants;
import com.quizly.users.dtos.UserDetailsDto;
import com.quizly.users.dtos.UserQuizsDto;
import com.quizly.users.models.Follow;
import com.quizly.users.models.User;
import com.quizly.users.services.FollowService;
import com.quizly.users.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {
    private final RestTemplate restTemplate;
    private final UserService userService;
    private final FollowService followService;

    @GetMapping("/me")
    public ResponseEntity<Optional<UserDetailsDto>>userProfile(@RequestHeader(name = SecurityConstants.AUTHORIZED_USER_HEADER) String username){
        var user = userService.findByUsername(username.toLowerCase());
        try{
            var quizs = restTemplate.getForObject("lb://quizs-service/statistics/" + username, UserQuizsDto.class);
            return user.map(
                            u->ResponseEntity.ok(Optional.of(UserDetailsDto.builder()
                                    .email(u.getEmail())
                                    .username(u.getUsername())
                                    .joinedAt(u.getCreatedAt())
                                    .quizsCreated(quizs.getQuizsCreated())
                                    .quizsTaken(quizs.getQuizsTaken())
                                    .followersCount((long) followService.findByFollowed(u).size())
                                    .build())))
                    .orElse(ResponseEntity.notFound().build());
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<Optional<UserDetailsDto>>findByUsername(@PathVariable("username") String username){
        var user = userService.findByUsername(username.toLowerCase());
        try{
            var quizs = restTemplate.getForObject("lb://quizs-service/statistics/" + username, UserQuizsDto.class);
            return user.map(
                            u->ResponseEntity.ok(Optional.of(UserDetailsDto.builder()
                                    .email(u.getEmail())
                                    .username(u.getUsername())
                                    .joinedAt(u.getCreatedAt())
                                    .quizsCreated(quizs.getQuizsCreated())
                                    .quizsTaken(quizs.getQuizsTaken())
                                    .followersCount((long) followService.findByFollowed(u).size())
                                    .build())))
                    .orElse(ResponseEntity.notFound().build());
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();

        }
    }

    @GetMapping("/follow/{username}")
    public ResponseEntity followUser(@RequestHeader(SecurityConstants.AUTHORIZED_USER_HEADER) String authorizedUser, @PathVariable("username") String username){
        try{
            var followed = userService.findByUsername(username).orElseThrow();
            var follower = userService.findByUsername(authorizedUser).orElseThrow();
            followService.createFollow(follower,followed).orElseThrow();
            return ResponseEntity.ok().build();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/unfollow/{username}")
    public ResponseEntity unfollowUser(@RequestHeader(SecurityConstants.AUTHORIZED_USER_HEADER) String authorizedUser, @PathVariable("username") String username){
        try{
            var followed = userService.findByUsername(username).orElseThrow();
            var follower = userService.findByUsername(authorizedUser).orElseThrow();
            var follow = followService.findByFollowerAndFollowed(follower,followed);
            follow.ifPresent(followService::deleteFollow);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/does-follow/{username}")
    public ResponseEntity<Boolean> doesUserFollow(@RequestHeader(SecurityConstants.AUTHORIZED_USER_HEADER) String authorizedUser, @PathVariable("username") String username){
        try{
            var followed = userService.findByUsername(username).orElseThrow();
            var follower = userService.findByUsername(authorizedUser).orElseThrow();
            var result = followService.doesUserFollow(follower,followed);
            return ResponseEntity.ok(result);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(false);
        }
    }

    @GetMapping("/followed")
    public ResponseEntity<List<String>> getAllFollowed(@RequestHeader(SecurityConstants.AUTHORIZED_USER_HEADER) String authorizedUser){
        try{
            var follower = userService.findByUsername(authorizedUser).orElseThrow();
            var result = followService.findByFollower(follower).stream().map(follow -> follow.getFollowed().getUsername()).collect(Collectors.toList());
            return ResponseEntity.ok(result);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<List<UserDetailsDto>> findUser(
            @RequestHeader(SecurityConstants.AUTHORIZED_USER_HEADER) String authorizedUser,
            @PathVariable("username") String username){
        try{
            return ResponseEntity.ok(this.userService.queryUser(username).stream().map(user -> {
                return UserDetailsDto.builder()
                        .email(user.getEmail())
                        .username(user.getUsername())
                        .joinedAt(user.getCreatedAt())
                        .followersCount((long) followService.findByFollowed(user).size())
                        .build();
            }).collect(Collectors.toList()));
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
