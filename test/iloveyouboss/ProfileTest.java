package iloveyouboss;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;


public class ProfileTest {

    public static final int BOOLEAN_QUESTION_ANSWER_NO = 0;
    public static final int BOOLEAN_QUESTION_ANSWER_YES = 1;
    private Profile profile = new Profile("xiao zhang");
    private Criteria empty_criteria = new Criteria();


    @Test
    public void not_match_when_criteria_is_empty() {
        Criteria empty_criteria = new Criteria();

        boolean matches = profile.matches(empty_criteria);

        assertThat(matches, is(false));
    }

    @Test
    public void match_when_answer_is_right_or_weight_is_dont_care() {
        BooleanQuestion question = new BooleanQuestion(1, "How old are u?");

        profile.add(new Answer(question, 0));

        Criteria criterions = new Criteria();
        Answer answer = new Answer(question, 1);
        criterions.add(new Criterion(answer, Weight.DontCare));

        boolean matches = profile.matches(criterions);

        assertThat(matches, is(true));
    }

    @Test
    public void match_when_answer_is_wrong_and_weight_is_care() {
        BooleanQuestion question = new BooleanQuestion(1, "How old are u?");

        profile.add(new Answer(question, BOOLEAN_QUESTION_ANSWER_NO));

        Criteria criterions = new Criteria();
        Answer other_answer = new Answer(question, BOOLEAN_QUESTION_ANSWER_YES);
        criterions.add(new Criterion(other_answer, Weight.Important));

        boolean matches = profile.matches(criterions);

        assertThat(matches, is(false));
    }

    @Test
    public void score_is_zero_when_criteria_is_empty() {
        profile.matches(empty_criteria);
        assertThat(profile.score(), is(0));
    }

    @Test
    public void score_is_zero_when_criteria_is_not_match() {
        BooleanQuestion question = new BooleanQuestion(1, "How old are u?");

        profile.add(new Answer(question, BOOLEAN_QUESTION_ANSWER_NO));

        Criteria criterions = new Criteria();
        Answer other_answer = new Answer(question, BOOLEAN_QUESTION_ANSWER_YES);
        criterions.add(new Criterion(other_answer, Weight.Important));

        profile.matches(criterions);

        assertThat(profile.score(), is(0));
    }

    @Test
    public void score_not_zero_when_criteria_is_match() {
        BooleanQuestion question = new BooleanQuestion(1, "How old are u?");

        profile.add(new Answer(question, BOOLEAN_QUESTION_ANSWER_NO));

        Criteria criterions = new Criteria();
        Answer other_answer = new Answer(question, BOOLEAN_QUESTION_ANSWER_NO);
        criterions.add(new Criterion(other_answer, Weight.Important));

        profile.matches(criterions);

        assertThat(profile.score(), is(1000));
    }
}