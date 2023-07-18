package com.labs.api.service.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labs.api.service.member.request.MemberTeamChangeRequest;
import com.labs.api.service.member.respoonse.MemberTeamChangeResponse;
import com.labs.api.service.team.TeamQueryService;
import com.labs.domain.member.Member;
import com.labs.domain.member.MemberRepository;
import com.labs.domain.team.Team;
import com.labs.domain.team.TeamRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberCommandService {

	private final MemberRepository memberRepository;
	private final TeamRepository teamRepository;
	private final TeamQueryService teamQueryService;

	public Long createMember(String username) {
		Member newMember = new Member(username);
		Member savedMember = memberRepository.save(newMember);
		return savedMember.getId();
	}

	public void changeUsername(Long memberId, String newUsername) {
		Member findMember = memberRepository.findById(memberId).orElseThrow();
		findMember.changeUsername(newUsername);
	}

	public MemberTeamChangeResponse changeTeam(MemberTeamChangeRequest request) {
		Member findMember = findMemberById(request.getMemberId());
		// Team findTeam = teamQueryService.findTeamById(request.getMemberId()); // readOnly=true 로 인해 더티 체킹 발생 안 함
		Team findTeam = teamRepository.findById(request.getTeamId()).get();

		findMember.changeTeam(findTeam);

		return new MemberTeamChangeResponse(findMember.getId(), findTeam.getId());
	}

	private Member findMemberById(Long id) {
		return memberRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("There is no member matching the given id."));
	}

}
