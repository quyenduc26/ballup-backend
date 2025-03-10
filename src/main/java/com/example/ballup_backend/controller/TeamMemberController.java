package com.example.ballup_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ballup_backend.dto.req.team.KickTeamMemberRequest;
import com.example.ballup_backend.dto.req.team.UpdateMemberRoleRequest;
import com.example.ballup_backend.dto.res.team.TeamOverviewResponse;
import com.example.ballup_backend.entity.TeamMemberEntity;
import com.example.ballup_backend.entity.TeamEntity.Sport;
import com.example.ballup_backend.entity.TeamMemberEntity.Role;
import com.example.ballup_backend.service.TeamMemberService;
    
@RestController
@RequestMapping("team-member")
public class TeamMemberController {
    @Autowired
    private TeamMemberService teamMemberService;

    @PostMapping("/create")
    public ResponseEntity<TeamMemberEntity> createTeamMember(@RequestParam Long userId, @RequestParam Long teamId, @RequestParam Role role) {
        TeamMemberEntity teamMember = teamMemberService.createTeamMember(userId, teamId, role );
        return ResponseEntity.ok(teamMember);
    }

    @PatchMapping("/{memberId}/role")
    public ResponseEntity<String> updateRole(@PathVariable Long memberId, @RequestBody UpdateMemberRoleRequest updateMemberRoleRequest) {
        teamMemberService.updateTeamMemberRole(memberId, updateMemberRoleRequest);
        return ResponseEntity.ok("Role updated successfully");
    }

    @DeleteMapping("/{memberId}/kick")
    public ResponseEntity<String> kickMember(@RequestBody KickTeamMemberRequest kickTeamMemberRequest, @PathVariable Long memberId) {
        teamMemberService.kickMember(kickTeamMemberRequest, memberId);
        return ResponseEntity.ok("Member kicked successfully");
    }

    @DeleteMapping("/{teamId}/leave")
    public ResponseEntity<String> kickMember(@PathVariable Long teamId, @RequestParam Long memberId) {
        teamMemberService.leaveTeam(teamId, memberId);
        return ResponseEntity.ok("Member kicked successfully");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Long>> getTeamMemberIds( @PathVariable Long userId,  @RequestParam Sport sport) {
        return ResponseEntity.ok(teamMemberService.getTeamMemberIds(userId, sport));
    }

    @GetMapping("/{userId}/team")
    public ResponseEntity<TeamOverviewResponse> getTeamByUserIdAndSport( @PathVariable Long userId,  @RequestParam Sport sport) {
        return ResponseEntity.ok(teamMemberService.getTeamByUserIdAndSport(userId, sport));
    }

}
