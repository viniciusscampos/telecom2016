package tele.com.election;

import java.util.ArrayList;

public class Survey {
    private String title;
    private ArrayList<Option> options;

    public Survey(String ReceivedTitle, ArrayList<Option> ReceivedOptions){
        this.title = ReceivedTitle;
        this.options = ReceivedOptions;
        System.out.println("Enquete "+ this.title + " foi criada!");
    }

    public ArrayList<Option> getResult(){
        return this.options;
    }

    public String getOptionText(Integer index){
        return options.get(index).text;
    }

    public Integer getOptionVoteAmount(Integer index){
        return options.get(index).voteAmount;
    }

    public void updateResult(Integer index){
        options.get(index).voteAmount++;
    }

    public String getTitle(){
        return this.title;
    }


}

