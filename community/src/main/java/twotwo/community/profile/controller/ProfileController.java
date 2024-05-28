package twotwo.community.profile.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import twotwo.community.dto.request.ProfileRequest;
import twotwo.community.profile.service.ProfileService;

@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @PutMapping(value = "/profile")
    public ResponseEntity<Void> updateWriterProfile(@RequestBody @Valid ProfileRequest form, @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token){
        profileService.updateWriterProfile(token, form);
        return ResponseEntity.ok().build();
    }
}
