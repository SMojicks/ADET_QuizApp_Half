package com.example.myapplication;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;
public class Question_TXT implements Parcelable {
    private String question;
    private List<String> choices;
    private int correctAnswerIndex;

    public Question_TXT(String question, List<String> choices, int correctAnswerIndex) {
        this.question = question;
        this.choices = choices;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    protected Question_TXT(Parcel in) {
        question = in.readString();
        choices = in.createStringArrayList();
        correctAnswerIndex = in.readInt();
    }

    public static final Creator<Question_TXT> CREATOR = new Creator<Question_TXT>() {
        @Override
        public Question_TXT createFromParcel(Parcel in) {
            return new Question_TXT(in);
        }

        @Override
        public Question_TXT[] newArray(int size) {
            return new Question_TXT[size];
        }
    };

    public String getQuestion() {
        return question;
    }

    public List<String> getChoices() {
        return choices;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeStringList(choices);
        dest.writeInt(correctAnswerIndex);
    }
}
