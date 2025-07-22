package gift.member.service;

import gift.jwt.JwtProvider;
import gift.member.dto.MemberRequestDto;
import gift.member.dto.MemberResponseDto;
import gift.member.entity.Member;
import gift.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public MemberService(MemberRepository memberRepository,
                         JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    //회원가입 기능
    public MemberResponseDto register(MemberRequestDto memberRequestDto){

        Member member = new Member(null,
                memberRequestDto.getEmail(),
                memberRequestDto.getPassword(),
                memberRequestDto.getRole());

        member = memberRepository.save(member);
        return new MemberResponseDto(jwtProvider.generateToken(member));

    }

    //로그인 기능
    public MemberResponseDto login(MemberRequestDto memberRequestDto){

        Member member = memberRepository.findMemberByEmail(memberRequestDto.getEmail()).orElseThrow(
                () -> new IllegalArgumentException  ("이메일이 일치하지 않습니다")
        );

        if(!member.getPassword().equals(memberRequestDto.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        return new MemberResponseDto(jwtProvider.generateToken(member));

    }

    public Member findMemberById(Long id){
        return memberRepository.findById(id).orElseThrow();
    }
}
