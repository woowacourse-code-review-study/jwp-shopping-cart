package cart.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PriceTest {

    @Test
    void 가격이_음수면_예외가_발생한다() {
        long price = -1;
        Assertions.assertThatThrownBy(() -> new Price(price))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 음수 혹은 빈 값이 될 수 없습니다." + System.lineSeparator() + "price : " + price);
    }

    @Test
    void 가격이_10억을_초과하면_예외가_발생한다() {
        long price = 1_000_000_001;
        Assertions.assertThatThrownBy(() -> new Price(price))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 10억을 초과할 수 없습니다." + System.lineSeparator() + "price : " + price);
    }
}
