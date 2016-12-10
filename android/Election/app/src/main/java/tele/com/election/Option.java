package tele.com.election;

public class Option {
    public String text;
    public Integer voteAmount;

    public Option(String receivedText){
        this.text = receivedText;
        this.voteAmount = 0;
    }

}
