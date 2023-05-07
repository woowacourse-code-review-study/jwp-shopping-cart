package cart.service;

import cart.dao.MemberDao;
import cart.dto.request.MemberRequest;
import cart.dto.response.MemberResponse;
import cart.entity.MemberEntity;
import cart.exception.ResourceNotFoundException;
import cart.mapper.MemberMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class MemberService {
    private final MemberDao memberDao;
    private final MemberMapper memberMapper;

    public MemberService(MemberDao memberDao, MemberMapper memberMapper) {
        this.memberDao = memberDao;
        this.memberMapper = memberMapper;
    }

    public MemberResponse create(MemberRequest memberRequest) {
        MemberEntity member = memberMapper.requestToEntity(memberRequest);
        MemberEntity created = memberDao.save(member)
                .orElseThrow(() -> new ResourceNotFoundException("데이터가 정상적으로 저장되지 않았습니다."));
        return memberMapper.entityToResponse(created);
    }

    public List<MemberResponse> findAll() {
        List<MemberEntity> members = memberDao.findAll();
        return members.stream()
                .map(memberMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public MemberResponse update(MemberRequest memberRequest, Long id) {
        MemberEntity member = memberDao.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다." + System.lineSeparator() + "id : " + id));
        member.replace(memberRequest);
        memberDao.update(member);
        return memberMapper.entityToResponse(member);
    }

    public void deleteById(Long id) {
        MemberEntity member = memberDao.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다." + System.lineSeparator() + "id : " + id));
        memberDao.deleteById(member.getId());
    }
}
