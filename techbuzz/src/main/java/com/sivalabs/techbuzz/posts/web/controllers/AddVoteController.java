package com.sivalabs.techbuzz.posts.web.controllers;

import com.sivalabs.techbuzz.config.annotations.AnyAuthenticatedUser;
import com.sivalabs.techbuzz.config.annotations.CurrentUser;
import com.sivalabs.techbuzz.posts.domain.dtos.CreateVoteRequest;
import com.sivalabs.techbuzz.posts.domain.dtos.PostViewDTO;
import com.sivalabs.techbuzz.posts.domain.services.PostService;
import com.sivalabs.techbuzz.users.domain.models.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
class AddVoteController {
    private final PostService postService;

    AddVoteController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/api/votes")
    @ResponseStatus
    @AnyAuthenticatedUser
    public ResponseEntity<Void> createVote(@Valid @RequestBody CreateVoteRequest request, @CurrentUser User loginUser) {
        var createVoteRequest = new CreateVoteRequest(request.postId(), loginUser.getId(), request.value());
        postService.addVote(createVoteRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/partials/add-vote")
    @AnyAuthenticatedUser
    public String createVote(@Valid @RequestBody CreateVoteRequest request, @CurrentUser User loginUser, Model model) {
        var createVoteRequest = new CreateVoteRequest(request.postId(), loginUser.getId(), request.value());
        postService.addVote(createVoteRequest);
        PostViewDTO post = postService.getPostViewDTO(request.postId());
        model.addAttribute("post", post);
        model.addAttribute("abcd", "1234");
        return "fragments/post";
    }
}
