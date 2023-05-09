package cart.fixture;

import cart.dto.request.MemberRequest;
import cart.dto.response.MemberResponse;

public class MemberFixture {
    public static class JUNO {
        public static MemberRequest REQUEST = new MemberRequest("juno@test.com", "12345", "juno", "010-1234-1234");
        public static MemberResponse RESPONSE = new MemberResponse(1L, "juno@test.com", "123456", "juno", "010-1234-1234");
    }
}
