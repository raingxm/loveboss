package iloveyouboss;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;


public class ProfileTest {
    @Test
    public void not_match_when_criteria_is_empty() throws Exception {
        Profile profile = new Profile("xiao zhang");
        Criteria empty_criteria = new Criteria();

        boolean matches = profile.matches(empty_criteria);

        assertThat(matches, is(false));
    }

    @Test
    public void match_when_answer_is_right() throws Exception {
        Profile profile = new Profile("xiao zhang");
        BooleanQuestion question = new BooleanQuestion(1, "How old are u?");

        profile.add(new Answer(question, 0));

        Criteria criterions = new Criteria();
        Answer answer = new Answer(question, 1);
        criterions.add(new Criterion(answer, Weight.DontCare));

        boolean matches = profile.matches(criterions);

        assertThat(matches, is(true));
    }
}