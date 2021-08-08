package edu.curtin.quizflag.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Store list of questions, and this represents all questions of a flag. Member field also store
 * data about how many question are answered and how many are correct.
 *
 * @author      Tao Hu
 * Date         20/09/2020
 */
public class QuestionSet
{
    private Question currentQuestion; /** the currently selected question */
    private List<Question> questions; /** all the questions */
    /** these two field can be calculated by iterating through the list but store it is more efficient*/
    private int questionAttempted; /** how many question have being attempted */
    private int correctQuestionAnswered; /** how many questions attempted are correct */

    public QuestionSet()
    {
        questions = new ArrayList<>();
        questionAttempted = 0;
    }

    public Question getQuestion(int index)
    {
        return questions.get(index);
    }

    public Question getCurrentQuestion()
    {
        return currentQuestion;
    }

    public void setCurrentQuestion(Question question)
    {
        currentQuestion = question;
    }

    public void setCurrentQuestion(int index)
    {
        currentQuestion = questions.get(index);
    }

    public List<Question> getQuestions()
    {
        return questions;
    }

    public void addQuestion(Question question)
    {
        if (question == null)
        {
            throw new IllegalArgumentException("null question");
        }

        questions.add(question);
    }

    public int getNumOfQuestions()
    {
        return questions.size();
    }

    public boolean isAllAttempted()
    {
        return questionAttempted == questions.size();
    }

    public int getCorrectQuestionAnswered()
    {
        return correctQuestionAnswered;
    }

    public void incrementCorrectQuestionAnswered()
    {
        correctQuestionAnswered++;
    }

    public void incrementQuestionAttempted()
    {
        questionAttempted++;
    }
}
