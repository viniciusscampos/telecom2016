package tele.com.election;

import java.io.Serializable;

public class Option implements Serializable{
    public String text;
    public Integer voteAmount;

    public Option(String receivedText){
        this.text = receivedText;
        this.voteAmount = 0;
    }

}
