package iloveyouboss;

import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;


public class ProfileTest {

    public static final int BOOLEAN_QUESTION_ANSWER_NO = 0;
    public static final int BOOLEAN_QUESTION_ANSWER_YES = 1;

    private Profile profile = new Profile("xiao zhang");

    private Criteria empty_criteria = new Criteria();
    private Criteria criteria = new Criteria();

    private BooleanQuestion BOOLEAN_QUESTION_DINNER = new BooleanQuestion(1, "Did you have dinner?");
    private BooleanQuestion BOOLEAN_QUESTION_WEIGHT = new BooleanQuestion(3, "Are you too fat?");

    private Answer ANSWER_NO_EAT_FOR_DINNER = new Answer(BOOLEAN_QUESTION_DINNER, BOOLEAN_QUESTION_ANSWER_NO);
    private Answer ANSWER_YES_EAT_FOR_DINNER = new Answer(BOOLEAN_QUESTION_DINNER, BOOLEAN_QUESTION_ANSWER_YES);

    private Answer ANSWER_YES_TOO_FAT_FOR_WEIGHT = new Answer(BOOLEAN_QUESTION_WEIGHT, BOOLEAN_QUESTION_ANSWER_YES);

    private Answer standard_answer_for_dinner = new Answer(BOOLEAN_QUESTION_DINNER, BOOLEAN_QUESTION_ANSWER_YES);
    private Answer standard_answer_for_your_weight = new Answer(BOOLEAN_QUESTION_WEIGHT, BOOLEAN_QUESTION_ANSWER_NO);

    private Criterion EAT_DINNER_CRITERION = new Criterion(standard_answer_for_dinner, Weight.Important);
    private Criterion WEIGHT_CRITERION = new Criterion(standard_answer_for_your_weight, Weight.DontCare);


    @Test
    public void not_match_when_criteria_is_empty() {
        boolean matches = profile.matches(empty_criteria);

        assertThat(matches, is(false));
    }

    @Test
    public void match_when_answer_is_right_or_weight_is_dont_care() {
        profile.add(ANSWER_YES_TOO_FAT_FOR_WEIGHT);
        criteria.add(WEIGHT_CRITERION);

        boolean matches = profile.matches(criteria);

        assertThat(matches, is(true));
    }

    @Test
    public void match_when_answer_is_wrong_and_weight_is_care() {
        profile.add(ANSWER_NO_EAT_FOR_DINNER);
        criteria.add(EAT_DINNER_CRITERION);

        boolean matches = profile.matches(criteria);

        assertThat(matches, is(false));
    }

    @Test
    public void score_is_zero_when_criteria_is_empty() {
        profile.matches(empty_criteria);
        assertThat(profile.score(), is(0));
    }

    @Test
    public void score_is_zero_when_criteria_is_not_match() {
        profile.add(ANSWER_NO_EAT_FOR_DINNER);
        criteria.add(EAT_DINNER_CRITERION);

        profile.matches(criteria);

        assertThat(profile.score(), is(0));
    }

    @Test
    public void score_not_zero_when_criteria_is_match() {
        profile.add(ANSWER_YES_EAT_FOR_DINNER);
        criteria.add(EAT_DINNER_CRITERION);

        profile.matches(criteria);

        assertThat(profile.score(), is(1000));
    }

    @Test
    public void calculate_score_correct_when_have_multiple_criterion() {
        PercentileQuestion percentileQuestion = new PercentileQuestion(1, "Which color do u like best?", new String[] {"green","red","yellow"});
        Answer answer = new Answer(percentileQuestion,"green");

        profile.add(new Answer(BOOLEAN_QUESTION_DINNER, BOOLEAN_QUESTION_ANSWER_NO));
        profile.add(answer);

        Criterion percentageCriteria = new Criterion(answer,Weight.VeryImportant);

        criteria.add(EAT_DINNER_CRITERION);
        criteria.add(percentageCriteria);

        profile.matches(criteria);

        assertThat(profile.score(), is(5000));
    }
}