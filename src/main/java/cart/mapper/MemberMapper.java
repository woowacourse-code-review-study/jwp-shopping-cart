package cart.mapper;

import cart.domain.Member;
import cart.dto.request.MemberRequest;
import cart.dto.response.MemberResponse;
import cart.entity.MemberEntity;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public Member requestToMember(MemberRequest request) {
        return new Member(request.getEmail(), request.getPassword(), request.getName(), request.getPhone());
    }

    public MemberResponse entityToResponse(MemberEntity entity) {
        return new MemberResponse(entity.getId(), entity.getEmail(), entity.getName(), entity.getPhone());
    }
}
