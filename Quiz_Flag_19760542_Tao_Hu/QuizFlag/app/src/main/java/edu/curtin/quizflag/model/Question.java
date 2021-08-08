package edu.curtin.quizflag.model;

/**
 * Question class that stores the information of a question.
 *
 * @author      Tao Hu
 * Date         20/09/2020
 */
public class Question
{
    private int points;
    private int penalty;
    private boolean isSpecial;
    private String questionStr;
    private String[] choices;
    private String correctAnswer;
    private String selectedAnswer;

    /**
     *
     * @param questionStr the description of the question
     * @param choices all the possible choices as a string array
     * @param correctAnswer the correct answer of the question
     * @param points the points this question gives when answered correctly
     * @param penalty the penalty the question gives when answered incorrectly
     * @param isSpecial whether the question is special or not
     */
    public Question(String questionStr, String[] choices, String correctAnswer, int points, int penalty, boolean isSpecial)
    {
        this.points = points;
        this.penalty = penalty;
        this.questionStr = questionStr;
        this.choices = choices;
        this.isSpecial = isSpecial;
        this.correctAnswer = correctAnswer;
        this.selectedAnswer = null;
    }

    /**
     *
     * @param questionStr the description of the question
     * @param choices all the possible choices as a string array
     * @param correctAnswer the correct answer of the question
     * @param points the points this question gives when answered correctly
     * @param penalty the penalty the question gives when answered incorrectly
     */
    public Question(String questionStr, String[] choices, String correctAnswer, int points, int penalty)
    {
        this.points = points;
        this.penalty = penalty;
        this.questionStr = questionStr;
        this.choices = choices;
        this.isSpecial = false;
        this.correctAnswer = correctAnswer;
        this.selectedAnswer = null;
    }

    public boolean isAttempted()
    {
        return selectedAnswer != null;
    }

    public void setSelectedAnswer(String selectedAnswer)
    {
        this.selectedAnswer = selectedAnswer;
    }

    public String getSelectedAnswer()
    {
        return selectedAnswer;
    }

    public boolean isAttemptCorrect() {
        return selectedAnswer.equals(correctAnswer);
    }

    public int getPoints() {
        return points;
    }

    public boolean isSpecial(){
        return isSpecial;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public String[] getChoices()
    {
        return choices;
    }

    public String getQuestionStr()
    {
        return questionStr;
    }

    public String getCorrectAnswer()
    {
        return correctAnswer;
    }
}
